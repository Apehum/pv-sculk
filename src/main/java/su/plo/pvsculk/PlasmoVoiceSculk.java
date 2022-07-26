package su.plo.pvsculk;

import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.core.conversion.ObjectConverter;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.google.common.collect.Maps;
import net.fabricmc.api.ModInitializer;

import java.io.File;
import java.util.Map;
import java.util.UUID;

public class PlasmoVoiceSculk implements ModInitializer {

    public static VoiceSculkConfig CONFIG;
    public static Map<UUID, Long> LAST_ACTIVATIONS = Maps.newConcurrentMap();

    private static final ObjectConverter objectConverter = new ObjectConverter();

    @Override
    public void onInitialize() {
        CONFIG = loadConfig();
    }

    private VoiceSculkConfig loadConfig() {
        VoiceSculkConfig config = null;
        var configDir = new File("config/pvsculk");
        var configFile = new File(configDir, "config.toml");
        if (configFile.exists()) {
            var builder = CommentedFileConfig.builder(configFile);
            var fileConfig = builder.build();
            fileConfig.load();

            config = objectConverter.toObject(fileConfig, VoiceSculkConfig::new);
        }

        if (config == null) {
            config = new VoiceSculkConfig();

            configDir.mkdirs();
            var builder = CommentedFileConfig.builder(configFile);
            var fileConfig = builder.build();

            fileConfig.putAll(objectConverter.toConfig(config, Config::inMemory));
            fileConfig.save();
        }
        return config;
    }
}
