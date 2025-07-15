package fun.sakuraspark.sakuracore;

import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import fun.sakuraspark.sakuracore.client.gui.TestScreen;
import fun.sakuraspark.sakuracore.client.renderer.entity.CameraEntityModel;
import fun.sakuraspark.sakuracore.client.renderer.entity.CameraEntityRenderer;
import fun.sakuraspark.sakuracore.registration.EntityRegister;
import fun.sakuraspark.sakuracore.registration.KeyMappingRegister;
import fun.sakuraspark.sakuracore.registration.ResRegister;
import fun.sakuraspark.sakuracore.world.entity.CameraEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class Test {
    // Define mod id in a common place for everything to reference
    private static final String MODID = "sakuracore";
    private static final Logger LOGGER = LogUtils.getLogger();

    private Entity customEntity;

    private static final ResRegister register = new ResRegister(MODID);
    private static final KeyMappingRegister keyMappingRegister = new KeyMappingRegister();
    private static final EntityRegister entityRegister = new EntityRegister(MODID);

    public Test() {
        LOGGER.info("SakuraCore is loading...");
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        final RegistryObject<Block> EXAMPLE_BLOCK = register.block("example_block",
                BlockBehaviour.Properties.of().mapColor(MapColor.STONE));
        register.item("example_block", EXAMPLE_BLOCK, new Item.Properties());
        final RegistryObject<Item> EXAMPLE_ITEM = register.item("example_item",
                new Item.Properties().food(new FoodProperties.Builder()
                        .alwaysEat().nutrition(1).saturationMod(2f).build()));

        register.creativeModeTab("example_tab", CreativeModeTab.builder()
                .withTabsBefore(CreativeModeTabs.COMBAT)
                .icon(() -> EXAMPLE_ITEM.get().getDefaultInstance())
                .displayItems((parameters, output) -> {
                    output.accept(EXAMPLE_ITEM.get()); // Add the example item to the tab. For your own tabs, this
                                                       // method is preferred over the event
                }).build());

        register.registerALL();
        keyMappingRegister.keyMapping("key.sakuracore.test", "key.categories.sakuracore.test", GLFW.GLFW_KEY_A);
        keyMappingRegister.keyMapping("key.sakuracore.test2", "key.categories.sakuracore.test", GLFW.GLFW_KEY_V);
        keyMappingRegister.registerALL();
        keyMappingRegister.bindKeyMapping("key.sakuracore.test", () -> {
            Minecraft.getInstance().setScreen(new TestScreen(Component.translatable("key.sakuracore.test")));
        });
        keyMappingRegister.bindKeyMapping("key.sakuracore.test2", () -> {
            if (customEntity != null) {
                Minecraft.getInstance().setCameraEntity(customEntity);
                customEntity = null;
            }
        });

        entityRegister.entity("camera", () -> EntityType.Builder.of(CameraEntity::new, MobCategory.MISC)
                .sized(1.0F, 1.0F).clientTrackingRange(10).build(ResourceKey
                        .create(Registries.ENTITY_TYPE, ResourceLocation.tryBuild(MODID, "camera")).toString()));
        entityRegister.entityRenderer("camera", CameraEntityRenderer::new);
        entityRegister.entityModelLayerLocation(CameraEntityModel.LAYER_LOCATION, CameraEntityModel::createBodyLayer);
        entityRegister.registerALL();

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the
        // config file for us
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");

        if (Config.logDirtBlock)
            LOGGER.info("DIRT BLOCK >> {}", ForgeRegistries.BLOCKS.getKey(Blocks.DIRT));

        LOGGER.info(Config.magicNumberIntroduction + Config.magicNumber);

        Config.items.forEach((item) -> LOGGER.info("ITEM >> {}", item.toString()));
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS)
            event.accept(register.getItem("example_block"));
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    @SubscribeEvent
    public void onPlayerAttackEntity(net.minecraftforge.event.entity.player.AttackEntityEvent event) {
        // 保存原摄像机实体
        if (customEntity == null) {
            customEntity = Minecraft.getInstance().getCameraEntity();
        }
        Minecraft.getInstance().setCameraEntity(event.getTarget());

    }
}
