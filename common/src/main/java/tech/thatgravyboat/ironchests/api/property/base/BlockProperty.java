package tech.thatgravyboat.ironchests.api.property.base;

import net.minecraft.world.level.block.state.BlockBehaviour;

public interface BlockProperty {

    BlockBehaviour.Properties getProperties();

    BlockPropertyType getType();
}
