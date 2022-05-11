package tech.thatgravyboat.ironchests.common.registry.minecraft.fabric;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import tech.thatgravyboat.ironchests.IronChests;
import tech.thatgravyboat.ironchests.common.registry.minecraft.BlockRegistry;

import java.util.function.Supplier;

public class BlockRegistryImpl {
    public static <T extends Block> Supplier<T> registerBlock(String id, Supplier<T> item) {
        T register = Registry.register(Registry.BLOCK, new ResourceLocation(IronChests.MODID, id), item.get());
        return () -> register;
    }

    public static <E extends BlockEntity, T extends BlockEntityType<E>> Supplier<T> registerBlockEntity(String id, Supplier<T> item) {
        T register = Registry.register(Registry.BLOCK_ENTITY_TYPE, new ResourceLocation(IronChests.MODID, id), item.get());
        return () -> register;
    }

    public static <E extends BlockEntity> BlockEntityType<E> createBlockEntityType(BlockRegistry.BlockEntityFactory<E> factory, Block... blocks) {
        return FabricBlockEntityTypeBuilder.create(factory::create, blocks).build();
    }
}
