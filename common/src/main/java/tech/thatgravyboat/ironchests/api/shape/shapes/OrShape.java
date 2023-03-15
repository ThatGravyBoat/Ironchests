package tech.thatgravyboat.ironchests.api.shape.shapes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import tech.thatgravyboat.ironchests.api.shape.ShapeTypes;
import tech.thatgravyboat.ironchests.api.shape.base.Shape;
import tech.thatgravyboat.ironchests.api.shape.base.ShapeType;

import java.util.List;

public record OrShape(List<Shape<?>> shapes, VoxelShape shape) implements Shape<OrShape> {

    public static final Type INSTANCE = new Type();

    private OrShape(List<Shape<?>> shape) {
        this(shape, shape.stream().map(Shape::shape).reduce((a, b) -> Shapes.join(a, b, BooleanOp.OR)).orElse(Shapes.block()));
    }

    @Override
    public ShapeType<OrShape> type() {
        return INSTANCE;
    }

    private static class Type implements ShapeType<OrShape> {

        @Override
        public Codec<OrShape> codec() {
            return RecordCodecBuilder.create(instance -> instance.group(
                ShapeTypes.CODEC.listOf().fieldOf("shape").forGetter(OrShape::shapes)
            ).apply(instance, OrShape::new));
        }

        @Override
        public String id() {
            return "or";
        }
    }
}
