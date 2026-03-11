package xyz.cimetieredesinnocents.underground.player

import xyz.cimetieredesinnocents.cdilib.player.PlayerCapabilityProvider
import xyz.cimetieredesinnocents.underground.loaders.PlayerCapabilityLoader

object UndergroundCapabilityProvider :
    PlayerCapabilityProvider<IUndergroundCapability, UndergroundCapability>(
        IUndergroundCapability::class.java,
        ::UndergroundCapability,
        { PlayerCapabilityLoader.UNDERGROUND }
    )