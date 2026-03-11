package xyz.cimetieredesinnocents.underground.loaders.listeners

import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent
import xyz.cimetieredesinnocents.underground.hud.UndergroundHudBackground
import xyz.cimetieredesinnocents.underground.utils.RLUtil

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, value = [Dist.CLIENT])
object HudLoader {
    @SubscribeEvent
    fun registerGuiLayers(event: RegisterGuiLayersEvent) {
        event.registerBelowAll(RLUtil.of("underground_hud_background"), UndergroundHudBackground::render)
    }
}