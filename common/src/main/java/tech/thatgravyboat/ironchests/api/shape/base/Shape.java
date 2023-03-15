package tech.thatgravyboat.ironchests.api.shape.base;

import net.minecraft.world.phys.shapes.VoxelShape;

public interface Shape<T extends Shape<T>> {

    ShapeType<T> type();

    VoxelShape shape();
}
