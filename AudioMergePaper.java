package sample;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextInputDialog;

/*
 * This class is the runnable class that creates a new creation whether it is the merging of audio files
 * or a searched term from Wikipedia
 */


public class AudioMergePaper implements Runnable{
	private String _task;
	private String[] _audioFiles;
	private String exitType;
	private String _creationName;
	private Boolean merge = true;
	private String images = "";

	public AudioMergePaper(String task) {
		this._task = task;
	}

	public AudioMergePaper(String task, String[] audioFiles) {
		this._task = task;
		this._audioFiles = audioFiles;
	}
	
	public AudioMergePaper(String task, Boolean merge) {
		this._task = task;
		this.merge = merge;
	}
	@Override
	public void run() {
		// Asking user for details regarding the creating of a new creation
		try {
			if (_task.equals("ERROR")) {
				Alert invalidSearchAlert = new Alert(Alert.AlertType.ERROR);
				invalidSearchAlert.setTitle("Invalid Word");
				invalidSearchAlert.setContentText("Please try again");
				invalidSearchAlert.getDialogPane().getStylesheets().add(AudioMergePaper.class.getResource("alert.css").toExternalForm());
				invalidSearchAlert.showAndWait();
				return;
			} else if (_task.equals("Ask user for creation name")) {
				TextInputDialog dialog = new TextInputDialog();
				dialog.setTitle("Name of creation");
				dialog.setHeaderText("Naming a new creation!");
				dialog.setContentText("Please enter the name of creation:");
				dialog.getDialogPane().getStylesheets().add(AudioMergePaper.class.getResource("alert.css").toExternalForm());

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
								invalidSearchAlert.getDialogPane().getStylesheets().add(AudioMergePaper.class.getResource("alert.css").toExternalForm());
								invalidSearchAlert.showAndWait();
								br.close();
								return;
							}
						}
						_creationName = result.get();
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
				dialog.getDialogPane().getStylesheets().add(AudioMergePaper.class.getResource("alert.css").toExternalForm());

				Optional<String> result = dialog.showAndWait();
				if (result.isPresent()){
					// creating the creation
					TextInputDialog dialog2 = new TextInputDialog();
					dialog2.setTitle("Images of creation");
					dialog2.setHeaderText("Images of creation");
					dialog2.setContentText("Please enter the images you would like for the creation:");
					dialog2.getDialogPane().getStylesheets().add(AudioMergePaper.class.getResource("alert.css").toExternalForm());
					Optional<String> result2 = dialog2.showAndWait();
					if (result2.isPresent()){
						images = result2.get();
						FlickrGetImages test = new FlickrGetImages(result2.get(), Integer.parseInt(result.get()));
						exitType = test.retrieveImages();
					}

					if (exitType.equals("ERROR")) {
						return;
					} else if (merge == true){
						String audioFiles;
						String pattern = "\'[0:0]";
						if (_audioFiles.length ==  1) {
							audioFiles = "-i ./audioFiles/" + _audioFiles[0] + ".wave ";
						} else {
							audioFiles = "-i ./audioFiles/" + _audioFiles[0] + ".wave ";
						}

						// Merging audio Files
						for (int i = 1; i < _audioFiles.length; i++) {
							audioFiles = audioFiles + "-i ./audioFiles/" + _audioFiles[i] + ".wave ";
							pattern = pattern + "[" + i + ":0]";
						}
						ProcessBuilder mergeAudio = new ProcessBuilder("/bin/bash", "-c", "ffmpeg " + audioFiles 
								+ "-filter_complex " + pattern + "concat=n=" + _audioFiles.length + ":v=0:a=1[out]\' -map \'[out]\' output.wav;");
						Process audioFileMerge = mergeAudio.start();
						int wait = audioFileMerge.waitFor();


						ProcessBuilder numberOfImagesFrameRate = new ProcessBuilder("/bin/bash", "-c", "ls ./" + result2.get());
						Process process = numberOfImagesFrameRate.start();
						BufferedReader stdout = new BufferedReader(new InputStreamReader(process.getInputStream()));

						// Calculate Number of images
						int numberOfImages = 0;
						String line;
						while ((line = stdout.readLine()) != null) {
							numberOfImages++;
						}

						mergeAudioNewCreation(numberOfImages, result2);
					} else {
						ProcessBuilder numberOfImagesFrameRate = new ProcessBuilder("/bin/bash", "-c", "ls ./" + result2.get());
						Process process = numberOfImagesFrameRate.start();
						BufferedReader stdout = new BufferedReader(new InputStreamReader(process.getInputStream()));

						// Calculate Number of images
						int numberOfImages = 0;
						String line;
						while ((line = stdout.readLine()) != null) {
							numberOfImages++;
						}

						waveAudio(numberOfImages, result2);
					}

				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * The following method is created of the merging of audio files decided by the user
	 * and then used to create a new creation where the images come from Flickr
	 * @param numberOfImages
	 * @param result2
	 */
	public void mergeAudioNewCreation(int numberOfImages, Optional<String> result2) {
		// Getting duration of audio File
		try {
			ProcessBuilder timeOfAudio = new ProcessBuilder("/bin/bash", "-c", "echo `soxi -D output.wav`");
			Process process2 = timeOfAudio.start();
			int exitAudio = process2.waitFor();
			BufferedReader stdout2 = new BufferedReader(new InputStreamReader(process2.getInputStream()));
			String duration = stdout2.readLine();
			double durationOfAudio = Double.parseDouble(duration);
			double framerate = (double)(numberOfImages / durationOfAudio);

			String command = "cat ./" + result2.get() + "/* | ffmpeg -framerate " + framerate + " -f image2pipe -i - output.mp4;"
					+ "ffmpeg -i output.mp4 -i output.wav -c:v copy -c:a aac -strict experimental " + _creationName + ".mp4"; 

			ProcessBuilder creatingCreation = new ProcessBuilder("/bin/bash", "-c", command);
			Process pb1 = creatingCreation.start();
			int exit1 = pb1.waitFor();

			ProcessBuilder removeAndMoveFiles = new ProcessBuilder("/bin/bash", "-c", "mv \"" + _creationName + ".mp4\" \"./creations\""
					+ ";rm \"output.wav\" \"output.mp4\";echo \"" + _creationName + "\" >> \"nameOfCreations.txt\";"
							+ "cp \"./creations/" + _creationName + ".mp4\" \"./creationsNoText\"");
			Process pb2 = removeAndMoveFiles.start();
			int exit2 = pb2.waitFor();
			
			
			ProcessBuilder addTextToVideo = new ProcessBuilder("/bin/bash", "-c", "ffmpeg -i ./creations/" + _creationName + ".mp4 "
					+ "-vf drawtext=\"fontfile=/path/to/font.ttf: text=\'" + images + "\': fontcolor=white: fontsize=18: box=1: boxcolor=black@0.5:"
					+ "boxborderw=5: x=(w-text_w)/2: y=(h-text_h)/2\" -codec:a copy ./creations/output.mp4;rm \"./creations/" + _creationName + ".mp4\";"
					+ "mv ./creations/output.mp4 ./creations/" + _creationName + ".mp4;"
							+ "echo \"" + images + "\" >> \"searchNames.txt\"");
			Process pb3 = addTextToVideo.start();
			int exit3 = pb3.waitFor();
			Alert invalidSearchAlert = new Alert(Alert.AlertType.INFORMATION);
			invalidSearchAlert.setTitle("Complete");
			invalidSearchAlert.setContentText("Creation of merging audioFiles is successfull!!!");
			invalidSearchAlert.getDialogPane().getStylesheets().add(AudioMergePaper.class.getResource("alert.css").toExternalForm());
			invalidSearchAlert.showAndWait();
			return;
		} catch (Exception e) {

		}
	}
	
	/**
	 * However, this method is creating a new creation based on one term searched on Wikipedia by the user, rather
	 * than merging existing audio files
	 * @param numberOfImages
	 * @param result2
	 */
	public void waveAudio(int numberOfImages, Optional<String> result2) {
		try {
			ProcessBuilder timeOfAudio = new ProcessBuilder("/bin/bash", "-c", "echo `soxi -D ./audioFiles/unnamedAudio.wave`");
			Process process2 = timeOfAudio.start();
			int exitAudio = process2.waitFor();
			BufferedReader stdout2 = new BufferedReader(new InputStreamReader(process2.getInputStream()));
			String duration = stdout2.readLine();
			double durationOfAudio = Double.parseDouble(duration);
			double framerate = (double)(numberOfImages / durationOfAudio);

			String command = "cat ./" + result2.get() + "/* | ffmpeg -framerate " + framerate + " -f image2pipe -i - output.mp4;"
					+ "ffmpeg -i output.mp4 -i ./audioFiles/unnamedAudio.wave -c:v copy -c:a aac -strict experimental " + _creationName + ".mp4"; 

			ProcessBuilder creatingCreation = new ProcessBuilder("/bin/bash", "-c", command);
			Process pb1 = creatingCreation.start();
			int exit1 = pb1.waitFor();

			ProcessBuilder removeAndMoveFiles = new ProcessBuilder("/bin/bash", "-c", "mv \"" + _creationName + ".mp4\" \"./creations\""
					+ ";rm \"./audioFiles/unnamedAudio.wave\" \"output.mp4\";echo \"" + _creationName + "\" >> \"nameOfCreations.txt\";"
					+ "cp \"./creations/" + _creationName + ".mp4\" \"./creationsNoText\"");
			Process pb2 = removeAndMoveFiles.start();
			int exit2 = pb2.waitFor();
			
			
			ProcessBuilder addTextToVideo = new ProcessBuilder("/bin/bash", "-c", "ffmpeg -i ./creations/" + _creationName + ".mp4 "
					+ "-vf drawtext=\"fontfile=/path/to/font.ttf: text=\'" + Main.searched + "\': fontcolor=white: fontsize=18: box=1: boxcolor=black@0.5:"
					+ "boxborderw=5: x=(w-text_w)/2: y=(h-text_h)/2\" -codec:a copy ./creations/output.mp4;rm \"./creations/" + _creationName + ".mp4\";"
					+ "mv ./creations/output.mp4 ./creations/" + _creationName + ".mp4;"
							+ "echo \"" + Main.searched + "\" >> \"searchNames.txt\"");
			Process pb3 = addTextToVideo.start();
			int exit3 = pb3.waitFor();
			Alert invalidSearchAlert = new Alert(Alert.AlertType.INFORMATION);
			invalidSearchAlert.setTitle("Complete");
			invalidSearchAlert.setContentText("Making of Creation is successfull!!!");
			invalidSearchAlert.getDialogPane().getStylesheets().add(AudioMergePaper.class.getResource("alert.css").toExternalForm());
			invalidSearchAlert.showAndWait();
			return;
		} catch (Exception e) {

		}
	}
}
