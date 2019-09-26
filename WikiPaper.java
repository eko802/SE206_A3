package sample;

import javafx.scene.control.TextArea;

public class WikiPaper implements Runnable {

	String search;
	String line;
	TextArea wikitTextArea;
	public WikiPaper(String search, String line, TextArea wikitTextArea) {
		this.search = search;
		this.line = line;
		this.wikitTextArea = wikitTextArea;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			if(line.equals(search + " not found :^(")) {
				//remove the txt file created
				ProcessBuilder pb2 = new ProcessBuilder("/bin/bash", "-c", "rm " + search + ".txt");
				Process process2 = pb2.start();
				
				Wikipedia.alertInvalidSearch();
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		wikitTextArea.appendText(line + "\n");
	}

}
