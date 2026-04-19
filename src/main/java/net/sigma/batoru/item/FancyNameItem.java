package net.sigma.batoru.item;

import eu.pb4.placeholders.api.node.parent.GradientNode;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class FancyNameItem extends Item {

    private final int color1;
    private final int color2;

    public FancyNameItem(Properties properties, int color1, int color2) {
        super(properties);
        this.color1 = color1;
        this.color2 = color2;
    }

    @Override
    public Component getName(ItemStack itemStack){
        var offset = System.currentTimeMillis() / 2;
        var colors = List.of(TextColor.fromRgb(color1), TextColor.fromRgb(color2), TextColor.fromRgb(color1));
        var gr = GradientNode.GradientProvider.colors(colors);
        return GradientNode.apply(Component.literal(super.getName(itemStack).getString()),
                (i, l) -> gr.getColorAt((int) ((i * 100 + offset) % (l * 100)), l * 100));
    }

}
