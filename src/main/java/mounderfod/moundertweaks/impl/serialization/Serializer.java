package mounderfod.moundertweaks.impl.serialization;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;

import com.google.common.collect.ImmutableMap;

import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.NbtOps;

import net.fabricmc.loader.api.FabricLoader;

public final class Serializer {
    public static final Path DATA_CONFIG = FabricLoader.getInstance().getConfigDir().resolve("moundertweaks-data.dat");
    private static final Consumer<String> STDERR = System.err::println;

    public static void check() {
        try {
            if (Files.isDirectory(DATA_CONFIG)) {
                Files.delete(DATA_CONFIG);
            }
            if (!Files.exists(DATA_CONFIG)) {
                Files.createFile(DATA_CONFIG);
                writeDefaultValues();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeDefaultValues() throws IOException {
        Fuel fuel = new Fuel(ImmutableMap.of(
                Items.GUNPOWDER, 1200,
                Items.BLAZE_POWDER, 1200
        ));
        ShovelPath path = new ShovelPath(
                ImmutableMap.of(
                        Blocks.DIRT, Blocks.GRASS_PATH.getDefaultState(),
                        Blocks.COBBLESTONE, Blocks.GRAVEL.getDefaultState(),
                        Blocks.GRAVEL, Blocks.SAND.getDefaultState()
                )
        );
        CompoundTag all = new CompoundTag();
        CompoundTag fuelTag = (CompoundTag) Fuel.CODEC.encodeStart(NbtOps.INSTANCE, fuel).getOrThrow(false, STDERR);
        CompoundTag pathTag = (CompoundTag) ShovelPath.CODEC.encodeStart(NbtOps.INSTANCE, path).getOrThrow(false, STDERR);
        all.put("fuel", fuelTag.get("fuel"));
        all.put("shovel_path", pathTag.get("shovel_path"));
        NbtIo.writeCompressed(all, DATA_CONFIG.toFile());
    }
}
