package xyz.cimetieredesinnocents.underground.loaders

import net.minecraft.data.DataProvider
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.data.event.GatherDataEvent
import xyz.cimetieredesinnocents.underground.datagen.ModDamageTypeProvider

@Suppress("Unused")
typealias F<T> = DataProvider.Factory<T>

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
object DataGenLoader {
    @SubscribeEvent
    fun gatherData(event: GatherDataEvent) {
        val lp = event.lookupProvider
        // val efh = event.existingFileHelper
        val generator = event.generator
        val includeServer = event.includeServer()
        // val includeClient = event.includeClient()

        generator.addProvider(includeServer, F { ModDamageTypeProvider(it, lp) })
    }
}