package lk.ijse.chat_Application1.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ClientFormController{
    @FXML
    private ImageView clickedImage;

    @FXML
    private AnchorPane emojiPane;

    @FXML
    private Pane header;

    @FXML
    private Pane imagePane;

    @FXML
    private Label lblName;

    @FXML
    private AnchorPane root;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private Pane textArea;

    @FXML
    private ImageView themeView;

    @FXML
    private TextField txtMessage;

    @FXML
    private VBox vBox;
    private BufferedReader bufferedReader;
    private static PrintWriter writer;
    private File file;
    private static String username;
    private String finalName;


    public void initialize(){
        username = LoginFormController.userName;
        lblName.setText(username);
        emojiPane.setVisible(false);

        new Thread(() -> {
            try {
                Socket socket = new Socket("localhost",8080);
                System.out.println("Server accepted " + LoginFormController.userName + "!");

                bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                writer = new PrintWriter(socket.getOutputStream(),true);

                writer.println("joi"+lblName.getText()+"~joining");

                while (true){
                    //reading response
                    String receive = bufferedReader.readLine();
                    String[] split = receive.split("~");
                    String name = split[0];
                    String message = split[1];

                    //find which type of message is came
                    String firstChars = "";
                    if (name.length() > 3) {
                        firstChars = name.substring(0, 3);
                    }
                    if (firstChars.equalsIgnoreCase("img")){
                        String[] imgs = name.split("img");
                        finalName = imgs[1];
                    }
                    else if(firstChars.equalsIgnoreCase("joi")) {
                        String[] imgs = name.split("joi");
                        finalName = imgs[1];

                    }
                    else if(firstChars.equalsIgnoreCase("lef")){
                        String[] imgs = name.split("lef");
                        finalName = imgs[1];
                    }

                    if (firstChars.equalsIgnoreCase("img")){
                        if (lblName.getText().equals(finalName)){

                            //adding image to message
                            File receiveFile = new File(message);
                            Image image = new Image(receiveFile.toURI().toString());
                            ImageView imageView = new ImageView(image);
                            imageView.setFitHeight(150);
                            imageView.setFitWidth(200);
                            imageView.setOnMouseClicked(mouseEvent -> {
                                clickedImage.setImage(imageView.getImage());
                                imagePane.setVisible(true);
                            });
                            //adding sender to message
                            Text text = new Text("~ Me");

                            //add time
                            HBox timeBox = setTime();

                            VBox vbox = new VBox(10);
                            vbox.getChildren().addAll(text, imageView, timeBox);

                            HBox hBox = new HBox(10);
                            hBox.setMaxHeight(190);
                            hBox.setMaxWidth(220);
                            hBox.getChildren().add(vbox);

                            StackPane stackPane = new StackPane(hBox);
                            stackPane.setAlignment(Pos.CENTER_RIGHT);

                            //adding message to message area
                            Platform.runLater(() -> {
                                vBox.getChildren().addAll(stackPane);
                                scrollPane.layout();
                                scrollPane.setVvalue(2.0);

                                //adding space between messages
                                HBox hBox1 = new HBox();
                                hBox1.setAlignment(Pos.CENTER_LEFT);
                                hBox1.setPadding(new Insets(5, 5, 5, 10));
                                vBox.getChildren().add(hBox1);
                            });
                        }else {
                            //adding image to message
                            File receives = new File(message);
                            Image image = new Image(receives.toURI().toString());
                            ImageView imageView = new ImageView(image);
                            imageView.setFitHeight(170);
                            imageView.setFitWidth(200);
                            imageView.setOnMouseClicked(mouseEvent -> {
                                clickedImage.setImage(imageView.getImage());
                                imagePane.setVisible(true);
                            });

                            //adding sender to message
                            Text text = new Text("~ "+finalName);

                            //add time
                            HBox timeBox = setTime();

                            VBox vbox = new VBox(10);
                            vbox.getChildren().addAll(text, imageView, timeBox);

                            HBox hBox = new HBox(10);
                            hBox.setMaxHeight(190);
                            hBox.setMaxWidth(220);
                            hBox.getChildren().add(vbox);

                            //adding message to message area
                            Platform.runLater(() -> {
                                vBox.getChildren().addAll(hBox);
                                scrollPane.layout();
                                scrollPane.setVvalue(2.0);


                                //adding space between messages
                                HBox hBox1 = new HBox();
                                hBox1.setPadding(new Insets(5, 5, 5, 10));
                                vBox.getChildren().add(hBox1);
                            });
                        }
                    }
                    else if(firstChars.equalsIgnoreCase("joi")) {
                        HBox hBox = new HBox();
                        if (lblName.getText().equals(finalName)){
                            Label joinText = new Label("You have join the chat");

                            hBox.getChildren().add(joinText);
                            hBox.setAlignment(Pos.CENTER);
                            hBox.setPadding(new Insets(5,5,5,10));

                        }else {
                            Label joinText = new Label(finalName + " has join the chat");

                            hBox.getChildren().add(joinText);
                            hBox.setAlignment(Pos.CENTER);
                            hBox.setPadding(new Insets(5, 5, 5, 10));
                        }

                        Platform.runLater(() ->
                                vBox.getChildren().addAll(hBox));

                    }
                    else if(firstChars.equalsIgnoreCase("lef")){
                        Label leftText = new Label(finalName + " has left the chat");

                        HBox hBox = new HBox();
                        hBox.getChildren().add(leftText);
                        hBox.setAlignment(Pos.CENTER);
                        hBox.setPadding(new Insets(5, 5, 5, 10));

                        Platform.runLater(() ->
                                vBox.getChildren().addAll(hBox));

                    } else{
                        if(lblName.getText().equals(finalName)){ //1

                            //add message
                            Text text = new Text(message);
                            text.setStyle("-fx-fill: black");
                            text.setWrappingWidth(150);
                            TextFlow tempFlow = new TextFlow(text);
                            tempFlow.setMaxWidth(200);

                            //add sender name
                            Text nameText = new Text("~ Me");

                            //add time
                            HBox timeBox = setTime();

                            VBox vbox = new VBox(10);
                            vbox.setPrefWidth(210);
                            vbox.getChildren().addAll(nameText, tempFlow, timeBox);

                            //add all into message
                            HBox hBox = new HBox(10);
                            hBox.setMaxWidth(190);
                            hBox.setMaxHeight(220);
                            hBox.getChildren().add(vbox);

                            StackPane stackPane = new StackPane(hBox);
                            stackPane.setAlignment(Pos.CENTER_RIGHT);

                            //add message into message area
                            Platform.runLater(() -> {
                                vBox.getChildren().addAll(stackPane); //2
                                scrollPane.layout();
                                scrollPane.setVvalue(2.0);

                                //adding space between messages
                                HBox hBox1 = new HBox();
                                hBox1.setAlignment(Pos.CENTER_LEFT);
                                hBox1.setPadding(new Insets(5, 5, 5, 10));
                                vBox.getChildren().add(hBox1);
                            });
                        }else {
                            //add message
                            TextFlow tempFlow = new TextFlow();
                            Text text = new Text(message);
                            text.setStyle("-fx-fill: black");
                            text.setWrappingWidth(200);
                            tempFlow.getChildren().add(text);
                            tempFlow.setMaxWidth(200);

                            //add sender name
                            Text nameText = new Text("~ "+finalName); //3

                            //add time
                            HBox timeBox = setTime();

                            VBox vbox = new VBox(10);
                            vbox.setPrefWidth(210);
                            vbox.getChildren().addAll(nameText, tempFlow, timeBox);

                            //add all into message
                            HBox hBox = new HBox(10);
                            hBox.setMaxWidth(220);
                            hBox.setMaxHeight(50);
                            hBox.getChildren().add(vbox);

                            //add message into message area
                            Platform.runLater(() -> {
                                vBox.getChildren().addAll(hBox);
                                scrollPane.layout();
                                scrollPane.setVvalue(2.0);

                                //adding space between messages
                                HBox hBox1 = new HBox();
                                hBox1.setPadding(new Insets(5, 5, 5, 10));
                                vBox.getChildren().add(hBox1);
                            });
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
    public static void leaveChat(){
        writer.println("lef"+username + "~leave");
    }

    @FXML
    void btnAttachmentOnAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select the image");
        FileChooser.ExtensionFilter imageFilter =
                new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.jpeg", "*.png", "*.gif");
        fileChooser.getExtensionFilters().add(imageFilter);
        file = fileChooser.showOpenDialog(txtMessage.getScene().getWindow());
        if (file != null){
            txtMessage.setText("1 image selected");
            txtMessage.setEditable(false);
        }
    }

    @FXML
    void btnSendOnAction(ActionEvent event) {
        if (!txtMessage.getText().isEmpty()){
            if (file != null){
                writer.println("img"+lblName.getText()+"~"+ file.getPath());
                file = null;
            }else {
                writer.println(lblName.getText() + "~" + txtMessage.getText());
            }
            txtMessage.setEditable(true);
            txtMessage.clear();
        }
    }
    private void unicodeEmoji(int unicode) {
        String emoji = new String(Character.toChars(unicode));
        txtMessage.setText(txtMessage.getText() + emoji);
        txtMessage.positionCaret(txtMessage.getText().length());
        emojiPane.setVisible(false);
    }

    @FXML
    void cry(MouseEvent event) {
        unicodeEmoji(128546);
    }

    @FXML
    void cry_sad(MouseEvent event) {
        unicodeEmoji(128550);
    }

    @FXML
    void btnEmojiOnAction(ActionEvent event) {
        emojiPane.setVisible(!emojiPane.isVisible());
    }

    @FXML
    void eyeclose_smile(MouseEvent event) {
        unicodeEmoji(128540);
    }

    @FXML
    void green_sad(MouseEvent event) {
        unicodeEmoji(128560);
    }

    @FXML
    void large_smile(MouseEvent event) {
        unicodeEmoji(128513);
    }

    @FXML
    void lot_sad(MouseEvent event) {
        unicodeEmoji(128554);
    }

    @FXML
    void love(MouseEvent event) {
        unicodeEmoji(128525);
    }

    @FXML
    void money(MouseEvent event) {
        unicodeEmoji(129297);
    }

    @FXML
    void normal_smile(MouseEvent event) {
        unicodeEmoji(128522);
    }

    @FXML
    void real_smile(MouseEvent event) {
        unicodeEmoji(128514);
    }

    @FXML
    void sad(MouseEvent event) {
        unicodeEmoji(128546);
    }

    @FXML
    void small_smile(MouseEvent event) {
        unicodeEmoji(128578);
    }

    @FXML
    void tong_smile(MouseEvent event) {
        unicodeEmoji(128539);
    }

    @FXML
    void tuin(MouseEvent event) {
        unicodeEmoji(128519);
    }

    @FXML
    void txtMessageOnAction(ActionEvent event) {
        btnSendOnAction(event);
    }

    @FXML
    void woow(MouseEvent event) {
        unicodeEmoji(128559);
    }
    public void themeViewOnAction(MouseEvent mouseEvent) {
        if(themeView.getImage().getUrl().equals(new Image("images/icon/light.png").getUrl())){
            root.setStyle("-fx-background-color: #fefae0;");
            textArea.getStyleClass().removeAll("dark-text");
            textArea.getStyleClass().add("light-text");
            header.setStyle("-fx-background-color: #2a9a84;");
            themeView.setImage(new Image("images/icon/dark.png"));
        }else{
            root.setStyle("-fx-background-color:  #2f3e46;");
            textArea.getStyleClass().removeAll("light-text");
            textArea.getStyleClass().add("dark-text");
            header.setStyle("-fx-background-color: transparent;");
            themeView.setImage(new Image("images/icon/light.png"));;
        }
    }
    public void closeOnAction(MouseEvent mouseEvent) {
        imagePane.setVisible(false);
    }
    private HBox setTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
        LocalDateTime now = LocalDateTime.now();

        Text time = new Text(dtf.format(now));
        time.setFont(Font.font(10));

        HBox timeBox = new HBox();
        timeBox.getChildren().add(time);
        timeBox.setAlignment(Pos.BASELINE_RIGHT);

        return timeBox;
    }

}
