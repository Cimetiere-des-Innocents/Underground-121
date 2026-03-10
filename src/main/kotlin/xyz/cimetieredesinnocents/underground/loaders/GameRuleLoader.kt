package xyz.cimetieredesinnocents.underground.loaders

import net.minecraft.world.level.GameRules
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
object GameRuleLoader {
    lateinit var UNDERGROUND_MODE: GameRules.Key<GameRules.BooleanValue>

    @SubscribeEvent
    fun commonSetup(event: FMLCommonSetupEvent) {
        event.enqueueWork {
            UNDERGROUND_MODE = GameRules.register("underground.undergroundMode", GameRules.Category.PLAYER, GameRules.BooleanValue.create(false))
        }
    }
}