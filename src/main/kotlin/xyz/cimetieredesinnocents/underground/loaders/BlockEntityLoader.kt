package xyz.cimetieredesinnocents.underground.loaders

import net.minecraft.core.BlockPos
import net.minecraft.core.registries.Registries
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import thedarkcolour.kotlinforforge.neoforge.forge.getValue
import xyz.cimetieredesinnocents.underground.Underground
import xyz.cimetieredesinnocents.underground.blockentity.GroundExplosionBlockEntity

object BlockEntityLoader {
    val REGISTRY = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Underground.ID)

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    fun <T : BlockEntity> register(
        name: String,
        blockEntity: (BlockPos, BlockState) -> T,
        block: () -> Block
    ): DeferredHolder<BlockEntityType<*>, BlockEntityType<T>> {
        return REGISTRY.register(name) { ->
            BlockEntityType.Builder.of(blockEntity, block()).build(null)
        }
    }

    val GROUND_EXPLOSION by register("ground_explosion", ::GroundExplosionBlockEntity) { BlockLoader.GROUND_EXPLOSION }
}