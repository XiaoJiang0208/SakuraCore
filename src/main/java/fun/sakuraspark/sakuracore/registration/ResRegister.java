package fun.sakuraspark.sakuracore.registration;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ResRegister {
    private final String MODID;
    private final DeferredRegister<Block> BLOCKS;
    private final DeferredRegister<Item> ITEMS;
    private final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS;
    private final Map<String, RegistryObject<Block>> BLOCKS_REGISTRY;
    private final Map<String, RegistryObject<Item>> ITEMS_REGISTRY;
    private final Map<String, RegistryObject<CreativeModeTab>> CREATIVE_MODE_TABS_REGISTRY;

    public ResRegister(String MODID) {
        this.MODID = MODID;
        this.BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, this.MODID);
        this.ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, this.MODID);
        this.CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, this.MODID);
        this.BLOCKS_REGISTRY = new HashMap<>();
        this.ITEMS_REGISTRY = new HashMap<>();
        this.CREATIVE_MODE_TABS_REGISTRY = new HashMap<>();
    }

    /**
     * 注册一个Block
     * @param name Block名字
     * @param properties Block属性
     * @return RegistryObject<Block>
     */
    public RegistryObject<Block> block(String name, BlockBehaviour.Properties properties) {
        RegistryObject<Block> block = BLOCKS.register(name, () -> new Block(properties));
        BLOCKS_REGISTRY.put(name, block);
        return block;
    }
    /**
     * 注册一个自定义Block
     * @param name Block名字
     * @param blockSupplier 注册函数
     * @return RegistryObject<Block>
     */
    public RegistryObject<Block> block(String name, Supplier<Block> blockSupplier) {
        RegistryObject<Block> block = BLOCKS.register(name, blockSupplier);
        BLOCKS_REGISTRY.put(name, block);
        return block;
    }

    /**
     * 注册一个BlockItem
     * @param name BlockItem名字
     * @param block 方块
     * @param properties Item属性
     * @return RegistryObject<Item>
     */
    public RegistryObject<Item> item(String name, RegistryObject<Block> block, Item.Properties properties) {
        RegistryObject<Item> item = ITEMS.register(name, () -> new BlockItem(block.get(), properties));
        ITEMS_REGISTRY.put(name, item);
        return item;
    }
    /**
     * 注册一个Item
     * @param name Item名字
     * @param properties Item属性
     * @return RegistryObject<Item>
     */
    public RegistryObject<Item> item(String name, Item.Properties properties) {
        RegistryObject<Item> item = ITEMS.register(name, () -> new Item(properties));
        ITEMS_REGISTRY.put(name, item);
        return item;
    }
    /**
     * 注册一个自定义Item
     * @param name Item名字
     * @param itemSupplier 注册函数
     * @return RegistryObject<Item>
     */
    public RegistryObject<Item> item(String name, Supplier<Item> itemSupplier) {
        RegistryObject<Item> item = ITEMS.register(name, itemSupplier);
        ITEMS_REGISTRY.put(name, item);
        return item;
    }

    /**
     * 注册一个CreativeModeTab
     * @param name CreativeModeTab名称
     * @param creativemodetab CreativeModeTab属性
     * @return RegistryObject<CreativeModeTab>
     */
    public RegistryObject<CreativeModeTab> creativeModeTab(String name, CreativeModeTab creativemodetab) {
        RegistryObject<CreativeModeTab> creative_mode_tab = CREATIVE_MODE_TABS.register(name, () -> creativemodetab);
        CREATIVE_MODE_TABS_REGISTRY.put(name, creative_mode_tab);
        return creative_mode_tab;
    }
    /**
     * 注册一个自定义CreativeModeTab
     * @param name CreativeModeTab名称
     * @param creativemodetabSupplier 注册函数
     * @return RegistryObject<CreativeModeTab>
     */
    public RegistryObject<CreativeModeTab> creativeModeTab(String name, Supplier<CreativeModeTab> creativemodetabSupplier) {
        RegistryObject<CreativeModeTab> creative_mode_tab = CREATIVE_MODE_TABS.register(name, creativemodetabSupplier);
        CREATIVE_MODE_TABS_REGISTRY.put(name, creative_mode_tab);
        return creative_mode_tab;
    }

    /**
     * 获取注册Item
     * @param name 物品名称
     * @return RegistryObject<Item>
     */
    public RegistryObject<Item> getItem(String name) {
        RegistryObject<Item> item = ITEMS_REGISTRY.get(name);
        if (item == null) {
            throw new IllegalArgumentException("Item not found: " + name);
        }
        return item;
    }
    /**
     * 获取注册Block
     * @param name 方块名称
     * @return RegistryObject<Block>
     */
    public RegistryObject<Block> getBlock(String name) {
        RegistryObject<Block> block = BLOCKS_REGISTRY.get(name);
        if (block == null) {
            throw new IllegalArgumentException("Block not found: " + name);
        }
        return block;
    }
    /**
     * 获取注册CreativeModeTab
     * @param name 创意模式标签名称
     * @return RegistryObject<CreativeModeTab>
     */
    public RegistryObject<CreativeModeTab> getCreativeModeTab(String name) {
        RegistryObject<CreativeModeTab> creative_mode_tab = CREATIVE_MODE_TABS_REGISTRY.get(name);
        if (creative_mode_tab == null) {
            throw new IllegalArgumentException("CreativeModeTab not found: " + name);
        }
        return creative_mode_tab;
    }

    /**
     * 将物品注册到总线
     * @param bus
     */
    public void register(IEventBus bus) {
        BLOCKS.register(bus);
        ITEMS.register(bus);
        CREATIVE_MODE_TABS.register(bus);
    }
}
