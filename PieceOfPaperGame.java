package sample;

import javafx.scene.control.Alert;

public class PieceOfPaperGame implements Runnable{

	@Override
	public void run() {
		Alert invalidSearchAlert = new Alert(Alert.AlertType.ERROR);
		invalidSearchAlert.setTitle("No creations exist");
		invalidSearchAlert.setContentText("Please create a new creation\nBefore playing Game");
		invalidSearchAlert.showAndWait();
		return;
	}

}
