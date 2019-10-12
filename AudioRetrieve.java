package sample;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextInputDialog;

public class AudioRetrieve {

	//This method returns string value of the selected voice
	static String chooseSynthSetting() {
		List<String> choices = new ArrayList<>();
		//List of available voices
		choices.add("kal_diphone");
		choices.add("akl_nz_jdt_diphone");
		choices.add("akl_nz_cw_cg_cg");
		//Ask user input for the voice
		ChoiceDialog<String> dialog = new ChoiceDialog<>("kal_diphone", choices);
		dialog.setTitle("Select Synthesizer Setting");
		dialog.setHeaderText("Select Synthesizer Setting [Voice type]");
		dialog.setContentText("Choose the type of voice: ");

		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()){
			// receiving the user input
			return result.get();
		}else {
			return "cancelled";
		}
	}

	//Show error when text is not selected from the wikit Text Area
	static void showError() {
		Alert highlightError = new Alert(Alert.AlertType.ERROR);
		highlightError.setTitle("Please select text");
		highlightError.setContentText("Please highlight text.");
		highlightError.showAndWait();
		return;
	}

	//Show error when word count > 20
	static void showWCError() {
		Alert highlightError = new Alert(Alert.AlertType.ERROR);
		highlightError.setTitle("Please select less number of words");
		highlightError.setContentText("Number of words cannot exceed 20.");
		highlightError.showAndWait();
		return;
	}


	static void saveTextToAudio(String text, boolean preview, boolean popup) throws Exception {
		int wc = text.split(" ").length;

		//If word count exceeds max, show error
		if(wc > 20) {
			showWCError();
			throw new Exception();
		}

		//When cancel  button is clicked, do nothing
		String voice = chooseSynthSetting();
		if(voice == null) {
			return;
		}
		if(voice.equals("cancelled")) {
			throw new Exception();
		}

		//replace newline with a space
		text = text.replace("\n", " ");

		//Distinguish whether the button pressed is preview audio or create audio
		if(preview == true) {
			previewAudio(text, voice);
		}else {
			saveAudio(text, voice, popup);
		}



	}


	static void saveAudio(String text, String voice, boolean popup) {

		String audioName = "unnamedAudio";


		//Recieve name of the audio from the user
		if(popup == true) {
			TextInputDialog dialog = new TextInputDialog();
			dialog.setTitle("Name of the audio");
			dialog.setHeaderText("Naming a new audio!");
			dialog.setContentText("Please enter the name of the audio:");

			Optional<String> result = dialog.showAndWait();
			if (result.isPresent()){
				audioName = result.get();
			}else {
				return;
			}
		}
		makeAudioCreationSCM(text, voice, audioName);
		String cmd = "festival -b createTemp.scm";
		try {
			ProcessBuilder pb = new ProcessBuilder("/bin/bash", "-c", cmd);
			Process process = pb.start();

		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	static void previewAudio(String text, String voice) {

		makeSCM(text, voice);

		String cmd = "festival -b temp.scm";
		try {
			ProcessBuilder pb = new ProcessBuilder("/bin/bash", "-c", cmd);
			Process process = pb.start();

		}catch(Exception e) {
			e.printStackTrace();
		}


	}

	//make scm for creation of audio file
	static void makeAudioCreationSCM(String text, String voice, String audioName) {
		String cmd = "printf \"(voice_"+voice+")\n(utt.save.wave (SayText \\\""+text+"\\\") \\\""+"./audioFiles/"+audioName+".wave\\\" 'riff)\" > createTemp.scm";
		try {
			ProcessBuilder pb = new ProcessBuilder("/bin/bash", "-c", cmd);
			Process process = pb.start();
		}catch(Exception e) {
			e.printStackTrace();
		}
		//add the audio name into the audio list
		String cmd2 = "echo "+audioName+" >> nameOfAudios.txt";
		try {
			ProcessBuilder pb = new ProcessBuilder("/bin/bash", "-c", cmd2);
			Process process = pb.start();
		}catch(Exception e) {
			e.printStackTrace();
		}

		Alert audioSaved = new Alert(Alert.AlertType.INFORMATION);
		audioSaved.setTitle("Audio Successfully saved");
		audioSaved.setContentText(audioName + " is successfully saved. The audio will play");
		audioSaved.showAndWait();
		return;

	}

	//make scm for preview audio
	static void makeSCM(String text, String voice) {
		String cmd = "printf \"" + "(voice_"+voice+")\n" + "(SayText \\\"" + text + "\\\")\" > temp.scm";
		try {
			ProcessBuilder pb = new ProcessBuilder("/bin/bash", "-c", cmd);
			Process process = pb.start();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}


}
