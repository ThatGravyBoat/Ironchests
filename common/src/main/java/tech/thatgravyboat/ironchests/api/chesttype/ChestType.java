package tech.thatgravyboat.ironchests.api.chesttype;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Decoder;
import com.mojang.serialization.Encoder;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.ItemPredicate;
import tech.thatgravyboat.ironchests.api.CodecUtils;
import tech.thatgravyboat.ironchests.api.property.Properties;
import tech.thatgravyboat.ironchests.api.property.base.IBlockProperty;

import java.util.Locale;

public record ChestType(String name, int length, int rows, int size,
                        int inventoryOffset, int menuOffset,
                        int width, int height,
                        IBlockProperty properties,
                        ChestRegistries registries,
                        boolean transparent, String texture,
                        ItemPredicate predicate,
                        boolean renderItems, boolean fireResistant) {

    public ChestType(String name,
                     int length, int rows,
                     int inventoryOffset, int menuOffset,
                     int width, int height,
                     IBlockProperty properties,
                     boolean transparent,
                     String texture, ItemPredicate slotPredicate, boolean renderItems, boolean fireResistant) {
        this(name, length, rows, length * rows, inventoryOffset, menuOffset, width, height, properties, new ChestRegistries(), transparent, texture, slotPredicate, renderItems, fireResistant);
    }

    public ChestType {
        if (size < 18 && renderItems) {
            throw new IllegalArgumentException("Chest Type requires the chest size to be at least 18 total for render items be shown as it looks the best with 18 items.");
        }
    }

    public static Codec<ChestType> codec(String name) {
        return RecordCodecBuilder.create(instance -> instance.group(
                MapCodec.of(Encoder.empty(), Decoder.unit(() -> name)).forGetter(ChestType::name),
                Codec.INT.fieldOf("length").forGetter(ChestType::length),
                Codec.INT.fieldOf("rows").forGetter(ChestType::rows),
                Codec.INT.fieldOf("inventoryOffset").forGetter(ChestType::inventoryOffset),
                Codec.INT.fieldOf("menuOffset").forGetter(ChestType::menuOffset),
                Codec.INT.fieldOf("width").forGetter(ChestType::width),
                Codec.INT.fieldOf("height").forGetter(ChestType::height),
                Properties.CODEC.fieldOf("properties").forGetter(ChestType::properties),
                Codec.BOOL.fieldOf("transparent").orElse(false).forGetter(ChestType::transparent),
                Codec.STRING.fieldOf("texture").orElse(name).forGetter(ChestType::texture),
                CodecUtils.passthrough(ItemPredicate::serializeToJson, ItemPredicate::fromJson).fieldOf("predicate").orElse(ItemPredicate.ANY).forGetter(ChestType::predicate),
                Codec.BOOL.fieldOf("renderItems").orElse(false).forGetter(ChestType::renderItems),
                Codec.BOOL.fieldOf("fireResistant").orElse(false).forGetter(ChestType::fireResistant)
        ).apply(instance, ChestType::new));
    }

    public String getId() {
        return name().toLowerCase(Locale.ROOT) + "_chest";
    }

}

