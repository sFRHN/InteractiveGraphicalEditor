package org.example.assignment3;
import java.util.ArrayList;

public class InteractionModel {

    private Box selected;
    private ArrayList<Subscriber> subs;
    private final int worldSize = 2000;
    private double viewLeft = 0;
    private double viewTop = 0;
    private double viewWidth, viewHeight;
    private final double handleRadius = 5;

    public InteractionModel() {
        selected = null;
        subs = new ArrayList<>();
    }

    public Box getSelected() { return this.selected; }
    public int getWorldSize() { return this.worldSize; }
    public double getViewLeft() { return this.viewLeft; }
    public double getViewTop() { return this.viewTop; }
    public double getViewWidth() { return this.viewWidth; }
    public double getViewHeight() { return this.viewHeight; }
    public double getHandleRadius() { return handleRadius; }
    public void setViewWidth(double w) { this.viewWidth = w; }
    public void setViewHeight(double h) { this.viewHeight = h; }


    public void setSelected(Box b) { this.selected = b; }
    public void addSubscriber(Subscriber sub) { subs.add(sub); }
    private void notifySubscribers() { subs.forEach(Subscriber::modelChanged); }

    public void moveViewport(double dX, double dY) {

        double newViewLeft = viewLeft + dX;
        if (newViewLeft > 0) {
            viewLeft = 0;
        }
        else if (newViewLeft < -(worldSize - viewWidth)) {
            viewLeft = -(worldSize - viewWidth);
        }
        else {
            viewLeft = newViewLeft;
        }

        double newViewTop = viewTop + dY;
        if (newViewTop > 0) {
            viewTop = 0;
        }
        else if (newViewTop < -(worldSize - viewHeight)) {
            viewTop = -(worldSize - viewHeight);
        }
        else {
            viewTop = newViewTop;
        }

        notifySubscribers();
    }

    public boolean onHandle(double mx, double my) {
        if (topLeftHandle(mx, my)) {
            System.out.println("topLeftHandle");
        }
        else if (topRightHandle(mx, my)) {
            System.out.println("topRightHandle");
        }
        else if (bottomLeftHandle(mx, my)) {
            System.out.println("bottomLeftHandle");
        }
        else if (bottomRightHandle(mx, my)) {
            System.out.println("bottomRightHandle");
        }
        return selected != null && (topLeftHandle(mx, my) || topRightHandle(mx, my) || bottomLeftHandle(mx, my) || bottomRightHandle(mx, my));
    }

    public String whichHandle(double mx, double my) {
        if (topLeftHandle(mx, my)) {
            return "topLeftHandle";
        }
        else if (topRightHandle(mx, my)) {
            return "topRightHandle";
        }
        else if (bottomLeftHandle(mx, my)) {
            return "bottomLeftHandle";
        }
        else if (bottomRightHandle(mx, my)) {
            return "bottomRightHandle";
        }
        return "none";
    }

    public boolean topLeftHandle(double mx, double my) {
        double x = selected.getX();
        double y = selected.getY();
        return Math.hypot(x - mx, y - my) <= handleRadius;
    }

    public boolean topRightHandle(double mx, double my) {
        double x = selected.getX();
        double y = selected.getY();
        double width = selected.getWidth();
        return Math.hypot(x + width - mx, y - my) <= handleRadius;
    }

    public boolean bottomLeftHandle(double mx, double my) {
        double x = selected.getX();
        double y = selected.getY();
        double height = selected.getHeight();
        return Math.hypot(x - mx, y + height - my) <= handleRadius;
    }

    public boolean bottomRightHandle(double mx, double my) {
        double x = selected.getX();
        double y = selected.getY();
        double width = selected.getWidth();
        double height = selected.getHeight();
        return Math.hypot(x + width - mx, y + height - my) <= handleRadius;
    }

}
