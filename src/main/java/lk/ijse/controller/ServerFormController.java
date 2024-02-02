package lk.ijse.controller;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import lk.ijse.server.ServerHandler;

import java.io.IOException;

public class ServerFormController {
    @FXML
    private AnchorPane serverPage;

    @FXML
    private ScrollPane serverScrollPane;

    @FXML
    private VBox serverVbox;

    private ServerHandler serverHandler;
    private static VBox staticVBox;

    @FXML
    void btnLogOnAction(MouseEvent event) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/login_form.fxml"));
        Scene scene = new Scene(anchorPane);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Login Form");
        stage.show();
    }


    public void initialize(){
        staticVBox = serverVbox;
        receiveMessage("Server Starting..");
        serverVbox.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                serverScrollPane.setVvalue((Double) newValue);
            }
        });

        new Thread(() -> {
            try {
                serverHandler = ServerHandler.getInstance();
                serverHandler.createSocket();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        receiveMessage("Server Started..");
        receiveMessage("Waiting for User..");
    }

     static void receiveMessage(String msgFromClient) {
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(5,5,5,10));

        Text text = new Text(msgFromClient);
        TextFlow textFlow = new TextFlow(text);
        textFlow.setStyle("-fx-background-color: #abb8c3; -fx-font-weight: bold; -fx-background-radius: 20px");
        textFlow.setPadding(new Insets(5,10,5,10));
        text.setFill(Color.color(0,0,0));

        hBox.getChildren().add(textFlow);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                staticVBox.getChildren().add(hBox);
            }
        });
    }
}
