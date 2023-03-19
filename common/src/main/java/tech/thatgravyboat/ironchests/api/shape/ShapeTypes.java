package tech.thatgravyboat.ironchests.api.shape;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.world.level.block.Block;
import tech.thatgravyboat.ironchests.api.shape.base.Shape;
import tech.thatgravyboat.ironchests.api.shape.base.ShapeType;
import tech.thatgravyboat.ironchests.api.shape.shapes.AndShape;
import tech.thatgravyboat.ironchests.api.shape.shapes.CubeShape;
import tech.thatgravyboat.ironchests.api.shape.shapes.OrShape;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ShapeTypes {

    public static final Shape<?> DEFAULT = new CubeShape(Block.box(1.0D, 0.0D, 1.0D, 15.0D, 14.0D, 15.0D));

    public static final Codec<ShapeType<?>> TYPE_CODEC = Codec.STRING.comapFlatMap(ShapeTypes::decode, ShapeType::id);
    public static final Codec<Shape<?>> CODEC = TYPE_CODEC.dispatch(Shape::type, ShapeType::codec);

    private static final Map<String, ShapeType<?>> TYPE_MAP = new HashMap<>();

    static {
        TYPE_MAP.put("cube", CubeShape.INSTANCE);
        TYPE_MAP.put("or", OrShape.INSTANCE);
        TYPE_MAP.put("and", AndShape.INSTANCE);
    }

    private static DataResult<? extends ShapeType<?>> decode(String id) {
        return Optional.ofNullable(TYPE_MAP.get(id))
                .map(DataResult::success)
                .orElse(DataResult.error(() -> "No shape type found. " + id));
    }
}
