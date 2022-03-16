package tech.thatgravyboat.ironchests.platform;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import tech.thatgravyboat.ironchests.Constants;
import tech.thatgravyboat.ironchests.platform.services.IRegistryHelper;

import java.util.function.Supplier;

import static tech.thatgravyboat.ironchests.Constants.MODID;

public class FabricRegistryHelper implements IRegistryHelper {

    @Override
    public <T extends Item> Supplier<T> registerItem(String id, Supplier<T> item) {
        var register = Registry.register(Registry.ITEM, new ResourceLocation(MODID, id), item.get());
        return () -> register;
    }

    @Override
    public <T extends Block> Supplier<T> registerBlock(String id, Supplier<T> item) {
        var register = Registry.register(Registry.BLOCK, new ResourceLocation(MODID, id), item.get());
        return () -> register;
    }

    @Override
    public <E extends BlockEntity, T extends BlockEntityType<E>> Supplier<T> registerBlockEntity(String id, Supplier<T> item) {
        var register = Registry.register(Registry.BLOCK_ENTITY_TYPE, new ResourceLocation(MODID, id), item.get());
        return () -> register;
    }

    @Override
    public <E extends BlockEntity> BlockEntityType<E> createBlockEntityType(BlockEntityFactory<E> factory, Block... blocks) {
        return FabricBlockEntityTypeBuilder.create(factory::create, blocks).build();
    }

    @Override
    public <E extends AbstractContainerMenu> Supplier<MenuType<E>> registerMenu(String id, MenuFactory<E> item) {
        var eMenuType = ScreenHandlerRegistry.registerSimple(new ResourceLocation(MODID, id), item::create);
        return () -> eMenuType;
    }
}
