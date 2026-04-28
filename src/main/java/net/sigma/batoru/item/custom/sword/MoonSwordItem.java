package net.sigma.batoru.item.custom.sword;

import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ToolMaterial;
import net.sigma.batoru.item.FancyNameItem;

public class MoonSwordItem extends FancyNameItem {
    public MoonSwordItem(Properties properties) {
        super(properties.sword(ToolMaterial.NETHERITE, 4.0F, -2.4F).fireResistant().rarity(Rarity.RARE).useCooldown(15), 0x891cbb, 0x2ce8f5);
    }
}
