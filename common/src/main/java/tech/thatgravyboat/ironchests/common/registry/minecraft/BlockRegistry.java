package tech.thatgravyboat.ironchests.common.registry.minecraft;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import tech.thatgravyboat.ironchests.api.chesttype.ChestType;
import tech.thatgravyboat.ironchests.common.blocks.GenericChestBlock;
import tech.thatgravyboat.ironchests.common.blocks.GenericChestBlockEntity;
import tech.thatgravyboat.ironchests.common.registry.custom.ChestTypeRegistry;

import java.util.function.Supplier;

public class BlockRegistry {

    public static void init() {
        ChestTypeRegistry.INSTANCE.getChests().forEach(BlockRegistry::registerBoth);
    }

    private static void registerBoth(String id, ChestType type) {
        registerChest(id, type);
        registerChestEntity(id, type);
    }

    private static void registerChest(String id, ChestType type) {
        var register = registerBlock(id, () -> new GenericChestBlock(type, type.properties().getProperties()));
        type.registries().setBlockSupplier(register);
    }

    private static void registerChestEntity(String id, ChestType type) {
        var entityType = registerBlockEntity(id,
                () -> createBlockEntityType((pos, state) -> new GenericChestBlockEntity(pos, state, type), type.registries().getBlock().get()));
        type.registries().setBlockEntityType(entityType);
    }

    @ExpectPlatform
    public static <T extends Block> Supplier<T> registerBlock(String id, Supplier<T> item) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static <E extends BlockEntity, T extends BlockEntityType<E>> Supplier<T> registerBlockEntity(String id, Supplier<T> item) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static <E extends BlockEntity> BlockEntityType<E> createBlockEntityType(BlockEntityFactory<E> factory, Block... blocks) {
        throw new AssertionError();
    }

    @FunctionalInterface
    public interface BlockEntityFactory<T extends BlockEntity> {
        @NotNull T create(BlockPos blockPos, BlockState blockState);
    }
}
