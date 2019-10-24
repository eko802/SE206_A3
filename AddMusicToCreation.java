package sample;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;

public class AddMusicToCreation {
//extract mp3 from video -> merge two mp3's -> replace audio of the original video
	public static void addMusic(TextField addTF) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("./nameOfCreations.txt"));
		try {
			String line;
		    while ((line = br.readLine()) != null) {
		    	if (line.equals(addTF.getText())) {
		    		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
	                alert.setTitle("Add background music?");
	                alert.setContentText("Please confirm adding background music");
	                alert.getDialogPane().getStylesheets().add(AddMusicToCreation.class.getResource("alert.css").toExternalForm());
	                Optional<ButtonType> result = alert.showAndWait();
	                if (result.get() == ButtonType.CANCEL){
	                	br.close();
	                    return;
	                }else if (result.get() == ButtonType.OK) {
	                	//extract audio from video
	                	extractAudio(addTF.getText());
	                	//merge audio + background
//	                	mergeTwoAudio(addTF.getText());
	                	//mp3 to wav
//	                	mp3ToWav(addTF.getText());
	                	
	                	String cmd = " commandadsdasdasdasdasdasdasd!!!!!!!!!!!!!!!!!";
	                	ProcessBuilder addMusicToCreation = new ProcessBuilder("bash", "-c", "");
	                	addMusicToCreation.start();
	        			Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                        alert2.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                        alert2.setTitle("Background music added");
                        alert2.setContentText("Background music is successfuly added to the selected creation");
                        alert2.getDialogPane().getStylesheets().add(AddMusicToCreation.class.getResource("alert.css").toExternalForm());
                        alert2.showAndWait();
                        return;
	                }
		    	}
		    }
		    Alert alert2 = new Alert(Alert.AlertType.ERROR);
            alert2.setTitle("CREATION NOT DELETED");
            alert2.setContentText("Creation not found. Try different name.");
            alert2.getDialogPane().getStylesheets().add(AddMusicToCreation.class.getResource("alert.css").toExternalForm());
            alert2.showAndWait();
            return;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			br.close();
		}
	}
	
	public static void extractAudio(String name) {
		String renameCmd = "rm creations/"+name+".mp4; mv creations/"+name+"_merged.mp4 creations/"+name+".mp4; rm creations/"+name+"_merged.wav";
		String replaceAudioCmd = "ffmpeg -i creations/"+name+".mp4 -i creations/"+name+"_merged.wav -c:v copy -map 0:v:0 -map 1:a:0 -strict -2 creations/"+name+"_merged.mp4;";
		String cmd = "ffmpeg -i creations/"+name+".mp4 creations/"+name+".mp3; ffmpeg -i creations/"+name+".mp3 -i background_music.mp3 -filter_complex amix=inputs=2:duration=first:dropout_transition=2 creations/"+name+"_merged.mp3; ffmpeg -i creations/"+name+"_merged.mp3 creations/"+name+"_merged.wav; rm creations/*.mp3;"+replaceAudioCmd + renameCmd;
		ProcessBuilder extractAudioProcess = new ProcessBuilder("bash", "-c", cmd);
		try {
			extractAudioProcess.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// the next command for merging video and audio(merged) is -> ffmpeg -i creations/"+name+".mp4 -i creations/"+name+"_merged.wav -c:v copy -map 0:v:0 -map 1:a:0 -strict -2 creations/"+name+"_new.mp4
		}
	}

	
}
