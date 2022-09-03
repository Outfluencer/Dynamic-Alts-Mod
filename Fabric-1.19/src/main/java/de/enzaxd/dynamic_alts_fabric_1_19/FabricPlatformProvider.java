package de.enzaxd.dynamic_alts_fabric_1_19;

import com.mojang.blaze3d.systems.RenderSystem;
import de.enzaxd.dynamic_alts.IPlatformProvider;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;

import java.io.File;

public class FabricPlatformProvider implements IPlatformProvider {

    @Override
    public File dataDir() {
        return MinecraftClient.getInstance().runDirectory;
    }

    @Override
    public void reloadSecureProfile() {

    }

    @Override
    public int scaledWidth() {
        return MinecraftClient.getInstance().getWindow().getScaledHeight();
    }

    @Override
    public int scaledHeight() {
        return MinecraftClient.getInstance().getWindow().getScaledHeight();
    }

    @Override
    public void renderScrollbar(double i, double j, double p, double o, double posBottom, double posTop) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        RenderSystem.blendFuncSeparate(770, 771, 0, 1);
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        bufferBuilder.vertex(i, posBottom, 0.0D).texture(0.0F, 1.0F).color(0, 0, 0, 255).next();
        bufferBuilder.vertex(j, posBottom, 0.0D).texture(1.0F, 1.0F).color(0, 0, 0, 255).next();
        bufferBuilder.vertex(j, posTop, 0.0D).texture(1.0F, 0.0F).color(0, 0, 0, 255).next();
        bufferBuilder.vertex(i, posTop, 0.0D).texture(0.0F, 0.0F).color(0, 0, 0, 255).next();
        tessellator.draw();

        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        bufferBuilder.vertex(i, p + o, 0.0D).texture(0.0F, 1.0F).color(128, 128, 128, 255).next();
        bufferBuilder.vertex(j, p + o, 0.0D).texture(1.0F, 1.0F).color(128, 128, 128, 255).next();
        bufferBuilder.vertex(j, p, 0.0D).texture(1.0F, 0.0F).color(128, 128, 128, 255).next();
        bufferBuilder.vertex(i, p, 0.0D).texture(0.0F, 0.0F).color(128, 128, 128, 255).next();
        tessellator.draw();

        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        bufferBuilder.vertex(i, p + o - 1, 0.0D).texture(0.0F, 1.0F).color(192, 192, 192, 255).next();
        bufferBuilder.vertex(j - 1, p + o - 1, 0.0D).texture(1.0F, 1.0F).color(192, 192, 192, 255).next();
        bufferBuilder.vertex(j - 1, p, 0.0D).texture(1.0F, 0.0F).color(192, 192, 192, 255).next();
        bufferBuilder.vertex(i, p, 0.0D).texture(0.0F, 0.0F).color(192, 192, 192, 255).next();
        tessellator.draw();
        RenderSystem.disableBlend();
    }
}
