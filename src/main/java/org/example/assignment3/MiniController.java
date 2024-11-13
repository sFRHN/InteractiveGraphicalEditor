/*
 * NAME: Sayed Farhaan Rafi Bhat
 * NSID: bcl568
 * Student Number: 11354916
 */

package org.example.assignment3;

import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class MiniController {

    private EntityModel model;
    private InteractionModel iModel;
    private ControllerState currentState;
    private double prevX, prevY, dX, dY, portalX, portalY;
    private double scale;


    /**
     * MiniController Constructor
     */
    public MiniController() {
        currentState = ready;
    }


    /**
     * Abstract class for the different states of the controller
     */
    public abstract static class ControllerState {
        void handlePressed(MouseEvent event) {}
        void handleDragged(MouseEvent event) {}
        void handleReleased(MouseEvent event) {}
        void handleKeyPressed(KeyEvent event) {}
        void handleKeyReleased(KeyEvent event) {}
    }


    // Getters and Setters
    public void setModel(EntityModel model) { this.model = model; }
    public void setiModel(InteractionModel iModel) { this.iModel = iModel; }
    public void setScale(double scale) { this.scale = scale; }

    public void handlePressed(MouseEvent event) { currentState.handlePressed(event); }
    public void handleDragged(MouseEvent event) { currentState.handleDragged(event); }
    public void handleReleased(MouseEvent event) { currentState.handleReleased(event); }
    public void handleKeyPressed(KeyEvent event) { currentState.handleKeyPressed(event); }
    public void handleKeyReleased(KeyEvent event) { currentState.handleKeyReleased(event); }


    /**
     * Different states of the controller
     */
    ControllerState ready = new ControllerState() {

        @Override
        public void handlePressed(MouseEvent event) {

            prevX = event.getX();
            prevY = event.getY();

            if (event.isShiftDown()) {
                currentState = panning;
            }

            // Check if control is pressed and pressing on a portal
            else if (event.isControlDown() && model.whichBox(prevX/scale, prevY/scale) instanceof Portal portal) {
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

            // Check if clicking on a handle
            else if (iModel.getSelected() != null && iModel.onHandle(prevX / scale, prevY / scale)) {
                currentState = resizing;
            }
            // Check if clicking on a box
            else if (model.contains(prevX / scale, prevY / scale)) {
                iModel.setSelected(model.whichBox(prevX / scale, prevY / scale));
                currentState = dragging;
            } else {
                currentState = preparing;
            }
        }

    };


    /**
     * Preparing state for the controller
     */
    ControllerState preparing = new ControllerState() {

        @Override
        public void handleDragged(MouseEvent event) {
            // Creating a portal
            if (event.isControlDown()) {
                model.addPortal(prevX/scale, prevY/scale, 1, 1);
            }
            // Creating a box
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


    /**
     * Creating state for the controller
     */
    ControllerState creating = new ControllerState() {

        @Override
        public void handleDragged(MouseEvent event) {

            // Calculate new position and size of the box
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


    /**
     * Dragging state for the controller
     */
    ControllerState dragging = new ControllerState() {

        public void handleDragged(MouseEvent event) {

            // Calculate the change in position
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


    /**
     * Panning state for the controller
     */
    ControllerState panning = new ControllerState() {

        @Override
        public void handlePressed(MouseEvent event) {
            prevX = event.getX();
            prevY = event.getY();
        }

        @Override
        public void handleDragged(MouseEvent event) {

            // Calculate the change in position
            dX = prevX - event.getX();
            dY = prevY - event.getY();

            prevX = event.getX();
            prevY = event.getY();

            // If panning in the portal
            if (event.isControlDown() && iModel.getSelected() instanceof Portal portal) {

                dX *= portal.getScale();
                dY *= portal.getScale();

                portal.setPLeft(portal.getPLeft() - dX);
                portal.setPTop(portal.getPTop() - dY);
                model.notifySubscribers();

            }

            // If panning the viewport
            else {
                iModel.moveViewport(dX/scale, dY/scale);
            }
        }

        @Override
        public void handleReleased(MouseEvent event) {
            currentState = ready;
        }

    };


    /**
     * Resizing state for the controller
     */
    ControllerState resizing = new ControllerState() {

        public void handleDragged(MouseEvent event) {

            // Calculate the change in position
            double newX = event.getX();
            double newY = event.getY();
            double dX = (newX - prevX)/scale;
            double dY = (newY - prevY)/scale;


            Box selectedBox = iModel.getSelected();
            String handle = iModel.whichHandle(prevX/scale, prevY/scale);

            // Resize the box based on the handle
            switch (handle) {
                case "topLeftHandle":
                    selectedBox.changePosition(selectedBox.getX() + dX, selectedBox.getY() + dY);
                    selectedBox.setWidth(selectedBox.getWidth() - dX);
                    selectedBox.setHeight(selectedBox.getHeight() - dY);

                    if (selectedBox.getWidth() < 0) {
                        handle = "topRightHandle";
                        selectedBox.changePosition(selectedBox.getX() + selectedBox.getWidth(), selectedBox.getY());
                        selectedBox.setWidth(-selectedBox.getWidth());
                    }
                    if (selectedBox.getHeight() < 0) {
                        handle = "bottomLeftHandle";
                        selectedBox.changePosition(selectedBox.getX(), selectedBox.getY() + selectedBox.getHeight());
                        selectedBox.setHeight(-selectedBox.getHeight());
                    }
                    break;

                case "topRightHandle":
                    selectedBox.changePosition(selectedBox.getX(), selectedBox.getY() + dY);
                    selectedBox.setWidth(selectedBox.getWidth() + dX);
                    selectedBox.setHeight(selectedBox.getHeight() - dY);

                    if (selectedBox.getWidth() < 0) {
                        handle = "topLeftHandle";
                        selectedBox.changePosition(selectedBox.getX() + selectedBox.getWidth(), selectedBox.getY());
                        selectedBox.setWidth(-selectedBox.getWidth());
                    }
                    if (selectedBox.getHeight() < 0) {
                        handle = "bottomRightHandle";
                        selectedBox.changePosition(selectedBox.getX(), selectedBox.getY() + selectedBox.getHeight());
                        selectedBox.setHeight(-selectedBox.getHeight());
                    }
                    break;

                case "bottomLeftHandle":
                    selectedBox.changePosition(selectedBox.getX() + dX, selectedBox.getY());
                    selectedBox.setWidth(selectedBox.getWidth() - dX);
                    selectedBox.setHeight(selectedBox.getHeight() + dY);

                    if (selectedBox.getWidth() < 0) {
                        handle = "bottomRightHandle";
                        selectedBox.changePosition(selectedBox.getX() + selectedBox.getWidth(), selectedBox.getY());
                        selectedBox.setWidth(-selectedBox.getWidth());
                    }
                    if (selectedBox.getHeight() < 0) {
                        handle = "topLeftHandle";
                        selectedBox.changePosition(selectedBox.getX(), selectedBox.getY() + selectedBox.getHeight());
                        selectedBox.setHeight(-selectedBox.getHeight());
                    }
                    break;

                case "bottomRightHandle":
                    selectedBox.setWidth(selectedBox.getWidth() + dX);
                    selectedBox.setHeight(selectedBox.getHeight() + dY);

                    if (selectedBox.getWidth() < 0) {
                        handle = "bottomLeftHandle";
                        selectedBox.changePosition(selectedBox.getX() + selectedBox.getWidth(), selectedBox.getY());
                        selectedBox.setWidth(-selectedBox.getWidth());
                    }
                    if (selectedBox.getHeight() < 0) {
                        handle = "topRightHandle";
                        selectedBox.changePosition(selectedBox.getX(), selectedBox.getY() + selectedBox.getHeight());
                        selectedBox.setHeight(-selectedBox.getHeight());
                    }
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
