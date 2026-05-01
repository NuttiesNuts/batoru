package net.sigma.batoru.item.custom;

import com.github.theredbrain.manaattributes.entity.ManaUsingEntity;
import eu.pb4.trinkets.api.callback.TrinketCallback;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.sigma.batoru.Batoru;
import net.sigma.batoru.component.BatoruComponents;
import net.sigma.batoru.component.GauntletContainerContents;
import net.sigma.batoru.item.BatoruItems;
import net.sigma.batoru.sound.BatoruSounds;
import net.sigma.batoru.spell.SpellRegistry;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class GauntletItem extends Item implements TrinketCallback {
    public GauntletItem(Properties properties) {
        super(properties.component(BatoruComponents.CONTAINER, GauntletContainerContents.EMPTY));
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
        if (itemStack.has(BatoruComponents.OWNER)){
            String owner = itemStack.get(BatoruComponents.OWNER);
            builder.accept(Component.translatable("item.batoru.gauntlet.description.owner", owner).withStyle(ChatFormatting.DARK_GRAY));
        }

        GauntletContainerContents contents = itemStack.get(BatoruComponents.CONTAINER);
        if (contents != null) {
            contents.addToTooltip(context, builder, tooltipFlag, itemStack);
        }
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
        }

        if (player.isCrouching()){ // this is outside to allow for "card stealing" if someone gets a hold of someone else's gauntlet
            GauntletContainerContents contents = stack.get(BatoruComponents.CONTAINER);
            ItemStack card = contents.copyOne();

            if (!player.addItem(card)) {
                player.drop(card, false);
            }

            stack.set(BatoruComponents.CONTAINER, GauntletContainerContents.EMPTY);
        }

        return InteractionResult.SUCCESS;
    }
}
