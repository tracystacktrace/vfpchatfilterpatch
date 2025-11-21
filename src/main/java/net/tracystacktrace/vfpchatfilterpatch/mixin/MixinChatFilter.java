package net.tracystacktrace.vfpchatfilterpatch.mixin;

import net.raphimc.vialegacy.protocol.release.r1_0_0_1tor1_1.rewriter.ChatFilter;
import net.tracystacktrace.vfpchatfilterpatch.QuickConfig;
import net.tracystacktrace.vfpchatfilterpatch.VFPChatFilterPatch;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChatFilter.class)
public class MixinChatFilter {

    @Inject(method = "filter", at = @At("HEAD"), cancellable = true)
    private static void vfpchatfilterpatch$applyPatch(@NotNull String message, CallbackInfoReturnable<String> cir) {
        if (QuickConfig.PATCH_MODE == 1) {
            cir.setReturnValue(message);
            cir.cancel();
            return;
        }

        if (QuickConfig.PATCH_MODE == 0) {
            final StringBuilder builder = new StringBuilder();
            for (int i = 0; i < message.length(); i++) {
                final char candidate = message.charAt(i);
                builder.append(VFPChatFilterPatch.allowedChar(candidate) ? candidate : QuickConfig.REPLACE_DATA);
            }
            cir.setReturnValue(builder.toString());
            cir.cancel();
            return;
        }
    }

}
