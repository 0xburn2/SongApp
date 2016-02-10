package application;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import design.Song;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;


public class SongLib extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/design/Style.fxml"));
			Scene scene = new Scene(root,700,500);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("Song App 1.0");
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	
	/*
	 * Populates the ArrayList from the songsList text file, then sorts it alphabetically.
	 */
	public static ArrayList<design.Song> populateList() throws FileNotFoundException{
		
		ArrayList<Song> songList = new ArrayList<>();
		Scanner sc = new Scanner(new File("./src/application/songsList.txt"));
		
		
		
		while(sc.hasNext()){
			//Reads the name, artist, album, and year one line at a time and creates a Song object with the information
			
			Song song1 = new Song(sc.nextLine(), sc.nextLine(), sc.nextLine(), sc.nextLine());
			songList.add(song1); 
		}
		sc.close();
		
		//Sort the ArrayList of Songs
		Collections.sort(songList, new Comparator<Song>(){
		    public int compare(Song s1, Song s2) {
		        return s1.getName().compareToIgnoreCase(s2.getName());
		    }
		});
		
		//Return the songList
		return songList;
		
		
	}
}
