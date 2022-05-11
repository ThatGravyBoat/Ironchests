package tech.thatgravyboat.ironchests.api;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;

import java.util.function.Function;

/**
 * This is taken from Resourceful Bees and with permission from myself and creator and owner of the code(ThatGravyBoat).
 */
public class CodecUtils {

    public static <T> Codec<T> passthrough(Function<T, JsonElement> encoder, Function<JsonElement, T> decoder) {
        return Codec.PASSTHROUGH.comapFlatMap(dynamic -> decoder(dynamic, decoder), item -> encoder(item, encoder));
    }

    private static <T> DataResult<T> decoder(Dynamic<?> dynamic, Function<JsonElement, T> decoder) {
        if (dynamic.getValue() instanceof JsonElement jsonElement) {
            return DataResult.success(decoder.apply(jsonElement));
        } else {
            return DataResult.error("value was some how not a JsonElement");
        }
    }

    private static <T> Dynamic<JsonElement> encoder(T input, Function<T, JsonElement> encoder) {
        return new Dynamic<>(JsonOps.INSTANCE, encoder.apply(input));
    }
}
