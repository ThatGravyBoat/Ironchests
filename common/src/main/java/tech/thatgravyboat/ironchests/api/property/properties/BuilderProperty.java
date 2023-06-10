package tech.thatgravyboat.ironchests.api.property.properties;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import tech.thatgravyboat.ironchests.api.property.SoundTypeHelper;
import tech.thatgravyboat.ironchests.api.property.base.BlockProperty;
import tech.thatgravyboat.ironchests.api.property.base.BlockPropertyType;

import java.util.function.Function;

public record BuilderProperty(
    int lightLevel, boolean noCollision,
    boolean noOcclusion, boolean noDrops, boolean requiresCorrectToolForDrops, float friction,
    float speedFactor, float jumpFactor, float destroyTime, float explosionResistance,
    SoundType soundType
) implements BlockProperty {

    public static final Type TYPE = new Type();

    @Override
    public BlockBehaviour.Properties getProperties() {
        BlockBehaviour.Properties properties = BlockBehaviour.Properties.of();
        properties.destroyTime(destroyTime());
        properties.explosionResistance(explosionResistance());
        if (noCollision) properties.noCollission();
        properties.lightLevel(state -> lightLevel());
        properties.sound(soundType());
        properties.friction(friction());
        properties.speedFactor(speedFactor());
        if (noOcclusion) properties.noOcclusion();
        if (requiresCorrectToolForDrops) properties.requiresCorrectToolForDrops();
        if (noDrops) properties.noLootTable();
        properties.jumpFactor(jumpFactor());
        return properties;
    }

    @Override
    public BlockPropertyType getType() {
        return TYPE;
    }

    private static class Type implements BlockPropertyType {

        private static final Codec<SoundType> SOUND_CODEC = Codec.STRING.comapFlatMap(SoundTypeHelper::getSound, Object::toString);

        private static final Codec<BuilderProperty> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.intRange(0, 15).fieldOf("lightLevel").orElse(0).forGetter(get(0)),
                Codec.BOOL.fieldOf("noCollision").orElse(false).forGetter(get(false)),
                Codec.BOOL.fieldOf("noOcclusion").orElse(false).forGetter(get(false)),
                Codec.BOOL.fieldOf("noDrops").orElse(false).forGetter(get(false)),
                Codec.BOOL.fieldOf("requiresCorrectToolForDrops").orElse(false).forGetter(get(false)),
                Codec.FLOAT.fieldOf("friction").orElse(0.6f).forGetter(get(0.6f)),
                Codec.FLOAT.fieldOf("speedFactor").orElse(1f).forGetter(get(1f)),
                Codec.FLOAT.fieldOf("jumpFactor").orElse(1f).forGetter(get(1f)),
                Codec.FLOAT.fieldOf("destroyTime").orElse(0f).forGetter(get(0f)),
                Codec.FLOAT.fieldOf("explosionResistance").orElse(0f).forGetter(get(0f)),
                SOUND_CODEC.fieldOf("sound").forGetter(get(SoundType.STONE))
        ).apply(instance, BuilderProperty::new));

        private static <E, T> Function<E, T> get(T type) {
            return e -> type;
        }

        @Override
        public Codec<BuilderProperty> codec() {
            return CODEC;
        }

        @Override
        public String getId() {
            return "builder";
        }
    }

}
