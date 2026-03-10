package xyz.cimetieredesinnocents.underground.loaders

import net.minecraft.world.item.Items
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent
import xyz.cimetieredesinnocents.underground.item.datacomponents.UndergroundModifiers

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
object DefaultDataComponentsLoader {
    @SubscribeEvent
    fun modifyComponents(event: ModifyDefaultComponentsEvent) {
        event.modify(Items.NETHERITE_HELMET) { it.set(DataComponentLoader.UNDERGROUND_MODIFIERS, UndergroundModifiers(
            listOf(
                UndergroundModifiers.Modifier(UndergroundModifiers.Modifier.Type.MAX_EXPOSURE, 25600)
            )))
        }
    }
}