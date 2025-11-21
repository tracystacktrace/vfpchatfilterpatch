package net.tracystacktrace.vfpchatfilterpatch.mixin;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.tracystacktrace.vfpchatfilterpatch.gui.GuiConfigScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MultiplayerScreen.class)
public abstract class MixinMultiplayerScreen extends Screen {

    @Unique
    private ButtonWidget vfpchatfilterpatch$cfConfigButton;

    protected MixinMultiplayerScreen(Text title) {
        super(title);
    }

    @Inject(method = "refreshWidgetPositions", at = @At("TAIL"))
    private void vfpchatfilterpatch$addConfigButton(CallbackInfo ci) {
        if (vfpchatfilterpatch$cfConfigButton == null) {
            vfpchatfilterpatch$cfConfigButton = GuiConfigScreen.getConfigEntryButton(this.client, this);
            this.addDrawableChild(vfpchatfilterpatch$cfConfigButton);
        }
        vfpchatfilterpatch$cfConfigButton.setPosition(this.width - 130, 5);
    }
}
