package net.sigma.batoru.rank;

import com.mojang.serialization.Codec;

public enum CombatRank {
    F(0),
    D(10),
    C(25),
    B(50),
    A(75),
    S(100);

    public static final Codec<CombatRank> CODEC = Codec.STRING.xmap(
            CombatRank::valueOf,
            CombatRank::name
    );

    public final int requiredKills;

    CombatRank(int requiredKills) {
        this.requiredKills = requiredKills;
    }
}
