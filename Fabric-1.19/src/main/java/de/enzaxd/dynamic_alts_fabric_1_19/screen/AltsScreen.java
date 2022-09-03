package de.enzaxd.dynamic_alts_fabric_1_19.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import de.enzaxd.dynamic_alts.DynamicAlts;
import de.enzaxd.dynamic_alts.gui.Scrollbar;
import de.enzaxd.dynamic_alts.struct.Account;
import de.enzaxd.dynamic_alts_fabric_1_19.wrapper.RenderWrapper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class AltsScreen extends Screen {

    private final static int ENTRY_WIDTH = 250;
    private final List<GridElement> CALC_TRACKER = new ArrayList<>();
    private final Scrollbar scrollbar = new Scrollbar(20);
    private ButtonWidget nextReload;
    private TextFieldWidget searchField;

    private Account selected;

    public AltsScreen() {
        super(Text.literal("Alts"));
    }

    public class GridElement {

        private final List<Account> elements;

        public GridElement(List<Account> elements) {
            this.elements = elements;
        }

        public List<Account> getElements() {
            return elements;
        }
    }

    private List<Account> accounts() {
        return DynamicAlts.self.getAccounts();
    }

    @Override
    protected void init() {
        super.init();
        CALC_TRACKER.clear();

        this.addDrawableChild(this.nextReload = new ButtonWidget(2, 2, 125, 20, this.setText(), (b) -> MinecraftClient.getInstance().setScreen(null)));
        this.addDrawableChild(new ButtonWidget(2, 24, 125, 20, Text.literal("Random Account"), (b) -> MinecraftClient.getInstance().setScreen(null)));

        this.addDrawableChild(new ButtonWidget(2, this.height - 22, 20, 20, Text.literal("<-"), (b) -> MinecraftClient.getInstance().setScreen(null)));

        this.addDrawableChild(this.searchField = new TextFieldWidget(MinecraftClient.getInstance().textRenderer, this.width / 2 - 150, this.height - 35, 300, 20, Text.empty()));
        this.addDrawableChild(new ButtonWidget(this.searchField.x + this.searchField.getWidth() + 2, this.searchField.y, 98, 20, Text.literal(Formatting.RED + "Reconnect"), (b) -> {
        }));

        int accountMax = this.width / ENTRY_WIDTH;
        if (accountMax > 3) accountMax = 3;

        final List<Account> allAccounts = new CopyOnWriteArrayList<>(new ArrayList<>(this.accounts()));
        final List<Account> nextStack = new ArrayList<>();

        int i = 0;
        for (Account account : this.accounts()) {
            nextStack.add(account);
            i++;

            if ((nextStack.size() == accountMax) || (this.accounts().size() < accountMax && i == this.accounts().size())) {
                allAccounts.removeAll(nextStack);
                CALC_TRACKER.add(new GridElement(new ArrayList<>(nextStack)));
                nextStack.clear();
            }
        }

        CALC_TRACKER.add(new GridElement(new ArrayList<>(allAccounts)));

        this.scrollbar.setListSize(this.accounts().size() / accountMax);
        this.scrollbar.setPosition(this.width - 3, 50, this.width, this.height - 50);
    }

    @Override
    public void tick() {
        super.tick();

        this.nextReload.setMessage(this.setText());
    }

    private Text setText() {
        return Text.literal("Reload List (" + (((1200000 - DynamicAlts.self.getGuiReloader().getDelay()) / 1000) / 60) + " minutes)");
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        this.scrollbar.mouseClicked(mouseX, mouseY);
        return super.mouseClicked(mouseX, mouseY, button);
    }

    public boolean isInBounds(final int mouseX, final int mouseY, final int x, final int y, final int width, final int height) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        this.scrollbar.mouseDragged(mouseY);
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        this.scrollbar.mouseInput((int) amount);
        return super.mouseScrolled(mouseX, mouseY, amount);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);

        RenderWrapper.rectangle(0, 0, this.width, 50, Integer.MIN_VALUE);
        RenderWrapper.rectangle(0, this.height - 50, this.width, this.height, Integer.MIN_VALUE);

        final String text = String.valueOf(DynamicAlts.self.getAccounts().size());
        RenderWrapper.text(text, this.width - RenderWrapper.textWidth(text) - 2, 2, -1);

        RenderWrapper.push();
        RenderWrapper.scale(2F);

        RenderWrapper.textCentered("Dynamic Alts", this.width / 4, 2, -1);
        RenderWrapper.pop();

        RenderWrapper.textCentered("Current User: " + MinecraftClient.getInstance().getSession().getUsername(), this.width / 2, 25, -1);

        RenderWrapper.startScissor(0, 51, this.width, this.height - 50);

        this.scrollbar.renderScrollbar();

        int accountMax = this.width / ENTRY_WIDTH;
        if (accountMax > 3) accountMax = 3;

        int i = 0;
        for (GridElement gridElement : CALC_TRACKER) {
            int veryMax = accountMax;

            if (CALC_TRACKER.size() == 1)
                veryMax = CALC_TRACKER.get(0).getElements().size();

            int x = (this.width / 2) - (veryMax * ENTRY_WIDTH) / 2;

            RenderWrapper.push();
            RenderWrapper.translate(0, 51, 0);

            for (Account element : gridElement.getElements()) {
                RenderWrapper.push();
                RenderWrapper.translate(x, (float) (i + this.scrollbar.getScrollY()), 0);

                RenderWrapper.rectangle(0, 0, ENTRY_WIDTH - 1, (int) (this.scrollbar.entryHeight - 1), Integer.MIN_VALUE);
                RenderWrapper.textCentered(element.getName(), ENTRY_WIDTH / 2, (int) (this.scrollbar.entryHeight / 4), element.getLastLogin() != 0L ? Color.ORANGE.getRGB() : -1);

                RenderWrapper.pop();
                x += ENTRY_WIDTH;
            }

            RenderWrapper.pop();

            i += this.scrollbar.entryHeight;
        }

        RenderWrapper.endScissor();

        super.render(matrices, mouseX, mouseY, delta);
    }
}
