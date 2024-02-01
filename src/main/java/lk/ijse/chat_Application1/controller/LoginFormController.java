package lk.ijse.chat_Application1.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginFormController {
    @FXML
    private TextField txtUserName;
    public static String userName;

    public void btnLoginOnAction(ActionEvent event) throws IOException {
        if (!txtUserName.getText().isEmpty() && txtUserName.getText().matches("[A-Za-z0-9]+")){
            userName = txtUserName.getText();

            Stage stage = new Stage();
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/client_form.fxml"))));
            stage.setTitle(userName + "'s chat");
            stage.getIcons().add(new javafx.scene.image.Image("images/icon/whatsapp_logo.png"));
            stage.setOnCloseRequest(windowEvent -> ClientFormController.leaveChat());
            stage.show();
            txtUserName.clear();
        }else{
            new Alert(Alert.AlertType.WARNING,"Please enter your name", ButtonType.OK).show();
        }
    }

    public void txtUsernameOnAction(ActionEvent event) throws IOException {
        btnLoginOnAction(event);
    }

}