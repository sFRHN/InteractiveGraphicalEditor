package org.example.assignment3;

public class Portal extends Box {

    private double scale;
    private double pLeft, pTop;

    public Portal(double x, double y, double width, double height) {
        super(x, y, width, height);
        scale = 0.5;
    }

    public void changePosition(double x, double y) {
        super.changePosition(x, y);
        pLeft = x;
        pTop = y;
    }

    public void move(double dX, double dY) {
        super.move(dX, dY);
        pLeft += dX;
        pTop += dY;
    }

    public double getScale() { return scale; }
    public double getPLeft() { return pLeft; }
    public double getPTop() { return pTop; }

    public void setScale(double scale) { this.scale = scale; }
    public void setPLeft(double pLeft) { this.pLeft = pLeft; }
    public void setPTop(double pTop) { this.pTop = pTop; }
}
