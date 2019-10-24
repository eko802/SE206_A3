package sample;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import javax.imageio.ImageIO;
import com.flickr4java.flickr.*;
import com.flickr4java.flickr.photos.*;

import javafx.scene.control.Alert;

/*
 * This class retrieves the images specified by the user from the web site Flickr.
 * It ensures to get the number of images specified by the user (between and including 1-10 images).
 */
public class FlickrGetImages {
	String _image;
	int _numberOfImages;

	public FlickrGetImages(String image, int numberOfImages) {
		this._image = image;
		this._numberOfImages = numberOfImages;
	}

	// Get Flickr keys
	public static String getAPIKey(String key) throws Exception {
		String config = System.getProperty("user.dir") 
				+ System.getProperty("file.separator")+ "flickr-api-keys.txt"; 

		File file = new File(config); 
		BufferedReader br = new BufferedReader(new FileReader(file)); 

		String line;
		while ( (line = br.readLine()) != null ) {
			if (line.trim().startsWith(key)) {
				br.close();
				return line.substring(line.indexOf("=")+1).trim();
			}
		}
		br.close();
		throw new RuntimeException("Couldn't find " + key +" in config file "+file.getName());
	}

	public String retrieveImages() {
		try {

			if (_image.equals("")) {
				Alert invalidSearchAlert = new Alert(Alert.AlertType.ERROR);
				invalidSearchAlert.setTitle("Invalid Search");
				invalidSearchAlert.setContentText("Please try again");
				invalidSearchAlert.getDialogPane().getStylesheets().add(FlickrGetImages.class.getResource("alert.css").toExternalForm());
				invalidSearchAlert.showAndWait();
				return "ERROR";
			}
			String apiKey = getAPIKey("apiKey");
			String sharedSecret = getAPIKey("sharedSecret");

			Flickr flickr = new Flickr(apiKey, sharedSecret, new REST());

			String query = _image;
			int resultsPerPage = _numberOfImages;
			int page = 0;

			PhotosInterface photos = flickr.getPhotosInterface();
			SearchParameters params = new SearchParameters();
			params.setSort(SearchParameters.RELEVANCE);
			params.setMedia("photos"); 
			params.setText(query);

			PhotoList<Photo> results = photos.search(params, resultsPerPage, page);

			ProcessBuilder folderOfImages = new ProcessBuilder("bash", "-c", "if [ ! -e \"" + query + "\" ]; then mkdir \"" + query + 
					"\"; else rm -v ./" + query + "/* &> /dev/null;fi");
			folderOfImages.start();
			int num = 1;

			for (Photo photo: results) {
				try {
					BufferedImage image = photos.getImage(photo,Size.LARGE);
					String filename = query.trim().replace(' ', '-')+"-"+System.currentTimeMillis()+"-"+photo.getId()+".jpg";
					File outputfile = new File("./" + query,filename);
					ImageIO.write(image, "jpg", outputfile);
					ProcessBuilder changeRatio = new ProcessBuilder("/bin/bash", "-c", "ffmpeg -i ./" + query + "/" + filename + " -vf scale=800:200 ./" + query + "/output" + num + ".jpg;"
							+ "rm \"./" + query + "/" + filename + "\"");
					Process process = changeRatio.start();
					int exitStatus = process.waitFor();
					num++;
				} catch (FlickrException fe) {
					Alert invalidSearchAlert = new Alert(Alert.AlertType.ERROR);
					invalidSearchAlert.setTitle("Invalid Search");
					invalidSearchAlert.setContentText("Please try again");
					invalidSearchAlert.getDialogPane().getStylesheets().add(FlickrGetImages.class.getResource("alert.css").toExternalForm());
					invalidSearchAlert.showAndWait();
					return "ERROR";
				}
				
			}
			
			if (results.size() == 0) {
				Alert invalidSearchAlert = new Alert(Alert.AlertType.ERROR);
				invalidSearchAlert.setTitle("Invalid Search");
				invalidSearchAlert.setContentText("Please try again");
				invalidSearchAlert.getDialogPane().getStylesheets().add(FlickrGetImages.class.getResource("alert.css").toExternalForm());
				invalidSearchAlert.showAndWait();
				return "ERROR";
			} else {
				return "";
			}
		} catch (Exception e) {
			Alert invalidSearchAlert = new Alert(Alert.AlertType.ERROR);
			invalidSearchAlert.setTitle("Invalid Search");
			invalidSearchAlert.setContentText("Please try again");
			invalidSearchAlert.getDialogPane().getStylesheets().add(FlickrGetImages.class.getResource("alert.css").toExternalForm());
			invalidSearchAlert.showAndWait();
			return "ERROR";
		}
	}
}
