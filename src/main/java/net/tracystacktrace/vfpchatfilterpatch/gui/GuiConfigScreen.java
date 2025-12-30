package net.tracystacktrace.vfpchatfilterpatch.gui;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.network.chat.Component;
import net.tracystacktrace.vfpchatfilterpatch.QuickConfig;
import net.tracystacktrace.vfpchatfilterpatch.VFPChatFilterPatch;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Path;

public class GuiConfigScreen extends Screen {

    private final Screen parentScreen;

    private EditBox fieldReplaceText;
    private Button buttonMode;
    private Button buttonReload;

    // limbo state values -> either save them or leave to destruction
    private byte currentMode = QuickConfig.PATCH_MODE;

    protected GuiConfigScreen(@Nullable Screen parentScreen) {
        super(Component.translatable("vfpchatfilterpatch.screen.config"));
        this.parentScreen = parentScreen;
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(this.parentScreen);
    }

    @Override
    protected void init() {
        buttonMode = Button.builder(
                Component.translatable("vfpchatfilterpatch.button.mode"), button -> performModeChange()
        ).bounds(this.width / 2 - 100, this.height / 2 - 55, 200, 20).build();

        buttonReload = Button.builder(
                Component.translatable("vfpchatfilterpatch.button.reload"), button -> performFontReload()
        ).bounds(this.width / 2 - 100, this.height / 2 - 25, 200, 20).build();

        fieldReplaceText = new EditBox(font, this.width / 2, this.height / 2 + 5, 100, 20, Component.empty());
        fieldReplaceText.setValue(QuickConfig.REPLACE_DATA);

        Button buttonExit = Button.builder(
                Component.translatable("vfpchatfilterpatch.button.exit"), button -> performExit()
        ).bounds(this.width / 2 - 100, this.height / 2 + 35, 200, 20).build();

        this.addRenderableWidget(buttonMode);
        this.addRenderableWidget(buttonReload);
        this.addRenderableWidget(buttonExit);
        this.addRenderableWidget(fieldReplaceText);

        this.updateModeButtonState();
    }

    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float deltaTicks) {
        super.render(context, mouseX, mouseY, deltaTicks);
        context.drawCenteredString(this.font, Component.translatable("vfpchatfilterpatch.screen.config"), this.width / 2, this.height / 2 - 80, 0xFFFFFFFF);
        context.drawString(this.font, Component.translatable("vfpchatfilterpatch.screen.replacesequence"), this.width / 2 - 100 + 2, this.height / 2 + 15 - this.font.lineHeight / 2, 0xFFFFFFFF);
    }

    private void performModeChange() {
        //TODO: replcace with normal solution if there are more than two choices
        this.currentMode = (byte) ((this.currentMode == 0) ? 1 : 0);
        this.updateModeButtonState();
    }

    private void performFontReload() {
        try {
            final Path target = FabricLoader.getInstance().getConfigDir().resolve("vfpchatfilterpatch/font.txt");

            //message the user if font.txt not found
            if(!target.toFile().exists()) {
                this.minecraft.getToastManager().addToast(SystemToast.multiline(
                        this.minecraft, SystemToast.SystemToastId.NARRATOR_TOGGLE, Component.nullToEmpty("VFP ChatFilter Patch"), Component.translatable("vfpchatfilterpatch.notify.txt.notfound")
                ));
                return;
            }

            //try to read the font.txt file
            VFPChatFilterPatch.readFontFile(target);
            this.minecraft.getToastManager().addToast(SystemToast.multiline(
                    this.minecraft, SystemToast.SystemToastId.NARRATOR_TOGGLE, Component.nullToEmpty("VFP ChatFilter Patch"), Component.translatable("vfpchatfilterpatch.notify.txt.success")
            ));
        } catch (IOException e) {
            //print error and notify player about it
            VFPChatFilterPatch.LOGGER.error("Failed to force load font.txt file!", e);
            e.printStackTrace();
            this.minecraft.getToastManager().addToast(SystemToast.multiline(
                    this.minecraft, SystemToast.SystemToastId.NARRATOR_TOGGLE, Component.nullToEmpty("VFP ChatFilter Patch"), Component.translatable("vfpchatfilterpatch.notify.txt.fail")
            ));
        }
    }

    private void performExit() {
        QuickConfig.PATCH_MODE = this.currentMode;
        QuickConfig.REPLACE_DATA = this.fieldReplaceText.getValue();
        QuickConfig.write();
        this.minecraft.setScreen(this.parentScreen);
    }

    private void updateModeButtonState() {
        buttonMode.setMessage(switch (this.currentMode) {
            case 0 -> Component.translatable("vfpchatfilterpatch.button.mode.txt");
            case 1 -> Component.translatable("vfpchatfilterpatch.button.mode.all");
            default -> Component.empty();
        });
        buttonReload.active = this.currentMode == 0;
    }

    public static @NotNull Button getConfigEntryButton(
            @NotNull Minecraft game,
            @Nullable Screen parentScreen
    ) {
        return Button.builder(
                Component.nullToEmpty("CF"),
                button -> game.setScreen(new GuiConfigScreen(parentScreen))
        ).bounds(0, 0, 20, 20).build();
    }
}
