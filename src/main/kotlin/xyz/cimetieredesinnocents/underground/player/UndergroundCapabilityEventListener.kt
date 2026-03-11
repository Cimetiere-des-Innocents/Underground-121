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
import xyz.cimetieredesinnocents.underground.loaders.PlayerCapabilityLoader
import xyz.cimetieredesinnocents.underground.loaders.GameRuleLoader

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
        val gameRule = event.player.level().gameRules.getRule(GameRuleLoader.UNDERGROUND_MODE).get()
        if (!gameRule) return

        val cap = event.player.getCapability(PlayerCapabilityLoader.UNDERGROUND) ?: return
        cap.onPickItem(event.itemEntity.item.count)
    }

    @SubscribeEvent
    fun onAttack(event: AttackEntityEvent) {
        if (event.entity.level().isClientSide) return
        val gameRule = event.entity.level().gameRules.getRule(GameRuleLoader.UNDERGROUND_MODE).get()
        if (!gameRule) return

        val cap = event.entity.getCapability(PlayerCapabilityLoader.UNDERGROUND) ?: return
        cap.onAttack()
    }

    @SubscribeEvent
    fun onPutBlock(event: BlockEvent.EntityPlaceEvent) {
        if (event.entity == null) return
        if (event.entity !is Player) return
        if (event.entity!!.level().isClientSide) return
        val gameRule = event.entity!!.level().gameRules.getRule(GameRuleLoader.UNDERGROUND_MODE).get()
        if (!gameRule) return

        val cap = event.entity!!.getCapability(PlayerCapabilityLoader.UNDERGROUND) ?: return
        cap.onPutBlock()
    }

    @SubscribeEvent
    fun onBreakBlock(event: BlockEvent.BreakEvent) {
        if (event.player.level().isClientSide) return
        val gameRule = event.player.level().gameRules.getRule(GameRuleLoader.UNDERGROUND_MODE).get()
        if (!gameRule) return

        val cap = event.player.getCapability(PlayerCapabilityLoader.UNDERGROUND) ?: return
        cap.onBreakBlock()
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
    fun onPlayerLoggedIn(event: PlayerEvent.PlayerLoggedInEvent) {
        val player = event.entity
        if (player.level().isClientSide) return
        val cap = player.getCapability(PlayerCapabilityLoader.UNDERGROUND) ?: return
        cap.onArmorChange()
        cap.onCurioChange()
    }
}