package design;

import application.SongLib;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

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
    
    /*
     * Initial list population
     * Calls the Populate List method from inside Controller.java, which runs the populateList method from Main.java.
     The Main.java method call needed to be inside a method for some reason otherwise it'd throw an error.
     *
     */
    List<Song> songArray = populateListc();
    
    ////

    @FXML
    private void addSong(ActionEvent event) {

    	songAdd();
       
    }
    
    public void songAdd(){
    	 ArrayList<Song> songArray = populateListc();

         String name = songName.getText();
         String artist = songArtist.getText();
         String album = songAlbum.getText();
         String year = songYear.getText();

         int yearString = 0;
         // If the year field is not blank
         if (!(year.equals(""))) {
             // Check for proper int input
             try {
                 yearString = Integer.parseInt(year);
                 if (!(yearString > -1 && yearString < 2017)) {
                	 Alert alert = new Alert(AlertType.INFORMATION);
                     alert.setTitle("ERROR");
                     alert.setHeaderText(null);
                     alert.setContentText("Invalid year. Please enter a positive integer value.");
                     alert.showAndWait();
                     return;
                 }
                 year = Integer.toString(yearString);
             } catch (NumberFormatException e) {
            	 Alert alert = new Alert(AlertType.INFORMATION);
                 alert.setTitle("ERROR");
                 alert.setHeaderText(null);
                 alert.setContentText("Invalid year. Please enter a positive integer value.");
                 alert.showAndWait();
                 return;
             }
         } else {
        	 Alert alert = new Alert(AlertType.INFORMATION);
             alert.setTitle("ERROR");
             alert.setHeaderText(null);
             alert.setContentText("Invalid year. Please enter a positive integer value.");
             alert.showAndWait();
             return;
         }

         if (name.equals("") || artist.equals("")) {
        	 Alert alert = new Alert(AlertType.INFORMATION);
             alert.setTitle("ERROR");
             alert.setHeaderText(null);
             alert.setContentText("Name and artist fields are required");
             alert.showAndWait();
             return;
         }

         //Check if song is already on the list
         if (findSong(songArray, name, artist)) {
             
             Alert alert = new Alert(AlertType.INFORMATION);
             alert.setTitle("ERROR");
             alert.setHeaderText(null);
             alert.setContentText("The song " + name + " is already on the list");
             alert.showAndWait();
             return;
         }

         //Writes the four current textfields to new lines in songsList.txt in order to be added to the ArrayList
         try {
             FileWriter out = new FileWriter("./src/application/songsList.txt", true);
             out.write(name + "\n" + artist + "\n" + album + "\n" + year + "\n");
             out.flush();
             out.close();

         } catch (IOException e) {
             e.printStackTrace();
         }

         //Re-populate the arrayList with the new Song
         songArray = populateListc();

         //Refresh the List View display
         listViewofSongs.setItems(FXCollections.observableList(songArray));

         //Select newly added song in ListView
         for (int i = 0; i < songArray.size(); i++) {
             Song song = songArray.get(i);
             if (song.getName().equals(songName.getText())) {
                 listViewofSongs.scrollTo(i);
                 listViewofSongs.getSelectionModel().select(i);
                 return;
             }
         }
    }

    @FXML
    private void editSong(ActionEvent event) {
        ArrayList<Song> songArray = populateListc();
      
            songArray.remove(listViewofSongs.getSelectionModel().getSelectedIndex());
            try {
                FileWriter out = new FileWriter("./src/application/songsList.txt", false);
                for (Song findSong : songArray) {
                    out.write(findSong.getName() + "\n" + findSong.getArtist() + "\n"
                            + findSong.getAlbum() + "\n" + findSong.getYear() + "\n");
                }
                out.flush();
                out.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
            listViewofSongs.setItems(FXCollections.observableList(songArray));
            songAdd();
    
    }

    @FXML
    private void deleteSong(ActionEvent event) {

        ArrayList<Song> songArray = populateListc();
        String name = songName.getText();
        String artist = songArtist.getText();

        // Delete the song if it is on the list
        int newSelectLocation = 0;
        for (int j = 0; j < songArray.size(); j++) {

            Song song = songArray.get(j);

            if (song.getName().equals(name) && song.getArtist().equals(artist)) {
                //found, delete.
                //If it is deleting at the last location
                if (j == songArray.size() - 1) {
                    newSelectLocation = j - 1;
                } else {
                    newSelectLocation = j;
                }
                songArray.remove(j);
                break;
            }
        }

        // Write everything to txt file; overwrite everything

        try {
            FileWriter out = new FileWriter("./src/application/songsList.txt", false);
            for (Song findSong : songArray) {
                out.write(findSong.getName() + "\n" + findSong.getArtist() + "\n"
                        + findSong.getAlbum() + "\n" + findSong.getYear() + "\n");
            }
            out.flush();
            out.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        listViewofSongs.setItems(FXCollections.observableList(songArray));
        if (songArray.size() > 0) {
            listViewofSongs.scrollTo(newSelectLocation);
            listViewofSongs.getSelectionModel().select(newSelectLocation);
        }
    }


    /*
     * Necessary in order to view the items in songList. Sets the ArrayList to an observable list that can be seen by ListView.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listViewofSongs.setItems(FXCollections.observableList(songArray));

        //Updates the textfields with the currently selected Song
        listViewofSongs.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                return;
            }
            songName.setText(newValue.getName());
            songArtist.setText(newValue.getArtist());
            songAlbum.setText(newValue.getAlbum());
            songYear.setText(newValue.getYear());
        });

        listViewofSongs.getSelectionModel().select(0);
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
            if (findSong.getName().equals(name) && findSong.getArtist().equals(artist)) {
                return true;
            }
        }
        return false;
    }

    /*Print all songs from songArray; for testing only
    private void printSongs(ArrayList<Song> songs) {
        for (Song findSong : songs) {
            System.out.print(findSong.getName());
            System.out.println(findSong.getArtist());
            System.out.println("---------------");
        }
    }*/

}
