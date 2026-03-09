package xyz.cimetieredesinnocents.underground.loaders

import net.minecraft.world.entity.EntityType
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.capabilities.EntityCapability
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent
import xyz.cimetieredesinnocents.underground.player.IUndergroundCapability
import xyz.cimetieredesinnocents.underground.player.UndergroundCapabilityProvider
import xyz.cimetieredesinnocents.underground.utils.RLUtil


@Suppress("MemberVisibilityCanBePrivate")
@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
object PlayerCapabilityLoader {
    val UNDERGROUND = EntityCapability.createVoid(RLUtil.of("underground"), IUndergroundCapability::class.java)

    @SubscribeEvent
    fun registerCapabilities(event: RegisterCapabilitiesEvent) {
        event.registerEntity(UNDERGROUND, EntityType.PLAYER, UndergroundCapabilityProvider())
    }
}