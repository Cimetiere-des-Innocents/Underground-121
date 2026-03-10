package xyz.cimetieredesinnocents.underground.blockentity

import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import xyz.cimetieredesinnocents.underground.loaders.BlockEntityLoader

class GroundExplosionBlockEntity(pos: BlockPos, blockState: BlockState) :
    BlockEntity(BlockEntityLoader.GROUND_EXPLOSION, pos, blockState) {
    var damageSource: Entity? = null
    var blocksToCreate = 0
    var isFirstTick = true

    override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.saveAdditional(tag, registries)
        if (damageSource != null) {
            tag.putUUID("damageSource", damageSource!!.uuid)
        }
        tag.putInt("blocksToCreate", blocksToCreate)
        tag.putBoolean("isFirstTick", isFirstTick)
    }

    override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.loadAdditional(tag, registries)
        blocksToCreate = if (tag.contains("blocksToCreate")) tag.getInt("blocksToCreate") else 0
        isFirstTick = if (tag.contains("isFirstTick")) tag.getBoolean("isFirstTick") else true

        if (level?.isClientSide ?: true) return
        val uuid = if (tag.hasUUID("damageSource")) tag.getUUID("damageSource") else null
        damageSource = if (uuid != null) (level as ServerLevel).entities.get(uuid) else null
    }
}