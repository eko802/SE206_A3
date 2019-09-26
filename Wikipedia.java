package sample;

import javafx.scene.control.Alert;

public class Wikipedia {
	//When invalid input is searched, alert error message
	public static void alertInvalidSearch(){
		Alert invalidSearchAlert = new Alert(Alert.AlertType.ERROR);
		invalidSearchAlert.setTitle("Invalid Search");
		invalidSearchAlert.setContentText("Please try again");
		invalidSearchAlert.showAndWait();
		return;
	}
	
	
	
}
