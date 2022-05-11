package tech.thatgravyboat.ironchests.api.property.base;

import com.mojang.serialization.Codec;
import tech.thatgravyboat.ironchests.api.property.properties.ReferenceProperty;

public interface IBlockPropertyType {

    Codec<? extends IBlockProperty> codec();

    String getId();
}
