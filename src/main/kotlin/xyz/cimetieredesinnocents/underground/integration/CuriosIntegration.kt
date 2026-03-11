package xyz.cimetieredesinnocents.underground.integration

import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.event.entity.living.LivingEvent
import net.neoforged.neoforge.items.IItemHandlerModifiable
import xyz.cimetieredesinnocents.underground.Underground
import xyz.cimetieredesinnocents.underground.loaders.PlayerCapabilityLoader
import java.util.*

object CuriosIntegration {
    private var warned = false
    private fun warn() {
        if (warned) return
        Underground.LOGGER.warn("Curios API not found")
        warned = true
    }

    private val curiosApi = try {
        Class.forName("top.theillusivec4.curios.api.CuriosApi")
    } catch (_: ClassNotFoundException) {
        warn()
        null
    }

    private val getCuriosInventory = curiosApi?.getDeclaredMethod("getCuriosInventory", LivingEntity::class.java)

    private val iCuriosItemHandler = try {
        Class.forName("top.theillusivec4.curios.api.type.capability.ICuriosItemHandler")
    } catch (_: ClassNotFoundException) {
        warn()
        null
    }

    private val getEquippedCurios = iCuriosItemHandler?.getDeclaredMethod("getEquippedCurios")

    fun getCurios(entity: LivingEntity): IItemHandlerModifiable? {
        if (getCuriosInventory == null) return null
        val curiosInventoryOptional = getCuriosInventory.invoke(null, entity)
        val curiosInventory = (try {
            @Suppress("UNCHECKED_CAST")
            Optional<Any>::get.invoke(curiosInventoryOptional as Optional<Any>)
        } catch (_: NoSuchElementException) {
            null
        }) ?: return null

        if (getEquippedCurios == null) return null
        return getEquippedCurios.invoke(curiosInventory) as IItemHandlerModifiable
    }

    @Suppress("UNCHECKED_CAST")
    private val curioChangeEvent: Class<LivingEvent>? = try {
        Class.forName("top.theillusivec4.curios.api.event.CurioChangeEvent") as Class<LivingEvent>
    } catch (_: ClassNotFoundException) {
        warn()
        null
    }

    fun bootstrap(bus: IEventBus) {
        if (curioChangeEvent == null) return
        bus.addListener(curioChangeEvent) {
            val player = it.entity
            if (player !is Player) return@addListener
            if (player.level().isClientSide) return@addListener

            val cap = player.getCapability(PlayerCapabilityLoader.UNDERGROUND) ?: return@addListener
            cap.onCurioChange()
        }
    }
}