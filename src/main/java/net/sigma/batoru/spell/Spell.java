package net.sigma.batoru.spell;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public interface Spell {
    Identifier id();

    int range();

    int cooldownTicks();

    float manaCost();

    Component displayName();

    void cast(ServerPlayer player, ItemStack gauntletStack);
}
