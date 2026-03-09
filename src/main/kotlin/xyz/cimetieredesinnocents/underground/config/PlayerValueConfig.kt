package xyz.cimetieredesinnocents.underground.config

import net.neoforged.neoforge.common.ModConfigSpec
import xyz.cimetieredesinnocents.underground.player.IUndergroundCapability

object PlayerValueConfig {
    private val BUILDER = ModConfigSpec.Builder()
        .comment("玩家相关配置")
        .push("playerValues")
    private val MAX_EXPOSURE = BUILDER
        .comment("最大暴露值")
        .define("maxExposure", 76800)
    private val EXPOSURE_RATE_BASE = BUILDER
        .comment("暴露值基础上升速率")
        .define("exposureRateBase", 128)
    private val EXPOSURE_RATE_THREAT = BUILDER
        .comment("与威胁值相关的暴露值上升速率")
        .define("exposureRateThreat", 32)
    private val THREAT_RATE_EXPOSE = BUILDER
        .comment("威胁值上升速率")
        .push("threatRate")
        .comment("暴露在阳光下")
        .define("expose", 32)
    private val THREAT_RATE_ATTACK = BUILDER
        .comment("攻击实体")
        .define("attack", 32)
    private val THREAT_RATE_PUT_BLOCK = BUILDER
        .comment("放置方块")
        .define("putBlock", 32)
    private val THREAT_RATE_BREAK_BLOCK = BUILDER
        .comment("摧毁方块")
        .define("breakBlock", 32)
    private val THREAT_RATE_PICK_ITEM = BUILDER
        .comment("拾取物品")
        .define("pickItem", 32)
    private val SHIELD_RATE = BUILDER
        .pop()
        .comment("护盾值下降速率")
        .define("shieldRate", 128)
    private val DEATH_THRESHOLD = BUILDER
        .comment("玩家死亡的威胁值阈值")
        .define("deathThreshold", 1024)
    private val EXPLOSION_THRESHOLD = BUILDER
        .comment("玩家爆炸的威胁值阈值")
        .define("explosionThreshold", 8192)
    val SPEC = BUILDER.pop().build()

    val maxExposure get() = MAX_EXPOSURE.get()
    val exposureRateBase get() = EXPOSURE_RATE_BASE.get()
    val exposureRateThreat get() = EXPOSURE_RATE_THREAT.get()
    val threatRate = object : IUndergroundCapability.IThreatRate {
        override val expose get() = THREAT_RATE_EXPOSE.get()
        override val attack get() = THREAT_RATE_ATTACK.get()
        override val putBlock get() = THREAT_RATE_PUT_BLOCK.get()
        override val breakBlock get() = THREAT_RATE_BREAK_BLOCK.get()
        override val pickItem get() = THREAT_RATE_PICK_ITEM.get()
    }
    val shieldRate get() = SHIELD_RATE.get()
    val deathThreshold get() = DEATH_THRESHOLD.get()
    val explosionThreshold get() = EXPLOSION_THRESHOLD.get()
}