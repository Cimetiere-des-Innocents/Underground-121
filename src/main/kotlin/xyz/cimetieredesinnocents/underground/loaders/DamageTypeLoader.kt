package xyz.cimetieredesinnocents.underground.loaders

import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.damagesource.DamageEffects
import net.minecraft.world.damagesource.DamageScaling
import net.minecraft.world.damagesource.DamageType
import net.minecraft.world.damagesource.DeathMessageType
import net.minecraft.world.level.Level
import xyz.cimetieredesinnocents.underground.utils.RLUtil
import kotlin.reflect.KProperty

object DamageTypeLoader {
    class RawDamageType(
        val name: String,
        val scaling: DamageScaling,
        val exhaustion: Float,
        val effects: DamageEffects,
        val deathMessageType: DeathMessageType
    ) {
        val resourceKey = ResourceKey.create(Registries.DAMAGE_TYPE, RLUtil.of(name))
        operator fun getValue(thisRef: Any?, propertyKey: KProperty<*>): (Level) -> Holder.Reference<DamageType> {
            return { it.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(resourceKey) }
        }
    }

    val REGISTRY = hashSetOf<RawDamageType>()

    fun register(
        name: String,
        scaling: DamageScaling = DamageScaling.NEVER,
        exhaustion: Float = 0.1f,
        effects: DamageEffects = DamageEffects.HURT,
        deathMessageType: DeathMessageType = DeathMessageType.DEFAULT
    ): RawDamageType {
        val value = RawDamageType(name, scaling, exhaustion, effects, deathMessageType)
        REGISTRY.add(value)
        return value
    }

    val SUNBURNT by register("sunburnt")
}