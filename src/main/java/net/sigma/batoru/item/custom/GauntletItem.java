package net.sigma.batoru.item.custom;

import eu.pb4.trinkets.api.callback.TrinketCallback;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import net.sigma.batoru.component.BatoruComponents;

import java.util.List;
import java.util.function.Consumer;

public class GauntletItem extends Item implements TrinketCallback {
    public static final int MAX_SLOTS = 3;

    public GauntletItem(Properties properties) {
        super(properties.component(BatoruComponents.SPELL_CARDS, List.of()));
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
        if (itemStack.has(BatoruComponents.OWNER)){
            String owner = itemStack.get(BatoruComponents.OWNER);
            builder.accept(Component.translatable("item.batoru.gauntlet.description.owner", owner).withStyle(ChatFormatting.DARK_GRAY));
        }

        if (itemStack.hasNonDefault(BatoruComponents.SPELL_CARDS)){
            builder.accept(Component.translatable("item.batoru.gauntlet.description.cards").withStyle(ChatFormatting.GRAY));
        }
    }

    public int getGetMaxSlots() {
        return MAX_SLOTS;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!stack.has(BatoruComponents.OWNER)) { // set owner
            stack.set(BatoruComponents.OWNER, player.getPlainTextName());
        }

        return InteractionResult.SUCCESS;
    }
}
