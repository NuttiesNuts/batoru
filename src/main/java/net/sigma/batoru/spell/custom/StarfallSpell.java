package net.sigma.batoru.spell.custom;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.sigma.batoru.Batoru;
import net.sigma.batoru.sound.BatoruSounds;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class StarfallSpell implements MultiPhaseSpell {
    public static final Identifier ID = Identifier.fromNamespaceAndPath(Batoru.MOD_ID, "starfall_spell");

    private static final int APEX_TICKS = 10;
    private static final double LAUNCH_VEL = 1.8;
    private static final double DIVE_SPEED = 2.67;
    private static final double IMPACT_RADIUS = 4.0;
    private static final float IMPACT_DAMAGE = 8.0f;
    private static final double KNOCKBACK = 1.5;

    public enum Phase {LAUNCHING, DIVING, IDLE}

    public static final Map<UUID, Phase> phases = new HashMap<>();
    private static final Map<UUID, Integer> ticks = new HashMap<>();

    @Override
    public Identifier id() {
        return ID;
    }

    @Override
    public int range() {
        return 0;
    }

    @Override
    public int cooldownTicks() {
        return 120;
    }

    @Override
    public float manaCost() {
        return 6.7f;
    }

    @Override
    public Component displayName() {
        return Component.literal("Starfall Spell");
    }

    @Override
    public void cast(ServerPlayer player, ItemStack gauntletStack) {
        if (!phases.containsKey(player.getUUID())) {
            Vec3 current = player.getDeltaMovement();
            player.setDeltaMovement(current.x * 0.3, LAUNCH_VEL, current.z * 0.3);

            player.connection.send(new ClientboundSetEntityMotionPacket(player));

            phases.put(player.getUUID(), Phase.LAUNCHING);
            ticks.put(player.getUUID(), 0);

            player.level().sendParticles(ParticleTypes.ELECTRIC_SPARK, player.getX(), player.getY(), player.getZ(), 20, 0.3, 0.1, 0.3, 0.1);
            player.level().playSound(null, player.blockPosition(), BatoruSounds.STAR_IMPULSE, SoundSource.NEUTRAL,0.67f,1F / (player.level().getRandom().nextFloat() * 0.4F + 0.8F));
        }
    }

    @Override
    public boolean tick(ServerPlayer player, ItemStack gauntlet) {
        UUID uuid = player.getUUID();
        Phase phase = phases.get(uuid);
        if (phase == null) {
            return false;
        }

        ServerLevel world = player.level();
        int t = ticks.merge(uuid, 1, Integer::sum);

        if (phase == Phase.LAUNCHING) {
            world.sendParticles(ParticleTypes.END_ROD, player.getX(), player.getY(), player.getZ(), 2, 0.1, 0.1, 0.1, 0.5);
            world.sendParticles(ParticleTypes.TRIAL_SPAWNER_DETECTED_PLAYER_OMINOUS, player.getX(), player.getY(), player.getZ(), 3, 0.2, 0, 0.2, 0.02);

            if (t >= APEX_TICKS || player.getDeltaMovement().y < 0) {
                phases.put(uuid, Phase.DIVING);
                ticks.put(uuid, 0);
                applyDiveVelocity(player);
            }

        } else if (phase == Phase.DIVING) {
            applyDiveVelocity(player);
            //world.sendParticles(ParticleTypes.ELECTRIC_SPARK, player.getX(), player.getY(), player.getZ(), 5, 0.2, 0.2, 0.2, 0.05);

            if (player.onGround()/* || t > 15*/) {
                onImpact(player, world);
                return false;
            }
        }

        return true;
    }

    private void applyDiveVelocity(ServerPlayer player) {
        player.setDeltaMovement(0, -DIVE_SPEED, 0);
        player.connection.send(new ClientboundSetEntityMotionPacket(player));
    }

    private static void onImpact(ServerPlayer player, ServerLevel world) {
        Vec3 pos = player.position();

        world.sendParticles(ParticleTypes.ELECTRIC_SPARK, pos.x, pos.y + 0.5, pos.z, 60, IMPACT_RADIUS * 0.5, 0.3, IMPACT_RADIUS * 0.5, 0.15);

        int count = 32;
        for (int i = 0; i < count; i++) {
            double angle = (2 * Math.PI / count) * i;
            world.sendParticles(ParticleTypes.END_ROD, pos.x + IMPACT_RADIUS * Math.cos(angle), pos.y + 0.1, pos.z + IMPACT_RADIUS * Math.sin(angle), 1, 0, 0.05, 0, 0.02);
        }

        int count1 = 8;
        for (int i = 0; i < count1; i++) {
            double angle = (2 * Math.PI / count1) * i;
            world.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, pos.x + 1 * Math.cos(angle), pos.y -0.5, pos.z + 1 * Math.sin(angle), 1, 0, 0.05, 0, 0.02);
        }

        DamageSource source = player.damageSources().playerAttack(player);
        AABB aoe = new AABB(pos.x - IMPACT_RADIUS, pos.y - 1, pos.z - IMPACT_RADIUS, pos.x + IMPACT_RADIUS, pos.y + IMPACT_RADIUS, pos.z + IMPACT_RADIUS);

        for (LivingEntity target : world.getEntitiesOfClass(LivingEntity.class, aoe)) {
            if (target == player) continue;
            target.hurtServer(world, source, IMPACT_DAMAGE);
            Vec3 knockDir = target.position().subtract(pos).normalize();
            target.knockback(KNOCKBACK, -knockDir.x, -knockDir.z);
        }
    }

    @Override
    public boolean isActive(ServerPlayer player) {
        return phases.containsKey(player.getUUID());
    }

    @Override
    public void onEnd(ServerPlayer player, ItemStack gauntlet) {
        UUID uuid = player.getUUID();
        phases.remove(uuid);
        ticks.remove(uuid);
    }

    public boolean hitGround(Player player){
        return player.onGround();
    }
}
