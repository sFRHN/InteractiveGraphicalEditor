/*
 * NAME: Sayed Farhaan Rafi Bhat
 * NSID: bcl568
 * Student Number: 11354916
 */

package org.example.assignment3;

public class Portal extends Box {

    private double scale;
    private double pLeft, pTop;


    /**
     * Portal Constructor
     * @param x x-coordinate
     * @param y y-coordinate
     * @param width width
     * @param height height
     */
    public Portal(double x, double y, double width, double height) {
        super(x, y, width, height);
        scale = 0.5;
    }


    // Getters and Setters
    public double getScale() { return scale; }
    public double getPLeft() { return pLeft; }
    public double getPTop() { return pTop; }

    public void setScale(double scale) { this.scale = scale; }
    public void setPLeft(double pLeft) { this.pLeft = pLeft; }
    public void setPTop(double pTop) { this.pTop = pTop; }
}
