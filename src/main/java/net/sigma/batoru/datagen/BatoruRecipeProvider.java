package net.sigma.batoru.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SmithingTransformRecipeBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.sigma.batoru.item.BatoruItems;

import java.util.concurrent.CompletableFuture;

public class BatoruRecipeProvider extends FabricRecipeProvider {
    public BatoruRecipeProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
        return new RecipeProvider(registries, output) {
            @Override
            public void buildRecipes() {
                HolderLookup.RegistryLookup<Item> itemLookup = registries.lookupOrThrow(Registries.ITEM);

                shaped(RecipeCategory.COMBAT, BatoruItems.GAUNTLET)
                        .pattern("HLH")
                        .pattern("HRH")
                        .define('L', Items.LAPIS_LAZULI)
                        .define('H', ConventionalItemTags.IRON_INGOTS)
                        .define('R', Items.RECOVERY_COMPASS)
                        .unlockedBy(getHasName(Items.CRAFTING_TABLE), has(Items.CRAFTING_TABLE))
                        .save(output);

                SmithingTransformRecipeBuilder.smithing(Ingredient.of(Items.BOLT_ARMOR_TRIM_SMITHING_TEMPLATE),
                                Ingredient.of(Items.NETHERITE_SWORD),
                                Ingredient.of(Items.ENDER_PEARL),
                                RecipeCategory.COMBAT,
                                BatoruItems.TECH_SWORD)
                        .unlocks(getHasName(Items.CRAFTING_TABLE), has(Items.CRAFTING_TABLE))
                        .save(output, BatoruItems.TECH_SWORD.toString());
            }
        };
    }

    @Override
    public String getName() {
        return "BatoruRecipeProvider";
    }
}
