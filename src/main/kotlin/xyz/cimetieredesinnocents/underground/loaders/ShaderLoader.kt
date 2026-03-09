package xyz.cimetieredesinnocents.underground.loaders

import com.mojang.blaze3d.vertex.DefaultVertexFormat
import net.minecraft.client.renderer.ShaderInstance
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.RegisterShadersEvent
import xyz.cimetieredesinnocents.underground.utils.RLUtil

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, value = [Dist.CLIENT])
object ShaderLoader {
    var UNDERGROUND_HUD_BACKGROUND: ShaderInstance? = null

    @SubscribeEvent
    fun registerShaders(event: RegisterShadersEvent) {
        event.registerShader(ShaderInstance(
            event.resourceProvider,
            RLUtil.of("underground_hud_background"),
            DefaultVertexFormat.POSITION_TEX_COLOR)
        ) { UNDERGROUND_HUD_BACKGROUND = it }
    }
}