package ru.nexsqaud.clickthrough.mixins;

import ru.nexsqaud.clickthrough.ClickThrough;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public class SneakingCancelsInteractionMixin {
    @Inject(method = "isSecondaryUseActive", at = @At("HEAD"), cancellable = true)
    private void noCancelWhenDyeing(CallbackInfoReturnable<Boolean> cir) {
        if (((Object) this) instanceof LocalPlayer) {
            if (ClickThrough.isDyeOnSign) {
                cir.setReturnValue(false);
                cir.cancel();
                ClickThrough.isDyeOnSign = false;
            }
        }
    }
}
