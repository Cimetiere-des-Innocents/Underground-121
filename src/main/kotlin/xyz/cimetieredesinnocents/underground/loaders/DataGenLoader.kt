package xyz.cimetieredesinnocents.underground.loaders

import xyz.cimetieredesinnocents.cdilib.loaders.DataGenLoaderFactory
import xyz.cimetieredesinnocents.underground.datagen.ModBlockStateProvider
import xyz.cimetieredesinnocents.underground.loaders.datagen.DamageTypeLoader

object DataGenLoader : DataGenLoaderFactory() {
    init {
        DamageTypeLoader.bootstrap(this)
        client(::ModBlockStateProvider)
    }
}