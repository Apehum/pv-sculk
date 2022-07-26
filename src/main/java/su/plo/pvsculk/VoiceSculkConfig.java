package su.plo.pvsculk;

import com.electronwill.nightconfig.core.conversion.Path;
import com.electronwill.nightconfig.core.conversion.SpecDoubleInRange;
import com.electronwill.nightconfig.core.conversion.SpecIntInRange;

public class VoiceSculkConfig {
    @Path("sneak_activation")
    public boolean sneakActivation = true;

    @Path("activation_threshold")
    @SpecDoubleInRange(min = -60, max = 0)
    public double activationThreshold = -30;

    @Path("notification_radius")
    @SpecIntInRange(min = 1, max = 128)
    public int notificationRadius = 16;
}
