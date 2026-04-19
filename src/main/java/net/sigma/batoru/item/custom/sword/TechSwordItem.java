package net.sigma.batoru.item.custom.sword;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.level.Level;
import net.sigma.batoru.item.FancyNameItem;

public class TechSwordItem extends FancyNameItem {
    public TechSwordItem(Properties properties) {
        super(properties.sword(ToolMaterial.NETHERITE, 4.0F, -2.4F).fireResistant().rarity(Rarity.UNCOMMON), 0x2ce8f5, 0x0095e9);
    }

    @Override
    public boolean allowComponentsUpdateAnimation(Player player, InteractionHand hand, ItemStack oldStack, ItemStack newStack) {
        return false;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (level.isClientSide()){
            return InteractionResult.PASS;
        }

        return InteractionResult.SUCCESS;
    }

    public void teleport(){

    }
}
