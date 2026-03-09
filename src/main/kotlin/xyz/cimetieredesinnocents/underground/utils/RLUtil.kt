package xyz.cimetieredesinnocents.underground.utils

import net.minecraft.resources.ResourceLocation
import xyz.cimetieredesinnocents.underground.Underground

object RLUtil {
    fun of(name: String): ResourceLocation {
        return ResourceLocation.fromNamespaceAndPath(Underground.ID, name)
    }
}