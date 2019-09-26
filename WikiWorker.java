package sample;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

public class WikiWorker extends Thread{

	String search;
	TextArea wikitTextArea;
	
	public WikiWorker(String search, TextArea wikitTextArea) {
		this.search = search;
		this.wikitTextArea = wikitTextArea;
	}
	
	public void run(){ 
		try {
			ProcessBuilder pb = new ProcessBuilder("/bin/bash", "-c", "wikit " + search + "| sed 's/[.] /&\\n/g' > \"" + search+ ".txt\"; cat "+search+ ".txt");
			Process process = pb.start();
			
			InputStream stdout = process.getInputStream();
			BufferedReader stdoutBuffered = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line;
			while((line = stdoutBuffered.readLine()) != null) {
				//show result in the text area 
				//When not found, alert error
				WikiPaper paper = new WikiPaper(search, line, wikitTextArea);
				Platform.runLater(paper);	
			}
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}

	}
	
}
