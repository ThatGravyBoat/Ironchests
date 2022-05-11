package tech.thatgravyboat.ironchests.api.chesttype;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import tech.thatgravyboat.ironchests.common.registry.custom.ChestTypeRegistry;

import java.util.Optional;

public record ChestUpgradeType(ChestType from, ChestType to) {

    public static Codec<ChestUpgradeType> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.optionalFieldOf("from").forGetter(upgrade -> Optional.of(upgrade.from.getId())),
            Codec.STRING.fieldOf("to").forGetter(upgrade -> upgrade.to.getId())
    ).apply(instance, ChestUpgradeType::new));

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public ChestUpgradeType(Optional<String> from, String to) {
        this(from.map(ChestUpgradeType::get).orElse(null), get(to));
    }

    private static ChestType get(String id) {
        if (!ChestTypeRegistry.INSTANCE.getChests().containsKey(id)) throw new RuntimeException("Chest type of '"+id+"' was not found.");
        return ChestTypeRegistry.INSTANCE.getChests().get(id);
    }

    public ChestUpgradeType {
        if (from != null && to != null && from.name().equals(to.name())){
            throw new IllegalArgumentException("You can not have a chest upgrade with the same chest type for both to and from.");
        }
    }
}
