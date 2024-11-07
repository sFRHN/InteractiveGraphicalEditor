package org.example.assignment3;

import javafx.scene.layout.StackPane;

public class MainUI extends StackPane {

    public MainUI() {
        EntityModel model = new EntityModel();
        InteractionModel iModel = new InteractionModel();
        DetailView View = new DetailView();
        AppController controller = new AppController();

        controller.setModel(model);
        controller.setiModel(iModel);
        View.setModel(model);
        View.setiModel(iModel);
        View.setupEvents(controller);
        model.addSubscriber(View);
        iModel.addSubscriber(View);

        this.getChildren().add(View);
    }

}
