package mounderfod.moundertweaks.impl;

import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.serializer.JanksonConfigSerializer;
import mounderfod.moundertweaks.impl.serialization.deserialization.Deserializer;
import mounderfod.moundertweaks.util.config.MounderTweaksConfig;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowerPotBlock;
import net.minecraft.block.RedstoneLampBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;

public class MounderTweaksMain implements ModInitializer {

    public static final MounderTweaksConfig CONFIG;

    @Override
    public void onInitialize() {
        Deserializer.run();

        UseBlockCallback.EVENT.register(((playerEntity, world, hand, blockHitResult) -> {
            ActionResult result = ActionResult.PASS;
            BlockState usedBlockState = world.getBlockState(blockHitResult.getBlockPos());
            Block usedBlock = usedBlockState.getBlock();
            Item heldItem = playerEntity.getStackInHand(hand).getItem();
            if(!world.isClient()) {
                if (MounderTweaksMain.CONFIG.common.lampToggle) {
                    if (heldItem == Items.REDSTONE_TORCH && usedBlock == Blocks.REDSTONE_LAMP) {
                        world.setBlockState(blockHitResult.getBlockPos(), usedBlock.getDefaultState().with(RedstoneLampBlock.LIT, !usedBlockState.get(RedstoneLampBlock.LIT)));
                    }
                }
                if (MounderTweaksMain.CONFIG.common.harvestablePots) {
                    if (heldItem.isIn(FabricToolTags.SHOVELS) && usedBlock instanceof FlowerPotBlock && usedBlock != Blocks.FLOWER_POT) {
                        world.setBlockState(blockHitResult.getBlockPos(), Blocks.FLOWER_POT.getDefaultState());
                        EntityType.ITEM.spawn((ServerWorld) world, null, null, null, blockHitResult.getBlockPos(), SpawnReason.EVENT, false, false);
                    }
                }
            }
            return result;
        }));
    }

    static {
        CONFIG = AutoConfig.register(MounderTweaksConfig.class, JanksonConfigSerializer::new).getConfig();
    }
}


