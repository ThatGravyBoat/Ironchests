package tech.thatgravyboat.ironchests.common.chesttypes;

import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import tech.thatgravyboat.ironchests.Constants;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ChestTypes {

    public static final Map<String, ChestType> TYPES = new HashMap<>();

    public static final ChestType DIRT = register(new ChestType.Builder("dirt", 1,1)
            .setOffsets(8,80)
            .setDimensions(176,133)
            .setProperties(BlockBehaviour.Properties.of(Material.DIRT).strength(0.5F).sound(SoundType.GRAVEL))
            .setMenuPredicate(stack -> stack.getItem().equals(Items.DIRT))
            .build()
    );

    public static final ChestType COPPER = register(new ChestType.Builder("copper", 9,4)
            .setOffsets(8,8)
            .setDimensions(176,187)
            .setProperties(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_ORANGE).requiresCorrectToolForDrops().strength(3.0F, 6.0F).sound(SoundType.COPPER))
            .build()
    );

    public static final ChestType IRON = register(new ChestType.Builder("iron", 9,6)
            .setOffsets(8,8)
            .setDimensions(176,222)
            .setProperties(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.METAL).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL))
            .build()
    );

    public static final ChestType GOLD = register(new ChestType.Builder("gold", 11,6)
            .setOffsets(26,8)
            .setDimensions(212,222)
            .setProperties(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.GOLD).requiresCorrectToolForDrops().strength(3.0F, 6.0F).sound(SoundType.METAL))
            .build()
    );

    public static final ChestType DIAMOND = register(new ChestType.Builder("diamond", 12,6)
            .setOffsets(35,8)
            .setDimensions(232,222)
            .setProperties(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.DIAMOND).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL))
            .build()
    );

    public static final ChestType OBSIDIAN = register(new ChestType.Builder("obsidian", 12,6)
            .setOffsets(35,8)
            .setDimensions(232,222)
            .setProperties(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_BLACK).requiresCorrectToolForDrops().strength(50.0F, 1200.0F))
            .setTexture("diamond")
            .build()
    );

    public static final ChestType CRYSTAL = register(new ChestType.Builder("crystal", 12,6)
            .setOffsets(35,8)
            .setDimensions(232,222)
            .setProperties(BlockBehaviour.Properties.of(Material.GLASS).strength(0.3F).sound(SoundType.GLASS).noOcclusion())
            .isTransparent()
            .shouldRenderItems()
            .setTexture("diamond")
            .build()
    );

    public static final ChestType NETHERITE = register(new ChestType.Builder("netherite", 13,7)
            .setOffsets(44,8)
            .setDimensions(250,240)
            .setProperties(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_BLACK).requiresCorrectToolForDrops().strength(50.0F, 1200.0F).sound(SoundType.NETHERITE_BLOCK))
            .build()
    );

    public static ChestType register(ChestType type){
        TYPES.put(Constants.MODID +":"+type.name().toLowerCase(Locale.ROOT) +"_chest", type);
        return type;
    }

}
