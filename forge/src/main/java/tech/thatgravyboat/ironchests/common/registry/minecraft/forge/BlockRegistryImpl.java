package tech.thatgravyboat.ironchests.common.registry.minecraft.forge;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import tech.thatgravyboat.ironchests.IronChests;
import tech.thatgravyboat.ironchests.common.registry.minecraft.BlockRegistry;

import java.util.function.Supplier;

public class BlockRegistryImpl {

    public static <E extends BlockEntity> BlockEntityType<E> createBlockEntityType(BlockRegistry.BlockEntityFactory<E> factory, Block... blocks) {
        return BlockEntityType.Builder.of(factory::create, blocks).build(null);
    }

}
