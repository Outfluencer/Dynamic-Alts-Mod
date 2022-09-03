package de.enzaxd.dynamic_alts.gui;

import de.enzaxd.dynamic_alts.DynamicAlts;

public class Scrollbar {

    public int listSize;
    public final double entryHeight;
    public double scrollY;
    private double barLength;
    public double backLength;
    public int posTop;
    public int posBottom;
    public double left;
    public double top;
    public double right;
    private int speed = 40;
    public final int spaceBelow = 0;
    private double clickY;
    private boolean hold;
    private boolean updating;
    private double wheel;

    public Scrollbar(int entryHeight) {
        this.entryHeight = entryHeight;
    }

    public void setPosition(int left, int top, int right, int bottom) {
        this.left = left;
        this.posTop = top;
        this.right = right;
        this.posBottom = bottom;
        this.calc();
    }

    public void calc() {
        double totalPixels = (double) this.listSize * this.entryHeight + (double) this.spaceBelow;
        double backLength = this.posBottom - this.posTop;
        if (backLength < totalPixels) {
            double scale = backLength / totalPixels;
            double barLength = scale * backLength;
            double scroll = this.scrollY / scale * scale * scale;
            this.top = -scroll + (double) this.posTop;
            this.barLength = barLength;
            this.backLength = backLength;
        }
    }

    public double clamp_double(double num, double min, double max) {
        return num < min ? min : (Math.min(num, max));
    }

    public boolean isHidden() {
        if (this.listSize == 0) {
            return true;
        } else {
            return (double) (this.posBottom - this.posTop) >= (double) this.listSize * this.entryHeight + (double)
                    this.spaceBelow;
        }
    }

    public void scrollToEnd() {
        scrollY =  clamp_double(-9999999, Math.min(0, -(this.listSize * this.entryHeight - (this.posBottom - posTop))), 0);
    }

    public boolean isOnBottom() {
        return scrollY == clamp_double(-9999999, Math.min(0, -(this.listSize * this.entryHeight - (this.posBottom - posTop))), 0);
    }

    public void renderScrollbar() {
        this.checkOutOfBorders();

        if (!this.isHidden()) {
            this.calc();

            DynamicAlts.self.getPlatformProvider().renderScrollbar(this.left, this.right, this.top, this.barLength, this.posBottom, this.posTop);
        }
    }

    public void mouseClicked(double mouseX, double mouseY) {
        calc();
        double key = backLength / ((double) listSize * entryHeight + spaceBelow);
        double value = (int) (-mouseY / key);

        if (hold) {
            hold = false;
        } else if (isHoverScrollbar(mouseX, mouseY)) {
            hold = true;
            clickY = value - getScrollY();
        }
        checkOutOfBorders();
    }

    public void mouseDragged(double mouseY) {
        calc();
        double key = backLength / ((double) listSize * entryHeight + spaceBelow);
        double value = (int) (-mouseY / key);
        if (hold) scrollY = value - clickY;
        checkOutOfBorders();
    }

    public boolean isHoverScrollbar(double mouseX, double mouseY) {
        return mouseX < right && mouseX > left && mouseY > top && mouseY < top + barLength;
    }

    public void mouseInput(final int wheel) {
        if (wheel > 0)
            this.scrollY += this.speed;
        else if (wheel < 0)
            this.scrollY -= this.speed;


        if (wheel != 0)
            this.checkOutOfBorders();
    }

    public void checkOutOfBorders() {
        scrollY = clamp_double(scrollY, Math.min(0, -(this.listSize * this.entryHeight - (this.posBottom - posTop))), 0);
    }

    public double getScrollY() {
        return clamp_double(scrollY, Math.min(0, -(this.listSize * this.entryHeight - (this.posBottom - posTop))), 0);
    }

    public void setListSize(int listSize) {
        this.listSize = listSize;
    }

    public void setLeft(double left) {
        this.left = left;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}
