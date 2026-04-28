package net.sigma.batoru.item.custom;

import eu.pb4.trinkets.api.callback.TrinketCallback;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import net.sigma.batoru.Batoru;
import net.sigma.batoru.component.BatoruComponents;
import net.sigma.batoru.sound.BatoruSounds;

import java.util.List;
import java.util.Objects;
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

            if (player instanceof ServerPlayer){
                ((ServerPlayer) player).getAdvancements().award(level.getServer().getAdvancements().get(Identifier.fromNamespaceAndPath(Batoru.MOD_ID, "use_gauntlet")), "used_gauntlet");
            }
        }

        if (stack.has(BatoruComponents.OWNER)){
            if (!Objects.equals(stack.get(BatoruComponents.OWNER), player.getPlainTextName())) {
                player.sendOverlayMessage(Component.translatable("item.batoru.gauntlet.invalid_owner").withStyle(ChatFormatting.DARK_RED));

                if (level.isClientSide()) {
                    level.playLocalSound(player.blockPosition(), BatoruSounds.DENIED, SoundSource.PLAYERS, 1.0F, 1.0F, false);
                }
            }
            if (stack.get(BatoruComponents.OWNER) == player.getPlainTextName()){
                // cast spell
            }
        }


        return InteractionResult.SUCCESS;
    }
}
