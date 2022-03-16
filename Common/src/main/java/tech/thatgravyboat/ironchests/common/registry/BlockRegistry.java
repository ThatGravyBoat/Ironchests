package tech.thatgravyboat.ironchests.common.registry;

import tech.thatgravyboat.ironchests.common.blocks.GenericChestBlock;
import tech.thatgravyboat.ironchests.common.blocks.GenericChestBlockEntity;
import tech.thatgravyboat.ironchests.common.chesttypes.ChestType;
import tech.thatgravyboat.ironchests.common.chesttypes.ChestTypes;
import tech.thatgravyboat.ironchests.platform.Services;

public class BlockRegistry {

    public static void init() {
        for (ChestType value : ChestTypes.TYPES.values()) {
            registryChest(value);
            registryChestEntity(value);
        }
    }

    private static void registryChest(ChestType type) {
        var register = Services.REGISTRY.registerBlock(type.name()+"_chest", () -> new GenericChestBlock(type, type.properties()));
        type.registries().setBlockSupplier(register);
    }

    private static void registryChestEntity(ChestType type) {
        var entityType = Services.REGISTRY.registerBlockEntity(type.name()+"_chest",
                () -> Services.REGISTRY.createBlockEntityType((pos, state) -> new GenericChestBlockEntity(pos, state, type), type.registries().getBlock().get()));
        type.registries().setBlockEntityType(entityType);
    }
}
