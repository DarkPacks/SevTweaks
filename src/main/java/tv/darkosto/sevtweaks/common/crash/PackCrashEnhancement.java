package tv.darkosto.sevtweaks.common.crash;

import net.minecraftforge.fml.common.ICrashCallable;
import net.minecraftforge.fml.common.Loader;
import tv.darkosto.sevtweaks.common.config.Configuration;

import java.util.ArrayList;
import java.util.List;

public class PackCrashEnhancement implements ICrashCallable {
    @Override
    public String getLabel() {
        String label = Configuration.crashData.name;

        if (label == null || label.length() == 0) {
            return "Modpack Data";
        }
        return label;
    }

    @Override
    public String call() {
        StringBuilder builder = new StringBuilder();
        for (String data : getData()) {
            builder.append(String.format("\n\t\t%s", data));
        }

        return builder.toString();
    }

    /**
     * Build a list of the data to return and then list on the Crash Report.
     */
    private List<String> getData() {
        List<String> data = new ArrayList<>();
        data.add(String.format("Version: %s", Configuration.crashData.version)); // Version of the modpack.

        // Is Optifine Installed? If so what version.
        try {
            Class<?> optifineConfClass = Class.forName("Config", false, Loader.instance().getModClassLoader());
            String optifineVersion = (String) optifineConfClass.getField("VERSION").get(null);
            data.add(String.format("Optifine Installed: True (%s)", optifineVersion));
        } catch (Exception err) {
            data.add("Optifine Installed: False");
        }

        return data;
    }
}
