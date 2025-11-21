package net.tracystacktrace.vfpchatfilterpatch.mixin;

import net.raphimc.vialegacy.protocol.release.r1_0_0_1tor1_1.rewriter.ChatFilter;
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
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < message.length(); i++) {
            final char m = message.charAt(i);
            if (VFPChatFilterPatch.allowedChar(m)) {
                builder.append(m);
            }
        }
        cir.setReturnValue(builder.toString());
        cir.cancel();
    }

}
