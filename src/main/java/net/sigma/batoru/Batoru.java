package net.sigma.batoru;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.sigma.batoru.component.BatoruComponents;
import net.sigma.batoru.item.BatoruItems;
import net.sigma.batoru.item.custom.sword.TechSwordItem;
import net.sigma.batoru.networking.WeaponAbilityPayload;
import net.sigma.batoru.spell.BatoruSpells;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;


public class Batoru implements ModInitializer {

    public static final String MOD_ID = "batoru";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID.substring(0, 1).toUpperCase() + MOD_ID.substring(1));

    @Override
    public void onInitialize() {
        LOGGER.info("https://discord.com/channels/674795434509598731/1490152095515414669/1490634229254590526 what should my username be? idk, maybe something actually normal. You are not tuff for picking \"what should my username be?\" as your username, I told you to check your logs, now we are both here staring at each other and debating what should my username be? well anyways. SKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDISKIBIDI");
        // who up modding they fest?
        for (int i = 0; i < 68; i++) {
            System.out.println(i + ": SKIBIDI");
        } // we do a lil funny goofy thingamajig

        BatoruSpells.initialize();
        BatoruItems.initialize();
        BatoruComponents.initialize();

        PayloadTypeRegistry.serverboundPlay().register(WeaponAbilityPayload.TYPE, WeaponAbilityPayload.CODEC);


        // payload thingy
        ServerPlayNetworking.registerGlobalReceiver(WeaponAbilityPayload.TYPE, (payload, context) -> {
            context.server().execute(() -> {
                ServerPlayer player = context.player();

                ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);

                BlockPos blockPos = new BlockPos((int) player.getX(), (int) player.getY(), (int) player.getZ());

                if (stack.is(BatoruItems.TECH_SWORD)){
                    stack.set(BatoruComponents.TELEPORT_POSITION, blockPos);

                    player.
                }
            });
        });
    }

}
