package xyz.cimetieredesinnocents.underground.loaders

import thedarkcolour.kotlinforforge.neoforge.forge.getValue
import xyz.cimetieredesinnocents.cdilib.loaders.DataAttachmentLoaderFactory
import xyz.cimetieredesinnocents.underground.Underground

object DataAttachmentLoader : DataAttachmentLoaderFactory(Underground.ID) {
    val UNDERGROUND_EXPOSURE by registerInt("exposure")
    val UNDERGROUND_THREAT by registerInt("threat")
    val UNDERGROUND_SHIELD by registerInt("shield")
}