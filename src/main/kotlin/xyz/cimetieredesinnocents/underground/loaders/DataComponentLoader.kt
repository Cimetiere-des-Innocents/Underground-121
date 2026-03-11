package xyz.cimetieredesinnocents.underground.loaders

import thedarkcolour.kotlinforforge.neoforge.forge.getValue
import xyz.cimetieredesinnocents.cdilib.loaders.DataComponentLoaderFactory
import xyz.cimetieredesinnocents.underground.Underground
import xyz.cimetieredesinnocents.underground.item.datacomponents.UndergroundModifiers

object DataComponentLoader : DataComponentLoaderFactory(Underground.ID) {
    val UNDERGROUND_MODIFIERS by register("underground_modifiers", UndergroundModifiers.CODEC, UndergroundModifiers.STREAM_CODEC)
}