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

    public void move(double dx, double dy) {
        this.x += dx;
        this.y += dy;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }
}
