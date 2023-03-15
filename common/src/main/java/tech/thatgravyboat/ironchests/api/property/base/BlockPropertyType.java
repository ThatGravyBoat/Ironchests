package tech.thatgravyboat.ironchests.api.property.base;

import com.mojang.serialization.Codec;

public interface BlockPropertyType {

    Codec<? extends BlockProperty> codec();

    String getId();
}
