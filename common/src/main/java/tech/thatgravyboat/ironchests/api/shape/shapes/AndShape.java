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

public record AndShape(List<Shape<?>> shapes, VoxelShape shape) implements Shape<AndShape> {

    public static final Type INSTANCE = new Type();

    private AndShape(List<Shape<?>> shape) {
        this(shape, shape.stream().map(Shape::shape).reduce((a, b) -> Shapes.join(a, b, BooleanOp.AND)).orElse(Shapes.block()));
    }

    @Override
    public ShapeType<AndShape> type() {
        return INSTANCE;
    }

    private static class Type implements ShapeType<AndShape> {

        @Override
        public Codec<AndShape> codec() {
            return RecordCodecBuilder.create(instance -> instance.group(
                ShapeTypes.CODEC.listOf().fieldOf("shape").forGetter(AndShape::shapes)
            ).apply(instance, AndShape::new));
        }

        @Override
        public String id() {
            return "and";
        }
    }
}
