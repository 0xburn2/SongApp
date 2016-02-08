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
	
	@FXML private ListView<Song> listViewofSongs;
	@FXML private TextField songName;
	@FXML private TextField songArtist;
	@FXML private TextField songAlbum;
	@FXML private TextField songYear;
	
	List<Song> songArray = populateList();
	
	
	
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
	        listViewofSongs.setItems(FXCollections.observableList(songArray));
	 }
	


	
	
	public ArrayList<Song> populateList(){
		try {
			ArrayList<Song> test = Main.populateList();
			return test;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
		

	}
	
	public void addSong(Song song){
		songArray.add(song);
	}
}
