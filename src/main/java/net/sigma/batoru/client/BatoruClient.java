package net.sigma.batoru.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.sigma.batoru.Batoru;
import net.sigma.batoru.networking.WeaponAbilityPayload;
import org.lwjgl.glfw.GLFW;

import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;

public class BatoruClient implements ClientModInitializer {

    KeyMapping.Category CATEGORY = KeyMapping.Category.register(
            Identifier.fromNamespaceAndPath(Batoru.MOD_ID, "category")
    );

    KeyMapping abilityKey = KeyMappingHelper.registerKeyMapping(
            new KeyMapping(
                    "key." + Batoru.MOD_ID + ".ability", // The translation key for the key mapping.
                    InputConstants.Type.KEYSYM, // // The type of the keybinding; KEYSYM for keyboard, MOUSE for mouse.
                    GLFW.GLFW_KEY_R, // The GLFW keycode of the key.
                    CATEGORY // The category of the mapping.
            ));

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (abilityKey.consumeClick()) {
                if (client.player != null) {
                    WeaponAbilityPayload payload = new WeaponAbilityPayload();
                    ClientPlayNetworking.send(payload);
                }
            }
        });
    }
}
