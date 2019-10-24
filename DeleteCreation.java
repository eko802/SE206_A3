package sample;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;

/**
 * This class deletes an existing creation based on what the user decides to delete
 * This class makes sure that the creation being deleted exists, and prompts the user 
 * to confirm before deleting the creation
 *
 */

public class DeleteCreation {
	public static void deleteExistingCreation(TextField deleteTF) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader("./nameOfCreations.txt"));
		
		try {
			String line;
			int count = 1;
		    while ((line = br.readLine()) != null) {
		    	if (line.equals(deleteTF.getText())) {
		    		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
	                alert.setTitle("Are you sure?");
	                alert.setContentText("Do you really want to delete it?");
	                alert.getDialogPane().getStylesheets().add(DeleteCreation.class.getResource("alert.css").toExternalForm());
	                Optional<ButtonType> result = alert.showAndWait();
	                if (result.get() == ButtonType.CANCEL){
	                	br.close();
	                    return;
	                }else if (result.get() == ButtonType.OK) {
	                	ProcessBuilder deleteCreation = new ProcessBuilder("bash", "-c", "rm ./creations/" + deleteTF.getText() + ".mp4;"
	        					+ "sed -i -e \"/\\(" + deleteTF.getText() + "\\)\\b/d\" nameOfCreations.txt;"
	        							+ "rm \"./creationsNoText/" + deleteTF.getText() + ".mp4\";"
	        									+ "sed -i \'" + count + "d\' \"searchNames.txt\"");
	        			deleteCreation.start();
	        			Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                        alert2.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                        alert2.setTitle("CREATION DELETED");
                        alert2.setContentText("Creation is successfully deleted. Please click [Update list] to remove the deleted creation on the list.");
                        alert2.getDialogPane().getStylesheets().add(DeleteCreation.class.getResource("alert.css").toExternalForm());
                        alert2.showAndWait();
                        return;
	                }
		    	}
		    	count++;
		    }
		    Alert alert2 = new Alert(Alert.AlertType.ERROR);
            alert2.setTitle("CREATION NOT DELETED");
            alert2.setContentText("Creation not found. Try different name.");
            alert2.getDialogPane().getStylesheets().add(DeleteCreation.class.getResource("alert.css").toExternalForm());
            alert2.showAndWait();
            return;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			br.close();
		}
	}
}
