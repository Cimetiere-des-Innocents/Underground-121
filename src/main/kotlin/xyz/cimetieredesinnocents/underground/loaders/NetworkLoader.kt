package xyz.cimetieredesinnocents.underground.loaders

import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler
import net.neoforged.neoforge.network.registration.PayloadRegistrar
import xyz.cimetieredesinnocents.underground.network.BasePacket
import xyz.cimetieredesinnocents.underground.network.UndergroundSyncPacket

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
object NetworkLoader {
    private val PACKETS = arrayListOf<BasePacket<*>>()

    private fun <T : BasePacket<*>> register(packet: T): T {
        PACKETS.add(packet)
        return packet
    }

    private fun <T : Any> register(registrar: PayloadRegistrar, packet: BasePacket<T>) {
        when (packet.phase) {
            BasePacket.Phase.PLAY -> {
                when (packet.direction) {
                    BasePacket.Direction.TO_CLIENT -> {
                        registrar.playToClient(packet.packetType, packet.codec) { payload, context ->
                            packet.onClientReceived(payload.data, context)
                        }
                    }
                    BasePacket.Direction.TO_SERVER -> {
                        registrar.playToServer(packet.packetType, packet.codec) { payload, context ->
                            packet.onServerReceived(payload.data, context)
                        }
                    }
                    else -> {
                        registrar.playBidirectional(packet.packetType, packet.codec, DirectionalPayloadHandler(
                            { payload, context -> packet.onClientReceived(payload.data, context) },
                            { payload, context -> packet.onServerReceived(payload.data, context) }
                        ))
                    }
                }
            }
            BasePacket.Phase.CONFIGURATION -> {
                when (packet.direction) {
                    BasePacket.Direction.TO_CLIENT -> {
                        registrar.configurationToClient(packet.packetType, packet.codec) { payload, context ->
                            packet.onClientReceived(payload.data, context)
                        }
                    }
                    BasePacket.Direction.TO_SERVER -> {
                        registrar.configurationToServer(packet.packetType, packet.codec) { payload, context ->
                            packet.onServerReceived(payload.data, context)
                        }
                    }
                    else -> {
                        registrar.configurationBidirectional(packet.packetType, packet.codec, DirectionalPayloadHandler(
                            { payload, context -> packet.onClientReceived(payload.data, context) },
                            { payload, context -> packet.onServerReceived(payload.data, context) }
                        ))
                    }
                }
            }
            else -> {
                when (packet.direction) {
                    BasePacket.Direction.TO_CLIENT -> {
                        registrar.commonToClient(packet.packetType, packet.codec) { payload, context ->
                            packet.onClientReceived(payload.data, context)
                        }
                    }
                    BasePacket.Direction.TO_SERVER -> {
                        registrar.commonToServer(packet.packetType, packet.codec) { payload, context ->
                            packet.onServerReceived(payload.data, context)
                        }
                    }
                    else -> {
                        registrar.commonBidirectional(packet.packetType, packet.codec, DirectionalPayloadHandler(
                            { payload, context -> packet.onClientReceived(payload.data, context) },
                            { payload, context -> packet.onServerReceived(payload.data, context) }
                        ))
                    }
                }
            }
        }
    }

    @SubscribeEvent
    fun onRegisterPayload(event: RegisterPayloadHandlersEvent) {
        val registrar = event.registrar("1")
        for (packet in PACKETS) {
            register(registrar, packet)
        }
    }

    val UNDERGROUND_SYNC = register(UndergroundSyncPacket)
}