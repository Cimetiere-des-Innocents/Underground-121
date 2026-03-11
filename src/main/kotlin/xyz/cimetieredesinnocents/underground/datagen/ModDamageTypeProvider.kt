package xyz.cimetieredesinnocents.underground.datagen

import xyz.cimetieredesinnocents.cdilib.datagen.DamageTypeProviderFactory
import xyz.cimetieredesinnocents.cdilib.loaders.DataGenLoaderFactory
import xyz.cimetieredesinnocents.underground.Underground
import xyz.cimetieredesinnocents.underground.loaders.datagen.DamageTypeLoader

class ModDamageTypeProvider(
    context: DataGenLoaderFactory.Context
) : DamageTypeProviderFactory(Underground.ID, DamageTypeLoader, context)