package net.sigma.batoru.entity.custom;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.hurtingprojectile.Fireball;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.sigma.batoru.entity.BatoruEntities;

public class EnergyBall extends Fireball {
    private int range = 20;
    private int age = 0;

    public EnergyBall(final EntityType<? extends EnergyBall> type, final Level level) {
        super(type, level);
        accelerationPower = 0.4;
    }


    public EnergyBall(final Level level, final LivingEntity mob, final Vec3 direction) {
        super(BatoruEntities.ENERGY_BALL, mob, direction, level);
        accelerationPower = 0.4;
    }

    public EnergyBall(final Level level, final double x, final double y, final double z, final Vec3 direction) {
        super(BatoruEntities.ENERGY_BALL, x, y, z, direction, level);
        accelerationPower = 0.4;
    }

    public void setStats(int r)
    {
        range = r;
        age = 0;
        accelerationPower = 0.4;
    }


    @Override
    protected void createParticleTrail() {
        ParticleOptions trailParticle = this.getTrailParticle();
        Vec3 position = this.position();
        if (trailParticle != null) {
            this.level().addParticle(trailParticle, position.x, position.y, position.z, 0.0, 0.0, 0.0);
        }
    }

    @Override
    protected ParticleOptions getTrailParticle() {
        return ParticleTypes.CRIT;
    }

    @Override
    protected boolean shouldBurn() {
        return false;
    }

    private boolean cannotHarmPlayer(LivingEntity target)
    {
        Entity owner = this.getOwner();
        if(owner instanceof Player playerAttacker)
            if(target instanceof Player playerTarget)
                return !playerAttacker.canHarmPlayer(playerTarget);
        return false;
    }

    @Override
    protected void onHitEntity(final EntityHitResult hitResult) {
        if (this.level() instanceof ServerLevel serverLevel) {
            Entity var7 = hitResult.getEntity();
            Entity owner = this.getOwner();

            if(var7 instanceof LivingEntity mob)
            {
                DamageSource damageSource = this.damageSources().source(DamageTypes.MAGIC,this, owner);
                playSound(SoundEvents.TRIDENT_THUNDER.value(),0.3f,0.4F / (level().getRandom().nextFloat() * 0.4F + 0.8F));

                if (var7.hurtServer(serverLevel, damageSource,8f))
                {
                    EnchantmentHelper.doPostAttackEffects(serverLevel, var7, damageSource);
                    //if(owner instanceof LivingEntity livingOwner)   livingOwner.heal(4f);
                }
            }
        }
        super.onHitEntity(hitResult);
    }

    @Override
    public void tick() {
        super.tick();
        age++;
        if(age == range && range <= 10)
        {
            this.level().addParticle(ParticleTypes.SMOKE, this.getX(), this.getY(), this.getZ(), 0.0, 0.2, 0.0);
            for(int i = 0; i < 4; i++)
            {
                this.level().addParticle(ParticleTypes.SMOKE, this.getX(), this.getY()+0.5, this.getZ(), this.level().getRandom().nextFloat() * 0.2f - 0.1f, this.level().getRandom().nextFloat() * 0.2F, this.level().getRandom().nextFloat() * 0.2f - 0.1f);
            }
        }
        if(age > range)
        {
            playSound(SoundEvents.BAMBOO_PLACE,5f,0.4F / (level().getRandom().nextFloat() * 0.4F + 0.8F));
            this.discard();
        }
    }

    @Override
    protected void onHitBlock(final BlockHitResult hitResult) {

        playSound(SoundEvents.BAMBOO_PLACE,0.5f,0.4F / (level().getRandom().nextFloat() * 0.4F + 0.8F));
        super.onHitBlock(hitResult);
    }

    @Override
    protected void onHit(final HitResult hitResult) {
        super.onHit(hitResult);
        if (hitResult.getType() == HitResult.Type.BLOCK) {
            this.level().addParticle(ParticleTypes.SMOKE, this.getX(), this.getY()+0.5, this.getZ(), 0.0, 0.2, 0.0);
            for(int i = 0; i < 4; i++)
            {
                this.level().addParticle(ParticleTypes.SMOKE, this.getX(), this.getY()+0.5, this.getZ(), this.level().getRandom().nextFloat() * 0.2f - 0.1f, this.level().getRandom().nextFloat() * 0.2F, this.level().getRandom().nextFloat() * 0.2f - 0.1f);
            }
        }

        if (!this.level().isClientSide()) {
            this.discard();
        }
    }

}