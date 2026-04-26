package net.sigma.batoru.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancements.*;
import net.minecraft.advancements.criterion.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.sigma.batoru.Batoru;
import net.sigma.batoru.item.BatoruItems;
import net.sigma.batoru.rank.CombatRank;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class BatoruAdvancementProvider extends FabricAdvancementProvider {
    protected BatoruAdvancementProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(output, registryLookup);
    }

    @Override
    public void generateAdvancement(HolderLookup.Provider provider, Consumer<AdvancementHolder> consumer) {
        final HolderLookup.RegistryLookup<Item> itemLookup = provider.lookupOrThrow(Registries.ITEM);
        AdvancementHolder useGauntlet = Advancement.Builder.advancement()
                .display(
                        BatoruItems.GAUNTLET,
                        Component.literal("Welcome Student!"),
                        Component.literal("Become a Batoru Academia student by using the Student Gauntlet"),
                        Identifier.fromNamespaceAndPath(Batoru.MOD_ID, "gui/grid_slot01"),
                        AdvancementType.TASK,
                        true,
                        true,
                        false
                )
                .addCriterion("used_gauntlet", UsingItemTrigger.TriggerInstance.lookingAt(EntityPredicate.Builder.entity(), ItemPredicate.Builder.item().of(itemLookup,BatoruItems.GAUNTLET)))
                .save(consumer, Batoru.MOD_ID + ":use_gauntlet");

        CombatRank[] ranks = CombatRank.values();

        AdvancementHolder previous = useGauntlet;

        for (CombatRank rank : ranks) {
            String id = rank.name().toLowerCase() + "_rank";

            AdvancementHolder current = Advancement.Builder.advancement()
                    .parent(previous)
                    .display(
                            BatoruItems.GAUNTLET,
                            Component.literal(rank.name() + " Rank"),
                            Component.literal("Defeat players " + rank.requiredKills + " times"),
                            null,
                            AdvancementType.GOAL,
                            true,
                            true,
                            false
                    )
                    .addCriterion(id, new Criterion<>(CriteriaTriggers.IMPOSSIBLE, new ImpossibleTrigger.TriggerInstance()))
                    .rewards(AdvancementRewards.Builder.experience(100))
                    .save(consumer, Batoru.MOD_ID + ":" + id);

            previous = current;
        }
    }
}
