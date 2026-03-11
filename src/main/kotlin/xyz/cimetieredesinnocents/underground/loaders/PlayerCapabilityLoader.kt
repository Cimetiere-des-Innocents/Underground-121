package xyz.cimetieredesinnocents.underground.loaders

import xyz.cimetieredesinnocents.cdilib.loaders.PlayerCapabilityLoaderFactory
import xyz.cimetieredesinnocents.underground.Underground
import xyz.cimetieredesinnocents.underground.player.UndergroundCapabilityProvider

object PlayerCapabilityLoader : PlayerCapabilityLoaderFactory(Underground.ID) {
    val UNDERGROUND = register("underground", UndergroundCapabilityProvider)
}