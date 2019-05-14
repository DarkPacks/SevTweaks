package tv.darkosto.sevtweaks.core;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.Map;

public class SevTweaksLoadingPlugin implements IFMLLoadingPlugin {
    public static Logger SEVTWEAKS_CORE_LOGGER = LogManager.getLogger("SevTweaksCore");
    public static String IS_VALID_LIGHT_LEVEL;
    
    @Override
    public String[] getASMTransformerClass() {
        return new String[]{"tv.darkosto.sevtweaks.core.SevTweaksTransformer"};
    }
    
    @Override
    public String getModContainerClass() {
        return null;
    }
    
    @Nullable
    @Override
    public String getSetupClass() {
        return null;
    }
    
    @Override
    public void injectData(Map<String, Object> data) {
        boolean dev = !(boolean) data.get("runtimeDeobfuscationEnabled");
        IS_VALID_LIGHT_LEVEL = dev ? "isValidLightLevel" : "func_70814_o";
    }
    
    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
