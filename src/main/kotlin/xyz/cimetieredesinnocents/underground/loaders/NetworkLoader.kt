package xyz.cimetieredesinnocents.underground.loaders

import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent
import xyz.cimetieredesinnocents.cdilib.loaders.NetworkLoaderFactory
import xyz.cimetieredesinnocents.underground.network.UndergroundSyncPacket

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
object NetworkLoader : NetworkLoaderFactory() {
    @SubscribeEvent
    fun onRegisterPayload(event: RegisterPayloadHandlersEvent) {
        bootstrap(event)
    }

    val UNDERGROUND_SYNC = register(UndergroundSyncPacket)
}