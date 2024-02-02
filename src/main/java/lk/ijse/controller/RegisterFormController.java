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

import java.io.IOException;
import java.sql.SQLException;

public class RegisterFormController {

    @FXML
    private AnchorPane registerPage;

    @FXML
    private TextField txtRegPass;

    @FXML
    private TextField txtRegUsername;

    UserDao userDao = new UserDaoImpl();

    @FXML
    void btnBackOnAction(ActionEvent event) throws IOException {
        registerPage.getScene().getWindow().hide();
        Stage stage = new Stage();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/View/login_form.fxml"))));
        stage.setTitle("Login");
        stage.centerOnScreen();
        stage.show();
    }

    @FXML
    void btnRegOnAction(ActionEvent event) {
        String name = txtRegUsername.getText();
        String pass = txtRegPass.getText();

        if (!name.matches("[A-Za-z ]+")) {
            new Alert(Alert.AlertType.ERROR, "Invalid name").show();
            txtRegUsername.requestFocus();
            clearFields();
        }else {
            try {
                if (userDao.existUser(name)) {
                    new Alert(Alert.AlertType.ERROR, name + " already exists").show();
                    clearFields();
                }else {
                    UserDto userDto = new UserDto(name, pass);
                    boolean saved = userDao.saveCustomer(userDto);
                    if (saved) {
                        new Alert(Alert.AlertType.CONFIRMATION, "Client Has been Registered!!!").show();
                        clearFields();
                    }
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, "Failed to save the Client!!! " + e.getMessage()).show();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void txtGoToRegPass(ActionEvent event) {
        txtRegPass.requestFocus();
    }

    @FXML
    void txtgoToReg(ActionEvent event) {
        btnRegOnAction(new ActionEvent());
    }


    private void clearFields() {
        txtRegUsername.setText("");
        txtRegPass.setText("");
    }
}
