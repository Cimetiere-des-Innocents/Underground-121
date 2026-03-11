package xyz.cimetieredesinnocents.underground.loaders

import xyz.cimetieredesinnocents.cdilib.loaders.DataGenLoaderFactory
import xyz.cimetieredesinnocents.underground.datagen.ModBlockStateProvider
import xyz.cimetieredesinnocents.underground.datagen.ModDamageTypeProvider

object DataGenLoader : DataGenLoaderFactory() {
    init {
        server(::ModDamageTypeProvider)
        client(::ModBlockStateProvider)
    }
}