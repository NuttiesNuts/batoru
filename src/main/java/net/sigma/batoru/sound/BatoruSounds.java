package net.sigma.batoru.sound;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.sigma.batoru.Batoru;

public class BatoruSounds {
    private BatoruSounds() {
        // private empty constructor to avoid accidental instantiation
    }

    public static final SoundEvent DENIED = registerInterfaceSound("denied");
    public static final SoundEvent TECH_SLASH = registerInterfaceSound("tech_slash");
    public static final SoundEvent TELEPORT = registerSpellSound("teleport");


    private static SoundEvent registerInterfaceSound(String id) {
        Identifier identifier = Identifier.fromNamespaceAndPath(Batoru.MOD_ID, "interface/" + id);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, identifier, SoundEvent.createVariableRangeEvent(identifier));
    }

    private static SoundEvent registerSlashSound(String id) {
        Identifier identifier = Identifier.fromNamespaceAndPath(Batoru.MOD_ID, "slash/" + id);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, identifier, SoundEvent.createVariableRangeEvent(identifier));
    }

    private static SoundEvent registerSpellSound(String id) {
        Identifier identifier = Identifier.fromNamespaceAndPath(Batoru.MOD_ID, "spell/" + id);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, identifier, SoundEvent.createVariableRangeEvent(identifier));
    }

    public static void initialize(){
    }
}
