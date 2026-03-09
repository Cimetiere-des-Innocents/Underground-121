package xyz.cimetieredesinnocents.underground.player

import xyz.cimetieredesinnocents.underground.item.datacomponents.UndergroundModifiers
import kotlin.math.max
import kotlin.reflect.KProperty

class ModifiedValue(private val baseValue: Int) {
    class ModifierSum {
        private var sum = 0
        private var modifiers = HashSet<UndergroundModifiers.Modifier>()
        fun addModifier(modifier: UndergroundModifiers.Modifier) {
            modifiers.add(modifier)
            sum += modifier.value
        }

        fun removeModifier(modifier: UndergroundModifiers.Modifier) {
            modifiers.remove(modifier)
            sum -= modifier.value
        }

        fun clear() {
            modifiers.clear()
            sum = 0
        }

        val value get() = sum
    }

    private var currentValue = baseValue
    private var dirty = true

    private val modifierMap = HashMap<IUndergroundCapability.ModifierGroup, ModifierSum>()
    init {
        for (group in IUndergroundCapability.ModifierGroup.entries) {
           modifierMap[group] = ModifierSum()
        }
    }

    fun updateCurrentValue() {
        currentValue = baseValue
        for (group in IUndergroundCapability.ModifierGroup.entries) {
            currentValue += modifierMap[group]!!.value
        }
    }

    operator fun getValue(thisRef: Any?, property: KProperty<*>): Int {
        if (dirty) {
            updateCurrentValue()
            dirty = false
        }

        return max(currentValue, 0)
    }

    val value by this

    fun addModifier(modifier: UndergroundModifiers.Modifier, group: IUndergroundCapability.ModifierGroup) {
        dirty = true
        modifierMap[group]!!.addModifier(modifier)
    }

    fun removeModifier(modifier: UndergroundModifiers.Modifier, group: IUndergroundCapability.ModifierGroup) {
        dirty = true
        modifierMap[group]!!.removeModifier(modifier)
    }

    fun clearModifierGroup(group: IUndergroundCapability.ModifierGroup) {
        dirty = true
        modifierMap[group]!!.clear()
    }
}