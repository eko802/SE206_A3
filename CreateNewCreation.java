package sample;

import javafx.application.Platform;

public class CreateNewCreation extends Thread{
	
	String task;
	Boolean merge;
	public CreateNewCreation(String task, Boolean merge) {
		this.task = task;
		this.merge = merge;
	}
	
	public void run() {
		try {
			AudioMergePaper paper = new AudioMergePaper(task, merge);
			Platform.runLater(paper);
		} catch (Exception e) {
			
		}
	}
}
