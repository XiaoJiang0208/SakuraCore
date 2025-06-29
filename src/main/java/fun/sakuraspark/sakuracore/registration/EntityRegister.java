package fun.sakuraspark.sakuracore.registration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import com.mojang.datafixers.util.Pair;

import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * 实体注册器，负责管理和注册Minecraft实体类型及其渲染器。
 * <p>
 * 该类提供了注册实体类型、实体渲染器的功能，并在Forge事件总线上注册相应的监听器。
 * </p>
 */
public class EntityRegister {
    private final String MODID;
    private final DeferredRegister<EntityType<?>> ENTITY_TYPES;
    private final Map<String, RegistryObject<EntityType<? extends Entity>>> ENTITY_TYPES_REGISTRY;
    private final Map<String, EntityRendererProvider<? extends Entity>> RENDERER_FACTORIES;
    private final List<Pair<ModelLayerLocation, Supplier<LayerDefinition>>> MODEL_LAYER_LOCATIONS;

    /**
     * 创建一个新的实体注册器实例。
     *
     * @param MODID 模组ID，用于注册实体类型
     */
    public EntityRegister(String MODID) {
        this.MODID = MODID;
        this.ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, this.MODID);
        this.ENTITY_TYPES_REGISTRY = new HashMap<>();
        this.RENDERER_FACTORIES = new HashMap<>();
        this.MODEL_LAYER_LOCATIONS = new ArrayList<>();
    }

    /**
     * 注册一个实体类型。
     *
     * @param <T>  实体类型
     * @param name 实体的注册名称
     * @param sup  提供EntityType实例的供应商
     * @return 注册的实体类型的RegistryObject
     */
    @SuppressWarnings("unchecked")
    public <T extends Entity> RegistryObject<EntityType<T>> entity(String name, Supplier<? extends EntityType<?>> sup) {
        RegistryObject<EntityType<?>> entity = ENTITY_TYPES.register(name, sup);
        ENTITY_TYPES_REGISTRY.put(name, entity);
        return (RegistryObject<EntityType<T>>) (RegistryObject<?>) entity;
    }

    /**
     * 为指定名称的实体注册渲染器工厂。
     *
     * @param <T>             实体类型
     * @param name            实体的注册名称
     * @param rendererFactory 实体渲染器提供者
     */
    public <T extends Entity> void entityRenderer(String name, EntityRendererProvider<T> rendererFactory) {
        RENDERER_FACTORIES.put(name, rendererFactory);
    }

    public ModelLayerLocation entityModelLayerLocation(String name, String layerName,
            Supplier<LayerDefinition> layerDefinition) {
        ModelLayerLocation layer = new ModelLayerLocation(ResourceLocation.tryBuild(MODID, name), layerName);
        return entityModelLayerLocation(layer, layerDefinition);
    }

    public ModelLayerLocation entityModelLayerLocation(ModelLayerLocation layer,
            Supplier<LayerDefinition> layerDefinition) {
        // 检查是否已存在相同的层，避免重复添加
        boolean exists = MODEL_LAYER_LOCATIONS.stream()
                .anyMatch(pair -> pair.getFirst().equals(layer));

        if (!exists) {
            MODEL_LAYER_LOCATIONS.add(Pair.of(layer, layerDefinition));
        }

        return layer;
    }

    /**
     * 通过名称获取已注册的实体类型。
     *
     * @param <T>  实体类型
     * @param name 实体的注册名称
     * @return 对应名称的实体类型的RegistryObject，如果不存在则可能返回null
     */
    @SuppressWarnings("unchecked")
    public <T extends Entity> RegistryObject<EntityType<T>> getEntity(String name) {
        return (RegistryObject<EntityType<T>>) (RegistryObject<?>) ENTITY_TYPES_REGISTRY.get(name);
    }

    /**
     * 通过名称获取已注册的实体渲染器工厂。
     *
     * @param <T>  实体类型
     * @param name 实体的注册名称
     * @return 对应名称的实体渲染器工厂，如果不存在则可能返回null
     */
    @SuppressWarnings("unchecked")
    public <T extends Entity> EntityRendererProvider<T> getEntityRenderer(String name) {
        return (EntityRendererProvider<T>) RENDERER_FACTORIES.get(name);
    }

    public List<Pair<ModelLayerLocation, Supplier<LayerDefinition>>> getEntityModelLayerLocations() {
        return MODEL_LAYER_LOCATIONS;
    }

    /**
     * 处理实体属性创建事件，为已注册的实体设置默认属性。
     * <p>
     * 此方法会在Forge的EntityAttributeCreationEvent事件中被调用。
     * </p>
     *
     * @param event 实体属性创建事件
     */
    public void createDefaultAttributes(EntityAttributeCreationEvent event) {
        // event.put(get("test"), CameraEntity.);
    }

    /**
     * 注册所有已配置的实体渲染器。
     * <p>
     * 此方法会在Forge的EntityRenderersEvent.RegisterRenderers事件中被调用。
     * </p>
     *
     * @param event 实体渲染器注册事件
     */
    public void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        for (String name : RENDERER_FACTORIES.keySet()) {
            if (getEntityRenderer(name) != null) {
                event.registerEntityRenderer(getEntity(name).get(), getEntityRenderer(name));
            }
        }
    }

    public void registerModelLayerLocations(EntityRenderersEvent.RegisterLayerDefinitions event) {
        if (getEntityModelLayerLocations() != null) {
            for (Pair<ModelLayerLocation, Supplier<LayerDefinition>> pair : getEntityModelLayerLocations()) {
                event.registerLayerDefinition(pair.getFirst(), pair.getSecond());
            }
        }
    }

    /**
     * 在Forge事件总线上注册此实体注册器。
     * <p>
     * 此方法应该在模组初始化阶段被调用，以确保实体类型和相关事件监听器被正确注册。
     * </p>
     */
    public void registerALL() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ENTITY_TYPES.register(bus);
        bus.addListener(this::createDefaultAttributes);
        bus.addListener(this::registerEntityRenderers);
        bus.addListener(this::registerModelLayerLocations);
    }
}
