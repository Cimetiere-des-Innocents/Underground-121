package xyz.cimetieredesinnocents.underground.loaders

import xyz.cimetieredesinnocents.cdilib.loaders.DamageTypeLoaderFactory
import xyz.cimetieredesinnocents.underground.Underground

object DamageTypeLoader : DamageTypeLoaderFactory(Underground.ID) {
    val SUNBURNT by register("sunburnt")
}