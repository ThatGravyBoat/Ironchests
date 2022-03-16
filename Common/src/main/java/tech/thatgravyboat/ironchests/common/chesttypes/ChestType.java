package tech.thatgravyboat.ironchests.common.chesttypes;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.Locale;
import java.util.function.Predicate;

public record ChestType(String name, int length, int rows, int size,
                        int inventoryOffset, int menuOffset,
                        int width, int height,
                        BlockBehaviour.Properties properties,
                        ChestRegistries registries,
                        boolean transparent, String texture,
                        Predicate<ItemStack> slotPredicate,
                        boolean renderItems) {

    public ChestType {
        if (size < 18 && renderItems) {
            throw new IllegalArgumentException("Chest Type requires the chest size to be at least 18 total for render items be shown as it looks the best with 18 items.");
        }
    }

    public static class Builder {

        private final String name;
        private final int length;
        private final int rows;
        private int invOffset;
        private int menuOffset;
        private int width;
        private int height;
        private ChestRegistries registries;
        private BlockBehaviour.Properties properties;
        private boolean transparent;
        private String texture;
        private Predicate<ItemStack> predicate;
        private boolean renderItems;

        public Builder(String name, int length, int rows){
            this.name = name;
            this.length = length;
            this.rows = rows;
            this.texture = this.name.toLowerCase(Locale.ENGLISH);
        }

        public Builder setOffsets(int inv, int menu){
            this.invOffset = inv;
            this.menuOffset = menu;
            return this;
        }

        public Builder setDimensions(int width, int height){
            this.width = width;
            this.height = height;
            return this;
        }

        public Builder setRegistries(ChestRegistries registries){
            this.registries = registries;
            return this;
        }

        public Builder setProperties(BlockBehaviour.Properties properties){
            this.properties = properties;
            return this;
        }

        public Builder isTransparent() {
            this.transparent = true;
            return this;
        }

        public Builder setTexture(String texture){
            this.texture = texture;
            return this;
        }

        public Builder setMenuPredicate(Predicate<ItemStack> predicate){
            this.predicate = predicate;
            return this;
        }

        public Builder shouldRenderItems(){
            this.renderItems = true;
            return this;
        }

        public ChestType build(){
            return new ChestType(name, length, rows, length*rows, invOffset, menuOffset, width, height, properties, registries, transparent, texture, predicate, renderItems);
        }
    }
}

