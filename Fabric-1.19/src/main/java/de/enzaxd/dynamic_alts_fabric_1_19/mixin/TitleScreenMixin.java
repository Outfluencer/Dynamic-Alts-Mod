package de.enzaxd.dynamic_alts_fabric_1_19.mixin;

import de.enzaxd.dynamic_alts_fabric_1_19.screen.SetAuthKeyScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class TitleScreenMixin extends Screen {

    public TitleScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("RETURN"))
    public void injectInit(CallbackInfo ci) {
        this.addDrawableChild(new ButtonWidget(2, 2, 98, 20, Text.literal("Dynamic Alts"), (b) -> MinecraftClient.getInstance().setScreen(new SetAuthKeyScreen())));
    }
}
