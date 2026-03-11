package xyz.cimetieredesinnocents.underground.player

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.LightLayer
import net.minecraft.world.level.block.Blocks
import net.neoforged.neoforge.network.PacketDistributor
import xyz.cimetieredesinnocents.underground.Underground
import xyz.cimetieredesinnocents.underground.blockentity.GroundExplosionBlockEntity
import xyz.cimetieredesinnocents.underground.config.Config
import xyz.cimetieredesinnocents.underground.integration.CuriosIntegration
import xyz.cimetieredesinnocents.underground.item.datacomponents.UndergroundModifiers
import xyz.cimetieredesinnocents.underground.loaders.*
import xyz.cimetieredesinnocents.underground.loaders.datagen.DamageTypeLoader
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

    private val maxExposureHandler = ModifiedValue(Config.maxExposure)
    override val maxExposure by maxExposureHandler

    private val exposureRateBaseHandler = ModifiedValue(Config.exposureRateBase)
    private val exposureRateThreatHandler = ModifiedValue(Config.exposureRateThreat)
    override val exposureRate get() = exposureRateBaseHandler.value + (sqrt(threat.toDouble()) * exposureRateThreatHandler.value).toInt()

    override val threat get() = player.getData(DataAttachmentLoader.UNDERGROUND_THREAT) ?: 0
    private var currentThreat
        get() = threat
        set(value) {
            player.setData(DataAttachmentLoader.UNDERGROUND_THREAT, value)
            dirty = true
        }

    private val threatRateHandlerExpose = ModifiedValue(Config.threatRate.expose)
    private val threatRateHandlerAttack = ModifiedValue(Config.threatRate.attack)
    private val threatRateHandlerPutBlock = ModifiedValue(Config.threatRate.putBlock)
    private val threatRateHandlerBreakBlock = ModifiedValue(Config.threatRate.breakBlock)
    private val threatRateHandlerPickItem = ModifiedValue(Config.threatRate.pickItem)
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

    private val shieldRateHandler = ModifiedValue(Config.shieldRate)
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

    private fun findNearestExplosionAnchor(level: ServerLevel, origin: BlockPos, radius: Int = 6): BlockPos? {
        var bestPos: BlockPos? = null
        var bestDistance = Double.MAX_VALUE

        for (y in -radius..radius) {
            for (x in -radius..radius) {
                for (z in -radius..radius) {
                    val candidate = origin.offset(x, y, z)
                    val state = level.getBlockState(candidate)
                    if (state.isAir || state.`is`(Blocks.BEDROCK)) continue

                    val distance = candidate.distSqr(origin)
                    if (distance < bestDistance) {
                        bestDistance = distance
                        bestPos = candidate.immutable()
                    }
                }
            }
        }

        return bestPos
    }

    private fun punish() {
        if (currentThreat < Config.deathThreshold) {
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

        if (currentThreat >= Config.explosionThreshold) {
            val level = player.level() as ServerLevel
            val pos = findNearestExplosionAnchor(level, player.blockPosition()) ?: return
            level.setBlock(pos, BlockLoader.GROUND_EXPLOSION.defaultBlockState(), 3)
            val be = level.getBlockEntity(pos)
            if (be !is GroundExplosionBlockEntity) return
            be.damageSource = player
            be.blocksToCreate = threat
            be.isFirstTick = true
            be.setChanged()
            level.scheduleTick(pos, BlockLoader.GROUND_EXPLOSION, 1)
        }
    }

    private fun tick() {
        if (player.level().isClientSide) {
            Underground.LOGGER.warn("Ticking underground capability in client")
            return
        }

        if (player.isCreative || player.isSpectator) {
            currentExposure = 0
            currentThreat = 0
            return
        }

        onHandChange()

        val level = player.level()
        val gameRule = level.gameRules.getRule(GameRuleLoader.UNDERGROUND_MODE).get()
        if (!gameRule) {
            currentExposure = 0
            currentThreat = 0
            return
        }

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
        for (modifiedValue in modifiedValues) {
            modifiedValue.clearModifierGroup(IUndergroundCapability.ModifierGroup.CURIO)
        }

        val curios = CuriosIntegration.getCurios(player) ?: return
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
                addModifier(modifier, IUndergroundCapability.ModifierGroup.HAND)
            }
        }
    }

    override fun afterRespawn() {
        currentThreat = 0
        currentExposure = 0
        onCurioChange()
        onArmorChange()
    }
}