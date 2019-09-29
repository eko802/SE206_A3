package sample;


import javafx.application.Platform;
import javafx.scene.control.TextArea;

public class MergeAudioFilesWorker extends Thread{
	TextArea _audioToCreationList;
	
	public MergeAudioFilesWorker(TextArea audioToCreationList) {
		this._audioToCreationList = audioToCreationList;
	}
	
	public void run(){ 
		try {
				
			String userResponse = _audioToCreationList.getText();
			
			// If the user inputs nothing error is given to user
			if (userResponse.equals("")) {
				String task = "ERROR";
				AudioMergePaper paper = new AudioMergePaper(task);
				Platform.runLater(paper);
				return;
			}
			
			String[] audioNames = userResponse.split("\n");
			
			// Making sure audio Files are valid
			for (String audioFile : audioNames) {
				ProcessBuilder pb = new ProcessBuilder("/bin/bash", "-c", "[ -e \"./audioFiles/" + audioFile + ".wave\" ]");
				Process process = pb.start();
				int exitStatus = process.waitFor();
				
				if (exitStatus == 1) {
					String task = "ERROR";
					AudioMergePaper paper = new AudioMergePaper(task);
					Platform.runLater(paper);
					return;
				}
			}
			
			String task = "Ask user for creation name";
			AudioMergePaper paper = new AudioMergePaper(task, audioNames);
			Platform.runLater(paper);
		}catch(Exception e) {
			e.printStackTrace();
		}

	}
}
