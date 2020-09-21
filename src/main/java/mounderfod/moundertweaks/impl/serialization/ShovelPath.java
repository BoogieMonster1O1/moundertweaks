package mounderfod.moundertweaks.impl.serialization;

import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;

public class ShovelPath {
    public static final Codec<ShovelPath> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.unboundedMap(RegistryCodecs.BLOCK, BlockState.CODEC).fieldOf("shovel_path").forGetter(ShovelPath::getMap)
    ).apply(instance, ShovelPath::new));

    private final Map<Block, BlockState> map;

    ShovelPath(Map<Block, BlockState> map) {
        this.map = map;
    }

    public Map<Block, BlockState> getMap() {
        return this.map;
    }

    public static ShovelPath getDefault() {
        return new ShovelPath(
                ImmutableMap.of(
                        Blocks.DIRT, Blocks.GRASS_PATH.getDefaultState(),
                        Blocks.COBBLESTONE, Blocks.GRAVEL.getDefaultState(),
                        Blocks.GRAVEL, Blocks.SAND.getDefaultState()
                )
        );
    }
}
