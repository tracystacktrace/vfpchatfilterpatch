package net.tracystacktrace.vfpchatfilterpatch;

import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class QuickConfig {
    private static final File CONFIG_FILE = FabricLoader.getInstance().getConfigDir().resolve("vfpchatfilterpatch/config.properties").toFile();

    public static byte PATCH_MODE = 0;
    public static @NotNull String REPLACE_DATA = "";

    public static void init() {
        if (CONFIG_FILE.exists()) {
            read();
            return;
        }
        CONFIG_FILE.getParentFile().mkdirs();
        write();
    }

    public static void write() {
        try (FileOutputStream fileOutputStream = new FileOutputStream(CONFIG_FILE)) {
            final Properties properties = new Properties();
            properties.setProperty("config.mode", String.valueOf(PATCH_MODE));
            properties.setProperty("config.replace", REPLACE_DATA);
            properties.store(fileOutputStream, "VFP ChatFilter Patch's quick and dirty config file");
        } catch (IOException e) {
            VFPChatFilterPatch.LOGGER.error("Oops! Couldn't write config file...", e);
            throw new RuntimeException(e);
        }
    }

    public static void read() {
        try (FileInputStream fileInputStream = new FileInputStream(CONFIG_FILE)) {
            final Properties properties = new Properties();
            properties.load(fileInputStream);
            PATCH_MODE = Byte.parseByte(properties.getProperty("config.mode"));
            REPLACE_DATA = properties.getProperty("config.replace");
        } catch (IOException e) {
            VFPChatFilterPatch.LOGGER.error("Oops! Couldn't read config file...", e);
            throw new RuntimeException(e);
        }
    }

}
