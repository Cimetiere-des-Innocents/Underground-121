package xyz.cimetieredesinnocents.underground.block

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.item.DyeColor
import net.minecraft.world.level.Explosion
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v3d.toVec3
import xyz.cimetieredesinnocents.underground.blockentity.GroundExplosionBlockEntity
import xyz.cimetieredesinnocents.underground.loaders.DamageTypeLoader

class GroundExplosionBlock : Block(
    Properties
        .of()
        .strength(10f, 0.000001f)
        .mapColor(DyeColor.RED)
), EntityBlock {
    override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity {
        return GroundExplosionBlockEntity(pos, state)
    }

    override fun onBlockExploded(state: BlockState, level: Level, pos: BlockPos, explosion: Explosion) {
        super.onBlockExploded(state, level, pos, explosion)
    }

    override fun tick(state: BlockState, level: ServerLevel, pos: BlockPos, random: RandomSource) {
        val be = level.getBlockEntity(pos)
        if (be !is GroundExplosionBlockEntity) return

        if (be.isFirstTick) {
            be.isFirstTick = false
            spreadGroundExplosion(level, pos, be)
            be.setChanged()
            level.scheduleTick(pos, this, 20)
            return
        }

        level.explode(
            be.damageSource,
            DamageSource(DamageTypeLoader.SUNBURNT(level), be.damageSource),
            null,
            pos.toVec3(),
            5f,
            true,
            Level.ExplosionInteraction.BLOCK
        )
    }

    private fun spreadGroundExplosion(level: ServerLevel, pos: BlockPos, be: GroundExplosionBlockEntity) {
        val blocksToCreate = be.blocksToCreate
        be.blocksToCreate = 0
        if (blocksToCreate <= 0) return

        val candidates = mutableListOf<BlockPos>()
        for (x in -1..1) {
            for (y in -1..1) {
                for (z in -1..1) {
                    if (x == 0 && y == 0 && z == 0) continue
                    val checkPos = pos.offset(x, y, z)
                    val checkState = level.getBlockState(checkPos)
                    if (!checkState.isAir &&
                        !checkState.`is`(Blocks.BEDROCK) &&
                        checkState.block !is GroundExplosionBlock
                    ) {
                        candidates.add(checkPos)
                    }
                }
            }
        }

        if (candidates.isEmpty()) return

        val maxToConvert = minOf(candidates.size, blocksToCreate)
        val numToConvert = level.random.nextInt(maxToConvert) + 1
        candidates.shuffle()
        val toConvert = candidates.take(numToConvert)

        val remainingBlocks = (blocksToCreate - numToConvert).coerceAtLeast(0)
        val blocksPerNew = remainingBlocks / numToConvert
        val extraBlocks = remainingBlocks % numToConvert

        toConvert.forEachIndexed { index, convertPos ->
            level.setBlock(convertPos, defaultBlockState(), 3)
            val newBe = level.getBlockEntity(convertPos)
            if (newBe is GroundExplosionBlockEntity) {
                newBe.damageSource = be.damageSource
                newBe.blocksToCreate = blocksPerNew + if (index < extraBlocks) 1 else 0
                newBe.isFirstTick = true
                newBe.setChanged()
                level.scheduleTick(convertPos, this, 1)
            }
        }
    }
}