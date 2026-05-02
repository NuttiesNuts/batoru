package net.sigma.batoru.spell;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.sigma.batoru.Batoru;
import net.sigma.batoru.item.custom.SpellCardItem;
import net.sigma.batoru.spell.custom.ProjectileSpell;
import net.sigma.batoru.spell.custom.StarfallSpell;

import java.util.ArrayList;
import java.util.List;

public class BatoruSpells {
    public static final List<Item> SPELL_CARDS = new ArrayList<>();

    public static void initialize(){
        register(new ProjectileSpell());
        register(new StarfallSpell());
    }

    private static void register(Spell spell) {
        SpellRegistry.register(spell);

        String cardName = spell.id().getPath() + "_card";

        ResourceKey<Item> itemKey = ResourceKey.create(
                Registries.ITEM,
                Identifier.fromNamespaceAndPath(Batoru.MOD_ID, cardName)
        );

        Item card = Registry.register(
                BuiltInRegistries.ITEM,
                itemKey,
                new SpellCardItem(new Item.Properties().setId(itemKey), spell)
        );

        SPELL_CARDS.add(card);
    }
}
