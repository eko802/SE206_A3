package sample;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
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
	private GridPane gameMenuPane = new GridPane();
	//Menu selection buttons
	private HBox menuSelectionButtons = new HBox(); //HBox where it holds menu buttons
	private Button mainMenuButton = new Button("Main Menu");
	private Button viewMenuButton = new Button("View Creations");
	private Button createMenuButton = new Button("Create New");
	private Button createAudioMenuButton = new Button("View Audio");
	private Button gameMenuButton = new Button("Game");
	private String currentGameLevel = "Easy";
	
	public static String searched = "";
	GameCreationWorker play;

	@Override
	public void start(Stage primaryStage) throws Exception{
		primaryStage.setTitle("VARpedia Prototype");

		//Show GUI
		Scene scene = new Scene(root, 400, 500);
		scene.getStylesheets().add(Main.class.getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();

		//menuSelectionButtons setup (The buttons at the bottom of the window)
		menuSelectionButtons.getChildren().addAll(createMenuButton, createAudioMenuButton, viewMenuButton, gameMenuButton);
		menuSelectionButtons.setAlignment(Pos.CENTER);

		//Main Menu Design
		Text welcomeText = new Text("## WELCOME TO VARpedia prototype ##");
		welcomeText.setStyle("-fx-font: 18 arial;");
		welcomeText.setFill(Color.GHOSTWHITE);
		Text instruction = new Text("Please select a menu at the bottom");
		instruction.setFill(Color.GHOSTWHITE);
		mainMenuPane.setCenter(instruction);
		mainMenuPane.setTop(welcomeText);
		mainMenuPane.setAlignment(welcomeText, Pos.CENTER);


		//Create Creation Menu Design
		Text createMenuTitle = new Text("######Search From Wikipedia######");
		createMenuTitle.setStyle("-fx-font: 18 arial;");
		createMenuTitle.setFill(Color.GHOSTWHITE);
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
				searched = searchTextField.getText();
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
		playCreationTitle.setFill(Color.GHOSTWHITE);
		playCreationMenu.setTop(playCreationTitle);

		//Create Audio Menu Design
		Text createAudioMenuTitle = new Text("######Create A New Audio File######");
		createAudioMenuTitle.setStyle("-fx-font: 18 arial;");
		createAudioMenuTitle.setFill(Color.GHOSTWHITE);
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
		creationViaAudio.setFill(Color.GHOSTWHITE);
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


		//Match Game Menu Design
		Text gameMenuTitle = new Text("######Simple Matching Game######");
		gameMenuTitle.setStyle("-fx-font: 18 arial;");
		gameMenuTitle.setFill(Color.GHOSTWHITE);
		//Buttons for level/play game
		HBox levelSelectionButtons = new HBox();
		Button EasyButton = new Button("Easy");
		Button MediumButton = new Button("Medium");
		Button DifficultButton = new Button("Difficult");
		levelSelectionButtons.getChildren().addAll(EasyButton, MediumButton, DifficultButton);
		levelSelectionButtons.setAlignment(Pos.CENTER);

		// Game starts at easy level
		EasyButton.setDisable(true);

		//Buttons for playing creation and getting correct answer
		HBox playOrGetAnswerOfCreation = new HBox();
		Button playCreationGame = new Button("Play Creation");
		Button playSameCreation = new Button("Play Creation again");
		Button getAnswer = new Button("Click to get Answer!");
		playSameCreation.setDisable(true);
		getAnswer.setDisable(true);
		playOrGetAnswerOfCreation.getChildren().addAll(playCreationGame, playSameCreation, getAnswer);
		playOrGetAnswerOfCreation.setAlignment(Pos.CENTER);


		gameMenuPane.add(gameMenuTitle,0,0,2,1);
		gameMenuPane.getColumnConstraints().add(new ColumnConstraints(300));
		gameMenuPane.getColumnConstraints().add(new ColumnConstraints(100));
		gameMenuPane.getRowConstraints().add(new RowConstraints(40));
		gameMenuPane.getRowConstraints().add(new RowConstraints(25));
		gameMenuPane.getRowConstraints().add(new RowConstraints(25));

		Text instructionText = new Text("                                Choose level to play");
		instructionText.setFill(Color.GHOSTWHITE);
		gameMenuPane.add(instructionText, 0, 1,2,1);
		gameMenuPane.add(levelSelectionButtons, 0, 2,2,1);
		Text blank = new Text("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
		gameMenuPane.add(blank, 0, 3,2,1);
		TextField answer = new TextField("Type answer");
		gameMenuPane.add(answer, 0, 4,2,1);
		Button submitButton = new Button("Submit");
		submitButton.setMaxWidth(100);
		gameMenuPane.add(submitButton, 1, 4,2,1);
		//Disable submit button before playing game
		submitButton.setDisable(true);
		Button instructionButton = new Button("Read Instruction");
		gameMenuPane.add(instructionButton, 0, 5, 2, 1);
		gameMenuPane.add(playOrGetAnswerOfCreation, 0, 3,4,1);

		instructionButton.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent event) {
				// show instruction dialog
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setTitle("Instruction");
				alert.setResizable(true);
				alert.setHeaderText("Simple Matching Game");
				alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
				alert.setContentText("[Instruction for Simple Matching Game]\n\nClick one of the three levels (Easy, Medium, Difficult) to start the game.\nClick \"Play Creation\" button to play a NEW creation.\nClick \"Play Creation again\""
						+ " to play the SAME creation again.\nClick \"Click to get Answer!\" to get the answer for the creation played."
						+ "\nGuess the subject of the creation and click Submit button.\n\n\nEasy: Audio + Video will be played\nMedium: only Video will be played\nDifficult: Only Audio will be played");
				alert.getDialogPane().getStylesheets().add(Main.class.getResource("alert.css").toExternalForm());
				alert.showAndWait();
				return;
			}

		});

		EasyButton.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent event) {
				removeAudioAndVideoFile();
				EasyButton.setDisable(true);
				MediumButton.setDisable(false);
				DifficultButton.setDisable(false);
				submitButton.setDisable(true);
				playSameCreation.setDisable(true);
				getAnswer.setDisable(true);
				currentGameLevel = "Easy";
			}

		});


		MediumButton.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent event) {
				removeAudioAndVideoFile();
				MediumButton.setDisable(true);
				EasyButton.setDisable(false);
				DifficultButton.setDisable(false);
				submitButton.setDisable(true);
				playSameCreation.setDisable(true);
				getAnswer.setDisable(true);
				currentGameLevel = "Medium";

			}

		});


		DifficultButton.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent event) {
				removeAudioAndVideoFile();
				DifficultButton.setDisable(true);
				EasyButton.setDisable(false);
				MediumButton.setDisable(false);
				submitButton.setDisable(true);
				playSameCreation.setDisable(true);
				getAnswer.setDisable(true);
				currentGameLevel = "Difficult";
			}

		});


		playCreationGame.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent event) {
				removeAudioAndVideoFile();
				submitButton.setDisable(false);
				playSameCreation.setDisable(false);
				getAnswer.setDisable(false);
				play = new GameCreationWorker(currentGameLevel);
				play.start();
			}

		});
		
		// Submit answer to the creation
		submitButton.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent event) {
				if (play.getStatus()) {
					PieceOfPaperGame errorMessage = new PieceOfPaperGame();
					Platform.runLater(errorMessage);
					root.getChildren().remove(gameMenuPane);
					gameMenuButton.setDisable(false);
					root.getChildren().add(createMenuPane);
					createMenuButton.setDisable(true);
					return;
				}
				String userAnswer = answer.getText().toLowerCase();
				String realAnswer = play.getAnswer().toLowerCase();
				
				if (userAnswer.equals(realAnswer)) {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Congratulations!!!");
					alert.setHeaderText("The Answer you have given in correct!!!!");
					alert.setContentText("The Answer you have given in correct!!!!");
					alert.getDialogPane().getStylesheets().add(Main.class.getResource("alert.css").toExternalForm());
					alert.showAndWait();
					return;
				} else {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Error Dialog");
					alert.setHeaderText("Sorry!");
					alert.setContentText("Ooops, your answer is INCORRECT (╯︵╰,)");
					alert.getDialogPane().getStylesheets().add(Main.class.getResource("alert.css").toExternalForm());
					alert.showAndWait();
				}
			}

		});
		
		
		
		
		// User wants to know the answer of the creation for the game
		getAnswer.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent event) {
				if (play.getStatus()) {
					PieceOfPaperGame errorMessage = new PieceOfPaperGame();
					Platform.runLater(errorMessage);
					root.getChildren().remove(gameMenuPane);
					gameMenuButton.setDisable(false);
					root.getChildren().add(createMenuPane);
					createMenuButton.setDisable(true);
					return;
				}
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Answer!");
				alert.setHeaderText("The Answer is \"" + play.getAnswer() + "\"");
				alert.setContentText("The Answer is \"" + play.getAnswer() + "\"");
				alert.getDialogPane().getStylesheets().add(Main.class.getResource("alert.css").toExternalForm());
				alert.showAndWait();
				return;
			}

		});

		// Play the same creation again 
		playSameCreation.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent event) {
				if (play.getStatus()) {
					PieceOfPaperGame errorMessage = new PieceOfPaperGame();
					Platform.runLater(errorMessage);
					root.getChildren().remove(gameMenuPane);
					gameMenuButton.setDisable(false);
					root.getChildren().add(createMenuPane);
					createMenuButton.setDisable(true);
					return;
				}
				
				if (currentGameLevel.equals("Easy")) {
					try {
						String creation = play.getCreationName();
						ProcessBuilder playAgain = new ProcessBuilder("/bin/bash", "-c", "ffplay -autoexit "
								+ "-x 400 -y 500 \"./creationsNoText/" + creation + ".mp4\"");
						Process pb;
						pb = playAgain.start();
						int exit = pb.waitFor();
						return;
					} catch (Exception e) {
					}
				} else if (currentGameLevel.equals("Medium")) {
					try {
						ProcessBuilder playAgain = new ProcessBuilder("/bin/bash", "-c", "ffplay -autoexit "
								+ "-x 400 -y 500 \"video.mp4\"");
						Process pb;
						pb = playAgain.start();
						int exit = pb.waitFor();
						return;
					} catch (Exception e) {
					}
				} else {
					try {
						ProcessBuilder playAgain = new ProcessBuilder("/bin/bash", "-c", "ffplay -autoexit "
								+ "-x 400 -y 500 \"audio.mp3\"");
						Process pb;
						pb = playAgain.start();
						int exit = pb.waitFor();
						return;
					} catch (Exception e) {
					}
				}
			}

		});

		//View Pane design
		Text viewMenuTitle = new Text("######View/Delete Existing Creations######");
		viewMenuTitle.setStyle("-fx-font: 18 arial;");
		viewMenuTitle.setFill(Color.GHOSTWHITE);
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
		TextField addBackgroundMus = new TextField("Enter name of creation to add music");
		viewMenuPane.add(addBackgroundMus, 0, 4, 1, 1);
		Button addButton = new Button("Add");
		addButton.setMaxWidth(100);
		viewMenuPane.add(addButton, 1, 4,1,1);
		
		//Adding Background music button function
		addButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				try {
					AddMusicToCreation.addMusic(addBackgroundMus);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		});
		
		
		//Default GUI configuration
		//When the program is launched, the default menu shown is the main menu
		root.setCenter(mainMenuPane);
		root.setBottom(menuSelectionButtons);
		root.setAlignment(menuSelectionButtons, Pos.CENTER);
		mainMenuButton.setDisable(true);

		//Menu Buttons Functions
		//Change to Game menu
		gameMenuButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				menuConfig("game");
			}

		});

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
				try {
					ViewCreations.displayExistingCreations(creationList, playButton, deleteButton);
					if ((playButton.isDisable() == true) && (deleteButton.isDisable() == true)) {
						addButton.setDisable(true);
						playTF.setText("There are Currently No Creations to Play!");
						deleteTF.setText("There are Currently No Creations to Delete!");
						addBackgroundMus.setText("There are Currently No Creation to Add Music");
					} else {
						addButton.setDisable(false);
						playTF.setText("Enter name of creation to play");
						deleteTF.setText("Enter name of creation to delete");
						addBackgroundMus.setText("Enter name of creation to add music");
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
							ProcessBuilder playCreation = new ProcessBuilder("/bin/bash", "-c", "ffplay -autoexit "
									+ "-x 400 -y 500 \"./creations/" + playTF.getText() + ".mp4\"");
							playCreation.start();
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
						alert2.getDialogPane().getStylesheets().add(Main.class.getResource("alert.css").toExternalForm());
						alert2.showAndWait();
					} 
				} catch (Exception e) {

				}
			}
		});
		//change to CreateAudio menu
		createAudioMenuButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) { menuConfig("createAudio"); 
			
			String cmd = "sed -i '/unnamedAudio/d' nameOfAudios.txt";
			ProcessBuilder extractAudioProcess = new ProcessBuilder("bash", "-c", cmd);
			try {
				extractAudioProcess.start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				// the next command for merging video and audio(merged) is -> ffmpeg -i creations/"+name+".mp4 -i creations/"+name+"_merged.wav -c:v copy -map 0:v:0 -map 1:a:0 -strict -2 creations/"+name+"_new.mp4
			}
			
			}
		});
	}

	//This method sets up the design of the menu pane when a menu is selected
	//If the user changes the menu before the creation has finished playing
	//This method also terminates the playing of the creation
	void menuConfig(String menu){
		if(menu.equals("create")){
			root.getChildren().remove(mainMenuPane);
			root.getChildren().remove(viewMenuPane);
			root.getChildren().remove(createAudioMenuPane);
			root.getChildren().remove(playCreationMenu);
			root.getChildren().remove(gameMenuPane);
			root.getChildren().add(createMenuPane);

			createMenuButton.setDisable(true);
			viewMenuButton.setDisable(false);
			mainMenuButton.setDisable(false);
			createAudioMenuButton.setDisable(false);
			gameMenuButton.setDisable(false);
		}else if(menu.equals("view")) {
			root.getChildren().remove(mainMenuPane);
			root.getChildren().remove(createMenuPane);
			root.getChildren().remove(createAudioMenuPane);
			root.getChildren().remove(playCreationMenu);
			root.getChildren().remove(gameMenuPane);
			root.getChildren().add(viewMenuPane);
			viewMenuButton.setDisable(true);
			createMenuButton.setDisable(false);
			mainMenuButton.setDisable(false);
			createAudioMenuButton.setDisable(false);
			gameMenuButton.setDisable(false);
		}else if(menu.equals("game")) {
			root.getChildren().remove(mainMenuPane);
			root.getChildren().remove(createMenuPane);
			root.getChildren().remove(createAudioMenuPane);
			root.getChildren().remove(playCreationMenu);
			root.getChildren().remove(viewMenuPane);
			root.getChildren().add(gameMenuPane);

			mainMenuButton.setDisable(false);
			createMenuButton.setDisable(false);
			viewMenuButton.setDisable(false);
			createAudioMenuButton.setDisable(false);
			gameMenuButton.setDisable(true);
		}else if (menu.equals("createAudio")){
			root.getChildren().add(createAudioMenuPane);
			root.getChildren().remove(gameMenuPane);
			root.getChildren().remove(mainMenuPane);
			root.getChildren().remove(createMenuPane);
			root.getChildren().remove(viewMenuPane);
			root.getChildren().remove(playCreationMenu);
			createAudioMenuButton.setDisable(true);
			viewMenuButton.setDisable(false);
			createMenuButton.setDisable(false);
			mainMenuButton.setDisable(false);
			gameMenuButton.setDisable(false);
		}else{
			root.getChildren().remove(createMenuPane);
			root.getChildren().remove(viewMenuPane);
			root.getChildren().remove(createAudioMenuPane);
			root.getChildren().remove(playCreationMenu);
			root.getChildren().remove(gameMenuPane);
			root.getChildren().add(mainMenuPane);
			createMenuButton.setDisable(false);
			viewMenuButton.setDisable(false);
			createAudioMenuButton.setDisable(false);
			gameMenuButton.setDisable(false);
			mainMenuButton.setDisable(true);
		}
	}

	public static void createFolder() {
		try {
			String key = "apiKey = a6c8799f554aa9e04a08158a015016e5\nsharedSecret = 2011587abd40f68a";
			ProcessBuilder newFolders = new ProcessBuilder("/bin/bash", "-c", "touch nameOfCreations.txt nameOfAudios.txt searchNames.txt;"
					+ "echo \"" + key + "\" > flickr-api-keys.txt;mkdir creations audioFiles creationsNoText");
			newFolders.start();
		} catch (Exception e) {

		}
	}

	public static void removeAudioAndVideoFile(){
		try {
			ProcessBuilder removeFiles = new ProcessBuilder("/bin/bash", "-c", "rm -f audio.mp3 video.mp4");
			Process pb = removeFiles.start();
			int exitStatus = pb.waitFor();
		} catch (Exception e) {

		}
	}

	public static void main(String[] args) {
		createFolder();
		launch(args);
	}
}