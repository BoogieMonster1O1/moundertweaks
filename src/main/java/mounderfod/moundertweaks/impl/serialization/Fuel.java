package mounderfod.moundertweaks.impl.serialization;

import java.util.Map;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.item.Item;

public class Fuel {
    public static final Codec<Fuel> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.unboundedMap(RegistryCodecs.ITEM, Codec.INT).fieldOf("fuel").forGetter(Fuel::getMap)
    ).apply(instance, Fuel::new));

    private final Map<Item, Integer> map;

    Fuel(Map<Item, Integer> map) {
        this.map = map;
    }

    public Map<Item, Integer> getMap() {
        return this.map;
    }
}
