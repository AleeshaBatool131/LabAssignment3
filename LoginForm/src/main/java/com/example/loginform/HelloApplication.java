package com.example.loginform;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;

public class HelloApplication extends Application {
    private final File file = new File("loginFile.txt");
    TextField usernameField = new TextField();
    StackPane rootPane = new StackPane();

    @Override
    public void start(Stage stage) throws IOException {
        createLoginScene(stage);

    }
    private void createLoginScene(Stage stage){
        // Ensure the file exists and populate it with default credentials
        try {
            initializeLoginFile();
        }
        catch (IOException e){
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        // Set up GridPane and Background
        GridPane gridPane = new GridPane();
        Image image = new Image(this.getClass().getResource("/BackGround Image.jpg").toExternalForm());
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        gridPane.setBackground(new Background(backgroundImage));
        gridPane.setPadding(new Insets(20));
        gridPane.setVgap(20);
        gridPane.setHgap(20);

        // Add Title
        Text text = new Text("Login");
        text.setStyle("-fx-font-size: 48px; -fx-font-weight: bold; -fx-font-family: 'Times New Roman'; -fx-fill: black;");
        VBox titleBox = new VBox(text);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setPadding(new Insets(20));
        titleBox.setStyle("-fx-padding: 20; -fx-background-color: lightblue");
        titleBox.setPrefWidth(420);
        titleBox.setSpacing(15);
        gridPane.add(titleBox, 1, 0, 2, 1);

        // Add Username and Password Fields
        Label usernameLabel = new Label("Username: ");
        usernameLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: black;-fx-font-weight: bold;");
        usernameField.setPromptText("Enter your username");
        usernameField.setStyle("-fx-background-radius: 5; -fx-padding: 5;");
        usernameField.setPrefWidth(250);
        HBox usernameBox = new HBox(10, usernameLabel, usernameField);
        usernameBox.setAlignment(Pos.CENTER);
        usernameBox.setPadding(new Insets(20));
        usernameBox.setPrefWidth(400);

        Label passwordLabel = new Label("Password: ");
        passwordLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: black;-fx-font-weight: bold;");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
        passwordField.setStyle("-fx-background-radius: 5; -fx-padding: 5;");
        passwordField.setPrefWidth(250);
        HBox passwordBox = new HBox(10, passwordLabel, passwordField);
        passwordBox.setAlignment(Pos.CENTER);
        passwordBox.setPadding(new Insets(20));
        passwordBox.setPrefWidth(400);

        gridPane.add(usernameBox, 1, 2);
        gridPane.add(passwordBox, 1, 3);

        // Add Buttons
        Button loginButton = new Button("Login");
        Button exitButton = new Button("Exit");
        HBox buttonBox = new HBox(10, loginButton, exitButton);
        buttonBox.setAlignment(Pos.CENTER);
        loginButton.setStyle("-fx-background-color: lightblue; -fx-text-fill: black; -fx-font-size: 18px; -fx-background-radius: 5;");
        exitButton.setStyle("-fx-background-color: lightblue; -fx-text-fill: black; -fx-font-size: 18px; -fx-background-radius: 5;");
        buttonBox.setPrefWidth(400);
        buttonBox.setPadding(new Insets(30));
        buttonBox.setSpacing(30);
        gridPane.add(buttonBox, 1, 4);

        // Set Button Actions
        loginButton.setOnAction(e -> handleLogin(usernameField.getText(), passwordField.getText(), stage));
        exitButton.setOnAction(e -> stage.close());

        // Create Scene
        Scene scene = new Scene(gridPane, 500, 500);
        stage.setTitle("Login Form");
        stage.setScene(scene);
        stage.show();
    }

    public void initializeLoginFile() throws IOException {
        if (!file.exists()) {
            file.createNewFile();
        }

        // Populate file with default users if empty
        if (file.length() == 0) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write("admin,pass123\n");
                writer.write("Aleesha,pass1\n");
                writer.write("Sara,pass2\n");
            }
        }

    }

    private void handleLogin(String username, String password, Stage stage) {
        boolean isValid = checkCredentials(username, password);

        if (isValid) {
            showNextScreen(stage, true, "Welcome " + username + "!");
        } else {
            showNextScreen(stage, false, "Incorrect Username or Password!");
        }
    }

    private boolean checkCredentials(String username, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2 && parts[0].equals(username) && parts[1].equals(password)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void showNextScreen(Stage stage, boolean isSuccess, String message) {
        VBox vbox = new VBox();
        Text messageText = new Text(message);
        messageText.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-fill: black; -fx-font-family: Elephant; -fx-font-style: italic;");
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(20);

        // Success or Failure Style
        if (isSuccess) {
            vbox.setStyle("-fx-padding: 20; -fx-background-color: skyblue;");
        } else {
            vbox.setStyle("-fx-padding: 20;-fx-background-color: skyblue;");
        }
        Button backButton = new Button("Logout");
        backButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: black; -fx-font-size: 14px; -fx-background-radius: 10;");
        backButton.setOnAction(e -> createLoginScene(stage));

        vbox.getChildren().addAll(messageText, backButton);

        Scene nextScene = new Scene(vbox, 500, 500);
        stage.setScene(nextScene);
        stage.setTitle("Screen");
    }

    public static void main(String[] args) {
        launch();
    }
}