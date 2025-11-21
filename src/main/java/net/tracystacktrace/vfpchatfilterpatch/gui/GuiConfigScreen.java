package net.tracystacktrace.vfpchatfilterpatch.gui;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.text.Text;
import net.tracystacktrace.vfpchatfilterpatch.QuickConfig;
import net.tracystacktrace.vfpchatfilterpatch.VFPChatFilterPatch;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GuiConfigScreen extends Screen {

    private final Screen parentScreen;

    private TextFieldWidget fieldReplaceText;
    private ButtonWidget buttonMode;
    private ButtonWidget buttonReload;

    // limbo state values -> either save them or leave to destruction
    private byte currentMode = QuickConfig.PATCH_MODE;

    protected GuiConfigScreen(@Nullable Screen parentScreen) {
        super(Text.translatable("vfpchatfilterpatch.screen.config"));
        this.parentScreen = parentScreen;
    }

    @Override
    public void close() {
        this.client.setScreen(this.parentScreen);
    }

    @Override
    protected void init() {
        buttonMode = ButtonWidget.builder(
                Text.translatable("vfpchatfilterpatch.button.mode"), button -> performModeChange()
        ).dimensions(this.width / 2 - 100, this.height / 2 - 55, 200, 20).build();

        buttonReload = ButtonWidget.builder(
                Text.translatable("vfpchatfilterpatch.button.reload"), button -> performFontReload()
        ).dimensions(this.width / 2 - 100, this.height / 2 - 25, 200, 20).build();

        fieldReplaceText = new TextFieldWidget(textRenderer, this.width / 2, this.height / 2 + 5, 100, 20, Text.of(QuickConfig.REPLACE_DATA));

        ButtonWidget buttonExit = ButtonWidget.builder(
                Text.translatable("vfpchatfilterpatch.button.exit"), button -> performExit()
        ).dimensions(this.width / 2 - 100, this.height / 2 + 35, 200, 20).build();

        this.addDrawableChild(buttonMode);
        this.addDrawableChild(buttonReload);
        this.addDrawableChild(buttonExit);
        this.addDrawableChild(fieldReplaceText);

        this.updateModeButtonState();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        super.render(context, mouseX, mouseY, deltaTicks);
        context.drawCenteredTextWithShadow(this.textRenderer, Text.translatable("vfpchatfilterpatch.screen.config"), this.width / 2, this.height / 2 - 80, 0xFFFFFFFF);
        context.drawTextWithShadow(this.textRenderer, Text.translatable("vfpchatfilterpatch.screen.replacesequence"), this.width / 2 - 100, this.height / 2 + 15 - this.textRenderer.fontHeight / 2, 0xFFFFFFFF);
    }

    private void performModeChange() {
        //TODO: replcace with normal solution if there are more than two choices
        this.currentMode = (byte) ((this.currentMode == 0) ? 1 : 0);
        this.updateModeButtonState();
    }

    private void performFontReload() {
        VFPChatFilterPatch.readFontFile(FabricLoader.getInstance().getConfigDir().resolve("vfpchatfilterpatch/font.txt"));
        this.client.getToastManager().add(SystemToast.create(
                this.client, SystemToast.Type.NARRATOR_TOGGLE, Text.of("VFP ChatFilter Patch"), Text.translatable("vfpchatfilterpatch.notify.txt.success")
        ));
    }

    private void performExit() {
        QuickConfig.PATCH_MODE = this.currentMode;
        QuickConfig.REPLACE_DATA = this.fieldReplaceText.getText();
        QuickConfig.write();
        this.client.setScreen(this.parentScreen);
    }

    private void updateModeButtonState() {
        buttonMode.setMessage(switch (this.currentMode) {
            case 0 -> Text.translatable("vfpchatfilterpatch.button.mode.txt");
            case 1 -> Text.translatable("vfpchatfilterpatch.button.mode.all");
            default -> Text.empty();
        });
        buttonReload.active = this.currentMode == 0;
    }

    public static @NotNull ButtonWidget getConfigEntryButton(
            @NotNull MinecraftClient game,
            @Nullable Screen parentScreen
    ) {
        return ButtonWidget.builder(
                Text.of("CF"),
                button -> game.setScreen(new GuiConfigScreen(parentScreen))
        ).dimensions(0, 0, 20, 20).build();
    }
}
