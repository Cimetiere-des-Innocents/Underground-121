package xyz.cimetieredesinnocents.underground.network

import net.minecraft.network.RegistryFriendlyByteBuf
import net.neoforged.neoforge.network.handling.IPayloadContext
import xyz.cimetieredesinnocents.underground.loaders.DataAttachmentLoader

object UndergroundSyncPacket : BasePacket<UndergroundSyncPacket.Data, RegistryFriendlyByteBuf>(
    "underground_sync",
    Direction.TO_CLIENT,
    Phase.PLAY
) {
    override val factory = ::Data
    override val codec = codec(int(Data::exposure).int(Data::threat).int(Data::shield))

    class Data {
        var exposure = 0
        var threat = 0
        var shield = 0
    }

    override fun onClientReceived(packet: Data, context: IPayloadContext) {
        context.enqueueWork {
            val player = context.player()
            player.setData(DataAttachmentLoader.UNDERGROUND_EXPOSURE, packet.exposure)
            player.setData(DataAttachmentLoader.UNDERGROUND_THREAT, packet.threat)
            player.setData(DataAttachmentLoader.UNDERGROUND_SHIELD, packet.shield)
        }
    }
}