package lk.ijse.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.Modle.UserDto;
import lk.ijse.dao.UserDao;
import lk.ijse.dao.UserDaoImpl;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;

import static lk.ijse.controller.ServerFormController.receiveMessage;

public class LoginFormController extends ClientFormController{
    @FXML
    private AnchorPane loginPage;

    @FXML
    private TextField txtUsername;

    @FXML
    private TextField txtPass;

    private Socket socket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private java.awt.Label enterMessageField;

    UserDao userDao = new UserDaoImpl();

    String name;
    String pass;

    @FXML
    void btnLogOnAction(ActionEvent event) {
         name = txtUsername.getText();
         pass = txtPass.getText();

        if (!name.matches("[A-Za-z ]+")) {
            new Alert(Alert.AlertType.ERROR, "Invalid name").show();
            txtUsername.requestFocus();
            clearFields();
        }else {
            try {
            boolean isValid = userDao.validUser(name, pass);
            if (isValid) {
                login();
                clientJoined();
                static_Lable.setText(txtUsername.getText());
            } else {
                new Alert(Alert.AlertType.ERROR, "User Name And Password Did Not Matched try again").showAndWait();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void login() throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/client_form.fxml"));
        Scene scene = new Scene(anchorPane);
        Stage stage = (Stage) this.loginPage.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle(name+" Client Form");
        stage.centerOnScreen();
    }

    public void clientJoined(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket = new Socket("localhost", 3002);
                    dataInputStream = new DataInputStream(socket.getInputStream());
                    dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    receiveMessage(name + " joined");

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


    private void clearFields() {
        txtUsername.setText("");
        txtPass.setText("");
    }

    @FXML
    void btnRegOnAction(ActionEvent event) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/register_form.fxml"));
        Scene scene = new Scene(anchorPane);
        Stage stage = (Stage) this.loginPage.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Register Form");
        stage.centerOnScreen();
    }



    @FXML
    void txtGoToLogin(ActionEvent event) {
        btnLogOnAction(new ActionEvent());
    }

    @FXML
    void txtGoToPass(ActionEvent event) {
        txtPass.requestFocus();
    }



}
