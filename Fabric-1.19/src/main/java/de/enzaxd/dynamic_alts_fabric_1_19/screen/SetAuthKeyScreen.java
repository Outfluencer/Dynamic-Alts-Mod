package de.enzaxd.dynamic_alts_fabric_1_19.screen;

import de.enzaxd.dynamic_alts.DynamicAlts;
import de.enzaxd.dynamic_alts.config.implementation.KeyConfig;
import de.enzaxd.dynamic_alts_fabric_1_19.wrapper.RenderWrapper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.awt.*;

public class SetAuthKeyScreen extends Screen {

    public static final int RED_COLOR = Color.RED.getRGB();

    private TextFieldWidget fieldWidget;
    private String errorId;

    public SetAuthKeyScreen() {
        super(Text.literal("Set Auth Key"));
    }

    @Override
    protected void init() {
        super.init();
        if (KeyConfig.authKey != null) {
            MinecraftClient.getInstance().setScreen(new AltsScreen());
            return;
        }

        this.fieldWidget = this.addDrawableChild(new TextFieldWidget(MinecraftClient.getInstance().textRenderer, this.width / 2 - 150, 50, 300, 20, Text.empty()));
        this.fieldWidget.setMaxLength(Integer.MAX_VALUE);

        this.addDrawableChild(new ButtonWidget(this.width / 2 - 150, 72, 149, 20, Text.literal("Register"), (b) -> {
            if (this.fieldWidget.getText().isEmpty()) {
                this.errorId = "Please enter something";
                return;
            }
            KeyConfig.authKey = this.fieldWidget.getText();

            DynamicAlts.self.saveConfigs();
        }));
        this.addDrawableChild(new ButtonWidget(this.width / 2 + 1, 72, 149, 20, Text.literal("Cancel"), (b) -> MinecraftClient.getInstance().setScreen(new TitleScreen())));
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);

        if (this.errorId != null)
            RenderWrapper.text(this.errorId, 2, 2, RED_COLOR);

        super.render(matrices, mouseX, mouseY, delta);
    }
}
