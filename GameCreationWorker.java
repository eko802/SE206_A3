package sample;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import javafx.application.Platform;

public class GameCreationWorker extends Thread{

	private String _currentLevel;
	private String gameAnswer = "";
	private String name = "";
	private Boolean noCreation = false;

	public GameCreationWorker(String currentLevel) {
		this._currentLevel = currentLevel;
	}

	public void run(){
		try {
			String numberOfLines = "";
			int numOfCreations;
			int randomCreation;
			
			// Get total Number of creations
			ProcessBuilder numberOfCreations = new ProcessBuilder("/bin/bash", "-c", "wc -l \"nameOfCreations.txt\" | awk \'{ print $1 }\'");
			Process process = numberOfCreations.start();
			BufferedReader stdout = new BufferedReader(new InputStreamReader(process.getInputStream()));
			int exitStatus = process.waitFor();
			if (exitStatus == 0) {
				numberOfLines = stdout.readLine();
			}
			
			numOfCreations = Integer.parseInt(numberOfLines);
			if (numOfCreations == 0) {
				// Alert user of error
				PieceOfPaperGame errorMessage = new PieceOfPaperGame();
				Platform.runLater(errorMessage);
				noCreation = true;
				return;
			} else {
				double randomDouble = Math.random();
				randomDouble = randomDouble * numOfCreations + 1;
				randomCreation = (int) randomDouble;
			}
			
			// Now creating creation based on difficulty and playing it to the user
			if (_currentLevel.equals("Easy")) {
				ProcessBuilder getCreation = new ProcessBuilder("/bin/bash", "-c", "sed \'" + randomCreation + "q;d\' \"nameOfCreations.txt\"");
				Process pb = getCreation.start();
				stdout = new BufferedReader(new InputStreamReader(pb.getInputStream()));
				exitStatus = pb.waitFor();
				if (exitStatus == 0) {
					ProcessBuilder answer = new ProcessBuilder("/bin/bash", "-c", "sed \'" + randomCreation + "q;d\' \"searchNames.txt\"");
					Process pb2 = answer.start();
					BufferedReader stdout2 = new BufferedReader(new InputStreamReader(pb2.getInputStream()));
					int wait = pb2.waitFor();
					gameAnswer = stdout2.readLine();
					
					name = stdout.readLine();
					ProcessBuilder playCreation = new ProcessBuilder("/bin/bash", "-c", "ffplay -autoexit -x 400 "
							+ "-y 500 \"./creationsNoText/" + name + ".mp4\"");
					pb = playCreation.start();
					exitStatus = pb.waitFor();
					return;
				}
				
			} else if (_currentLevel.contentEquals("Medium")) {
				ProcessBuilder getCreation = new ProcessBuilder("/bin/bash", "-c", "sed \'" + randomCreation + "q;d\' \"nameOfCreations.txt\"");
				Process pb = getCreation.start();
				stdout = new BufferedReader(new InputStreamReader(pb.getInputStream()));
				exitStatus = pb.waitFor();
				if (exitStatus == 0) {
					ProcessBuilder answer = new ProcessBuilder("/bin/bash", "-c", "sed \'" + randomCreation + "q;d\' \"searchNames.txt\"");
					Process pb2 = answer.start();
					BufferedReader stdout2 = new BufferedReader(new InputStreamReader(pb2.getInputStream()));
					int wait = pb2.waitFor();
					gameAnswer = stdout2.readLine();
					
					name = stdout.readLine();
					ProcessBuilder removeAudioAndPlay = new ProcessBuilder("/bin/bash", "-c", "ffmpeg -i \"./creationsNoText/" + name + ".mp4\" -vcodec copy -an \"video.mp4\";" + 
							"ffplay -autoexit -x 400 -y 500 \"video.mp4\"");
					pb = removeAudioAndPlay.start();
					exitStatus = pb.waitFor();
					return;
				}
			} else {
				ProcessBuilder getCreation = new ProcessBuilder("/bin/bash", "-c", "sed \'" + randomCreation + "q;d\' \"nameOfCreations.txt\"");
				Process pb = getCreation.start();
				stdout = new BufferedReader(new InputStreamReader(pb.getInputStream()));
				exitStatus = pb.waitFor();
				if (exitStatus == 0) {
					ProcessBuilder answer = new ProcessBuilder("/bin/bash", "-c", "sed \'" + randomCreation + "q;d\' \"searchNames.txt\"");
					Process pb2 = answer.start();
					BufferedReader stdout2 = new BufferedReader(new InputStreamReader(pb2.getInputStream()));
					int wait = pb2.waitFor();
					gameAnswer = stdout2.readLine();
					
					name = stdout.readLine();
					ProcessBuilder removeVideoAndPlay = new ProcessBuilder("/bin/bash", "-c", "ffmpeg -i \"./creationsNoText/" + name + ".mp4\" -f mp3 -ab 192000 -vn \"audio.mp3\";" + 
							"ffplay -autoexit -x 400 -y 500 \"audio.mp3\"");
					removeVideoAndPlay.start();
					return;
				}
			}
			
		} catch (Exception e) {

		}
	}
	
	public String getCreationName() {
		return this.name;
	}
	
	public Boolean getStatus() {
		return this.noCreation;
	}
	
	public String getAnswer() {
		return this.gameAnswer;
	}
}
