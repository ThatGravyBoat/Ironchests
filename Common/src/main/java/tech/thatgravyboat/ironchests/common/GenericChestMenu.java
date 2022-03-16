package tech.thatgravyboat.ironchests.common;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import tech.thatgravyboat.ironchests.common.chesttypes.ChestType;

public class GenericChestMenu extends AbstractContainerMenu {

    private final Container container;
    public final ChestType type;

    public GenericChestMenu(int id, Inventory inventory, ChestType type){
        this(type.registries().getMenu().get(), id, inventory, new SimpleContainer(type.size()), type);
    }

    public GenericChestMenu(MenuType<GenericChestMenu> menuType, int id, Inventory inventory, Container container, ChestType type) {
        super(menuType, id);
        checkContainerSize(container, type.size());
        this.container = container;
        this.type = type;
        container.startOpen(inventory.player);

        for(int row = 0; row < type.rows(); ++row) {
            for (int slot = 0; slot < type.length(); ++slot) {
                this.addSlot(new ChestTypeSlot(container, slot + row * type.length(), type.menuOffset() + slot * 18, 18 + row * 18, type));
            }
        }

        int k = 1 + (type.rows() - 4) * 18;

        for(int row = 0; row < 3; ++row) {
            for(int slot = 0; slot < 9; ++slot) {
                this.addSlot(new Slot(inventory, slot + row * 9 + 9, type.inventoryOffset()  + slot * 18, 103 + row * 18 + k));
            }
        }

        for(int slot = 0; slot < 9; ++slot) {
            this.addSlot(new Slot(inventory, slot, type.inventoryOffset() + slot * 18, 161 + k));
        }

    }

    @Override
    public void removed(@NotNull Player player) {
        super.removed(player);
        this.container.stopOpen(player);
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return this.container.stillValid(player);
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int slotIndex) {
        Slot slot = this.slots.get(slotIndex);
        if (!slot.hasItem()) return ItemStack.EMPTY;

        ItemStack slotStack = slot.getItem();

        if (slotIndex < type.size()) {
            if (!this.moveItemStackTo(slotStack, type.size(), this.slots.size(), true)) {
                return ItemStack.EMPTY;
            }
        } else if (!this.moveItemStackTo(slotStack, 0, type.size(), false)) {
            return ItemStack.EMPTY;
        }

        if (slotStack.isEmpty()) {
            slot.set(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }

        return slotStack.copy();
    }

    private static class ChestTypeSlot extends Slot {

        private final ChestType type;

        public ChestTypeSlot(Container container, int i, int j, int k, ChestType type) {
            super(container, i, j, k);
            this.type = type;
        }

        @Override
        public boolean mayPlace(@NotNull ItemStack stack) {
            return type.slotPredicate() == null || type.slotPredicate().test(stack);
        }
    }
}
