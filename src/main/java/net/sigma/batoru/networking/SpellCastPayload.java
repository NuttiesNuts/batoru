package net.sigma.batoru.networking;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.sigma.batoru.Batoru;

public record SpellCastPayload() implements CustomPacketPayload {
    public static final Identifier SPELL_CAST_PAYLOAD_ID = Identifier.fromNamespaceAndPath(Batoru.MOD_ID, "spell_cast");

    public static final Type<SpellCastPayload> TYPE = new Type<>(SPELL_CAST_PAYLOAD_ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, SpellCastPayload> CODEC = StreamCodec.unit(new SpellCastPayload());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
