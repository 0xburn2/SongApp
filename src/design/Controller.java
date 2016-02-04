package design;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class Controller {

	@FXML private Button buttontest;
	
	@FXML public void createDialog(ActionEvent event){
		System.out.println("Button works");
	}
	
}
