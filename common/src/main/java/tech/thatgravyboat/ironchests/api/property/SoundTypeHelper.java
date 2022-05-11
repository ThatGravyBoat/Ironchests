package tech.thatgravyboat.ironchests.api.property;

import com.mojang.serialization.DataResult;
import net.minecraft.world.level.block.SoundType;

import java.util.Locale;

public class SoundTypeHelper {

    public static DataResult<SoundType> getSound(String id) {
        return DataResult.success(switch (id.toLowerCase(Locale.ROOT)) {
            case "wood" -> SoundType.WOOD;
            case "gravel" -> SoundType.GRAVEL;
            case "grass" -> SoundType.GRASS;
            case "lily_pad" -> SoundType.LILY_PAD;
            case "metal" -> SoundType.METAL;
            case "glass" -> SoundType.GLASS;
            case "wool" -> SoundType.WOOL;
            case "sand" -> SoundType.SAND;
            case "snow" -> SoundType.SNOW;
            case "powder_snow" -> SoundType.POWDER_SNOW;
            case "ladder" -> SoundType.LADDER;
            case "anvil" -> SoundType.ANVIL;
            case "slime_block" -> SoundType.SLIME_BLOCK;
            case "honey_block" -> SoundType.HONEY_BLOCK;
            case "wet_grass" -> SoundType.WET_GRASS;
            case "coral_block" -> SoundType.CORAL_BLOCK;
            case "bamboo" -> SoundType.BAMBOO;
            case "bamboo_sapling" -> SoundType.BAMBOO_SAPLING;
            case "scaffolding" -> SoundType.SCAFFOLDING;
            case "sweet_berry_bush" -> SoundType.SWEET_BERRY_BUSH;
            case "crop" -> SoundType.CROP;
            case "hard_crop" -> SoundType.HARD_CROP;
            case "vine" -> SoundType.VINE;
            case "nether_wart" -> SoundType.NETHER_WART;
            case "lantern" -> SoundType.LANTERN;
            case "stem" -> SoundType.STEM;
            case "nylium" -> SoundType.NYLIUM;
            case "fungus" -> SoundType.FUNGUS;
            case "roots" -> SoundType.ROOTS;
            case "shroomlight" -> SoundType.SHROOMLIGHT;
            case "weeping_vines" -> SoundType.WEEPING_VINES;
            case "twisting_vines" -> SoundType.TWISTING_VINES;
            case "soul_sand" -> SoundType.SOUL_SAND;
            case "soul_soil" -> SoundType.SOUL_SOIL;
            case "basalt" -> SoundType.BASALT;
            case "wart_block" -> SoundType.WART_BLOCK;
            case "netherrack" -> SoundType.NETHERRACK;
            case "nether_bricks" -> SoundType.NETHER_BRICKS;
            case "nether_sprouts" -> SoundType.NETHER_SPROUTS;
            case "nether_ore" -> SoundType.NETHER_ORE;
            case "bone_block" -> SoundType.BONE_BLOCK;
            case "netherite_block" -> SoundType.NETHERITE_BLOCK;
            case "ancient_debris" -> SoundType.ANCIENT_DEBRIS;
            case "lodestone" -> SoundType.LODESTONE;
            case "chain" -> SoundType.CHAIN;
            case "nether_gold_ore" -> SoundType.NETHER_GOLD_ORE;
            case "gilded_blackstone" -> SoundType.GILDED_BLACKSTONE;
            case "candle" -> SoundType.CANDLE;
            case "amethyst" -> SoundType.AMETHYST;
            case "amethyst_cluster" -> SoundType.AMETHYST_CLUSTER;
            case "small_amethyst_bud" -> SoundType.SMALL_AMETHYST_BUD;
            case "medium_amethyst_bud" -> SoundType.MEDIUM_AMETHYST_BUD;
            case "large_amethyst_bud" -> SoundType.LARGE_AMETHYST_BUD;
            case "tuff" -> SoundType.TUFF;
            case "calcite" -> SoundType.CALCITE;
            case "dripstone_block" -> SoundType.DRIPSTONE_BLOCK;
            case "pointed_dripstone" -> SoundType.POINTED_DRIPSTONE;
            case "copper" -> SoundType.COPPER;
            case "cave_vines" -> SoundType.CAVE_VINES;
            case "spore_blossom" -> SoundType.SPORE_BLOSSOM;
            case "azalea" -> SoundType.AZALEA;
            case "flowering_azalea" -> SoundType.FLOWERING_AZALEA;
            case "moss_carpet" -> SoundType.MOSS_CARPET;
            case "moss" -> SoundType.MOSS;
            case "big_dripleaf" -> SoundType.BIG_DRIPLEAF;
            case "small_dripleaf" -> SoundType.SMALL_DRIPLEAF;
            case "rooted_dirt" -> SoundType.ROOTED_DIRT;
            case "hanging_roots" -> SoundType.HANGING_ROOTS;
            case "azalea_leaves" -> SoundType.AZALEA_LEAVES;
            case "sculk_sensor" -> SoundType.SCULK_SENSOR;
            case "glow_lichen" -> SoundType.GLOW_LICHEN;
            case "deepslate" -> SoundType.DEEPSLATE;
            case "deepslate_bricks" -> SoundType.DEEPSLATE_BRICKS;
            case "deepslate_tiles" -> SoundType.DEEPSLATE_TILES;
            case "polished_deepslate" -> SoundType.POLISHED_DEEPSLATE;
            default -> SoundType.STONE;
        });
    }
}
