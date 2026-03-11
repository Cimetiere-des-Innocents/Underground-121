package xyz.cimetieredesinnocents.underground.loaders

import xyz.cimetieredesinnocents.cdilib.loaders.NetworkLoaderFactory
import xyz.cimetieredesinnocents.underground.network.UndergroundSyncPacket

object NetworkLoader : NetworkLoaderFactory() {
    val UNDERGROUND_SYNC = register(UndergroundSyncPacket)
}