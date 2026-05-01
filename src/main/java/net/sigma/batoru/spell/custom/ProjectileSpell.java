package net.sigma.batoru.spell.custom;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.sigma.batoru.Batoru;
import net.sigma.batoru.entity.custom.EnergyBall;
import net.sigma.batoru.spell.Spell;

public class ProjectileSpell implements Spell {

    public static final Identifier ID = Identifier.fromNamespaceAndPath(Batoru.MOD_ID, "projectile_spell");

    @Override
    public Identifier id() {
        return ID;
    }

    @Override
    public int range() {
        return 400;
    }

    @Override
    public int cooldownTicks() {
        return 20;
    }

    @Override
    public float manaCost() {
        return 2;
    }

    @Override
    public Component displayName() {
        return Component.literal("Projectile Spell");
    }

    @Override
    public void cast(ServerPlayer player, ItemStack gauntletStack) {
        System.out.println("mi bombo casted");
        double d = 20.0;
        Vec3 viewVector = player.getViewVector(1.0F);
        Vec3 direction = new Vec3(viewVector.x, viewVector.y, viewVector.z);
        EnergyBall entity = new EnergyBall(player.level(), player, (direction.normalize()).scale(d));
        entity.setPos(player.getX() + viewVector.x, player.getY(0.5) + 0.5, entity.getZ() + viewVector.z);
        entity.setStats(range());
        Projectile.spawnProjectile(entity, player.level(), gauntletStack);
    }
}
