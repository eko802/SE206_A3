package sample;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {

	//List of panes we will be using
	private BorderPane root = new BorderPane(); //Root pane where it holds everything
	private BorderPane mainMenuPane = new BorderPane(); //Pane for main menu
	private BorderPane playCreationMenu = new BorderPane();
	private GridPane viewMenuPane = new GridPane(); //Pane for view/remove/play menu
	private GridPane createMenuPane = new GridPane(); //Pane for create creation menu
	private GridPane createAudioMenuPane = new GridPane();// Pane for create audio menu
	//Menu selection buttons
	private HBox menuSelectionButtons = new HBox(); //HBox where it holds menu buttons
	private Button mainMenuButton = new Button("Main Menu");
	private Button viewMenuButton = new Button("View Creations");
	private Button createMenuButton = new Button("Create New");
	private Button createAudioMenuButton = new Button("View Audio");

	private MediaView mediaView = null;
	private MediaPlayer player = null;
	@Override
	public void start(Stage primaryStage) throws Exception{
		primaryStage.setTitle("VARpedia Prototype");

		//Show GUI
		Scene scene = new Scene(root, 400, 500);
		primaryStage.setScene(scene);
		primaryStage.show();

		//menuSelectionButtons setup (The buttons at the bottom of the window)
		menuSelectionButtons.getChildren().addAll(mainMenuButton,createMenuButton, createAudioMenuButton, viewMenuButton);
		menuSelectionButtons.setAlignment(Pos.CENTER);

		//Main Menu Design
		Text welcomeText = new Text("## WELCOME TO VARpedia prototype ##");
		welcomeText.setStyle("-fx-font: 18 arial;");
		Text instruction = new Text("Please select a menu at the bottom");
		mainMenuPane.setCenter(instruction);
		mainMenuPane.setTop(welcomeText);
		mainMenuPane.setAlignment(welcomeText, Pos.CENTER);

		//Create Creation Menu Design
		Text createMenuTitle = new Text("######Search From Wikipedia######");
		createMenuTitle.setStyle("-fx-font: 18 arial;");
		createMenuPane.add(createMenuTitle, 0,0,2,1);
		createMenuPane.getColumnConstraints().add(new ColumnConstraints(300));
		createMenuPane.getColumnConstraints().add(new ColumnConstraints(100));
		createMenuPane.getRowConstraints().add(new RowConstraints(40));
		createMenuPane.getRowConstraints().add(new RowConstraints(25));
		createMenuPane.getRowConstraints().add(new RowConstraints(340));
		createMenuPane.getRowConstraints().add(new RowConstraints(25));
		createMenuPane.getRowConstraints().add(new RowConstraints(25));
		TextField searchTextField = new TextField("Type here to search!");
		searchTextField.setPrefSize(300, 1000);
		createMenuPane.add(searchTextField, 0, 1,1,1);
		Button searchButton = new Button("Search");
		searchButton.setMaxWidth(100);
		createMenuPane.add(searchButton,1,1,1,1);
		TextArea wikitTextArea = new TextArea("Click Search Button!\n\n\n"
				+ "[Button name]: [functions]\n"
				+ "[Create Creation]: [Create a new creation from dragged texts]\n"
				+ "[Create Audio]: [Create a new audio from dragged texts]\n"
				+ "[Preview Audio]: [Preview the dragged text]");

		createMenuPane.add(wikitTextArea, 0, 2, 2, 1);
		HBox createMenuSelection = new HBox(); //HBox where it holds menu buttons
		Button createCreation = new Button("Create Creation");
		Button createAudio = new Button("Create Audio");
		Button previewAudio = new Button("Preview Audio");
		createMenuSelection.getChildren().addAll(createCreation, createAudio, previewAudio);
		createMenuSelection.setAlignment(Pos.CENTER);
		createMenuPane.add(createMenuSelection, 0, 3, 2, 1);

		//Create Menu Button functions
		searchButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				String searched = searchTextField.getText();
				//If invalid input is given (empty input, or default input)
				if(searched.equals("")||searched.equals("Type here to search!")) {
					Wikipedia.alertInvalidSearch();
				}else {
					wikitTextArea.clear();
					WikiWorker worker = new WikiWorker(searched, wikitTextArea);
					worker.start();
				}

			}			
		});

		createCreation.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// Respond to highlighted text
				boolean error = false;
				String highlightedText = wikitTextArea.getSelectedText();
				if (highlightedText.equals("")||highlightedText == null) {
					AudioRetrieve.showError();

				}else {
					try {
					AudioRetrieve.saveTextToAudio(highlightedText, false, false);
					} catch(Exception e) {
						error = true;
					}
					if(error == false) {
						CreateNewCreation createCreation = new CreateNewCreation("Ask user for creation name", false);
						createCreation.start();
					}
				}

			}

		});

		previewAudio.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// Respond to highlighted text
				String highlightedText = wikitTextArea.getSelectedText();
				if (highlightedText.equals("")||highlightedText == null) {
					AudioRetrieve.showError();

				}else {
					try {
						AudioRetrieve.saveTextToAudio(highlightedText, true, true);	
					}catch(Exception e) {
						
					}

				}

			}

		});


		createAudio.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// Respond to highlighted text
				String highlightedText = wikitTextArea.getSelectedText();
				if (highlightedText.equals("")||highlightedText == null) {
					AudioRetrieve.showError();

				}else {
					try {
						AudioRetrieve.saveTextToAudio(highlightedText, false, true);
					}catch(Exception e) {
						
					}
				}
			}	
		});




		// Create Playing of Creations Design
		Text playCreationTitle = new Text("######PLaying Creation######");
		playCreationTitle.setStyle("-fx-font: 18 arial;");
		playCreationMenu.setTop(playCreationTitle);

		//Create Audio Menu Design
		Text createAudioMenuTitle = new Text("######Create A New Audio File######");
		createAudioMenuTitle.setStyle("-fx-font: 18 arial;");
		createAudioMenuPane.add(createAudioMenuTitle,0,0,2,1);
		createAudioMenuPane.getColumnConstraints().add(new ColumnConstraints(300));
		createAudioMenuPane.getColumnConstraints().add(new ColumnConstraints(100));
		createAudioMenuPane.getRowConstraints().add(new RowConstraints(40));
		createAudioMenuPane.getRowConstraints().add(new RowConstraints(200));
		createAudioMenuPane.getRowConstraints().add(new RowConstraints(25));
		createAudioMenuPane.getRowConstraints().add(new RowConstraints(200));

		TextArea audioList = new TextArea("Click \"Update list\"!");
		audioList.setEditable(false);
		createAudioMenuPane.add(audioList,0,1,1,1);
		Button updateAudioButton = new Button("Update list");
		updateAudioButton.setMaxWidth(100);
		createAudioMenuPane.add(updateAudioButton,1,1,1,1);
		Text creationViaAudio = new Text("Copy the audios that you want from the above list");
		createAudioMenuPane.add(creationViaAudio, 0, 2,2,1);
		TextArea audioToCreationList = new TextArea("Type names of audios to merge"
				+ "\neach separed by a new line");
		createAudioMenuPane.add(audioToCreationList, 0, 3, 1,1);
		Button createViaAudioButton = new Button("Create");		
		createAudioMenuPane.add(createViaAudioButton, 1, 3, 1, 1);
		createViaAudioButton.setMaxWidth(100);

		//Create Audio menu button functions
		updateAudioButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					//Display saved audio file list
					AudioList.displayExistingAudioList(audioList);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		});

		//Merge Audio files to create a new creation
		createViaAudioButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					//Display saved audio file list
					MergeAudioFilesWorker workerAudio = new MergeAudioFilesWorker(audioToCreationList);
					workerAudio.start();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		});

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
		creationList.setEditable(false);
		viewMenuPane.add(creationList,0,1,1,1);
		Button updateButton = new Button("Update list");
		updateButton.setMaxWidth(100);
		viewMenuPane.add(updateButton,1,1,1,1);
		TextField deleteTF = new TextField("Enter name of creation to delete");
		viewMenuPane.add(deleteTF, 0, 2,1,1);
		Button deleteButton = new Button("Delete");
		deleteButton.setMaxWidth(100);
		viewMenuPane.add(deleteButton,1,2,1,1);
		TextField playTF = new TextField("Enter name of creation to play");
		viewMenuPane.add(playTF, 0, 3,1,1);
		Button playButton = new Button("Play");
		viewMenuPane.add(playButton,1,3,1,1);
		playButton.setMaxWidth(100);

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
				menuConfig("create", mediaView, player);
			}
		});
		//Change to Main menu
		mainMenuButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				menuConfig("main", mediaView, player);
			}
		});
		//Change to View menu
		viewMenuButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				menuConfig("view", mediaView, player);
				try {
					ViewCreations.displayExistingCreations(creationList, playButton, deleteButton);
					if ((playButton.isDisable() == true) && (deleteButton.isDisable() == true)) {
						playTF.setText("There are Currently No Creations to Play!");
						deleteTF.setText("There are Currently No Creations to Delete!");
					} else {
						playTF.setText("Enter name of creation to play");
						deleteTF.setText("Enter name of creation to delete");
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		// Update current creations list
		updateButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent){
				try {
					ViewCreations.displayExistingCreations(creationList, playButton, deleteButton);
					if ((playButton.isDisable() == true) && (deleteButton.isDisable() == true)) {
						playTF.setText("There are Currently No Creations to Play!");
						deleteTF.setText("There are Currently No Creations to Delete!");
					} else {
						playTF.setText("Enter name of creation to play");
						deleteTF.setText("Enter name of creation to delete");
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		// Delete an existing creation
		deleteButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				try {
					DeleteCreation.deleteExistingCreation(deleteTF);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		// Play an existing creation
		playButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				try {
					BufferedReader br = new BufferedReader(new FileReader("./nameOfCreations.txt"));
					String line;
					while ((line = br.readLine()) != null) {
						if (line.equals(playTF.getText())) {
							line = "creation found";
							viewMenuButton.setDisable(false);
							root.getChildren().remove(viewMenuPane);
							File fileUrl = new File("./creations/" + playTF.getText() + ".mp4"); 
							Media video = new Media(fileUrl.toURI().toString());
							player = new MediaPlayer(video);
							player.setAutoPlay(true);
							mediaView = new MediaView(player);
							playCreationMenu.setCenter(mediaView);
							root.getChildren().add(playCreationMenu);
							mediaView.fitWidthProperty().bind(root.widthProperty());
							mediaView.fitHeightProperty().bind(root.heightProperty());
							break; 
						}
					}

					/*
					 * If the creation which the user specifies is not found then alert is thrown
					 * Otherwise the creation is played until the end or user switches to a different
					 * menu
					 */
					if (line == null) {
						Alert alert2 = new Alert(Alert.AlertType.ERROR);
						alert2.setTitle("CREATION NOT PLAYED!");
						alert2.setContentText("Creation not found. Try different name.");
						alert2.showAndWait();
					} else {
						// Auto playing the creation
						player.setOnEndOfMedia(new Runnable() {
							public void run() {
								player.stop();
								mediaView.fitHeightProperty().unbind();
								mediaView.fitWidthProperty().unbind();
								playCreationMenu.getChildren().remove(mediaView);
								viewMenuButton.setDisable(true);
								root.getChildren().remove(playCreationMenu);
								root.getChildren().add(viewMenuPane);
								return;
							}
						});
					}
				} catch (Exception e) {

				} finally {

				}
			}
		});
		//change to CreateAudio menu
		createAudioMenuButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) { menuConfig("createAudio", mediaView, player); }
		});
	}

	//This method sets up the design of the menu pane when a menu is selected
	//If the user changes the menu before the creation has finished playing
	//This method also terminates the playing of the creation
	void menuConfig(String menu, MediaView mediaView, MediaPlayer player){
		if(menu.equals("create")){

			// If creation is currently playing then it is stopped and removed from the GUI  
			if ((mediaView != null) && (player != null)) {
				player.stop();
				mediaView.fitHeightProperty().unbind();
				mediaView.fitWidthProperty().unbind();
				playCreationMenu.getChildren().remove(mediaView);
				player = null; mediaView = null;
			}
			root.getChildren().remove(mainMenuPane);
			root.getChildren().remove(viewMenuPane);
			root.getChildren().remove(createAudioMenuPane);
			root.getChildren().remove(playCreationMenu);
			root.getChildren().add(createMenuPane);
			createMenuButton.setDisable(true);
			viewMenuButton.setDisable(false);
			mainMenuButton.setDisable(false);
			createAudioMenuButton.setDisable(false);
		}else if(menu.equals("view")) {

			// If creation is currently playing then it is stopped and removed from the GUI 
			if ((mediaView != null) && (player != null)) {
				player.stop();
				mediaView.fitHeightProperty().unbind();
				mediaView.fitWidthProperty().unbind();
				playCreationMenu.getChildren().remove(mediaView);
				player = null; mediaView = null;
			}
			root.getChildren().remove(mainMenuPane);
			root.getChildren().remove(createMenuPane);
			root.getChildren().remove(createAudioMenuPane);
			root.getChildren().remove(playCreationMenu);
			root.getChildren().add(viewMenuPane);
			viewMenuButton.setDisable(true);
			createMenuButton.setDisable(false);
			mainMenuButton.setDisable(false);
			createAudioMenuButton.setDisable(false);
		}else if (menu.equals("createAudio")){

			// If creation is currently playing then it is stopped and removed from the GUI 
			if ((mediaView != null) && (player != null)) {
				player.stop();
				mediaView.fitHeightProperty().unbind();
				mediaView.fitWidthProperty().unbind();
				playCreationMenu.getChildren().remove(mediaView);
				player = null; mediaView = null;
			}
			root.getChildren().add(createAudioMenuPane);
			root.getChildren().remove(mainMenuPane);
			root.getChildren().remove(createMenuPane);
			root.getChildren().remove(viewMenuPane);
			root.getChildren().remove(playCreationMenu);
			createAudioMenuButton.setDisable(true);
			viewMenuButton.setDisable(false);
			createMenuButton.setDisable(false);
			mainMenuButton.setDisable(false);
		}else{

			// If creation is currently playing then it is stopped and removed from the GUI 
			if ((mediaView != null) && (player != null)) {
				player.stop();
				mediaView.fitHeightProperty().unbind();
				mediaView.fitWidthProperty().unbind();
				playCreationMenu.getChildren().remove(mediaView);
				player = null; mediaView = null;
			}
			root.getChildren().remove(createMenuPane);
			root.getChildren().remove(viewMenuPane);
			root.getChildren().remove(createAudioMenuPane);
			root.getChildren().remove(playCreationMenu);
			root.getChildren().add(mainMenuPane);
			mainMenuButton.setDisable(true);
			createMenuButton.setDisable(false);
			viewMenuButton.setDisable(false);
			createAudioMenuButton.setDisable(false);
		}
	}

	public static void createFolder() {
		try {
			String key = "apiKey = a6c8799f554aa9e04a08158a015016e5\nsharedSecret = 2011587abd40f68a";
			ProcessBuilder newFolders = new ProcessBuilder("/bin/bash", "-c", "touch nameOfCreations.txt nameOfAudios.txt;"
					+ "echo \"" + key + "\" > flickr-api-keys.txt;mkdir -p creations audioFiles");
			newFolders.start();
		} catch (Exception e) {

		}
	}

	public static void main(String[] args) {
		createFolder();
		launch(args);
	}
}