package xyz.cimetieredesinnocents.underground.loaders

import thedarkcolour.kotlinforforge.neoforge.forge.getValue
import xyz.cimetieredesinnocents.cdilib.loaders.BlockEntityLoaderFactory
import xyz.cimetieredesinnocents.underground.Underground
import xyz.cimetieredesinnocents.underground.blockentity.GroundExplosionBlockEntity

object BlockEntityLoader : BlockEntityLoaderFactory(Underground.ID) {
    val GROUND_EXPLOSION by register("ground_explosion", ::GroundExplosionBlockEntity) { BlockLoader.GROUND_EXPLOSION }
}