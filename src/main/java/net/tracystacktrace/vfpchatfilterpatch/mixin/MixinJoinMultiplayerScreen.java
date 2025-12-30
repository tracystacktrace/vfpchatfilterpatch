package net.tracystacktrace.vfpchatfilterpatch.mixin;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.tracystacktrace.vfpchatfilterpatch.gui.GuiConfigScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(JoinMultiplayerScreen.class)
public abstract class MixinJoinMultiplayerScreen extends Screen {

    @Unique
    private Button vfpchatfilterpatch$cfConfigButton;

    protected MixinJoinMultiplayerScreen(Component title) {
        super(title);
    }

    @Inject(method = "repositionElements", at = @At("TAIL"))
    private void vfpchatfilterpatch$addConfigButton(CallbackInfo ci) {
        if (vfpchatfilterpatch$cfConfigButton == null) {
            vfpchatfilterpatch$cfConfigButton = GuiConfigScreen.getConfigEntryButton(this.minecraft, this);
            this.addRenderableWidget(vfpchatfilterpatch$cfConfigButton);
        }
        vfpchatfilterpatch$cfConfigButton.setPosition(this.width - 130, 5);
    }
}
