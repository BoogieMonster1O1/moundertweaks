package mounderfod.moundertweaks.impl.serialization;

import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class Composting {
    public static final Codec<Composting> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.unboundedMap(RegistryCodecs.ITEM, Codec.FLOAT).fieldOf("composting").forGetter(Composting::getMap)
    ).apply(instance, Composting::new));
    private final Map<Item, Float> map;

    public Composting(Map<Item, Float> map) {
        this.map = map;
    }

    public Map<Item, Float> getMap() {
        return this.map;
    }

    public static Composting getDefault() {
        return new Composting(
                ImmutableMap.of(
                        Items.POISONOUS_POTATO, 0.1F,
                        Items.DIRT, 0.25F,
                        Items.GRASS_BLOCK, 0.25F
                )
        );
    }
}
