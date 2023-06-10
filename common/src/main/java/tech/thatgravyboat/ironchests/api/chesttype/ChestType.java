package tech.thatgravyboat.ironchests.api.chesttype;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Decoder;
import com.mojang.serialization.Encoder;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamresourceful.resourcefullib.common.codecs.CodecExtras;
import com.teamresourceful.resourcefullib.common.codecs.EnumCodec;
import net.minecraft.advancements.critereon.ItemPredicate;
import tech.thatgravyboat.ironchests.api.property.Properties;
import tech.thatgravyboat.ironchests.api.property.base.BlockProperty;
import tech.thatgravyboat.ironchests.api.shape.ShapeTypes;
import tech.thatgravyboat.ironchests.api.shape.base.Shape;

import java.util.Locale;
import java.util.Optional;

public record ChestType(
    String name,
    ChestBlockType blockType,
    Shape<?> shape,
    int length, int rows, int size,
    int inventoryOffset, int menuOffset,
    int width, int height,
    BlockProperty properties,
    ChestRegistries registries,
    boolean transparent, String texture,
    ItemPredicate predicate,
    boolean renderItems, boolean fireResistant,
    String oxidizedChest
) {

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public ChestType(
        String name,
        ChestBlockType renderType,
        Shape<?> shape,
        int length, int rows,
        int inventoryOffset, int menuOffset,
        int width, int height,
        BlockProperty properties,
        boolean transparent,
        Optional<String> texture, ItemPredicate slotPredicate, boolean renderItems, boolean fireResistant,
        Optional<String> oxidizedChest
    ) {
        this(name, renderType, shape, length, rows, length * rows, inventoryOffset, menuOffset, width, height, properties, new ChestRegistries(), transparent, texture.orElse(null), slotPredicate, renderItems, fireResistant, oxidizedChest.orElse(null));
    }

    public ChestType {
        if (size < 18 && renderItems) {
            throw new IllegalArgumentException("Chest Type requires the chest size to be at least 18 total for render items be shown as it looks the best with 18 items.");
        }
    }

    public static Codec<ChestType> codec(String name) {
        return RecordCodecBuilder.create(instance -> instance.group(
            MapCodec.of(Encoder.empty(), Decoder.unit(() -> name)).forGetter(ChestType::name),
            EnumCodec.of(ChestBlockType.class).fieldOf("block_type").orElse(ChestBlockType.CHEST).forGetter(ChestType::blockType),
            ShapeTypes.CODEC.fieldOf("shape").orElse(ShapeTypes.DEFAULT).forGetter(ChestType::shape),
            Codec.INT.fieldOf("length").forGetter(ChestType::length),
            Codec.INT.fieldOf("rows").forGetter(ChestType::rows),
            Codec.INT.fieldOf("inventoryOffset").forGetter(ChestType::inventoryOffset),
            Codec.INT.fieldOf("menuOffset").forGetter(ChestType::menuOffset),
            Codec.INT.fieldOf("width").forGetter(ChestType::width),
            Codec.INT.fieldOf("height").forGetter(ChestType::height),
            Properties.CODEC.fieldOf("properties").forGetter(ChestType::properties),
            Codec.BOOL.fieldOf("transparent").orElse(false).forGetter(ChestType::transparent),
            Codec.STRING.optionalFieldOf("texture").forGetter(CodecExtras.optionalFor(ChestType::texture)),
            CodecExtras.passthrough(ItemPredicate::serializeToJson, ItemPredicate::fromJson).fieldOf("predicate").orElse(ItemPredicate.ANY).forGetter(ChestType::predicate),
            Codec.BOOL.fieldOf("renderItems").orElse(false).forGetter(ChestType::renderItems),
            Codec.BOOL.fieldOf("fireResistant").orElse(false).forGetter(ChestType::fireResistant),
            Codec.STRING.optionalFieldOf("oxidizedChest").forGetter(CodecExtras.optionalFor(ChestType::oxidizedChest))
        ).apply(instance, ChestType::new));
    }

    public String getId() {
        return name().toLowerCase(Locale.ROOT) + "_" + blockType().id();
    }

    public ChestType getOxidizedChest() {
        if (oxidizedChest == null) {
            return null;
        }
        return ChestUpgradeType.get(oxidizedChest());
    }
}

