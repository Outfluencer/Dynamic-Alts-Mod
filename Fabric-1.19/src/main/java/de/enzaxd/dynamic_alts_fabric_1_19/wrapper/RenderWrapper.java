package de.enzaxd.dynamic_alts_fabric_1_19.wrapper;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import org.lwjgl.opengl.GL11C;

public class RenderWrapper {

    public static MatrixStack matrices = new MatrixStack();

    public static void rectangle(final int x, final int y, final int width, final int height, final int color) {
        DrawableHelper.fill(matrices, x, y, width, height, color);
    }

    public static void text(final String text, final int x, final int y, final int color) {
        MinecraftClient.getInstance().textRenderer.drawWithShadow(matrices, text, x, y, color);
    }

    public static int textWidth(final String text) {
        return MinecraftClient.getInstance().textRenderer.getWidth(text);
    }

    public static void push() {
        matrices.push();
    }

    public static void scale(float scale) {
        matrices.scale(scale, scale, scale);
    }

    public static void translate(float x, float y, float z) {
        matrices.translate(x, y, z);
    }

    public static void pop() {
        matrices.pop();
    }

    public static void textCentered(final String text, final int x, final int y, final int color) {
        final int length = textWidth(text);

        MinecraftClient.getInstance().textRenderer.drawWithShadow(matrices, text, x - (length / 2F), y, color);
    }

    public static void startScissor(int x, int y, int width, int height) {
        int finalWidth = width - x;
        int finalHeight = height - y;

        int factor = (int) MinecraftClient.getInstance().getWindow().getScaleFactor();
        int bottomY = MinecraftClient.getInstance().getWindow().getScaledHeight() - height;

        GL11C.glScissor(x * factor, bottomY * factor, finalWidth * factor, finalHeight * factor);
        GL11C.glEnable(GL11C.GL_SCISSOR_TEST);
    }

    public static void endScissor() {
        GL11C.glDisable(GL11C.GL_SCISSOR_TEST);
    }
}
