package net.sigma.batoru.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.sigma.batoru.Batoru;
import net.sigma.batoru.item.BatoruItems;
import net.sigma.batoru.spell.BatoruSpells;

import java.util.concurrent.CompletableFuture;

public class BatoruItemTagProvider extends FabricTagsProvider.ItemTagsProvider {
    public BatoruItemTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    public static final TagKey<Item> SPELL_CARDS = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Batoru.MOD_ID, "spell_cards"));

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        for (int i = 0; i < BatoruSpells.SPELL_CARDS.size(); i++) {
            valueLookupBuilder(SPELL_CARDS)
                    .add(BatoruSpells.SPELL_CARDS.get(i))
                    .setReplace(false);
        }

        valueLookupBuilder(ItemTags.SWORDS)
                .add(BatoruItems.TECH_SWORD)
                .add(BatoruItems.BIBERTA_SWORD)
                .add(BatoruItems.ASTRAMENTAL_SWORD);

        valueLookupBuilder(ItemTags.AXES)
                .add(BatoruItems.STREET_AXE);

        valueLookupBuilder(ItemTags.MELEE_WEAPON_ENCHANTABLE)
                .add(BatoruItems.TECH_SWORD)
                .add(BatoruItems.BIBERTA_SWORD)
                .add(BatoruItems.ASTRAMENTAL_SWORD)
                .add(BatoruItems.STREET_AXE);
    }
}
