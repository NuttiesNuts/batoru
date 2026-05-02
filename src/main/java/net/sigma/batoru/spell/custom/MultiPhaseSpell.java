package net.sigma.batoru.spell.custom;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.sigma.batoru.spell.Spell;

public interface MultiPhaseSpell extends Spell {

    boolean tick(ServerPlayer player, ItemStack gauntlet);

    boolean isActive(ServerPlayer player);

    default void onEnd(ServerPlayer player, ItemStack gauntlet) {}
}
