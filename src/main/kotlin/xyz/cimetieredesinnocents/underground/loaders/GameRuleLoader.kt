package xyz.cimetieredesinnocents.underground.loaders

import net.minecraft.world.level.GameRules
import xyz.cimetieredesinnocents.cdilib.loaders.GameRuleLoaderFactory
import xyz.cimetieredesinnocents.underground.Underground

object GameRuleLoader : GameRuleLoaderFactory(Underground.ID) {
    val UNDERGROUND_MODE by registerBool("undergroundMode", GameRules.Category.PLAYER, false)
}