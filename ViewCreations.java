package sample;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

public class ViewCreations {
	
	public static void displayExistingCreations(TextArea creationList, Button Play, Button Delete) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("./nameOfCreations.txt"));
		String creationNames = "";
		try {
			int i = 1;
		    String line;
		    while ((line = br.readLine()) != null) {
		       creationNames = creationNames + i + ": " + line + "\n";
		       i++;
		    }
		    
		    // In this case there are no creations in the nameOfCreations file
		    if (creationNames.equals("")) {
		    	creationList.setText("There are Currently no existing Creations!");
		    	Play.setDisable(true);
		    	Delete.setDisable(true);
		    } else {
		    	creationList.setText(creationNames);
		    	Play.setDisable(false);
		    	Delete.setDisable(false);
		    }
		} catch (Exception e) {
			
		}	finally {
		    br.close();
		}
	}
}
