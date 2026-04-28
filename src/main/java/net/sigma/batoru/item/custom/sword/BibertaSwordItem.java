package net.sigma.batoru.item.custom.sword;

import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ToolMaterial;
import net.sigma.batoru.item.FancyNameItem;

public class BibertaSwordItem extends FancyNameItem {
    public BibertaSwordItem(Properties properties) {
        super(properties.sword(ToolMaterial.NETHERITE, 4.5F, -2.4F).fireResistant().rarity(Rarity.EPIC).useCooldown(15), 0x124e89, 0xd44eac);
    }
}
