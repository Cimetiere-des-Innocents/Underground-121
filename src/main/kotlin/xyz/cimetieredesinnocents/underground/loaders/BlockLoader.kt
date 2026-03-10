package xyz.cimetieredesinnocents.underground.loaders

import thedarkcolour.kotlinforforge.neoforge.forge.getValue
import xyz.cimetieredesinnocents.cdilib.loaders.BlockLoaderFactory
import xyz.cimetieredesinnocents.underground.Underground
import xyz.cimetieredesinnocents.underground.block.GroundExplosionBlock

object BlockLoader : BlockLoaderFactory(Underground.ID, ItemLoader) {
    val GROUND_EXPLOSION by blockOnly("ground_explosion", ::GroundExplosionBlock)
}