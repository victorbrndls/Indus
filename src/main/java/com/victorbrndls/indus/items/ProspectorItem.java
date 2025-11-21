package com.victorbrndls.indus.items;

import com.victorbrndls.indus.shared.FluidLocator;
import com.victorbrndls.indus.shared.OreLocator;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;

import java.util.List;

public class ProspectorItem extends Item {

    public ProspectorItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        var level = context.getLevel();
        var player = context.getPlayer();

        if (level.isClientSide()) return InteractionResult.SUCCESS;

        var pos = context.getClickedPos();
        var ore = OreLocator.prospect(level, pos);
        var fluid = FluidLocator.prospect(level, pos);

        if (ore == null && fluid == null) {
            player.displayClientMessage(
                    Component.literal("Nothing found..."),
                    false
            );
            return InteractionResult.SUCCESS;
        }

        if (ore != null) {
            player.displayClientMessage(
                    Component.literal("You found a " + ore.getDescription().getString() + " vein!"),
                    false
            );
        }
        if (fluid != null) {
            player.displayClientMessage(
                    Component.literal("You found a " + fluid.getDescription().getString() + " source!"),
                    false
            );
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.literal("Right click to prospect for ores and fluids"));
    }

}
