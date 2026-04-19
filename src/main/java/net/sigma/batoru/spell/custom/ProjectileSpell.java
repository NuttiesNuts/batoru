package net.sigma.batoru.spell.custom;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.sigma.batoru.Batoru;
import net.sigma.batoru.spell.Spell;

public class ProjectileSpell implements Spell {

    public static final Identifier ID = Identifier.fromNamespaceAndPath(Batoru.MOD_ID, "projectile_spell");

    @Override
    public Identifier id() {
        return ID;
    }

    @Override
    public int cooldownTicks() {
        return 20;
    }

    @Override
    public Component displayName() {
        return Component.literal("Projectile Spell");
    }

    @Override
    public void cast(ServerPlayer player, ItemStack gauntletStack) {
    }
}
