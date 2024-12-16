# Overview
This project is a visual editor built in **JavaFX** that demonstrates skills in the following areas:  
- Model-View-Controller (MVC) architecture  
- Multiple views  
- Immediate-mode graphics  
- Interaction with graphical elements  

The system serves as an interactive editor for creating and manipulating objects in a visual workspace.

---

# Project Structure

## Classes Implemented
- **EditorApp**: The main application class.  
- **MainUI**: Organizes the views into an interface.  
- **EntityModel**: Main model class that stores all objects.  
- **Box**: Represents rectangular objects stored in the model.  
- **InteractionModel**: Stores information related to the app’s interaction state.  
- **DetailView**: An immediate-mode graphical view of the objects in the model.  
- **AppController**: The controller that interprets all user events.  
- **MiniView**: Displays a miniature view of the entire world.  
- **MiniController**: Handles interactions for the MiniView.  
- **Portal**: A special type of object that shows a view of part or all of the world.  
- **Subscriber**: Interface for notifying subscribers when the model changes.  

---

# Interactions

### Basic Interactions
- **Create Rectangles**:  
  Press the mouse on the background and drag to resize.  

- **Move Rectangles**:  
  Press and drag existing rectangles.  

- **Select Rectangles**:  
  - Press and release on a rectangle to select it.  
  - Creation or dragging an object also selects it.  
  - The selected rectangle is **orange**, and non-selected rectangles are **blue**.  
  - Clicking on the background unselects all objects.  

- **Delete Rectangles**:  
  Press **Delete** or **Backspace** to delete any selected object.  

- **Pan the DetailView**:  
  - Hold down the **Shift** key.  
  - Press the mouse button and drag to pan within the world.  

- **Resize Rectangles**:  
  Circular handles appear on the corners of a selected rectangle for resizing.

---

### Portal-Specific Interactions
- **Create a Portal**:  
  Hold down **Control** while creating a rectangle to create a portal.  

- **Interact with Objects Inside a Portal**:  
  - Hold **Control** and press/drag to select and move objects within the portal.  

- **Pan Within a Portal**:  
  - Hold **Control** and press/drag on the portal’s background to pan the view.  

- **Zoom a Portal**:  
  - Press the **Up Arrow** key to zoom in on the portal's view.  
  - Press the **Down Arrow** key to zoom out.  

---

# Software Requirements
- The **DetailView** dynamically adjusts its size to fill the window and resizes as the window is resized.  
- Fully adheres to the **MVC architecture** with proper separation of components and controlled access.  
- Uses **publish-subscribe communication** between models and views.  
- Implements the controller with a **state machine** approach for clean interaction handling.

---

# Running the Project
1. Extract the folder.  
2. Open the project in **IntelliJ** and click **"Trust this project"**.  
3. Wait for the configuration to load.  
4. Run the **EditorApp** class.

---

# Notes
- A separate controller has been created to handle interactions specifically for the **MiniView**.
