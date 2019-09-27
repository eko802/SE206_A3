package sample;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextInputDialog;

public class AudioMergePaper implements Runnable{
	String _task;
	String[] _audioFiles;
	
	public AudioMergePaper(String task) {
		this._task = task;
	}
	
	public AudioMergePaper(String task, String[] audioFiles) {
		this._task = task;
		this._audioFiles = audioFiles;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			if (_task.equals("ERROR")) {
				Alert invalidSearchAlert = new Alert(Alert.AlertType.ERROR);
				invalidSearchAlert.setTitle("Invalid Word");
				invalidSearchAlert.setContentText("Please try again");
				invalidSearchAlert.showAndWait();
				return;
			} else if (_task.equals("Ask user for creation name")) {
				TextInputDialog dialog = new TextInputDialog();
				dialog.setTitle("Name of creation");
				dialog.setHeaderText("Naming a new creation!");
				dialog.setContentText("Please enter the name of creation:");

				Optional<String> result = dialog.showAndWait();
				if (result.isPresent()){
					try {
						BufferedReader br = new BufferedReader(new FileReader("./nameOfCreations.txt"));
						String line;
						while ((line = br.readLine()) != null) {
							if ((line.equals(result.get())) || (result.get().equals("")))  {
								Alert invalidSearchAlert = new Alert(Alert.AlertType.ERROR);
								invalidSearchAlert.setTitle("Invalid Word");
								invalidSearchAlert.setContentText("Please enter another name");
								invalidSearchAlert.showAndWait();
								br.close();
								return;
							}
						}
						_task = "Ask user for number of images";
						br.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} 

			if (_task.equals("Ask user for number of images")) {
				List<String> choices = new ArrayList<>();
				for (int i = 1; i <= 10; i++) {
					choices.add("" + i);
				}

				ChoiceDialog<String> dialog = new ChoiceDialog<>("1", choices);
				dialog.setTitle("Creation Images");
				dialog.setHeaderText("Creation Images");
				dialog.setContentText("Choose the number of images for the creation:");

				Optional<String> result = dialog.showAndWait();
				if (result.isPresent()){
					// creating the creation
					 System.out.println("Your choice: " + result.get());
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}

	}
}
