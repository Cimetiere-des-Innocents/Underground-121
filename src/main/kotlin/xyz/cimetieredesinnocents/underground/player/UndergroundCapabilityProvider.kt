package xyz.cimetieredesinnocents.underground.player

import net.minecraft.world.entity.player.Player
import net.neoforged.neoforge.capabilities.ICapabilityProvider

class UndergroundCapabilityProvider : ICapabilityProvider<Player, Void?, IUndergroundCapability> {
    private var serverMap = hashMapOf<Player, UndergroundCapability>()
    private var clientMap = hashMapOf<Player, UndergroundCapability>()

    private fun getServerCapability(player: Player): UndergroundCapability {
        if (player !in serverMap) {
            serverMap[player] = UndergroundCapability(player)
        }

        val result = serverMap[player]!!
        val newMap = hashMapOf<Player, UndergroundCapability>()
        serverMap.filterTo(newMap) { it.key.removalReason == null }
        serverMap = newMap
        return result
    }

    private fun getClientCapability(player: Player): UndergroundCapability {
        if (player !in clientMap) {
            clientMap[player] = UndergroundCapability(player)
        }

        val result = clientMap[player]!!
        val newMap = hashMapOf<Player, UndergroundCapability>()
        clientMap.filterTo(newMap) { it.key.removalReason == null }
        clientMap = newMap
        return result
    }

    @Suppress("WRONG_NULLABILITY_FOR_JAVA_OVERRIDE")
    override fun getCapability(player: Player?, context: Void?): IUndergroundCapability? {
        if (player == null || player.removalReason != null) {
            return null
        }

        return if (player.level().isClientSide) {
            getClientCapability(player)
        } else {
            getServerCapability(player)
        }
    }
}