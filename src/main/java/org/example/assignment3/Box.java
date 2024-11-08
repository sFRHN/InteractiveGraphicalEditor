package org.example.assignment3;

public class Box {

    private double width, height;
    private double x, y;

    public Box(double x, double y, double width, double height) {

        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

    }

    public void move(double dX, double dY) {
        this.x += dX;
        this.y += dY;
    }

    public void changePosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public boolean contains(double mx, double my) {
        return (this.x <= mx && mx <= this.x + this.width) &&
                (this.y <= my && my <= this.y + this.height);
    }


    // Getters and Setters
    public double getX() { return x; }
    public double getY() { return y; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }

    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }
    public void setWidth(double width) { this.width = width; }
    public void setHeight(double height) { this.height = height; }
}
