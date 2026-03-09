package xyz.cimetieredesinnocents.underground.datagen

import net.minecraft.core.HolderLookup
import net.minecraft.core.RegistrySetBuilder
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.world.damagesource.DamageType
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider
import xyz.cimetieredesinnocents.underground.Underground
import xyz.cimetieredesinnocents.underground.loaders.DamageTypeLoader
import java.util.concurrent.CompletableFuture

class ModDamageTypeProvider(
    output: PackOutput,
    registries: CompletableFuture<HolderLookup.Provider>
) : DatapackBuiltinEntriesProvider(
    output,
    registries,
    RegistrySetBuilder().add(Registries.DAMAGE_TYPE) {
        for (rawValue in DamageTypeLoader.REGISTRY) {
            it.register(rawValue.resourceKey, DamageType(
                "${Underground.ID}.${rawValue.name}",
                rawValue.scaling,
                rawValue.exhaustion,
                rawValue.effects,
                rawValue.deathMessageType
            ))
        }
    },
    setOf(Underground.ID)
)