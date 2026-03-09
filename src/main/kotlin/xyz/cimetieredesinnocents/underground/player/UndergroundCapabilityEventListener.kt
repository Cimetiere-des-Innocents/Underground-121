package xyz.cimetieredesinnocents.underground.player

import net.minecraft.world.entity.player.Player
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.entity.living.LivingEquipmentChangeEvent
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent
import net.neoforged.neoforge.event.entity.player.ItemEntityPickupEvent
import net.neoforged.neoforge.event.entity.player.PlayerEvent
import net.neoforged.neoforge.event.level.BlockEvent
import net.neoforged.neoforge.event.tick.PlayerTickEvent
import top.theillusivec4.curios.api.event.CurioChangeEvent
import xyz.cimetieredesinnocents.underground.loaders.PlayerCapabilityLoader

@EventBusSubscriber
object UndergroundCapabilityEventListener {
    @SubscribeEvent
    fun onTick(event: PlayerTickEvent.Pre) {
        if (event.entity.level().isClientSide) return
        val cap = event.entity.getCapability(PlayerCapabilityLoader.UNDERGROUND) ?: return
        cap.onTick()
    }

    @SubscribeEvent
    fun onPick(event: ItemEntityPickupEvent.Pre) {
        if (event.player.level().isClientSide) return
        val cap = event.player.getCapability(PlayerCapabilityLoader.UNDERGROUND) ?: return
        cap.onPickItem(event.itemEntity.item.count)
    }

    @SubscribeEvent
    fun onAttack(event: AttackEntityEvent) {
        if (event.entity.level().isClientSide) return
        val cap = event.entity.getCapability(PlayerCapabilityLoader.UNDERGROUND) ?: return
        cap.onAttack()
    }

    @SubscribeEvent
    fun onPutBlock(event: BlockEvent.EntityPlaceEvent) {
        if (event.entity == null) return
        if (event.entity !is Player) return
        if (event.entity!!.level().isClientSide) return
        val cap = event.entity!!.getCapability(PlayerCapabilityLoader.UNDERGROUND) ?: return
        cap.onPutBlock()
    }

    @SubscribeEvent
    fun onBreakBlock(event: BlockEvent.BreakEvent) {
        if (event.player.level().isClientSide) return
        val cap = event.player.getCapability(PlayerCapabilityLoader.UNDERGROUND) ?: return
        cap.onBreakBlock()
    }

    @SubscribeEvent
    fun onCurioChange(event: CurioChangeEvent) {
        val player = event.entity
        if (player !is Player) return
        if (player.level().isClientSide) return
        val cap = player.getCapability(PlayerCapabilityLoader.UNDERGROUND) ?: return
        cap.onCurioChange()
    }

    @SubscribeEvent
    fun onEquipmentChange(event: LivingEquipmentChangeEvent) {
        val player = event.entity
        if (player !is Player) return
        if (player.level().isClientSide) return
        val cap = player.getCapability(PlayerCapabilityLoader.UNDERGROUND) ?: return
        cap.onArmorChange()
    }

    @SubscribeEvent
    fun onRespawn(event: PlayerEvent.PlayerRespawnEvent) {
        val player = event.entity
        val cap = player.getCapability(PlayerCapabilityLoader.UNDERGROUND) ?: return
        if (cap is UndergroundCapability) {
            cap.player = player
        }
    }
}