package com.victorbrndls.indus.items;

import com.victorbrndls.indus.shared.OreLocator;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;

import java.util.function.Consumer;

public class ProspectorItem extends Item {

    public ProspectorItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (level.isClientSide()) return InteractionResult.SUCCESS;

        var pos = player.blockPosition();
        var ore = OreLocator.prospect(level, pos);

        if (ore == null) {
            player.displayClientMessage(
                    Component.literal("Nothing found..."),
                    false
            );
            return InteractionResult.SUCCESS;
        }

        player.displayClientMessage(
                Component.literal("You found a " + ore.getName().getString() + " vein!"),
                false
        );

        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
        tooltipAdder.accept(Component.literal("Right click to prospect for ores"));
    }
}
