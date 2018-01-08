package Main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.event.EventHandler;


public class Controller {

    @FXML
    Button autoLevelButton = new Button();
    @FXML
    Button autoMLevelButton = new Button();


    public void handle(ActionEvent e){
        if(e.getSource()==autoLevelButton){
            if(autoLevelButton.getText().equals("Activate")) {
                Main.canWalk = true;
                Main.canHunt = true;
                autoLevelButton.setText("Desactivate");
            }else{
                Main.canWalk = false;
                Main.canHunt = false;
                autoLevelButton.setText("Activate");
            }
        }
    }

    public void handleMLeveling(ActionEvent e){
        if(e.getSource()==autoMLevelButton){
            if(autoMLevelButton.getText().equals("Activate")) {
                Main.magicLevel = true;
                Main.canAntyKick = true;
                autoMLevelButton.setText("Desactivate");
            }else{
                Main.magicLevel = false;
                Main.canAntyKick = false;
                autoMLevelButton.setText("Activate");
            }
        }
    }
}
