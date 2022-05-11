package tech.thatgravyboat.ironchests.api.property.base;

import net.minecraft.world.level.block.state.BlockBehaviour;

public interface IBlockProperty {

    BlockBehaviour.Properties getProperties();

    IBlockPropertyType getType();
}
