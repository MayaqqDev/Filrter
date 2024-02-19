package dev.mayaqq.filrter.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.mayaqq.filrter.utils.FilterProcessor;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.Hopper;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HopperBlockEntity.class)
public class HopperBlockEntityMixin {

    @ModifyExpressionValue(
            method = "tryTakeInItemFromSlot",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/entity/HopperBlockEntity;canTakeItemFromContainer(Lnet/minecraft/world/Container;Lnet/minecraft/world/Container;Lnet/minecraft/world/item/ItemStack;ILnet/minecraft/core/Direction;)Z")
    )
    private static boolean onlyAllowIfFiltered(boolean original, Hopper hopper, Container container, int i, Direction direction) {
        if (hopper instanceof HopperBlockEntity entity) {
            if (entity.hasCustomName()) {
                ItemStack itemStack = container.getItem(i);
                return original && FilterProcessor.test(entity.getCustomName(), itemStack);
            }
        }
        return original;
    }

    @Inject(method = "addItem(Lnet/minecraft/world/Container;Lnet/minecraft/world/entity/item/ItemEntity;)Z", at = @At("HEAD"), cancellable = true)
    private static void onlyAllowIfFiltered(Container container, ItemEntity itemEntity, CallbackInfoReturnable<Boolean> cir) {
        if (container instanceof HopperBlockEntity be) {
            if (be.hasCustomName()) {
                if (!FilterProcessor.test(be.getCustomName(), itemEntity.getItem())) {
                    cir.setReturnValue(false);
                }
            }
        }
    }
}