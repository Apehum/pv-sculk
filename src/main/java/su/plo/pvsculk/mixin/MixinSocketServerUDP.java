package su.plo.pvsculk.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.gameevent.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import su.plo.pvsculk.PlasmoVoiceSculk;
import su.plo.voice.client.sound.opus.OpusDecoder;
import su.plo.voice.client.utils.AudioUtils;
import su.plo.voice.common.packets.Packet;
import su.plo.voice.common.packets.udp.VoiceServerPacket;
import su.plo.voice.server.VoiceServer;
import su.plo.voice.server.socket.SocketServerUDP;

@Mixin(value = SocketServerUDP.class, remap = false)
public abstract class MixinSocketServerUDP {
    private static OpusDecoder decoder;

    @Inject(method = "sendToNearbyPlayers", at = @At("HEAD"))
    private static void sendToNearbyPlayers(Packet packet, ServerPlayer player, double maxDistance, CallbackInfo ci) {
        if (!(packet instanceof VoiceServerPacket serverPacket)) return;

        if (decoder == null) {
            var sampleRate = VoiceServer.getServerConfig().getSampleRate();
            decoder = new OpusDecoder(
                    sampleRate,
                    (sampleRate / 1000) * 2 * 20,
                    1024
            );
        }

        if (!PlasmoVoiceSculk.CONFIG.sneakActivation && player.isCrouching()) return;

        decoder.reset();
        var decoded = decoder.decode(serverPacket.getData());
        var offset = AudioUtils.getActivationOffset(decoded, PlasmoVoiceSculk.CONFIG.activationThreshold);
        if (offset <= 0) return;

        var lastActivation = PlasmoVoiceSculk.LAST_ACTIVATIONS.getOrDefault(player.getUUID(), 0L);
        if (System.currentTimeMillis() - lastActivation < 500L) return;
        PlasmoVoiceSculk.LAST_ACTIVATIONS.put(player.getUUID(), System.currentTimeMillis());

        VoiceServer.getServer().execute(() -> player.gameEvent(GameEvent.EAT));
    }
}
