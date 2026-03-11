package xyz.cimetieredesinnocents.underground.loaders.datagen

import xyz.cimetieredesinnocents.cdilib.loaders.datagen.DamageTypeLoaderFactory
import xyz.cimetieredesinnocents.underground.Underground

object DamageTypeLoader : DamageTypeLoaderFactory(Underground.ID) {
    val SUNBURNT by register("sunburnt")
}