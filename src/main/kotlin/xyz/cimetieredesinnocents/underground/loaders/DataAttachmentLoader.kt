package xyz.cimetieredesinnocents.underground.loaders

import com.mojang.serialization.Codec
import net.neoforged.neoforge.attachment.AttachmentType
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import net.neoforged.neoforge.registries.NeoForgeRegistries
import thedarkcolour.kotlinforforge.neoforge.forge.getValue
import xyz.cimetieredesinnocents.underground.Underground

object DataAttachmentLoader {
    val REGISTRY = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Underground.ID)

    fun registerInt(name: String, default: Int = 0): DeferredHolder<AttachmentType<*>?, AttachmentType<Int?>> {
        return REGISTRY.register(name) { -> AttachmentType.builder { -> default }.serialize(Codec.INT).build() }
    }

    val UNDERGROUND_EXPOSURE by registerInt("exposure")
    val UNDERGROUND_THREAT by registerInt("threat")
    val UNDERGROUND_SHIELD by registerInt("shield")
}