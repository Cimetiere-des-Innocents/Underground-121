package xyz.cimetieredesinnocents.underground.network

import xyz.cimetieredesinnocents.cdilib.network.LibBasePacket
import xyz.cimetieredesinnocents.underground.Underground

abstract class BasePacket<P : Any>(name: String, direction: Direction, phase: Phase) :
    LibBasePacket<P>(Underground.ID, name, direction, phase)