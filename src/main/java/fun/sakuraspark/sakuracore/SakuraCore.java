package fun.sakuraspark.sakuracore;

import com.mojang.logging.LogUtils;


import net.minecraftforge.fml.common.Mod;


import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(SakuraCore.MODID)
public class SakuraCore
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "sakuracore";
    // // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    private static final boolean DEBUG = false;

    public SakuraCore()
    {
        if (DEBUG) {
            new Test();
        }
    }

}
