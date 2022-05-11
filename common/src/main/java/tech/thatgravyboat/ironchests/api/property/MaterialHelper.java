package tech.thatgravyboat.ironchests.api.property;

import com.mojang.serialization.DataResult;
import net.minecraft.world.level.material.Material;

import java.util.Locale;

public class MaterialHelper {

    public static DataResult<Material> getMaterial(String id) {
        Material material = switch (id.toLowerCase(Locale.ROOT)) {
            case "air" -> Material.AIR;
            case "structural_air" -> Material.STRUCTURAL_AIR;
            case "portal" -> Material.PORTAL;
            case "cloth_decoration" -> Material.CLOTH_DECORATION;
            case "plant" -> Material.PLANT;
            case "water_plant" -> Material.WATER_PLANT;
            case "replaceable_plant" -> Material.REPLACEABLE_PLANT;
            case "replaceable_fireproof_plant" -> Material.REPLACEABLE_FIREPROOF_PLANT;
            case "replaceable_water_plant" -> Material.REPLACEABLE_WATER_PLANT;
            case "water" -> Material.WATER;
            case "bubble_column" -> Material.BUBBLE_COLUMN;
            case "lava" -> Material.LAVA;
            case "top_snow" -> Material.TOP_SNOW;
            case "fire" -> Material.FIRE;
            case "decoration" -> Material.DECORATION;
            case "web" -> Material.WEB;
            case "sculk" -> Material.SCULK;
            case "buildable_glass" -> Material.BUILDABLE_GLASS;
            case "clay" -> Material.CLAY;
            case "dirt" -> Material.DIRT;
            case "grass" -> Material.GRASS;
            case "ice_solid" -> Material.ICE_SOLID;
            case "sand" -> Material.SAND;
            case "sponge" -> Material.SPONGE;
            case "shulker_shell" -> Material.SHULKER_SHELL;
            case "wood" -> Material.WOOD;
            case "nether_wood" -> Material.NETHER_WOOD;
            case "bamboo_sapling" -> Material.BAMBOO_SAPLING;
            case "bamboo" -> Material.BAMBOO;
            case "wool" -> Material.WOOL;
            case "explosive" -> Material.EXPLOSIVE;
            case "leaves" -> Material.LEAVES;
            case "glass" -> Material.GLASS;
            case "ice" -> Material.ICE;
            case "cactus" -> Material.CACTUS;
            case "stone" -> Material.STONE;
            case "metal" -> Material.METAL;
            case "snow" -> Material.SNOW;
            case "heavy_metal" -> Material.HEAVY_METAL;
            case "barrier" -> Material.BARRIER;
            case "piston" -> Material.PISTON;
            case "moss" -> Material.MOSS;
            case "vegetable" -> Material.VEGETABLE;
            case "egg" -> Material.EGG;
            case "cake" -> Material.CAKE;
            case "amethyst" -> Material.AMETHYST;
            case "powder_snow" -> Material.POWDER_SNOW;
            default -> null;
        };
        return material == null ? DataResult.error("Material not found") : DataResult.success(material);
    }
}
