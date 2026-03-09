package xyz.cimetieredesinnocents.underground

import net.neoforged.fml.common.Mod
import net.neoforged.fml.config.ModConfig
import org.apache.logging.log4j.LogManager
import thedarkcolour.kotlinforforge.neoforge.forge.LOADING_CONTEXT
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS
import xyz.cimetieredesinnocents.underground.config.PlayerValueConfig
import xyz.cimetieredesinnocents.underground.loaders.DataAttachmentLoader
import xyz.cimetieredesinnocents.underground.loaders.DataComponentLoader

@Mod(Underground.ID)
object Underground {
    const val ID = "underground"
    val LOGGER = LogManager.getLogger(ID)

    init {
        LOADING_CONTEXT.activeContainer.registerConfig(ModConfig.Type.COMMON, PlayerValueConfig.SPEC, "underground/player_value.toml")
        DataAttachmentLoader.REGISTRY.register(MOD_BUS)
        DataComponentLoader.REGISTRY.register(MOD_BUS)
    }
}