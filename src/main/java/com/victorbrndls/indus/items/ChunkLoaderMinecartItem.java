package com.victorbrndls.indus.items;

import com.victorbrndls.indus.entities.ChunkLoaderMinecart;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.level.gameevent.GameEvent;

public class ChunkLoaderMinecartItem extends Item {
    public ChunkLoaderMinecartItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos blockPos = context.getClickedPos();
        BlockState blockState = level.getBlockState(blockPos);
        if (!blockState.is(BlockTags.RAILS)) {
            return InteractionResult.FAIL;
        } else {
            ItemStack itemStack = context.getItemInHand();
            if (!level.isClientSide) {
                RailShape railShape = blockState.getBlock() instanceof BaseRailBlock ? ((BaseRailBlock) blockState.getBlock()).getRailDirection(blockState, level, blockPos, null) : RailShape.NORTH_SOUTH;
                double yOffset = 0.0;
                if (railShape.isAscending()) {
                    yOffset = 0.5;
                }

                ChunkLoaderMinecart minecart = new ChunkLoaderMinecart(level,
                        (double) blockPos.getX() + 0.5,
                        (double) blockPos.getY() + 0.0625 + yOffset,
                        (double) blockPos.getZ() + 0.5
                );

                level.addFreshEntity(minecart);
                level.gameEvent(context.getPlayer(), GameEvent.ENTITY_PLACE, blockPos);
            }

            itemStack.shrink(1);
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
    }
}
