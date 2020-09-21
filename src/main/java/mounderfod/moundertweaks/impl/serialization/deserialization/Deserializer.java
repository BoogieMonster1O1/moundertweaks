package mounderfod.moundertweaks.impl.serialization.deserialization;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.function.Consumer;

import mounderfod.moundertweaks.impl.serialization.Fuel;
import mounderfod.moundertweaks.impl.serialization.Serializer;
import mounderfod.moundertweaks.impl.serialization.ShovelPath;
import net.szum123321.tool_action_helper.api.ShovelPathHelper;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.NbtOps;

import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.fabricmc.loader.api.FabricLoader;

public final class Deserializer {
    private static boolean initialized = false;
    private static final Consumer<String> STDERR = System.err::println;
    public static final Path DATA_CONFIG = FabricLoader.getInstance().getConfigDir().resolve("moundertweaks-data.dat");

    public static void initialize() {
        if (initialized) {
            throw new IllegalStateException("Can't Initialize twice!");
        }
        Serializer.check();
        initialized = true;
        try {
            CompoundTag all = NbtIo.readCompressed(DATA_CONFIG.toFile());
            if (all.contains("fuel")) {
                Fuel fuel = Fuel.CODEC.decode(NbtOps.INSTANCE, all).getOrThrow(false, STDERR).getFirst();
                for (Map.Entry<Item, Integer> entry : fuel.getMap().entrySet()) {
                    Item item = entry.getKey();
                    Integer time = entry.getValue();
                    FuelRegistry.INSTANCE.add(item, time);
                }
            } else {
                all.put("fuel", (CompoundTag) Fuel.CODEC.encodeStart(NbtOps.INSTANCE, Fuel.getDefault()).getOrThrow(false, STDERR));
            }
            if (all.contains("shovel_path")) {
                ShovelPath path = ShovelPath.CODEC.decode(NbtOps.INSTANCE, all).getOrThrow(false, STDERR).getFirst();
                for (Map.Entry<Block, BlockState> e : path.getMap().entrySet()) {
                    Block block = e.getKey();
                    BlockState blockState = e.getValue();
                    ShovelPathHelper.getInstance().addNewPathPair(block, blockState);
                }
            } else {
                all.put("shovel_path", (CompoundTag) ShovelPath.CODEC.encodeStart(NbtOps.INSTANCE, ShovelPath.getDefault()).getOrThrow(false, STDERR));
            }
            NbtIo.writeCompressed(all, DATA_CONFIG.toFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isInitialized() {
        return initialized;
    }
}
