package xyz.cimetieredesinnocents.underground.network

import net.minecraft.network.FriendlyByteBuf
import xyz.cimetieredesinnocents.cdilib.network.LibBasePacket
import xyz.cimetieredesinnocents.underground.Underground

abstract class BasePacket<P : Any, B : FriendlyByteBuf>(name: String, direction: Direction, phase: Phase<B>) :
    LibBasePacket<P, B>(Underground.ID, name, direction, phase)