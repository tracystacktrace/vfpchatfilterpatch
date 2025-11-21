package net.tracystacktrace.vfpchatfilterpatch;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.stream.Collectors;

public class VFPChatFilterPatch implements ClientModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger("vfpchatfilterpatch");
    public static char[] ALLOWED_CHARS = new char[0];

    public static boolean allowedChar(char c) {
        for (char candidate : ALLOWED_CHARS) {
            if (c == candidate) return true;
        }
        return false;
    }

    @Override
    public void onInitializeClient() {
        QuickConfig.init();

        LOGGER.info("Trying to find out candidate font.txt...");
        final Path fontPath = FabricLoader.getInstance().getConfigDir().resolve("vfpchatfilterpatch/font.txt");
        final File fontFile = fontPath.toFile();

        if (!fontFile.exists()) {
            LOGGER.info("Custom font.txt is not found! Resulting to default one...");
            fontFile.getParentFile().mkdirs();
            final String fallbackFontTxt = readInsideFile("/assets/vfpchatfilterpatch/font.txt");
            this.writeToFile(fontPath, fallbackFontTxt);
        }

        readFontFile(fontPath);
    }

    public static void readFontFile(final @NotNull Path file) {
        try {
            final List<String> allLines = Files.readAllLines(file, StandardCharsets.UTF_8);
            allLines.add(" ");
            allLines.removeIf(s -> s.isEmpty() || s.startsWith("#"));
            ALLOWED_CHARS = allLines.stream().collect(Collectors.joining()).toCharArray();
            LOGGER.info("Loaded custom font.txt, reading {} characters in total!", ALLOWED_CHARS.length);
        } catch (IOException e) {
            LOGGER.error("Oops! Couldn't read font.txt file!", e);
            throw new RuntimeException(e);
        }
    }

    private @NotNull String readInsideFile(final @NotNull String file) {
        final StringBuilder builder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(file), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append('\n');
            }
        } catch (IOException e) {
            LOGGER.error("Oops! Couldn't read default font.txt file!", e);
            throw new RuntimeException(e);
        }
        return builder.toString();
    }

    private void writeToFile(final @NotNull Path file, final @NotNull String content) {
        try {
            Files.writeString(file, content, StandardCharsets.UTF_8, StandardOpenOption.CREATE);
        } catch (IOException e) {
            LOGGER.error("Oops! Couldn't write to font.txt file!", e);
            throw new RuntimeException(e);
        }
    }
}
