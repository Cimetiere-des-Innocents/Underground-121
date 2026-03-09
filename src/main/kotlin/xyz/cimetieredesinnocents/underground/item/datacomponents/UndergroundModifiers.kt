package xyz.cimetieredesinnocents.underground.item.datacomponents

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec

data class UndergroundModifiers(
    val modifiers: List<Modifier>
) {
    data class Modifier(
        val type: Type,
        val value: Int
    ) {
        enum class Type {
            MAX_EXPOSURE,
            EXPOSURE_RATE_BASE,
            EXPOSURE_RATE_THREAT,
            THREAT_RATE_EXPOSE,
            THREAT_RATE_ATTACK,
            THREAT_RATE_PUT_BLOCK,
            THREAT_RATE_BREAK_BLOCK,
            THREAT_RATE_PICK_ITEM,
            SHIELD_RATE
        }

        companion object {
            val CODEC = RecordCodecBuilder.create{
                it.group(
                    Codec.INT.fieldOf("type").forGetter { modifier -> modifier.type.ordinal },
                    Codec.INT.fieldOf("value").forGetter(Modifier::value)
                ).apply(it) { type, value -> Modifier(Type.entries[type], value) }
            }

            val STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.INT,
                { value: Modifier -> value.type.ordinal },
                ByteBufCodecs.INT,
                Modifier::value,
                { type, value -> Modifier(Type.entries[type], value) }
            )
        }
    }

    companion object {
        val CODEC = RecordCodecBuilder.create {
            it.group(
                Codec
                        .list(Modifier.CODEC)
                        .fieldOf("modifiers")
                        .forGetter(UndergroundModifiers::modifiers)
            ).apply(it, ::UndergroundModifiers)
        }

        val STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.list<ByteBuf, Modifier>().apply(Modifier.STREAM_CODEC),
            UndergroundModifiers::modifiers,
            ::UndergroundModifiers
        )
    }
}
