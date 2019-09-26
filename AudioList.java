package sample;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

public class AudioList {
	//This method prints out saved audio files onto a textArea
	public static void displayExistingAudioList(TextArea audioList) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("./nameOfAudios.txt"));
		String audioNames = "";
		try {
			int i = 1;
		    String line;
		    while ((line = br.readLine()) != null) {
		       audioNames = audioNames + i + ": " + line + "\n";
		       i++;
		    }
		    
		    // In this case there are no audio files in nameOfAudio.txt file
		    if (audioNames.equals("")) {
		    	audioList.setText("There are Currently no existing audios!");
		    } else {
		    	audioList.setText(audioNames);
		    }
		} catch (Exception e) {
			
		}	finally {
		    br.close();
		}
	}
}
