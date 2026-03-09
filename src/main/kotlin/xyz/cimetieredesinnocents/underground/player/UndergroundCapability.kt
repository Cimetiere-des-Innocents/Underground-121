package xyz.cimetieredesinnocents.underground.player

import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.LightLayer
import net.neoforged.neoforge.network.PacketDistributor
import top.theillusivec4.curios.api.CuriosApi
import xyz.cimetieredesinnocents.underground.Underground
import xyz.cimetieredesinnocents.underground.config.PlayerValueConfig
import xyz.cimetieredesinnocents.underground.item.datacomponents.UndergroundModifiers
import xyz.cimetieredesinnocents.underground.loaders.DamageTypeLoader
import xyz.cimetieredesinnocents.underground.loaders.DataAttachmentLoader
import xyz.cimetieredesinnocents.underground.loaders.DataComponentLoader
import xyz.cimetieredesinnocents.underground.loaders.NetworkLoader
import kotlin.jvm.optionals.getOrNull
import kotlin.math.max
import kotlin.math.sqrt

class UndergroundCapability(override var player: Player) : IUndergroundCapability {
    private var dirty = false
    override val exposure get() = player.getData(DataAttachmentLoader.UNDERGROUND_EXPOSURE) ?: 0
    private var currentExposure
        get() = exposure
        set(value) {
            player.setData(DataAttachmentLoader.UNDERGROUND_EXPOSURE, value)
            dirty = true
        }

    private val maxExposureHandler = ModifiedValue(PlayerValueConfig.maxExposure)
    override val maxExposure by maxExposureHandler

    private val exposureRateBaseHandler = ModifiedValue(PlayerValueConfig.exposureRateBase)
    private val exposureRateThreatHandler = ModifiedValue(PlayerValueConfig.exposureRateThreat)
    override val exposureRate get() = exposureRateBaseHandler.value + (sqrt(threat.toDouble()) * exposureRateThreatHandler.value).toInt()

    override val threat get() = player.getData(DataAttachmentLoader.UNDERGROUND_THREAT) ?: 0
    private var currentThreat
        get() = threat
        set(value) {
            player.setData(DataAttachmentLoader.UNDERGROUND_THREAT, value)
            dirty = true
        }

    private val threatRateHandlerExpose = ModifiedValue(PlayerValueConfig.threatRate.expose)
    private val threatRateHandlerAttack = ModifiedValue(PlayerValueConfig.threatRate.attack)
    private val threatRateHandlerPutBlock = ModifiedValue(PlayerValueConfig.threatRate.putBlock)
    private val threatRateHandlerBreakBlock = ModifiedValue(PlayerValueConfig.threatRate.breakBlock)
    private val threatRateHandlerPickItem = ModifiedValue(PlayerValueConfig.threatRate.pickItem)
    override val threatRate = object : IUndergroundCapability.IThreatRate {
        override val expose by threatRateHandlerExpose
        override val attack by threatRateHandlerAttack
        override val putBlock by threatRateHandlerPutBlock
        override val breakBlock by threatRateHandlerBreakBlock
        override val pickItem by threatRateHandlerPickItem
    }

    override var shield
        get() = player.getData(DataAttachmentLoader.UNDERGROUND_SHIELD) ?: 0
        set(value) {
            player.setData(DataAttachmentLoader.UNDERGROUND_SHIELD, value)
            dirty = true
        }

    private val shieldRateHandler = ModifiedValue(PlayerValueConfig.shieldRate)
    override val shieldRate by shieldRateHandler

    private val modifiedValues = listOf(
        maxExposureHandler,
        exposureRateBaseHandler,
        exposureRateThreatHandler,
        threatRateHandlerExpose,
        threatRateHandlerAttack,
        threatRateHandlerPutBlock,
        threatRateHandlerBreakBlock,
        threatRateHandlerPickItem,
        shieldRateHandler
    )

    override fun addModifier(modifier: UndergroundModifiers.Modifier, group: IUndergroundCapability.ModifierGroup) {
        when (modifier.type) {
            UndergroundModifiers.Modifier.Type.MAX_EXPOSURE -> maxExposureHandler.addModifier(modifier, group)
            UndergroundModifiers.Modifier.Type.EXPOSURE_RATE_BASE -> exposureRateBaseHandler.addModifier(modifier, group)
            UndergroundModifiers.Modifier.Type.EXPOSURE_RATE_THREAT -> exposureRateThreatHandler.addModifier(modifier, group)
            UndergroundModifiers.Modifier.Type.THREAT_RATE_EXPOSE -> threatRateHandlerExpose.addModifier(modifier, group)
            UndergroundModifiers.Modifier.Type.THREAT_RATE_ATTACK -> threatRateHandlerAttack.addModifier(modifier, group)
            UndergroundModifiers.Modifier.Type.THREAT_RATE_PUT_BLOCK -> threatRateHandlerPutBlock.addModifier(modifier, group)
            UndergroundModifiers.Modifier.Type.THREAT_RATE_BREAK_BLOCK -> threatRateHandlerBreakBlock.addModifier(modifier, group)
            UndergroundModifiers.Modifier.Type.THREAT_RATE_PICK_ITEM -> threatRateHandlerPickItem.addModifier(modifier, group)
            UndergroundModifiers.Modifier.Type.SHIELD_RATE -> shieldRateHandler.addModifier(modifier, group)
        }
    }

    override fun removeModifier(modifier: UndergroundModifiers.Modifier, group: IUndergroundCapability.ModifierGroup) {
        when (modifier.type) {
            UndergroundModifiers.Modifier.Type.MAX_EXPOSURE -> maxExposureHandler.removeModifier(modifier, group)
            UndergroundModifiers.Modifier.Type.EXPOSURE_RATE_BASE -> exposureRateBaseHandler.removeModifier(modifier, group)
            UndergroundModifiers.Modifier.Type.EXPOSURE_RATE_THREAT -> exposureRateThreatHandler.removeModifier(modifier, group)
            UndergroundModifiers.Modifier.Type.THREAT_RATE_EXPOSE -> threatRateHandlerExpose.removeModifier(modifier, group)
            UndergroundModifiers.Modifier.Type.THREAT_RATE_ATTACK -> threatRateHandlerAttack.removeModifier(modifier, group)
            UndergroundModifiers.Modifier.Type.THREAT_RATE_PUT_BLOCK -> threatRateHandlerPutBlock.removeModifier(modifier, group)
            UndergroundModifiers.Modifier.Type.THREAT_RATE_BREAK_BLOCK -> threatRateHandlerBreakBlock.removeModifier(modifier, group)
            UndergroundModifiers.Modifier.Type.THREAT_RATE_PICK_ITEM -> threatRateHandlerPickItem.removeModifier(modifier, group)
            UndergroundModifiers.Modifier.Type.SHIELD_RATE -> shieldRateHandler.removeModifier(modifier, group)
        }
    }

    override fun clearModifierGroup(group: IUndergroundCapability.ModifierGroup) {
        maxExposureHandler.clearModifierGroup(group)
        exposureRateBaseHandler.clearModifierGroup(group)
        exposureRateThreatHandler.clearModifierGroup(group)
        threatRateHandlerExpose.clearModifierGroup(group)
        threatRateHandlerAttack.clearModifierGroup(group)
        threatRateHandlerPutBlock.clearModifierGroup(group)
        threatRateHandlerBreakBlock.clearModifierGroup(group)
        threatRateHandlerPickItem.clearModifierGroup(group)
    }

    private fun onExpose() {
        val inventory = player.inventory
        for (item in inventory.items) {
            currentThreat += item.count * threatRate.expose
        }
    }

    private fun punish() {
        if (currentThreat < PlayerValueConfig.deathThreshold) {
            player.addEffect(MobEffectInstance(MobEffects.WEAKNESS, 210, 5))
            player.addEffect(MobEffectInstance(MobEffects.BLINDNESS, 210))
            player.addEffect(MobEffectInstance(MobEffects.CONFUSION, 210))
            player.addEffect(MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 210, 3))
            return
        }

        if (!player.isAlive) {
            return
        }

        player.hurt(
            DamageSource(DamageTypeLoader.SUNBURNT(player.level())),
            Float.MAX_VALUE
        )

        if (currentThreat >= PlayerValueConfig.explosionThreshold) {
            player.level().explode(
                player,
                DamageSource(DamageTypeLoader.SUNBURNT(player.level())),
                null,
                player.position(),
                threat / 512f,
                true,
                Level.ExplosionInteraction.BLOCK
            )
        }
    }

    private fun tick() {
        if (player.level().isClientSide) {
            Underground.LOGGER.warn("WTF")
        }

        if (player.isCreative || player.isSpectator) {
            currentExposure = 0
            currentThreat = 0
            return
        }

        onHandChange()

        val level = player.level()
        val lightLevel = level.getBrightness(LightLayer.SKY, player.blockPosition())

        if (lightLevel <= 0) {
            currentExposure = 0
            currentThreat = 0
            return
        }

        if (shield > 0) {
            currentExposure = 0
            currentThreat = 0
            shield -= max(shieldRate, 0)
            if (shield < 0) {
                shield = 0
            }
            return
        }

        if (currentExposure == 0) {
            onExpose()
        }

        if (currentExposure >= maxExposure) {
            punish()
            return
        }
        currentExposure += exposureRate
    }

    override fun onTick() {
        tick()

        if (dirty) {
            PacketDistributor.sendToPlayer(player as ServerPlayer, NetworkLoader.UNDERGROUND_SYNC.packet {
                it.exposure = exposure
                it.threat = threat
                it.shield = shield
            })
            dirty = false
        }
    }

    override fun onPickItem(count: Int) {
        currentThreat += count * threatRate.pickItem
    }

    override fun onAttack() {
        currentThreat += threatRate.attack
    }

    override fun onPutBlock() {
        currentThreat += threatRate.putBlock
    }

    override fun onBreakBlock() {
        currentThreat += threatRate.breakBlock
    }

    override fun onCurioChange() {
        val curiosHandler = CuriosApi.getCuriosInventory(player).getOrNull()
        for (modifiedValue in modifiedValues) {
            modifiedValue.clearModifierGroup(IUndergroundCapability.ModifierGroup.CURIO)
        }
        if (curiosHandler == null) return
        val curios = curiosHandler.equippedCurios
        for (slotId in 0..<curios.slots) {
            val item = curios.getStackInSlot(slotId)
            val dataComponent = item.components.get(DataComponentLoader.UNDERGROUND_MODIFIERS) ?: continue
            for (modifier in dataComponent.modifiers) {
                addModifier(modifier, IUndergroundCapability.ModifierGroup.CURIO)
            }
        }
    }

    override fun onArmorChange() {
        val armors = player.inventory.armor
        for (modifiedValue in modifiedValues) {
            modifiedValue.clearModifierGroup(IUndergroundCapability.ModifierGroup.ARMOR)
        }
        for (item in armors) {
            val dataComponent = item.components.get(DataComponentLoader.UNDERGROUND_MODIFIERS) ?: continue
            for (modifier in dataComponent.modifiers) {
                addModifier(modifier, IUndergroundCapability.ModifierGroup.ARMOR)
            }
        }
    }

    private fun onHandChange() {
        val hands = player.handSlots
        for (modifiedValue in modifiedValues) {
            modifiedValue.clearModifierGroup(IUndergroundCapability.ModifierGroup.HAND)
        }

        for (item in hands) {
            val dataComponent = item.components.get(DataComponentLoader.UNDERGROUND_MODIFIERS) ?: continue
            for (modifier in dataComponent.modifiers) {
                addModifier(modifier, IUndergroundCapability.ModifierGroup.ARMOR)
            }
        }
    }

    override fun onRespawn() {
        currentThreat = 0
        currentExposure = 0
    }
}