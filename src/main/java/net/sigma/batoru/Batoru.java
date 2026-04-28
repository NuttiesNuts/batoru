package net.sigma.batoru;

import eu.pb4.trinkets.api.TrinketAttachment;
import eu.pb4.trinkets.api.TrinketsApi;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.sigma.batoru.component.BatoruComponents;
import net.sigma.batoru.item.BatoruItems;
import net.sigma.batoru.networking.WeaponAbilityPayload;
import net.sigma.batoru.rank.CombatRank;
import net.sigma.batoru.rank.RankUtil;
import net.sigma.batoru.sound.BatoruSounds;
import net.sigma.batoru.spell.BatoruSpells;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Batoru implements ModInitializer {

    public static final String MOD_ID = "batoru";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID.substring(0, 1).toUpperCase() + MOD_ID.substring(1));

    public static final SimpleParticleType TECH_SWEEP = FabricParticleTypes.simple();

    @Override
    public void onInitialize() {
        LOGGER.info("https://discord.com/channels/674795434509598731/1490152095515414669/1490634229254590526 what should my username be? idk, maybe something actually normal. You are not tuff for picking \"what should my username be?\" as your username, I told you to check your logs, now we are both here staring at each other and debating what should my username be? well anyways. SKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDI");
        // who up modding they fest?
        for (int i = 0; i < 68; i++) {
            System.out.println(i + ": SKIBIDI");
        } // we do a lil funny goofy thingamajig

        BatoruSounds.initialize();
        BatoruSpells.initialize();
        BatoruItems.initialize();
        BatoruComponents.initialize();

        Registry.register(BuiltInRegistries.PARTICLE_TYPE, Identifier.fromNamespaceAndPath(MOD_ID, "tech_sweep"), TECH_SWEEP);

        PayloadTypeRegistry.serverboundPlay().register(WeaponAbilityPayload.TYPE, WeaponAbilityPayload.CODEC);

        // payload thingy
        ServerPlayNetworking.registerGlobalReceiver(WeaponAbilityPayload.TYPE, (payload, context) -> {
            context.server().execute(() -> {
                ServerPlayer player = context.player();

                ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);

                BlockPos blockPos = new BlockPos((int) player.getX(), (int) player.getY(), (int) player.getZ());

                if (stack.is(BatoruItems.TECH_SWORD) && !stack.has(BatoruComponents.TELEPORT_POSITION)){
                    stack.set(BatoruComponents.TELEPORT_POSITION, blockPos);
                }
            });
        });


        ServerLivingEntityEvents.AFTER_DEATH.register((entity, damageSource) -> {
            if ((damageSource.getEntity() instanceof ServerPlayer killer) && (entity instanceof ServerPlayer)){
                TrinketAttachment trinkets = TrinketsApi.getAttachment(killer);
                ItemStack gauntlet = trinkets.getEquipped(BatoruItems.GAUNTLET).getFirst().getB();
                if (gauntlet.isEmpty()) return;

                gauntlet.set(BatoruComponents.RANK, RankUtil.getRank(killer));

                CombatRank rank = RankUtil.getRank(killer);
                RankUtil.awardRankAdvancement(killer, rank);
            }
        });
    }
}
