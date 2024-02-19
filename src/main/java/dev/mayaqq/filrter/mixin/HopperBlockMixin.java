package dev.mayaqq.filrter.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HopperBlock;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HopperBlock.class)
public class HopperBlockMixin {
    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void filrter$hopperUse(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult, CallbackInfoReturnable<InteractionResult> cir) {
        if (!level.isClientSide) {
            ItemStack stack = player.getItemInHand(interactionHand);
            if (stack.is(Items.NAME_TAG)) {
                if (stack.hasCustomHoverName()) {
                    Component name = stack.getHoverName();
                    HopperBlockEntity be = (HopperBlockEntity) level.getBlockEntity(blockPos);
                    if (be != null) {
                        be.setCustomName(Component.literal("\uD83D\uDD0D " + name.getString()));
                        level.playSound(null, blockPos, SoundEvents.LEVER_CLICK, SoundSource.BLOCKS, 1.0F, 1.0F);
                        cir.setReturnValue(InteractionResult.CONSUME);
                    }
                } else {
                    HopperBlockEntity be = (HopperBlockEntity) level.getBlockEntity(blockPos);
                    if (be != null && be.hasCustomName()) {
                        be.setCustomName(null);
                        level.playSound(null, blockPos, SoundEvents.AXOLOTL_SPLASH, SoundSource.BLOCKS, 1.0F, 1.0F);
                        cir.setReturnValue(InteractionResult.CONSUME);
                    }
                }
            }
        }
    }
}
