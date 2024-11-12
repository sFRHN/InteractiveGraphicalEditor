package org.example.assignment3;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class MiniController {

    private EntityModel model;
    private InteractionModel iModel;
    private ControllerState currentState;
    private double prevX, prevY, dX, dY, portalX, portalY;
    private double scale;

    public abstract static class ControllerState {
        void handlePressed(MouseEvent event) {}
        void handleDragged(MouseEvent event) {}
        void handleReleased(MouseEvent event) {}
        void handleKeyPressed(KeyEvent event) {}
        void handleKeyReleased(KeyEvent event) {}
    }

    public MiniController() {
        currentState = ready;
    }

    public void setModel(EntityModel model) { this.model = model; }
    public void setiModel(InteractionModel iModel) { this.iModel = iModel; }
    public void setScale(double scale) { this.scale = scale; }

    public void handlePressed(MouseEvent event) { currentState.handlePressed(event); }
    public void handleDragged(MouseEvent event) { currentState.handleDragged(event); }
    public void handleReleased(MouseEvent event) { currentState.handleReleased(event); }
    public void handleKeyPressed(KeyEvent event) { currentState.handleKeyPressed(event); }
    public void handleKeyReleased(KeyEvent event) { currentState.handleKeyReleased(event); }



    ControllerState ready = new ControllerState() {

        @Override
        public void handlePressed(MouseEvent event) {

            prevX = event.getX();
            prevY = event.getY();

            // Check if control is pressed and pressing on a portal
            if (event.isControlDown() && model.whichBox(prevX/scale, prevY/scale) instanceof Portal portal) {
                portalX = (prevX/scale) - portal.getX() - portal.getPLeft();
                portalY = (prevY/scale) - portal.getY() - portal.getPTop();

                portalX /= portal.getScale();
                portalY /= portal.getScale();

                if (model.contains(portalX, portalY)) {
                    iModel.setSelected(model.whichBox(portalX, portalY));
                    currentState = dragging;
                }
                else {
                    iModel.setSelected(portal);
                    currentState = panning;
                }
            }

            else if (iModel.getSelected() != null && iModel.onHandle(prevX / scale, prevY / scale)) {
                currentState = resizing;
            } else if (model.contains(prevX / scale, prevY / scale)) {
                iModel.setSelected(model.whichBox(prevX / scale, prevY / scale));
                currentState = dragging;
            } else {
                currentState = preparing;
            }
        }

    };


    ControllerState preparing = new ControllerState() {

        @Override
        public void handleDragged(MouseEvent event) {
            if (event.isControlDown()) {
                model.addPortal(prevX/scale, prevY/scale, 1, 1);
            }
            else {
                model.addBox(prevX/scale, prevY/scale, 1/scale,1/scale);
            }
            iModel.setSelected(model.whichBox(prevX/scale, prevY/scale));
            currentState = creating;
        }

        @Override
        public void handleReleased(MouseEvent event) {
            iModel.setSelected(null);
            currentState = ready;
        }

    };


    ControllerState creating = new ControllerState() {

        @Override
        public void handleDragged(MouseEvent event) {
            double newX = Math.min(event.getX()/scale, prevX/scale);
            double newY = Math.min(event.getY()/scale, prevY/scale);
            double newWidth = Math.abs(event.getX() - prevX);
            double newHeight = Math.abs(event.getY() - prevY);

            iModel.getSelected().changePosition(newX, newY);
            iModel.getSelected().setWidth(newWidth/scale);
            iModel.getSelected().setHeight(newHeight/scale);

            model.notifySubscribers();
        }

        @Override
        public void handleReleased(MouseEvent event) {
            currentState = ready;
        }

    };


    ControllerState dragging = new ControllerState() {

        public void handleDragged(MouseEvent event) {

            dX = event.getX() - prevX;
            dY = event.getY() - prevY;

            prevX = event.getX();
            prevY = event.getY();

            model.moveBox(iModel.getSelected(), dX/scale, dY/scale);
        }

        public void handleReleased(MouseEvent event) {
            currentState = ready;
        }

    };


    ControllerState panning = new ControllerState() {

        @Override
        public void handlePressed(MouseEvent event) {
            prevX = event.getX();
            prevY = event.getY();
        }

        @Override
        public void handleDragged(MouseEvent event) {

            if (event.isControlDown() && iModel.getSelected() instanceof Portal portal) {

                dX = prevX - event.getX();
                dY = prevY - event.getY();

                prevX = event.getX();
                prevY = event.getY();

                dX /= portal.getScale();
                dY /= portal.getScale();

                portal.setPLeft(portal.getPLeft() - dX);
                portal.setPTop(portal.getPTop() - dY);
                model.notifySubscribers();

            }
        }

        @Override
        public void handleReleased(MouseEvent event) {
            currentState = ready;
        }

    };


    ControllerState resizing = new ControllerState() {

        @Override
        public void handleDragged(MouseEvent event) {
            double newX = event.getX();
            double newY = event.getY();
            double dX = (newX - prevX)/scale;
            double dY = (newY - prevY)/scale;

            String handle = iModel.whichHandle(prevX/scale, prevY/scale);
            switch (handle) {
                case "topLeftHandle":
                    iModel.getSelected().changePosition(iModel.getSelected().getX() + dX, iModel.getSelected().getY() + dY);
                    iModel.getSelected().setWidth(iModel.getSelected().getWidth() - dX);
                    iModel.getSelected().setHeight(iModel.getSelected().getHeight() - dY);
                    break;
                case "topRightHandle":
                    iModel.getSelected().changePosition(iModel.getSelected().getX(), iModel.getSelected().getY() + dY);
                    iModel.getSelected().setWidth(iModel.getSelected().getWidth() + dX);
                    iModel.getSelected().setHeight(iModel.getSelected().getHeight() - dY);
                    break;
                case "bottomLeftHandle":
                    iModel.getSelected().changePosition(iModel.getSelected().getX() + dX, iModel.getSelected().getY());
                    iModel.getSelected().setWidth(iModel.getSelected().getWidth() - dX);
                    iModel.getSelected().setHeight(iModel.getSelected().getHeight() + dY);
                    break;
                case "bottomRightHandle":
                    iModel.getSelected().setWidth(iModel.getSelected().getWidth() + dX);
                    iModel.getSelected().setHeight(iModel.getSelected().getHeight() + dY);
                    break;
                default:
                    break;
            }

            prevX = newX;
            prevY = newY;
            model.notifySubscribers();
        }

        @Override
        public void handleReleased(MouseEvent event) {
            currentState = ready;
        }

    };


}
