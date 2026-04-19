package net.sigma.batoru.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.sigma.batoru.Batoru;
import net.sigma.batoru.item.custom.sword.TechSwordItem;
import net.sigma.batoru.sound.BatoruSounds;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {
	protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
		super(entityType, level);
	}

	@Inject(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;getAttackStrengthScale(F)F"))
	private void onAttack(Entity target, CallbackInfo ci) {
		Player player = (Player) (Object) this;

		if (player.getAttackStrengthScale(0.5F) > 0.9F) {
			ItemStack held = player.getMainHandItem();

			if (held.getItem() instanceof TechSwordItem) {
				if (player.level() instanceof ServerLevel serverLevel) {
					double dx = -Mth.sin(player.getYRot() * (float) (Math.PI / 180.0));
					double dz = Mth.cos(player.getYRot() * (float) (Math.PI / 180.0));

					serverLevel.sendParticles(Batoru.TECH_SWEEP, player.getX() + dx, player.getY(0.5), player.getZ() + dz, 0, dx, 0.0, dz, 0.0);

					serverLevel.playSound(null,
							player.getX(), player.getY(), player.getZ(),
							BatoruSounds.TECH_SLASH, SoundSource.PLAYERS,
							1.0F, 1.0F
					);
				}
			}
		}
	}
}