package net.sigma.batoru.item.custom.sword;

import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ToolMaterial;
import net.sigma.batoru.item.FancyNameItem;

public class StreetAxeItem extends FancyNameItem {
    public StreetAxeItem(Properties properties) {
        super(properties.axe(ToolMaterial.NETHERITE, 5.0F, -3F).fireResistant().rarity(Rarity.COMMON).useCooldown(15), 0xffffff, 0xc0cbdc);
    }
}
