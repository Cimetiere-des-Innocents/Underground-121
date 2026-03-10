package xyz.cimetieredesinnocents.underground.datagen

import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import xyz.cimetieredesinnocents.cdilib.datagen.DamageTypeProviderFactory
import xyz.cimetieredesinnocents.underground.Underground
import xyz.cimetieredesinnocents.underground.loaders.DamageTypeLoader
import java.util.concurrent.CompletableFuture

class ModDamageTypeProvider(
    output: PackOutput,
    registries: CompletableFuture<HolderLookup.Provider>
) : DamageTypeProviderFactory(Underground.ID, DamageTypeLoader, output, registries)