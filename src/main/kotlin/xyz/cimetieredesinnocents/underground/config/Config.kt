package xyz.cimetieredesinnocents.underground.config

import net.neoforged.neoforge.common.ModConfigSpec
import xyz.cimetieredesinnocents.cdilib.utils.getValue
import xyz.cimetieredesinnocents.underground.player.IUndergroundCapability

object Config {
    private val BUILDER = ModConfigSpec.Builder()
        .comment("地下生存配置")
    val maxExposure by BUILDER
        .comment("最大暴露值")
        .define("maxExposure", 76800)
    val exposureRateBase by BUILDER
        .comment("暴露值基础上升速率")
        .define("exposureRateBase", 128)
    val exposureRateThreat by BUILDER
        .comment("与威胁值相关的暴露值上升速率")
        .define("exposureRateThreat", 32)
    val threatRate = object : IUndergroundCapability.IThreatRate {
        override val expose by BUILDER
            .comment("威胁值上升速率")
            .push("threatRate")
            .comment("暴露在阳光下")
            .define("expose", 32)
        override val attack by BUILDER
            .comment("攻击实体")
            .define("attack", 32)
        override val putBlock by BUILDER
            .comment("放置方块")
            .define("putBlock", 32)
        override val breakBlock by BUILDER
            .comment("摧毁方块")
            .define("breakBlock", 32)
        override val pickItem by BUILDER
            .comment("拾取物品")
            .define("pickItem", 32)
    }
    val shieldRate by BUILDER
        .pop()
        .comment("护盾值下降速率")
        .define("shieldRate", 128)
    val deathThreshold by BUILDER
        .comment("玩家死亡的威胁值阈值")
        .define("deathThreshold", 1024)
    val explosionThreshold by BUILDER
        .comment("玩家爆炸的威胁值阈值")
        .define("explosionThreshold", 8192)

    val SPEC = BUILDER.build()
}