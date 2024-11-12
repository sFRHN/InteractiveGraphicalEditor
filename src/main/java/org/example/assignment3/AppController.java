package org.example.assignment3;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class AppController {

    private EntityModel model;
    private InteractionModel iModel;
    private ControllerState currentState;
    private double prevX, prevY, dX, dY, adjustedX, adjustedY, portalX, portalY;

    public abstract static class ControllerState {
        void handlePressed(MouseEvent event) {}
        void handleDragged(MouseEvent event) {}
        void handleReleased(MouseEvent event) {}
        void handleKeyPressed(KeyEvent event) {}
        void handleKeyReleased(KeyEvent event) {}
    }

    public AppController() {
        currentState = ready;
    }

    public void setModel(EntityModel model) { this.model = model; }
    public void setiModel(InteractionModel iModel) { this.iModel = iModel; }

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

            adjustedX = event.getX() + iModel.getViewLeft();
            adjustedY = event.getY() + iModel.getViewTop();


            // Check if control is pressed and pressing on a portal
            if (event.isControlDown() && model.whichBox(prevX, prevY) instanceof Portal portal) {
                portalX = adjustedX - portal.getX() - portal.getPLeft();
                portalY = adjustedY - portal.getY() - portal.getPTop();

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
            else if (iModel.getSelected() != null && iModel.onHandle(adjustedX, adjustedY)) {
                currentState = resizing;
            }
            else if (model.contains(adjustedX, adjustedY)) {
                iModel.setSelected(model.whichBox(adjustedX, adjustedY));
                currentState = dragging;
            }
            else {
                currentState = preparing;
            }
        }

        @Override
        public void handleKeyPressed(KeyEvent event) {
            switch (event.getCode()) {
                case DELETE:
                case BACK_SPACE:
                    if (iModel.getSelected() != null) {
                        model.deleteBox(iModel.getSelected());
                        iModel.setSelected(null);
                    }
                    break;
                case SHIFT:
                    currentState = panning;
                    break;
                default:
                    break;
            }
        }

    };


    ControllerState preparing = new ControllerState() {

        @Override
        public void handleDragged(MouseEvent event) {
            if (event.isControlDown()) {
                model.addPortal(adjustedX, adjustedY, 1, 1);
            }
            else {
                model.addBox(adjustedX, adjustedY, 1, 1);
            }
            iModel.setSelected(model.whichBox(adjustedX, adjustedY));
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
            double newX = Math.min(event.getX() + iModel.getViewLeft(), prevX + iModel.getViewLeft());
            double newY = Math.min(event.getY() + iModel.getViewTop(), prevY + iModel.getViewTop());
            double newWidth = Math.abs(event.getX() - prevX);
            double newHeight = Math.abs(event.getY() - prevY);

            iModel.getSelected().changePosition(newX, newY);
            iModel.getSelected().setWidth(newWidth);
            iModel.getSelected().setHeight(newHeight);

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

            model.moveBox(iModel.getSelected(), dX, dY);
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

            dX = prevX - event.getX();
            dY = prevY - event.getY();

            prevX = event.getX();
            prevY = event.getY();

            if (event.isControlDown() && iModel.getSelected() instanceof Portal portal) {

                dX *= portal.getScale();
                dY *= portal.getScale();

                portal.setPLeft(portal.getPLeft() - dX);
                portal.setPTop(portal.getPTop() - dY);
                model.notifySubscribers();
            }
            else {
                iModel.moveViewport(dX, dY);
            }
        }

        @Override
        public void handleReleased(MouseEvent event) {
            currentState = ready;
        }

        @Override
        public void handleKeyReleased(KeyEvent event) {
            currentState = ready;
        }

    };


    ControllerState resizing = new ControllerState() {

        @Override
        public void handleDragged(MouseEvent event) {
            double newX = event.getX() + iModel.getViewLeft();
            double newY = event.getY() + iModel.getViewTop();
            double dX = newX - adjustedX;
            double dY = newY - adjustedY;

            String handle = iModel.whichHandle(adjustedX, adjustedY);
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

            adjustedX = newX;
            adjustedY = newY;
            model.notifySubscribers();
        }

        @Override
        public void handleReleased(MouseEvent event) {
            currentState = ready;
        }


    };

}
