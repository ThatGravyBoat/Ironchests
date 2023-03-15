package tech.thatgravyboat.ironchests.api.shape.base;

import com.mojang.serialization.Codec;

public interface ShapeType<T extends Shape<T>> {

    Codec<T> codec();

    String id();
}
