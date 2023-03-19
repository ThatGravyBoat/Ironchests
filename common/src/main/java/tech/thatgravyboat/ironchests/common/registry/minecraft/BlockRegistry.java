package tech.thatgravyboat.ironchests.common.registry.minecraft;

import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistries;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import tech.thatgravyboat.ironchests.IronChests;
import tech.thatgravyboat.ironchests.api.chesttype.ChestType;
import tech.thatgravyboat.ironchests.common.blocks.GenericChestBlock;
import tech.thatgravyboat.ironchests.common.blocks.GenericChestBlockEntity;
import tech.thatgravyboat.ironchests.common.registry.custom.ChestTypeRegistry;

import java.util.function.Supplier;

public class BlockRegistry {

    public static final ResourcefulRegistry<Block> BLOCKS = ResourcefulRegistries.create(BuiltInRegistries.BLOCK, IronChests.MODID);
    public static final ResourcefulRegistry<BlockEntityType<?>> BLOCK_ENTITIES = ResourcefulRegistries.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, IronChests.MODID);

    public static void init() {
        ChestTypeRegistry.INSTANCE.getChests().forEach(BlockRegistry::registerBoth);
        BLOCKS.init();
        BLOCK_ENTITIES.init();
    }

    private static void registerBoth(String id, ChestType type) {
        type.registries().setBlockSupplier(BLOCKS.register(id,
                () -> GenericChestBlock.create(type, type.properties().getProperties())
        ));
        type.registries().setBlockEntityType(BLOCK_ENTITIES.register(id,
                () -> createBlockEntityType((pos, state) -> new GenericChestBlockEntity(pos, state, type), type.registries().getBlock().get())
        ));
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
