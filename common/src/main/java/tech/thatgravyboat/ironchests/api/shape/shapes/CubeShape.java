package tech.thatgravyboat.ironchests.api.shape.shapes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import tech.thatgravyboat.ironchests.api.shape.base.Shape;
import tech.thatgravyboat.ironchests.api.shape.base.ShapeType;

public record CubeShape(VoxelShape shape) implements Shape<CubeShape> {

    public static final Type INSTANCE = new Type();

    private CubeShape(Vec3 min, Vec3 max) {
        this(Shapes.box(min.x, min.y, min.z, max.x, max.y, max.z));
    }

    private Vec3 getBottomLeftFront() {
        return new Vec3(shape.min(Direction.Axis.X), shape.min(Direction.Axis.Y), shape.min(Direction.Axis.Z));
    }

    private Vec3 getTopRightBack() {
        return new Vec3(shape.max(Direction.Axis.X), shape.max(Direction.Axis.Y), shape.max(Direction.Axis.Z));
    }

    @Override
    public ShapeType<CubeShape> type() {
        return INSTANCE;
    }

    private static class Type implements ShapeType<CubeShape> {

        @Override
        public Codec<CubeShape> codec() {
            return RecordCodecBuilder.create(instance -> instance.group(
                Vec3.CODEC.fieldOf("min").forGetter(CubeShape::getBottomLeftFront),
                Vec3.CODEC.fieldOf("max").forGetter(CubeShape::getTopRightBack)
            ).apply(instance, CubeShape::new));
        }

        @Override
        public String id() {
            return "cube";
        }
    }
}
