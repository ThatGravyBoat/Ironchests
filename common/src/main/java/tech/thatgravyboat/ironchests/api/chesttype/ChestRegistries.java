package tech.thatgravyboat.ironchests.api.chesttype;

import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import tech.thatgravyboat.ironchests.common.blocks.GenericChestMenu;
import tech.thatgravyboat.ironchests.common.blocks.GenericChestBlock;
import tech.thatgravyboat.ironchests.common.blocks.GenericChestBlockEntity;

import java.util.function.Supplier;

public class ChestRegistries {

    private Supplier<BlockEntityType<GenericChestBlockEntity>> blockEntityType;
    private Supplier<GenericChestBlock> blockSupplier;
    private Supplier<MenuType<GenericChestMenu>> menuSupplier;

    public Supplier<BlockEntityType<GenericChestBlockEntity>> getBlockEntity() {
        return blockEntityType;
    }

    public void setBlockEntityType(Supplier<BlockEntityType<GenericChestBlockEntity>> blockEntityType) {
        this.blockEntityType = blockEntityType;
    }

    public Supplier<GenericChestBlock> getBlock() {
        return blockSupplier;
    }

    public void setBlockSupplier(Supplier<GenericChestBlock> blockSupplier) {
        this.blockSupplier = blockSupplier;
    }

    public Supplier<MenuType<GenericChestMenu>> getMenu() {
        return menuSupplier;
    }

    public void setMenuSupplier(Supplier<MenuType<GenericChestMenu>> menuSupplier) {
        this.menuSupplier = menuSupplier;
    }
}
