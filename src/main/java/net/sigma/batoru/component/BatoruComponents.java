package net.sigma.batoru.component;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.sigma.batoru.Batoru;
import net.sigma.batoru.rank.CombatRank;

import java.util.List;

public class BatoruComponents {
    public static final DataComponentType<BlockPos> TELEPORT_POSITION = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            Identifier.fromNamespaceAndPath(Batoru.MOD_ID, "teleport_position"),
            DataComponentType.<BlockPos>builder().persistent(BlockPos.CODEC).build()
    );

    public static final DataComponentType<String> OWNER = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            Identifier.fromNamespaceAndPath(Batoru.MOD_ID, "owner"),
            DataComponentType.<String>builder().persistent(Codec.STRING).build()
    );

    public static final DataComponentType<List<Identifier>> SPELL_CARDS = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            Identifier.fromNamespaceAndPath(Batoru.MOD_ID, "spell_cards"),
            DataComponentType.<List<Identifier>>builder().persistent(Identifier.CODEC.listOf()).build()
    );

    public static final DataComponentType<CombatRank> RANK = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            Identifier.fromNamespaceAndPath(Batoru.MOD_ID, "rank"),
            DataComponentType.<CombatRank>builder().persistent(CombatRank.CODEC).build()
    );

    public static void initialize() {}
}
