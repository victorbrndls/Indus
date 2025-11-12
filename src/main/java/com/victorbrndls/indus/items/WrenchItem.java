package com.victorbrndls.indus.items;

import com.victorbrndls.indus.blocks.tileentity.BaseStructureBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import java.util.function.Consumer;

public class WrenchItem extends Item {

    public WrenchItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        if (level.isClientSide()) return InteractionResult.SUCCESS;

        BlockPos blockPos = context.getClickedPos();
        ItemStack stack = context.getItemInHand();
        CompoundTag tag = stack.get(DataComponents.CUSTOM_DATA) != null
                ? stack.get(DataComponents.CUSTOM_DATA).copyTag()
                : new CompoundTag();

        if (level.getBlockEntity(blockPos) instanceof BaseStructureBlockEntity be) {
            if (context.getPlayer().isShiftKeyDown()) {
                var networkId = be.getNetworkId();
                tag.putLong("network_id", networkId);
                stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
                context.getPlayer().displayClientMessage(Component.literal("Network ID: " + networkId), false);
            } else {
                tag.getLong("network_id").ifPresent(networkId -> {
                    be.setNetworkId(networkId);
                    context.getPlayer().displayClientMessage(Component.literal("Assigned Network ID: " + networkId), false);
                });
            }

            return InteractionResult.CONSUME;
        }

        return InteractionResult.PASS;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
        tooltipAdder.accept(Component.literal("Shift-Right click to get network id from a machine."));
        tooltipAdder.accept(Component.literal("Right click to assign a network id to a machine."));
    }
}
