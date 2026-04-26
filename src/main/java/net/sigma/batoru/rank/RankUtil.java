package net.sigma.batoru.rank;

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.item.ItemStack;
import net.sigma.batoru.Batoru;
import net.sigma.batoru.component.BatoruComponents;

public class RankUtil {

    public static int getPlayerKills(ServerPlayer player){
        return player.getStats().getValue(Stats.CUSTOM.get(Stats.PLAYER_KILLS));
    }

    public static CombatRank getRank(ServerPlayer player){
        int kills = getPlayerKills(player);
        CombatRank rank = CombatRank.F;

        for (CombatRank combatRank : CombatRank.values()) {
            if (kills >= combatRank.requiredKills) rank = combatRank;
        }

        return rank;
    }

    public static CombatRank getRank(ItemStack gauntlet){
        if (gauntlet.has(BatoruComponents.RANK)) {
            return gauntlet.get(BatoruComponents.RANK);
        }else {
            //Batoru.LOGGER.error("Item stack has no rank component");
            return null;
        }
    }

    public static CombatRank getPreviousRank(CombatRank rank) {
        CombatRank[] ranks = CombatRank.values();
        int index = rank.ordinal();
        return index > 0 ? ranks[index - 1] : ranks[0];
    }

    public static CombatRank getNextRank(CombatRank rank) {
        CombatRank[] ranks = CombatRank.values();
        int index = rank.ordinal();
        return index < ranks.length - 1 ? ranks[index + 1] : ranks[ranks.length - 1];
    }

    /**
     * Awards the advancement for the given rank.
     */
    public static void awardRankAdvancement(ServerPlayer player, CombatRank rank) {
        String id = rank.name().toLowerCase() + "_rank";
        AdvancementHolder advancement = player.level().getServer()
                .getAdvancements()
                .get(Identifier.fromNamespaceAndPath(Batoru.MOD_ID, id));

        if (advancement != null) {
            player.getAdvancements().award(advancement, id);
        }
    }
}
