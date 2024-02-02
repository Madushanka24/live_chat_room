package lk.ijse.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import com.jfoenix.controls.JFXButton;
import lk.ijse.Emoji.EmojiPicker;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.nio.file.Files;
import java.util.ResourceBundle;

import static javafx.scene.control.PopupControl.USE_COMPUTED_SIZE;
import static lk.ijse.controller.ServerFormController.receiveMessage;

public class ClientFormController implements Initializable {
    @FXML
    private JFXButton EmojiButton;

    @FXML
    private AnchorPane clientPage;


    @FXML
    private Label lblClientName;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private TextField txtClientInput;

    @FXML
    private VBox vBox;



    private Socket socket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private String clientName = "";
    private java.awt.Label enterMessageField;

    private ObjectOutputStream outStream;

    @FXML
    void btnEmojiOnAction(ActionEvent event) {
        emojiPick();
    }

    private void emojiPick() {
        EmojiPicker emojiPicker = new EmojiPicker();

        VBox vBox = new VBox(emojiPicker);
        vBox.setPrefSize(150,300);
        vBox.setLayoutX(5);
        vBox.setLayoutY(105);
        vBox.setStyle("-fx-font-size: 30");
        clientPage.getChildren().add(vBox);

        emojiPicker.setVisible(true);

        EmojiButton.setOnAction(event3 -> {
            if (emojiPicker.isVisible()){
                emojiPicker.setVisible(false);
            }else {
                emojiPicker.setVisible(true);
            }
        });

        emojiPicker.getEmojiListView().setOnMouseClicked(event3-> {
            String selectedEmoji = emojiPicker.getEmojiListView().getSelectionModel().getSelectedItem();
            if (selectedEmoji != null) {
                txtClientInput.setText(txtClientInput.getText()+selectedEmoji);
            }
            emojiPicker.setVisible(false);
        });
    }

    @FXML
    void btnImageSendAction(MouseEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image File");
        FileChooser.ExtensionFilter filteredImage = new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg");
        fileChooser.getExtensionFilters().add(filteredImage);
        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            try {
                byte[] bytes = Files.readAllBytes(selectedFile.toPath());
                displayImage(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void displayImage(byte[] imageBytes) {
        // Assuming you have an ImageView in your UI for displaying images
        ImageView imageView = new ImageView(new Image(new ByteArrayInputStream(imageBytes)));

        imageView.setFitHeight(USE_COMPUTED_SIZE);
        imageView.setFitWidth(USE_COMPUTED_SIZE);
        imageView.setFitWidth(200);
        imageView.setFitHeight(200);
        vBox.getChildren().add(imageView);
        scrollPane.setVvalue(1.0);
    }

    private void displayText(String messageText) {
        // Create a Label with the specified text
        Label textLabel = new Label(messageText);
        // Allow the text to wrap onto the next line
        textLabel.setWrapText(true);
        vBox.getChildren().add(textLabel);
        scrollPane.setVvalue(1.0);
        txtClientInput.clear();
    }

    @FXML
    void btnTxtSendAction(MouseEvent event) {
        String messageText = txtClientInput.getText();
        displayText("You: "+messageText);
    }

    @FXML
    void txtGoToSend(ActionEvent event) {

    }

    @FXML
    void btnLogOutOnAction(MouseEvent event) {
        clientPage.getScene().getWindow().hide();
        clientExit();
    }

    public void clientExit(){
        String name = String.valueOf(static_Lable);
        System.out.println(name);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket = new Socket("localhost", 3002);
                    dataInputStream = new DataInputStream(socket.getInputStream());
                    dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    receiveMessage(name + " left");

                    while (socket.isConnected()){
                        String receivedMessage = dataInputStream.readUTF();
                        receiveMessage(receivedMessage);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }


    public static Label static_Lable;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        static_Lable=lblClientName;

        new Thread(()->{
            try {
                socket = new Socket("localhost",3002);
                while (true){
                    DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                    String s = dataInputStream.readUTF();
                    Platform.runLater(()->{
                        Text text1 = new Text(s);
                        if (s.startsWith("me")) {
                            if (s.startsWith("me: C")) {
                                Image image = new Image(s);
                                ImageView imageView = new ImageView(image);
                                imageView.setFitHeight(200);
                                imageView.setFitWidth(200);
                                HBox hBox = new HBox();
                                hBox.setPadding(new Insets(5, 5, 5, 10));
                                Text text11 = new Text("me: ");
                                HBox hBox1 = new HBox();
                                hBox1.getChildren().add(text11);
                                hBox.getChildren().add(imageView);
                                hBox.setAlignment(Pos.CENTER_RIGHT);
                                hBox1.setAlignment(Pos.CENTER_RIGHT);

                                vBox.getChildren().add(hBox1);
                                vBox.getChildren().add(hBox);
                            } else {
                                HBox hBox = new HBox();
                                hBox.getChildren().add(text1);
                                hBox.setAlignment(Pos.CENTER_RIGHT);
                                vBox.getChildren().add(hBox);
                            }
                        } else {

                            System.out.println(clientName);
                            if (s.startsWith(clientName+" : C")) {
                                Image image = new Image(s);
                                ImageView imageView = new ImageView(image);
                                imageView.setFitHeight(200);
                                imageView.setFitWidth(200);
                                HBox hBox = new HBox();
                                hBox.setPadding(new Insets(5, 5, 5, 10));
                                hBox.getChildren().add(imageView);
                                Text text11 = new Text(clientName+": ");
                                HBox hBox1 = new HBox();
                                hBox1.getChildren().add(text11);
                                hBox.setAlignment(Pos.CENTER_LEFT);
                                hBox1.setAlignment(Pos.CENTER_LEFT);

                                vBox.getChildren().add(text11);
                                vBox.getChildren().add(hBox);
                            }else {
                                HBox hBox = new HBox();
                                hBox.getChildren().add(text1);
                                hBox.setAlignment(Pos.CENTER_LEFT);
                                vBox.getChildren().add(hBox);
                            }
                        }
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }




    public void setClientName(String name) {
        clientName = name;
    }
}
