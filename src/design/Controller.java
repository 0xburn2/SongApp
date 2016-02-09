package design;
import application.Main;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class Controller implements Initializable {
	
	
	/*
	 * Initialize all of the Scene objects
	 */
	@FXML private ListView<Song> listViewofSongs;
	@FXML private TextField songName;
	@FXML private TextField songArtist;
	@FXML private TextField songAlbum;
	@FXML private TextField songYear;
	
	
	/*
	 * Calls the Populate List method from inside Controller.java, which runs the populateList method from Main.java.
	The Main.java method call needed to be inside a method for some reason otherwise it'd throw an error.
	*
	*/
	List<Song> songArray = populateListc();
	
	
	
	
	/*
	 * Necessary in order to view the items in songList. Sets the ArrayList to an observable list that can be seen by ListView.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
	        listViewofSongs.setItems(FXCollections.observableList(songArray));
	 }
	


	
	/*
	 * Calls populateList method from Main.java
	 * Try catch is necessary just in case of a FileNotFoundException
	 */
	public ArrayList<Song> populateListc(){
		try {
			ArrayList<Song> test = Main.populateList();
			return test;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
		

	}
	

}
