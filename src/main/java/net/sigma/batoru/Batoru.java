package net.sigma.batoru;

import com.github.theredbrain.manaattributes.entity.ManaUsingEntity;
import eu.pb4.trinkets.api.TrinketAttachment;
import eu.pb4.trinkets.api.TrinketsApi;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.gameevent.GameEvent;
import net.sigma.batoru.component.BatoruComponents;
import net.sigma.batoru.component.GauntletContainerContents;
import net.sigma.batoru.entity.BatoruEntities;
import net.sigma.batoru.item.BatoruItems;
import net.sigma.batoru.item.custom.SpellCardItem;
import net.sigma.batoru.networking.SpellCastPayload;
import net.sigma.batoru.networking.WeaponAbilityPayload;
import net.sigma.batoru.rank.CombatRank;
import net.sigma.batoru.rank.RankUtil;
import net.sigma.batoru.sound.BatoruSounds;
import net.sigma.batoru.spell.BatoruSpells;
import net.sigma.batoru.spell.Spell;
import net.sigma.batoru.spell.SpellRegistry;
import net.sigma.batoru.spell.custom.MultiPhaseSpell;
import net.sigma.batoru.spell.custom.StarfallSpell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;


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
        BatoruEntities.initialize();
        BatoruComponents.initialize();

        Registry.register(BuiltInRegistries.PARTICLE_TYPE, Identifier.fromNamespaceAndPath(MOD_ID, "tech_sweep"), TECH_SWEEP);

        PayloadTypeRegistry.serverboundPlay().register(WeaponAbilityPayload.TYPE, WeaponAbilityPayload.CODEC);
        PayloadTypeRegistry.serverboundPlay().register(SpellCastPayload.TYPE, SpellCastPayload.CODEC);

        // On weapon ability press, save block pos on Tech Sword
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

        ServerPlayNetworking.registerGlobalReceiver(SpellCastPayload.TYPE, (payload, context) -> {
            context.server().execute(() -> {
                ServerPlayer player = context.player();

                float currentMana = ((ManaUsingEntity) player).manaattributes$getMana();

                TrinketAttachment trinkets = TrinketsApi.getAttachment(player);

                if (trinkets.isEquipped(BatoruItems.GAUNTLET)){
                    var equipped = trinkets.getEquipped(BatoruItems.GAUNTLET);
                    if (equipped.isEmpty()) return;

                    ItemStack gauntlet = trinkets.getEquipped(BatoruItems.GAUNTLET).getFirst().getB();

                    if (Objects.equals(gauntlet.get(BatoruComponents.OWNER), player.getPlainTextName())){
                        GauntletContainerContents contents = gauntlet.get(BatoruComponents.CONTAINER);
                        ItemStack card = contents.copyOne();

                        if (card.getItem() instanceof SpellCardItem spellCardItem){ // shadow wizard money gang, we love casting spells
                            SpellRegistry.get(spellCardItem.getSpellId()).ifPresent(spell -> {
                                if (currentMana >= spell.manaCost()) {
                                    spell.cast(player, gauntlet);

                                    ((ManaUsingEntity) player).manaattributes$addMana(-spell.manaCost());
                                } else if (currentMana <= spell.manaCost()) {
                                    player.sendOverlayMessage(Component.translatable("batoru.insufficient_mana").withStyle(ChatFormatting.DARK_RED));

                                    player.level().playLocalSound(player.blockPosition(), BatoruSounds.DENIED, SoundSource.PLAYERS, 1.0F, 1.0F, false);
                                }
                            });
                        }

                        player.awardStat(Stats.ITEM_USED.get(gauntlet.getItem()));
                        gauntlet.causeUseVibration(player, GameEvent.ITEM_INTERACT_START);
                    }
                }
            });
        });


        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                TrinketAttachment trinkets = TrinketsApi.getAttachment(player);
                if (!trinkets.isEquipped(BatoruItems.GAUNTLET)) continue;

                var equipped = trinkets.getEquipped(BatoruItems.GAUNTLET);
                if (equipped.isEmpty()) continue;
                ItemStack gauntlet = equipped.getFirst().getB();

                for (Spell spell : SpellRegistry.getAll()) {
                    if (!(spell instanceof MultiPhaseSpell mp)) continue;
                    if (!mp.isActive(player)) continue;

                    boolean keepGoing = mp.tick(player, gauntlet);
                    if (!keepGoing) mp.onEnd(player, gauntlet);
                }
            }
        });

        ServerLivingEntityEvents.AFTER_DEATH.register((entity, damageSource) -> {
            if ((damageSource.getEntity() instanceof ServerPlayer killer) && (entity instanceof ServerPlayer)){
                TrinketAttachment trinkets = TrinketsApi.getAttachment(killer);

                var equipped = trinkets.getEquipped(BatoruItems.GAUNTLET);
                if (equipped.isEmpty()) return;

                ItemStack gauntlet = trinkets.getEquipped(BatoruItems.GAUNTLET).getFirst().getB();

                gauntlet.set(BatoruComponents.RANK, RankUtil.getRank(killer));

                CombatRank rank = RankUtil.getRank(killer);
                RankUtil.awardRankAdvancement(killer, rank);
            }
        });

        ServerLivingEntityEvents.ALLOW_DAMAGE.register((entity, source, amount) -> {
                TrinketAttachment trinkets = TrinketsApi.getAttachment(entity);
                if (trinkets.isEquipped(BatoruItems.GAUNTLET) && entity instanceof ServerPlayer player) {
                    var equipped = trinkets.getEquipped(BatoruItems.GAUNTLET);
                    if (!equipped.isEmpty()) {
                        ItemStack gauntlet = equipped.getFirst().getB();
                        GauntletContainerContents contents = gauntlet.get(BatoruComponents.CONTAINER);
                        ItemStack card = contents.copyOne();
                        if (card.getItem() instanceof SpellCardItem spellCard) {
                            if (spellCard.getSpell() instanceof StarfallSpell starfallSpell){
                                if (starfallSpell.hitGround(player) && source.is(DamageTypes.FALL)){
                                    return false;
                                }
                            }
                        }
                    }
                }
                return true;
        });
    }
}
