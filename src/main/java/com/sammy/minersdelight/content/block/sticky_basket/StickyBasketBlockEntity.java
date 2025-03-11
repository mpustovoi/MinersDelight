package com.sammy.minersdelight.content.block.sticky_basket;

import com.sammy.minersdelight.setup.*;
import net.minecraft.core.*;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.shapes.*;
import vectorwing.farmersdelight.common.block.*;
import vectorwing.farmersdelight.common.block.entity.*;

import javax.annotation.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public class StickyBasketBlockEntity extends RandomizableContainerBlockEntity implements Basket
{
    private NonNullList<ItemStack> items = NonNullList.withSize(27, ItemStack.EMPTY);
    private int transferCooldown = -1;

    public StickyBasketBlockEntity(BlockPos pos, BlockState state) {
        super(MDBlockEntities.STICKY_BASKET.get(), pos, state);
    }

    @Override
    protected void loadAdditional(CompoundTag compound, HolderLookup.Provider registries) {
        super.loadAdditional(compound, registries);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        if (!this.tryLoadLootTable(compound)) {
            ContainerHelper.loadAllItems(compound, this.items, registries);
        }
        this.transferCooldown = compound.getInt("TransferCooldown");
    }

    @Override
    public void saveAdditional(CompoundTag compound, HolderLookup.Provider registries) {
        super.saveAdditional(compound, registries);
        if (!this.trySaveLootTable(compound)) {
            ContainerHelper.saveAllItems(compound, this.items, registries);
        }

        compound.putInt("TransferCooldown", this.transferCooldown);
    }

    @Override
    public int getContainerSize() {
        return this.items.size();
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        this.unpackLootTable(null);
        return ContainerHelper.removeItem(this.getItems(), index, count);
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        this.unpackLootTable(null);
        this.getItems().set(index, stack);
        if (stack.getCount() > this.getMaxStackSize()) {
            stack.setCount(this.getMaxStackSize());
        }
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("minersdelight.container.sticky_basket");
    }

    public static boolean pullItems(Level level, Basket basket, int facingIndex) {
        for (ItemEntity itementity : getCaptureItems(level, basket, facingIndex)) {
            if (captureItem(basket, itementity)) {
                return true;
            }
        }
        return false;
    }

    public static ItemStack putStackInInventoryAllSlots(Container destination, ItemStack stack) {
        int i = destination.getContainerSize();

        for (int j = 0; j < i && !stack.isEmpty(); ++j) {
            stack = insertStack(destination, stack, j);
        }

        return stack;
    }

    private static boolean canInsertItemInSlot(Container inventoryIn, ItemStack stack, int index, @Nullable Direction side) {
        if (!inventoryIn.canPlaceItem(index, stack)) return false;
        return !(inventoryIn instanceof WorldlyContainer) || ((WorldlyContainer) inventoryIn).canPlaceItemThroughFace(index, stack, side);
    }

    private static boolean canCombine(ItemStack stack1, ItemStack stack2) {
        return stack1.getCount() <= stack1.getMaxStackSize() && ItemStack.isSameItemSameComponents(stack1, stack2);
    }

    private static ItemStack insertStack(Container destination, ItemStack stack, int index) {
        ItemStack itemstack = destination.getItem(index);
        if (canInsertItemInSlot(destination, stack, index, null)) {
            boolean flag = false;
            boolean isDestinationEmpty = destination.isEmpty();
            if (itemstack.isEmpty()) {
                destination.setItem(index, stack);
                stack = ItemStack.EMPTY;
                flag = true;
            } else if (canCombine(itemstack, stack)) {
                int i = stack.getMaxStackSize() - itemstack.getCount();
                int j = Math.min(stack.getCount(), i);
                stack.shrink(j);
                itemstack.grow(j);
                flag = j > 0;
            }

            if (flag) {
                if (isDestinationEmpty && destination instanceof BasketBlockEntity firstBasket) {
                    if (!firstBasket.mayTransfer()) {
                        int k = 0;
                        firstBasket.setTransferCooldown(8 - k);
                    }
                }

                destination.setChanged();
            }
        }

        return stack;
    }

    public static boolean captureItem(Container inventory, ItemEntity itemEntity) {
        boolean flag = false;
        ItemStack entityItemStack = itemEntity.getItem().copy();
        ItemStack remainderStack = putStackInInventoryAllSlots(inventory, entityItemStack);
        if (remainderStack.isEmpty()) {
            flag = true;
            itemEntity.discard();
        } else {
            itemEntity.setItem(remainderStack);
        }

        return flag;
    }

    public static List<ItemEntity> getCaptureItems(Level level, Basket basket, int facingIndex) {
        return basket.getFacingCollectionArea(facingIndex).toAabbs().stream().flatMap((aabb) -> level.getEntitiesOfClass(ItemEntity.class, aabb.move(basket.getLevelX() - 0.5D, basket.getLevelY() - 0.5D, basket.getLevelZ() - 0.5D), EntitySelector.ENTITY_STILL_ALIVE).stream()).collect(Collectors.toList());
    }

    // -- STANDARD INVENTORY STUFF --
    @Override
    protected NonNullList<ItemStack> getItems() {
        return this.items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> itemsIn) {
        this.items = itemsIn;
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory player) {
        return ChestMenu.threeRows(id, player, this);
    }

    public void setTransferCooldown(int ticks) {
        this.transferCooldown = ticks;
    }

    private boolean isOnTransferCooldown() {
        return this.transferCooldown > 0;
    }

    public boolean mayTransfer() {
        return this.transferCooldown > 8;
    }

    private void updateHopper(Supplier<Boolean> supplier) {
        if (this.level != null && !this.level.isClientSide) {
            if (!this.isOnTransferCooldown() && this.getBlockState().getValue(BlockStateProperties.ENABLED)) {
                boolean flag = false;
                if (!this.isFull()) {
                    flag = supplier.get();
                }

                if (flag) {
                    this.setTransferCooldown(8);
                    this.setChanged();
                }
            }
        }
    }

    private boolean isFull() {
        for (ItemStack itemstack : this.items) {
            if (itemstack.isEmpty() || itemstack.getCount() != itemstack.getMaxStackSize()) {
                return false;
            }
        }

        return true;
    }

    public void onEntityCollision(Entity entity) {
        if (entity instanceof ItemEntity) {
            BlockPos blockpos = this.getBlockPos();
            int facing = this.getBlockState().getValue(BasketBlock.FACING).get3DDataValue();
            if (Shapes.joinIsNotEmpty(Shapes.create(entity.getBoundingBox().move(-blockpos.getX(), -blockpos.getY(), -blockpos.getZ())), this.getFacingCollectionArea(facing), BooleanOp.AND)) {
                this.updateHopper(() -> captureItem(this, (ItemEntity) entity));
            }
        }
    }

    @Override
    public double getLevelX() {
        return (double) this.worldPosition.getX() + 0.5D;
    }

    @Override
    public double getLevelY() {
        return (double) this.worldPosition.getY() + 0.5D;
    }

    @Override
    public double getLevelZ() {
        return (double) this.worldPosition.getZ() + 0.5D;
    }

    public static void pushItemsTick(Level level, BlockPos pos, BlockState state, StickyBasketBlockEntity blockEntity) {
        --blockEntity.transferCooldown;
        if (!blockEntity.isOnTransferCooldown()) {
            blockEntity.setTransferCooldown(0);
            int facing = state.getValue(BasketBlock.FACING).get3DDataValue();
            blockEntity.updateHopper(() -> pullItems(level, blockEntity, facing));
        }
    }
}