package fun.sakuraspark.sakuracore.registration;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.common.MinecraftForge;

/**
 * KeyMappingRegister 用于创建和注册 KeyMapping 实例。
 * <p>
 * 该类提供多种方法来构造懒加载的 {@link KeyMapping} 对象，并在触发 {@link RegisterKeyMappingsEvent} 事件时完成注册。
 * </p>
 */
public class KeyMappingRegister {
    /**
     * 存放所有待注册的 KeyMapping 懒加载对象。
     */
    private final List<Lazy<KeyMapping>> keyMappings = new ArrayList<>();

    /**
     * 创建一个懒加载的 KeyMapping 实例，并自动设定keyCode为 {@link InputConstants.Type#KEYSYM}
     * 与context为 {@link KeyConflictContext#UNIVERSAL}。
     *
     * @param name     键映射的名称
     * @param category 键映射所属的类别
     * @param keyCode  键的代码
     * @return         懒加载的 {@link KeyMapping} 实例
     */
    public Lazy<KeyMapping> keyMapping(String name, String category, int keyCode) {
        return keyMapping(name, category, keyCode, InputConstants.Type.KEYSYM, KeyConflictContext.UNIVERSAL);
    }
    /**
     * 创建一个懒加载的 KeyMapping 实例，并自动设定context为 {@link KeyConflictContext#UNIVERSAL}。
     *
     * @param name     键映射的名称
     * @param category 键映射所属的类别
     * @param keyCode  键的代码
     * @param type     按键类型
     * @return         懒加载的 {@link KeyMapping} 实例
     */
    public Lazy<KeyMapping> keyMapping(String name, String category, int keyCode, InputConstants.Type type) {
        return keyMapping(name, category, keyCode, type, KeyConflictContext.UNIVERSAL);
    }
    /**
     * 创建一个懒加载的 KeyMapping 实例。
     *
     * @param name     键映射的名称
     * @param category 键映射所属的类别
     * @param keyCode  键的代码
     * @param type     按键类型
     * @param context  按键冲突上下文
     * @return         懒加载的 {@link KeyMapping} 实例
     */
    public Lazy<KeyMapping> keyMapping(String name, String category, int keyCode, InputConstants.Type type, KeyConflictContext context) {
        return keyMapping(() -> new KeyMapping(name, context, type, keyCode, category));
    }
    /**
     * 创建一个懒加载的 KeyMapping 实例，并自动设定按键修饰符。
     * 默认按键类型为 {@link InputConstants.Type#KEYSYM} 和上下文 {@link KeyConflictContext#UNIVERSAL}。
     *
     * @param name        键映射的名称
     * @param category    键映射所属的类别
     * @param keyModifier 按键修饰符
     * @param keyCode     键的代码
     * @return            懒加载的 {@link KeyMapping} 实例
     */
    public Lazy<KeyMapping> keyMapping(String name, String category, KeyModifier keyModifier, int keyCode) {
        return keyMapping(name, category, keyModifier, keyCode, InputConstants.Type.KEYSYM, KeyConflictContext.UNIVERSAL);
    }
    /**
     * 创建一个懒加载的 KeyMapping 实例，并自动设定按键修饰符与按键类型。
     * 默认上下文为 {@link KeyConflictContext#UNIVERSAL}。
     *
     * @param name        键映射的名称
     * @param category    键映射所属的类别
     * @param keyModifier 按键修饰符
     * @param keyCode     键的代码
     * @param type        按键类型
     * @return            懒加载的 {@link KeyMapping} 实例
     */
    public Lazy<KeyMapping> keyMapping(String name, String category, KeyModifier keyModifier, int keyCode, InputConstants.Type type) {
        return keyMapping(name, category, keyModifier, keyCode, type, KeyConflictContext.UNIVERSAL);
    }
    /**
     * 创建一个懒加载的 KeyMapping 实例，并自动设定按键修饰符、按键类型以及按键冲突上下文。
     *
     * @param name        键映射的名称
     * @param category    键映射所属的类别
     * @param keyModifier 按键修饰符
     * @param keyCode     键的代码
     * @param type        按键类型
     * @param context     按键冲突上下文
     * @return            懒加载的 {@link KeyMapping} 实例
     */
    public Lazy<KeyMapping> keyMapping(String name, String category, KeyModifier keyModifier, int keyCode, InputConstants.Type type, KeyConflictContext context) {
        return keyMapping(() -> new KeyMapping(name, context, keyModifier, type, keyCode, category));
    }
    /**
     * 通过一个 {@link Supplier} 对象构造一个懒加载的 KeyMapping 实例，并将其添加到键映射列表中。
     *
     * @param supplier 创建 {@link KeyMapping} 对象的供应器
     * @return         懒加载的 {@link KeyMapping} 实例
     */
    public Lazy<KeyMapping> keyMapping(Supplier<KeyMapping> supplier) {
        Lazy<KeyMapping> keyMapping = Lazy.of(supplier);
        keyMappings.add(keyMapping);
        return keyMapping;
    }

    public KeyMapping getKeyMapping(String name) {
        for (Lazy<KeyMapping> keyMapping : keyMappings) {
            if (keyMapping.get().getName().equals(name)) {
                return keyMapping.get();
            }
        }
        return null;
    }
    public void bindKeyMapping(String name, Runnable function) {
        MinecraftForge.EVENT_BUS.addListener((ClientTickEvent event)-> {
            if (event.phase == TickEvent.Phase.END) { // Only call code once as the tick event is called twice every tick
                while (getKeyMapping(name).consumeClick()) {
                    function.run(); // Call the function when the key is pressed
                }
            }
        });
    }

    /**
     * 注册所有已创建的 KeyMapping 实例到指定的事件总线中。
     * <p>
     * 在 {@link RegisterKeyMappingsEvent} 事件发生时，将调用每个 KeyMapping 实例的获取方法并注册之。
     * </p>
     *
     * @param bus 注册事件监听的事件总线
     */
    public void register(IEventBus bus) {
        for (Lazy<KeyMapping> keyMapping : keyMappings) {
            bus.addListener((RegisterKeyMappingsEvent event) -> {
                event.register(keyMapping.get());
            });
        }
    }
}
