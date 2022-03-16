package tech.thatgravyboat.ironchests.platform;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import tech.thatgravyboat.ironchests.Constants;
import tech.thatgravyboat.ironchests.platform.services.IRegistryHelper;

import java.util.function.Supplier;

public class ForgeRegistryHelper implements IRegistryHelper {

    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.CONTAINERS, Constants.MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Constants.MODID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Constants.MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, Constants.MODID);

    @Override
    public <T extends Item> Supplier<T> registerItem(String id, Supplier<T> item) {
        return ITEMS.register(id, item);
    }

    @Override
    public <T extends Block> Supplier<T> registerBlock(String id, Supplier<T> item) {
        return BLOCKS.register(id, item);
    }

    @Override
    public <E extends BlockEntity, T extends BlockEntityType<E>> Supplier<T> registerBlockEntity(String id, Supplier<T> item) {
        return BLOCK_ENTITIES.register(id, item);
    }

    @Override
    public <E extends BlockEntity> BlockEntityType<E> createBlockEntityType(BlockEntityFactory<E> factory, Block... blocks) {
        return BlockEntityType.Builder.of(factory::create, blocks).build(null);
    }

    @Override
    public <E extends AbstractContainerMenu> Supplier<MenuType<E>> registerMenu(String id, MenuFactory<E> item) {
        return MENU_TYPES.register(id, () -> IForgeMenuType.create((syncId, inv, c) -> item.create(syncId, inv)));
    }
}
