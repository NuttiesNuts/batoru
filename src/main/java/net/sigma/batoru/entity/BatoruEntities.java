package net.sigma.batoru.entity;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.sigma.batoru.Batoru;
import net.sigma.batoru.entity.custom.EnergyBall;

public class BatoruEntities {
    //who up pondering they orb? v2

    public static final EntityType<EnergyBall> ENERGY_BALL = register(
            "energy_ball",
            EntityType.Builder.<EnergyBall>of(EnergyBall::new, MobCategory.MISC)
                    .noLootTable().sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(10)
    );


    private static <T extends Entity> EntityType<T> register(String name, EntityType.Builder<T> builder) {
        ResourceKey<EntityType<?>> key = ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(Batoru.MOD_ID, name));
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, key, builder.build(key));
    }

    public static void initialize()
    {

    }
}
