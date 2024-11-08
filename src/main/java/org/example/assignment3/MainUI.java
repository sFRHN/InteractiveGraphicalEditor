package org.example.assignment3;

import javafx.application.Platform;
import javafx.scene.layout.StackPane;

public class MainUI extends StackPane {

    public MainUI() {
        EntityModel model = new EntityModel();
        InteractionModel iModel = new InteractionModel();
        DetailView View = new DetailView();
        MiniView miniView = new MiniView();
        AppController controller = new AppController();

        controller.setModel(model);
        controller.setiModel(iModel);
        View.setModel(model);
        View.setiModel(iModel);
        View.setupEvents(controller);
        miniView.setModel(model);
        miniView.setiModel(iModel);
        model.addSubscriber(View);
        model.addSubscriber(miniView);
        iModel.addSubscriber(View);
        iModel.addSubscriber(miniView);

        Platform.runLater(View::requestFocus);
        this.getChildren().addAll(miniView, View);
    }

}
