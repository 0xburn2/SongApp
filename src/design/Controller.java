package design;

import application.SongLib;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

/*
 * TO DO:
 1. FINISHED - Catch the error that occurs if somebody enters a non-integer value in the Year field
 2. Auto-select the newly added song
 3. FINISH - Before adding a song, check to see if a Song with that same name and artist already exists and refuse the add if it does
 4. Make the album and year fields optional for adding/editing
 5. Fix the error in console that occurs when adding (but for some reason doesn't interfere otherwise)
 6. Add Edit functionality
 7. Add Delete Functionality
 */
public class Controller implements Initializable {

    Charset utf8 = StandardCharsets.UTF_8;

    /*
     * Initialize all of the Scene objects
     */
    @FXML
    private ListView<Song> listViewofSongs;
    @FXML
    private TextField songName;
    @FXML
    private TextField songArtist;
    @FXML
    private TextField songAlbum;
    @FXML
    private TextField songYear;

    @FXML
    private void addSong(ActionEvent event) {

        ArrayList<Song> songArray = populateListc();
        
        // Printing all songs for testing
        printSongs(songArray);

        String name = songName.getText();
        String artist = songArtist.getText();
        String album = songAlbum.getText();
        String year = songYear.getText();

        int yearString = 0;
        // If the year field is not blank
        if (!(year.equals(""))) {
            System.out.println("testing year");
            // Check for proper int input
            try {
                yearString = Integer.parseInt(year);
                if (!(yearString > -1 && yearString < 2017)) {
                    songYear.setText("INVALID YEAR!!");
                    return;
                }
                year = Integer.toString(yearString);
            } catch (NumberFormatException e) {
                songYear.setText("INVALID YEAR!!");
                return;
            }
        } else {
            year = "N/A";
        }

        //Check if song is already on the list
        System.out.println("testing for repeats");
        if (findSong(songArray, name, artist)) {
            songName.setText(name + " <-Song is Already on List");
            return;
        }

        //Writes the four current textfields to new lines in songsList.txt in order to be added to the ArrayList
        try {
            FileWriter out = new FileWriter("./src/application/songsList.txt", true);
            out.write(name + "\n" + artist + "\n" + album + "\n" + year + "\n");
//            out.write(artist + "\n");
//            out.write(album + "\n");
//            out.write(year + "\n");
            out.flush();
            out.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        //Re-populate the arrayList with the new Song
        songArray = populateListc();
        System.out.println("check point");

        //Refresh the List View display
        listViewofSongs.setItems(FXCollections.observableList(songArray));

    }
    
    @FXML
    private void editSong(ActionEvent event) {
        ArrayList<Song> songArray = populateListc();
               
    }
    
    @FXML
    private void deleteSong(ActionEvent event) {
        
        ArrayList<Song> songArray = populateListc();
        String name = songName.getText();
        String artist = songArtist.getText();
        String album = songAlbum.getText();
        String year = songYear.getText();
        
        // Delete the song if it is on the list
        for(int j = 0; j < songArray.size(); j++)
        {
            Song song = songArray.get(j);

            if(song.getName().equals(name) && song.getArtist().equals(artist)){
               //found, delete.
                songArray.remove(j);
                break;
            }
        }
        System.out.println("finish delete");
//        for (Song findSong : songArray) {
//            System.out.print(findSong.getName() + ", ");
//            System.out.println(findSong.getArtist());
//            if (findSong.getName().equals(name) && findSong.getArtist().equals(artist)) {
//                System.out.println("trying to delete song");
//                songArray.remove(songArray.indexOf(findSong));
//            }
//            else{
//                songName.setText(name + " <-Song is not on the list");
//            }
//        }
        // Write everything to txt file; overwrite everything
        System.out.println("writing everything to file");
        try {
            FileWriter out = new FileWriter("./src/application/songsList.txt", false);
            for (Song findSong : songArray) {
                System.out.print(findSong.getName() + ", ");
                System.out.println(findSong.getArtist());
                out.write(findSong.getName() + "\n" + findSong.getArtist() + "\n" 
                        + findSong.getAlbum() + "\n" + findSong.getYear() + "\n");
            }
            out.flush();
            out.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        
                listViewofSongs.setItems(FXCollections.observableList(songArray));

    }

    /*
     * Initial list population
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

        //Updates the textfields with the currently selected Song
        listViewofSongs.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue == null){
                System.out.println("newvalue is null");
                return;
            }
            songName.setText(newValue.getName());
            songArtist.setText(newValue.getArtist());
            songAlbum.setText(newValue.getAlbum());
            songYear.setText(newValue.getYear());
            System.out.println("Currently selected song: " + newValue.getName() + " by " + newValue.getArtist() + ".");
        });
    }

    /*
     * Calls populateList method from Main.java
     * Try catch is necessary just in case of a FileNotFoundException
     */
    public ArrayList<Song> populateListc() {
        try {
            ArrayList<Song> test = SongLib.populateList();
            return test;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Used to find if song is already on the list
    private boolean findSong(ArrayList<Song> songs, String name, String artist) {
        for (Song findSong : songs) {
            System.out.print(findSong.getName() + ", ");
            System.out.println(findSong.getArtist());
            if (findSong.getName().equals(name) && findSong.getArtist().equals(artist)) {
                return true;
            }
        }
        return false;
    }
    
    //Print all songs from songArray; for testing only
    private void printSongs(ArrayList<Song> songs) {
        for (Song findSong : songs) {
            System.out.print(findSong.getName());
            System.out.println(findSong.getArtist());
            System.out.println("---------------");
        }
    }
}
