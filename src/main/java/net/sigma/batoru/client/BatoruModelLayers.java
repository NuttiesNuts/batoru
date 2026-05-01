package net.sigma.batoru.client;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.resources.Identifier;
import net.sigma.batoru.Batoru;
import net.sigma.batoru.client.renderer.EnergyBallRenderer;
import net.sigma.batoru.entity.BatoruEntities;

public class BatoruModelLayers {

    public static final ModelLayerLocation BALL = createMain("ball");

    private static ModelLayerLocation createMain(String name) {
        return new ModelLayerLocation(Identifier.fromNamespaceAndPath(Batoru.MOD_ID, name), "main");
    }

    public static void initialize() {
        EntityRenderers.register(BatoruEntities.ENERGY_BALL, EnergyBallRenderer::new);
    }
}
