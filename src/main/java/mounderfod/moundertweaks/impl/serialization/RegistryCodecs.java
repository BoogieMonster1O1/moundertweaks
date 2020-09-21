package mounderfod.moundertweaks.impl.serialization;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Lifecycle;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public interface RegistryCodecs {
    Codec<Block> BLOCK = Identifier.CODEC.comapFlatMap((id) -> DataResult.success(Registry.BLOCK.get(id), Lifecycle.stable()), Registry.BLOCK::getId);
    Codec<Item> ITEM = Identifier.CODEC.comapFlatMap((id) -> DataResult.success(Registry.ITEM.get(id), Lifecycle.stable()), Registry.ITEM::getId);
}
