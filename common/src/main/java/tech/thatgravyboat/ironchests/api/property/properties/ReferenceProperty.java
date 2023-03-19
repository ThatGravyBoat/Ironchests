package tech.thatgravyboat.ironchests.api.property.properties;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import tech.thatgravyboat.ironchests.api.property.base.BlockProperty;
import tech.thatgravyboat.ironchests.api.property.base.BlockPropertyType;

public record ReferenceProperty(Block block) implements BlockProperty {

    public static final Type TYPE = new Type();

    @Override
    public BlockBehaviour.Properties getProperties() {
        return BlockBehaviour.Properties.copy(block);
    }

    @Override
    public BlockPropertyType getType() {
        return TYPE;
    }

    private static class Type implements BlockPropertyType {

        private static final Codec<ReferenceProperty> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BuiltInRegistries.BLOCK.byNameCodec().fieldOf("block").forGetter(ReferenceProperty::block)
        ).apply(instance, ReferenceProperty::new));

        @Override
        public Codec<ReferenceProperty> codec() {
            return CODEC;
        }

        @Override
        public String getId() {
            return "reference";
        }
    }
}
