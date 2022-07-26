package su.plo.pvsculk.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import su.plo.pvsculk.PlasmoVoiceSculk;
import su.plo.voice.server.network.ServerNetworkHandler;

import java.util.UUID;

@Mixin(value = ServerNetworkHandler.class, remap = false)
public abstract class MixinServerNetworkHandler {
    @Inject(method = "disconnectClient", at = @At("HEAD"))
    private static void sendToNearbyPlayers(UUID uuid, CallbackInfo ci) {
        PlasmoVoiceSculk.LAST_ACTIVATIONS.remove(uuid);
    }
}
