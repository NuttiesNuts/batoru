package net.sigma.batoru.item.custom.sword;

import com.github.theredbrain.manaattributes.entity.ManaUsingEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.Vec3;
import net.sigma.batoru.component.BatoruComponents;
import net.sigma.batoru.item.FancyNameItem;
import net.sigma.batoru.sound.BatoruSounds;

import java.util.function.Consumer;

public class TechSwordItem extends FancyNameItem {
    public static final float MANA_COST = 6.7f;

    public TechSwordItem(Properties properties) {
        super(properties.sword(ToolMaterial.NETHERITE, 4.0F, -2.4F).fireResistant().rarity(Rarity.UNCOMMON).useCooldown(15), 0x2ce8f5, 0x0095e9);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getMainHandItem();

        float currentMana = ((ManaUsingEntity) player).manaattributes$getMana();

        if (stack.has(BatoruComponents.TELEPORT_POSITION) && stack.get(BatoruComponents.TELEPORT_POSITION) != null){
            if (currentMana >= MANA_COST) {

                BlockPos blockPos = stack.get(BatoruComponents.TELEPORT_POSITION);

                addParticle(ParticleTypes.TRIAL_SPAWNER_DETECTED_PLAYER_OMINOUS, new Vec3(player.blockPosition()), level);
                teleport(level, player, blockPos);
                addParticle(ParticleTypes.TRIAL_SPAWNER_DETECTED_PLAYER_OMINOUS, new Vec3(blockPos), level);

                stack.remove(BatoruComponents.TELEPORT_POSITION);

                ((ManaUsingEntity) player).manaattributes$addMana(-MANA_COST);

                level.playSound(null, player.blockPosition(), BatoruSounds.TELEPORT, SoundSource.NEUTRAL, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));

                player.awardStat(Stats.ITEM_USED.get(this));
            } else if (currentMana <= MANA_COST){
                player.sendOverlayMessage(Component.translatable("batoru.insufficient_mana").withStyle(ChatFormatting.DARK_RED));

                if (level.isClientSide()) {
                    level.playLocalSound(player.blockPosition(), BatoruSounds.DENIED, SoundSource.PLAYERS, 1.0F, 1.0F, false);
                }

                return InteractionResult.FAIL;
            }
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    private static void addParticle(SimpleParticleType type, Vec3 vec, Level level) {
        if (level instanceof ServerLevel){
            ((ServerLevel) level).sendParticles(type, vec.x, vec.y, vec.z, 30, 0.3, 0.5, 0.3, 0.05);
        }

    }

    public void teleport(Level level, Player player, BlockPos blockPos){
        Vec3 pos = new Vec3(blockPos);

        if (level instanceof ServerLevel){
            player.teleport(new TeleportTransition((ServerLevel) level, pos, Vec3.ZERO, player.getYRot(), player.getXRot(), TeleportTransition.DO_NOTHING));
        }
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
        if (itemStack.has(BatoruComponents.TELEPORT_POSITION)){
            String pos = itemStack.get(BatoruComponents.TELEPORT_POSITION).toShortString();

            builder.accept(Component.literal(pos).withStyle(ChatFormatting.DARK_GRAY));
        }
    }
}
