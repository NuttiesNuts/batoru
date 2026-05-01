package net.sigma.batoru.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleProviderRegistry;
import net.minecraft.client.particle.AttackSweepParticle;
import net.sigma.batoru.Batoru;
import net.sigma.batoru.networking.WeaponAbilityPayload;
import org.lwjgl.glfw.GLFW;

import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;

@Environment(EnvType.CLIENT)
public class BatoruClient implements ClientModInitializer {

    KeyMapping.Category CATEGORY = KeyMapping.Category.register(
            Identifier.fromNamespaceAndPath(Batoru.MOD_ID, "category")
    );

    KeyMapping weaponAbilityKey = KeyMappingHelper.registerKeyMapping(
            new KeyMapping(
                    "key." + Batoru.MOD_ID + ".ability",
                    InputConstants.Type.KEYSYM,
                    GLFW.GLFW_KEY_R,
                    CATEGORY
            ));
    KeyMapping spellCastKey = KeyMappingHelper.registerKeyMapping(
            new KeyMapping(
                    "key." + Batoru.MOD_ID + ".spell_cast",
                    InputConstants.Type.KEYSYM,
                    GLFW.GLFW_KEY_LEFT_ALT,
                    CATEGORY
            ));

    @Override
    public void onInitializeClient() {
        ParticleProviderRegistry.getInstance().register(Batoru.TECH_SWEEP, AttackSweepParticle.Provider::new);

        BatoruModelLayers.initialize();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (weaponAbilityKey.consumeClick()) {
                if (client.player != null) {
                    WeaponAbilityPayload payload = new WeaponAbilityPayload();
                    ClientPlayNetworking.send(payload);
                }
            }
            while (spellCastKey.consumeClick()) {
                if (client.player != null) {
                    WeaponAbilityPayload payload = new WeaponAbilityPayload();
                    ClientPlayNetworking.send(payload);
                }
            }
        });
    }
}
