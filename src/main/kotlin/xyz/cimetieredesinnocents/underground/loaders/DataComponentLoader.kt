package xyz.cimetieredesinnocents.underground.loaders

import com.mojang.serialization.Codec
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import thedarkcolour.kotlinforforge.neoforge.forge.getValue
import xyz.cimetieredesinnocents.underground.Underground
import xyz.cimetieredesinnocents.underground.item.datacomponents.UndergroundModifiers

object DataComponentLoader {
    val REGISTRY = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, Underground.ID)

    fun <T> register(
        name: String,
        codec: Codec<T?>,
        streamCodec: StreamCodec<in RegistryFriendlyByteBuf, T?>
    ): DeferredHolder<DataComponentType<*>, DataComponentType<T>> {
        return REGISTRY.registerComponentType(name) { it.persistent(codec).networkSynchronized(streamCodec) }
    }

    val UNDERGROUND_MODIFIERS by register("underground_modifiers", UndergroundModifiers.CODEC, UndergroundModifiers.STREAM_CODEC)
}