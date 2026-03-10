package xyz.cimetieredesinnocents.underground.loaders

import net.minecraft.core.HolderLookup
import net.minecraft.data.DataProvider
import net.minecraft.data.PackOutput
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.common.data.ExistingFileHelper
import net.neoforged.neoforge.data.event.GatherDataEvent
import xyz.cimetieredesinnocents.underground.datagen.ModBlockStateProvider
import xyz.cimetieredesinnocents.underground.datagen.ModDamageTypeProvider
import java.util.concurrent.CompletableFuture

@Suppress("Unused")
typealias F<T> = DataProvider.Factory<T>

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
object DataGenLoader {
    @SubscribeEvent
    fun gatherData(event: GatherDataEvent) {
        val lp = event.lookupProvider
        val efh = event.existingFileHelper
        val generator = event.generator
        val includeServer = event.includeServer()
        // val includeClient = event.includeClient()

        fun <T : DataProvider> register(run: Boolean, constructor: (output: PackOutput, lp: CompletableFuture<HolderLookup.Provider>) -> T) {
            generator.addProvider(run, DataProvider.Factory { constructor(it, lp) })
        }

        fun <T : DataProvider> register(run: Boolean, constructor: (output: PackOutput, efh: ExistingFileHelper) -> T) {
            generator.addProvider(run, DataProvider.Factory { constructor(it, efh) })
        }

        register(includeServer, ::ModDamageTypeProvider)
        register(includeServer, ::ModBlockStateProvider)
    }
}