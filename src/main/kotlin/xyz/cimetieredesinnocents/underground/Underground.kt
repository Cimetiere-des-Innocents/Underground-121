package xyz.cimetieredesinnocents.underground

import net.neoforged.fml.common.Mod
import net.neoforged.fml.config.ModConfig
import org.apache.logging.log4j.LogManager
import thedarkcolour.kotlinforforge.neoforge.forge.FORGE_BUS
import thedarkcolour.kotlinforforge.neoforge.forge.LOADING_CONTEXT
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS
import xyz.cimetieredesinnocents.underground.config.PlayerValueConfig
import xyz.cimetieredesinnocents.underground.integration.CuriosIntegration
import xyz.cimetieredesinnocents.underground.loaders.*

@Mod(Underground.ID)
object Underground {
    const val ID = "underground"
    val LOGGER = LogManager.getLogger(ID)

    init {
        LOADING_CONTEXT.activeContainer.registerConfig(ModConfig.Type.COMMON, PlayerValueConfig.SPEC, "underground/player_value.toml")
        DataGenLoader.bootstrap(MOD_BUS)
        DataAttachmentLoader.bootstrap(MOD_BUS)
        DataComponentLoader.bootstrap(MOD_BUS)
        BlockLoader.bootstrap(MOD_BUS)
        ItemLoader.bootstrap(MOD_BUS)
        BlockEntityLoader.bootstrap(MOD_BUS)
        PlayerCapabilityLoader.bootstrap(MOD_BUS, FORGE_BUS)
        NetworkLoader.bootstrap(MOD_BUS)
        CuriosIntegration.bootstrap(FORGE_BUS)
    }
}