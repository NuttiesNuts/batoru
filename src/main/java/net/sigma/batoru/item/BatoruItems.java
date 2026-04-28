package net.sigma.batoru.item;

import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.sigma.batoru.Batoru;
import net.sigma.batoru.item.custom.GauntletItem;
import net.sigma.batoru.item.custom.SpellCardItem;
import net.sigma.batoru.item.custom.sword.BibertaSwordItem;
import net.sigma.batoru.item.custom.sword.MoonSwordItem;
import net.sigma.batoru.item.custom.sword.StreetAxeItem;
import net.sigma.batoru.item.custom.sword.TechSwordItem;
import net.sigma.batoru.spell.custom.ProjectileSpell;

import java.util.function.Function;

public class BatoruItems {
    public static <T extends Item> T register(String name, Function<Item.Properties, T> itemFactory, Item.Properties settings) {
        // Create the item key.
        ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Batoru.MOD_ID, name));

        // Create the item instance.
        T item = itemFactory.apply(settings.setId(itemKey));

        // Register the item.
        Registry.register(BuiltInRegistries.ITEM, itemKey, item);

        return item;
    }

    public static final Item GAUNTLET = register("gauntlet", GauntletItem::new, new Item.Properties());
    public static final Item PROJECTILE_CARD = register("projectile_card", properties -> new SpellCardItem(properties, new ProjectileSpell()), new Item.Properties());

    public static final Item TECH_SWORD = register("tech_sword", TechSwordItem::new, new Item.Properties());
    public static final Item ASTRAMENTAL_SWORD = register("astramental_sword", MoonSwordItem::new, new Item.Properties());
    public static final Item BIBERTA_SWORD = register("biberta_sword", BibertaSwordItem::new, new Item.Properties());
    public static final Item STREET_AXE = register("street_axe", StreetAxeItem::new, new Item.Properties());

    public static void initialize() {
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.COMBAT)
                .register((creativeTab) -> creativeTab.accept(BatoruItems.GAUNTLET));
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.COMBAT)
                .register((creativeTab) -> creativeTab.accept(BatoruItems.TECH_SWORD));
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.COMBAT)
                .register((creativeTab) -> creativeTab.accept(BatoruItems.ASTRAMENTAL_SWORD));
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.COMBAT)
                .register((creativeTab) -> creativeTab.accept(BatoruItems.BIBERTA_SWORD));
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.COMBAT)
                .register((creativeTab) -> creativeTab.accept(BatoruItems.STREET_AXE));
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.COMBAT)
                .register((creativeTab) -> creativeTab.accept(BatoruItems.PROJECTILE_CARD));
    }

}
