/*
 * NAME: Sayed Farhaan Rafi Bhat
 * NSID: bcl568
 * Student Number: 11354916
 */

package org.example.assignment3;

public class Box {

    private double width, height;
    private double x, y;


    /**
     * Box Constructor
     * @param x x-coordinate
     * @param y y-coordinate
     * @param width width
     * @param height height
     */
    public Box(double x, double y, double width, double height) {

        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

    }


    /**
     * Move the box by dX and dY
     * @param dX distance to move in x-direction
     * @param dY distance to move in y-direction
     */
    public void move(double dX, double dY) {
        this.x += dX;
        this.y += dY;
    }


    /**
     * Change the position of the box to x and y
     * @param x new x-coordinate
     * @param y new y-coordinate
     */
    public void changePosition(double x, double y) {
        this.x = x;
        this.y = y;
    }


    /**
     * Check if the box contains the point (mx, my)
     * @param mx x-coordinate of the point
     * @param my y-coordinate of the point
     * @return true if the box contains the point, false otherwise
     */
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
