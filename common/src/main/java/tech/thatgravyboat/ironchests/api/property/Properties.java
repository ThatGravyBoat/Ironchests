package tech.thatgravyboat.ironchests.api.property;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import tech.thatgravyboat.ironchests.api.property.base.IBlockProperty;
import tech.thatgravyboat.ironchests.api.property.base.IBlockPropertyType;
import tech.thatgravyboat.ironchests.api.property.properties.BuilderProperty;
import tech.thatgravyboat.ironchests.api.property.properties.ReferenceProperty;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Properties {

    public static final Codec<IBlockPropertyType> TYPE_CODEC = Codec.STRING.comapFlatMap(Properties::decode, IBlockPropertyType::getId);
    public static final Codec<IBlockProperty> CODEC = TYPE_CODEC.dispatch(IBlockProperty::getType, IBlockPropertyType::codec);

    private static final Map<String, IBlockPropertyType> PROPERTY_MAP = new HashMap<>();

    static {
        PROPERTY_MAP.put("reference", ReferenceProperty.TYPE);
        PROPERTY_MAP.put("builder", BuilderProperty.TYPE);
    }

    private static DataResult<IBlockPropertyType> decode(String id) {
        return Optional.ofNullable(PROPERTY_MAP.get(id)).map(DataResult::success).orElse(DataResult.error("No property type found."));
    }

}
