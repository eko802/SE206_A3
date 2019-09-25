package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {

    //List of panes we will be using
    private BorderPane root = new BorderPane(); //Root pane where it holds everything
    private BorderPane mainMenuPane = new BorderPane(); //Pane for main menu
    private GridPane viewMenuPane = new GridPane(); //Pane for view/remove/play menu
    private GridPane createMenuPane = new GridPane(); //Pane for create creation menu
    private GridPane createAudioMenuPane = new GridPane();// Pane for create audio menu
    //Menu selection buttons
    private HBox menuSelectionButtons = new HBox(); //HBox where it holds menu buttons
    private Button mainMenuButton = new Button("Main Menu");
    private Button viewMenuButton = new Button("Play/Remove");
    private Button createMenuButton = new Button("Create New Creation");
    private Button createAudioMenuButton = new Button("Create New Audio");

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("VARpedia Prototype");

        //Show GUI
        Scene scene = new Scene(root, 500, 500);
        primaryStage.setScene(scene);
        primaryStage.show();

        //menuSelectionButtons setup (The buttons at the bottom of the window)
        menuSelectionButtons.getChildren().addAll(createMenuButton, createAudioMenuButton, mainMenuButton,viewMenuButton);
        menuSelectionButtons.setAlignment(Pos.CENTER);

        //Main Menu Design
        Text welcomeText = new Text("## WELCOME TO VARpedia prototype ##");
        welcomeText.setStyle("-fx-font: 18 arial;");
        Text instruction = new Text("Please select a menu at the bottom");
        mainMenuPane.setCenter(instruction);
        mainMenuPane.setTop(welcomeText);
        mainMenuPane.setAlignment(welcomeText, Pos.CENTER);

        //Create Creation Menu Design
        Text createMenuTitle = new Text("######Create A New Creation######");
        createMenuTitle.setStyle("-fx-font: 18 arial;");
        createMenuPane.add(createMenuTitle, 0,0,2,1);

        //Create Audion Menu Design
        Text createAudioMenuTitle = new Text("######Create A New Audio File######");
        createAudioMenuTitle.setStyle("-fx-font: 18 arial;");
        createAudioMenuPane.add(createAudioMenuTitle,0,0,2,1);

        //View Pane design
        Text viewMenuTitle = new Text("######View/Delete Existing Creations######");
        viewMenuTitle.setStyle("-fx-font: 18 arial;");
        viewMenuPane.add(viewMenuTitle,0,0,2,1);
        viewMenuPane.getColumnConstraints().add(new ColumnConstraints(300));
        viewMenuPane.getColumnConstraints().add(new ColumnConstraints(100));
        viewMenuPane.getRowConstraints().add(new RowConstraints(40));
        viewMenuPane.getRowConstraints().add(new RowConstraints(200));
        viewMenuPane.getRowConstraints().add(new RowConstraints(25));
        viewMenuPane.getRowConstraints().add(new RowConstraints(25));

        TextArea creationList = new TextArea("[Click \"Update list\" to update the list]");
        viewMenuPane.add(creationList,0,1,1,1);
        Button updateButton = new Button("Update list");
        viewMenuPane.add(updateButton,1,1,1,1);
        TextField deleteTF = new TextField("Enter name of creation to delete");
        viewMenuPane.add(deleteTF, 0, 2,1,1);
        Button deleteButton = new Button("Delete");
        viewMenuPane.add(deleteButton,1,2,1,1);
        TextField playTF = new TextField("Enter name of creation to play");
        viewMenuPane.add(playTF, 0, 3,1,1);
        Button playButton = new Button("Play");
        viewMenuPane.add(playButton,1,3,1,1);

        //Default GUI configuration
        //When the program is launched, the default menu shown is the main menu
        root.setCenter(mainMenuPane);
        root.setBottom(menuSelectionButtons);
        root.setAlignment(menuSelectionButtons, Pos.CENTER);
        mainMenuButton.setDisable(true);

        //Menu Buttons Functions
        //Change to Create menu
        createMenuButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                menuConfig("create");
            }
        });
        //Change to Main menu
        mainMenuButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
              menuConfig("main");
            }
        });
        //Change to View menu
        viewMenuButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                menuConfig("view");
            }
        });
        //change to CreateAudio menu
        createAudioMenuButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) { menuConfig("createAudio"); }
        });
    }

    //This method sets up the design of the menu pane when a menu is selected
    void menuConfig(String menu){
        if(menu.equals("create")){
            root.getChildren().remove(mainMenuPane);
            root.getChildren().remove(viewMenuPane);
            root.getChildren().remove(createAudioMenuPane);
            root.getChildren().add(createMenuPane);
            createMenuButton.setDisable(true);
            viewMenuButton.setDisable(false);
            mainMenuButton.setDisable(false);
            createAudioMenuButton.setDisable(false);
        }else if(menu.equals("view")) {
            root.getChildren().remove(mainMenuPane);
            root.getChildren().remove(createMenuPane);
            root.getChildren().remove(createAudioMenuPane);
            root.getChildren().add(viewMenuPane);
            viewMenuButton.setDisable(true);
            createMenuButton.setDisable(false);
            mainMenuButton.setDisable(false);
            createAudioMenuButton.setDisable(false);
        }else if (menu.equals("createAudio")){
            root.getChildren().add(createAudioMenuPane);
            root.getChildren().remove(mainMenuPane);
            root.getChildren().remove(createMenuPane);
            root.getChildren().remove(viewMenuPane);
            createAudioMenuButton.setDisable(true);
            viewMenuButton.setDisable(false);
            createMenuButton.setDisable(false);
            mainMenuButton.setDisable(false);
        }else{
            root.getChildren().remove(createMenuPane);
            root.getChildren().remove(viewMenuPane);
            root.getChildren().remove(createAudioMenuPane);
            root.getChildren().add(mainMenuPane);
            mainMenuButton.setDisable(true);
            createMenuButton.setDisable(false);
            viewMenuButton.setDisable(false);
            createAudioMenuButton.setDisable(false);
        }
    }



    public static void main(String[] args) {
        launch(args);
    }
}
