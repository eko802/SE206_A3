package sample;

import javafx.scene.control.Alert;

/*
 * Alerting the user if no creations exists so the game cannot be played.
 */

public class PieceOfPaperGame implements Runnable{

	@Override
	public void run() {
		Alert invalidSearchAlert = new Alert(Alert.AlertType.ERROR);
		invalidSearchAlert.setTitle("No creations exist");
		invalidSearchAlert.setContentText("Please create a new creation\nBefore playing Game");
		invalidSearchAlert.getDialogPane().getStylesheets().add(PieceOfPaperGame.class.getResource("alert.css").toExternalForm());
		invalidSearchAlert.showAndWait();
		return;
	}

}
