package xyz.cimetieredesinnocents.underground.player

import xyz.cimetieredesinnocents.cdilib.player.PlayerCapabilityBase
import xyz.cimetieredesinnocents.underground.item.datacomponents.UndergroundModifiers

interface IUndergroundCapability : PlayerCapabilityBase {
    interface IThreatRate {
        val expose: Int
        val attack: Int
        val putBlock: Int
        val breakBlock: Int
        val pickItem: Int
    }

    /**
     * ModifierGroup决定修改器何时失效
     */
    enum class ModifierGroup {
        /**
         * 更换饰品时失效
         */
        CURIO,

        /**
         * 更换盔甲时失效
         */
        ARMOR,

        /**
         * 更换手上物品时失效
         */
        HAND
    }

    /**
     * 暴露值，当该值达到`maxExposure`时造成阳光暴露惩罚
     */
    val exposure: Int

    /**
     * 最大暴露值
     */
    val maxExposure: Int

    /**
     * 暴露值上升速率，受多种因素影响
     */
    val exposureRate: Int

    /**
     * 威胁值，玩家在自然光下活动时积累，影响暴露值上升速率
     */
    val threat: Int

    /**
     * 威胁值积累速率，受多种因素影响
     */
    val threatRate: IThreatRate

    /**
     * 护盾值，会不断消耗以抑制暴露值和威胁值的积累
     */
    var shield: Int

    /**
     * 护盾值消耗速率
     */
    val shieldRate: Int

    fun addModifier(modifier: UndergroundModifiers.Modifier, group: ModifierGroup)
    fun removeModifier(modifier: UndergroundModifiers.Modifier, group: ModifierGroup)
    fun clearModifierGroup(group: ModifierGroup)

    fun onTick()
    fun onPickItem(count: Int)
    fun onAttack()
    fun onPutBlock()
    fun onBreakBlock()
    fun onCurioChange()
    fun onArmorChange()
    fun onRespawn()
}