package net.sigma.batoru.item.custom;

import eu.pb4.trinkets.api.TrinketAttachment;
import eu.pb4.trinkets.api.TrinketsApi;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.sigma.batoru.component.BatoruComponents;
import net.sigma.batoru.item.BatoruItems;
import net.sigma.batoru.component.GauntletContainerContents;
import net.sigma.batoru.spell.Spell;

import java.util.List;

public class SpellCardItem extends Item {

    private final Spell spell;
    private final Identifier spellId;

    public SpellCardItem(Properties properties, Spell spell) {
        super(properties.stacksTo(8));
        this.spell = spell;
        this.spellId = spell.id();
    }

    public Identifier getSpellId() {
        return spellId;
    }

    public Spell getSpell() {
        return spell;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        TrinketAttachment trinkets = TrinketsApi.getAttachment(player);

        if (trinkets.isEquipped(BatoruItems.GAUNTLET)){
            ItemStack gauntlet = trinkets.getEquipped(BatoruItems.GAUNTLET).getFirst().getB();

            gauntlet.set(BatoruComponents.CONTAINER, GauntletContainerContents.fromItems(List.of(player.getMainHandItem())));

            player.getMainHandItem().shrink(1);

            player.sendOverlayMessage(Component.translatable("item.batoru.spell_card.equip_message", spell.displayName()).withStyle(ChatFormatting.GRAY));

            return InteractionResult.SUCCESS;
        }

        return InteractionResult.FAIL;
    }
}
