package net.sigma.batoru.client;

import com.github.theredbrain.manaattributes.ManaAttributesClient;
import com.github.theredbrain.manaattributes.config.ClientConfig;
import com.github.theredbrain.resourcebarapi.ResourceBarAPI;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.minecraft.resources.Identifier;
import net.sigma.batoru.Batoru;

import java.util.HashMap;

public class BatoruManaBar implements ClientModInitializer {
    private static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(Batoru.MOD_ID, "textures/gui/mana/" + path);
    }

    private static HashMap<Integer, Identifier> textureMap(String path) {
        HashMap<Integer, Identifier> map = new HashMap<>();
        map.put(0, id(path));
        return map;
    }

    @Override
    public void onInitializeClient() {
        ClientLifecycleEvents.CLIENT_STARTED.register(client -> {
            ClientConfig config = ManaAttributesClient.CLIENT_CONFIG;

            config.mana_bar_display = ResourceBarAPI.ResourceBarDisplay.SMOOTH;

            ClientConfig.SmoothBarSettings.TextureSettings tex = config.smoothBarSettings.textureSettings;

            tex.backgroundTextureSettings.texture_ids.accept(textureMap("horizontal_mana_background.png"));

            tex.progressTextureSettings.progress_texture_ids.accept(textureMap("horizontal_mana_progress.png"));
            tex.progressTextureSettings.progress_decrease_animation_texture_ids.accept(textureMap("horizontal_mana_progress_decrease_animation.png"));
            tex.progressTextureSettings.progress_increase_animation_texture_ids.accept(textureMap("horizontal_mana_progress_increase_animation.png"));
            tex.progressTextureSettings.progress_increase_value_texture_ids.accept(textureMap("horizontal_mana_progress_increase_value.png"));

            tex.reservedTextureSettings.texture_ids.accept(textureMap("horizontal_mana_reserved.png"));

            //tex.overlayTextureSettings.texture_ids.accept(textureMap("horizontal_mana_overlay.png"));
        });
    }
}
