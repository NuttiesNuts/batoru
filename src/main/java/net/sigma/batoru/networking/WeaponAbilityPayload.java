package net.sigma.batoru.networking;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.sigma.batoru.Batoru;

public record WeaponAbilityPayload() implements CustomPacketPayload {
    public static final Identifier WEAPON_ABILITY_PAYLOAD_ID = Identifier.fromNamespaceAndPath(Batoru.MOD_ID, "weapon_ability");

    public static final CustomPacketPayload.Type<WeaponAbilityPayload> TYPE = new CustomPacketPayload.Type<>(WEAPON_ABILITY_PAYLOAD_ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, WeaponAbilityPayload> CODEC = StreamCodec.unit(new WeaponAbilityPayload());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
