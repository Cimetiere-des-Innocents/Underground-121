package xyz.cimetieredesinnocents.underground.hud

import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.BufferUploader
import com.mojang.blaze3d.vertex.DefaultVertexFormat
import com.mojang.blaze3d.vertex.Tesselator
import com.mojang.blaze3d.vertex.VertexFormat
import net.minecraft.client.DeltaTracker
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import xyz.cimetieredesinnocents.underground.loaders.PlayerCapabilityLoader
import xyz.cimetieredesinnocents.underground.loaders.listeners.ShaderLoader


object UndergroundHudBackground {
    fun render(guiGraphics: GuiGraphics, deltaTracker: DeltaTracker) {
        val mc = Minecraft.getInstance()
        val shader = ShaderLoader.UNDERGROUND_HUD_BACKGROUND ?: return
        val width = mc.window.guiScaledWidth
        val height = mc.window.guiScaledHeight
        val player = mc.player ?: return
        val cap = player.getCapability(PlayerCapabilityLoader.UNDERGROUND) ?: return
        val fireSize = cap.exposure * 2f / cap.maxExposure
        val fireSpeed = cap.threat / 1024f + 0.05f
        if (fireSize <= 0.01f) return

        RenderSystem.setShader { shader }
        val time = (if (mc.player != null) mc.player!!.tickCount.toFloat() + deltaTracker.gameTimeDeltaTicks else 0f) / 20
        shader.getUniform("ModTime")?.set(time)
        shader.getUniform("ScreenSize")?.set(width.toFloat(), height.toFloat())
        shader.getUniform("size")?.set(fireSize)
        shader.getUniform("speed")?.set(fireSpeed)

        RenderSystem.enableBlend()
        RenderSystem.defaultBlendFunc()
        RenderSystem.depthMask(false)
        val tesselator = Tesselator.getInstance()
        val bufferBuilder = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR)
        val matrix = guiGraphics.pose().last().pose()

        // 绘制覆盖全屏幕的四边形
        bufferBuilder.addVertex(matrix, 0.0f, height.toFloat(), 0.0f)
            .setUv(0.0f, 1.0f).setColor(255, 255, 255, 255)
        bufferBuilder.addVertex(matrix, width.toFloat(), height.toFloat(), 0.0f)
            .setUv(1.0f, 1.0f).setColor(255, 255, 255, 255)
        bufferBuilder.addVertex(matrix, width.toFloat(), 0.0f, 0.0f)
            .setUv(1.0f, 0.0f).setColor(255, 255, 255, 255)
        bufferBuilder.addVertex(matrix, 0.0f, 0.0f, 0.0f)
            .setUv(0.0f, 0.0f).setColor(255, 255, 255, 255)
        BufferUploader.drawWithShader(bufferBuilder.buildOrThrow())

        // 恢复渲染状态
        RenderSystem.depthMask(true)
        RenderSystem.disableBlend()
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f)
    }
}