package Main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.awt.event.KeyEvent;


public class Controller {

    @FXML
    Button autoLevelButton = new Button();

    @FXML
    Button autoMLevelButton = new Button();

    @FXML
    Button addActionButton = new Button();

    @FXML
    Button deleteActionButton = new Button();

    @FXML
    TextField nameField = new TextField();

    @FXML
    TextField hpField = new TextField();

    @FXML
    TextField manaField = new TextField();

    @FXML
    TextField cdField = new TextField();

    @FXML
    ComboBox boolComboBox = new ComboBox();

    @FXML
    ComboBox typeComboBox = new ComboBox();

    @FXML
    ComboBox hotkeyComboBox = new ComboBox();

    @FXML
    TableView tabela = new TableView();

    @FXML
    public void initialize(){
        boolComboBox.getItems().addAll(
                "On",
                "Off"
        );
        typeComboBox.getItems().addAll(
                "None",
                "Heal",
                "Attack",
                "Support",
                "Potion"
        );
        hotkeyComboBox.getItems().addAll(
                "F1",
                "F2",
                "F3",
                "F4",
                "F5",
                "F6",
                "F7",
                "F8",
                "F9",
                "F10",
                "F11",
                "F12"

        );
    }

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

    public void addToTable(ActionEvent e){
        if(e.getSource()==addActionButton){
            Action akcja = new Action();
            akcja.type = typeComboBox.getValue().toString();
            akcja.name = nameField.getText();

            switch (hotkeyComboBox.getValue().toString()) {
                case "F1":
                    akcja.keyCode = KeyEvent.VK_F1;
                    break;
                case "F2":
                    akcja.keyCode = KeyEvent.VK_F2;
                    break;
                case "F3":
                    akcja.keyCode = KeyEvent.VK_F3;
                    break;
                case "F4":
                    akcja.keyCode = KeyEvent.VK_F4;
                    break;
                case "F5":
                    akcja.keyCode = KeyEvent.VK_F5;
                    break;
                case "F6":
                    akcja.keyCode = KeyEvent.VK_F6;
                    break;
                case "F7":
                    akcja.keyCode = KeyEvent.VK_F7;
                    break;
                case "F8":
                    akcja.keyCode = KeyEvent.VK_F8;
                    break;
                case "F9":
                    akcja.keyCode = KeyEvent.VK_F9;
                    break;
                case "F10":
                    akcja.keyCode = KeyEvent.VK_F10;
                    break;
                case "F11":
                    akcja.keyCode = KeyEvent.VK_F11;
                    break;
                case "F12":
                    akcja.keyCode = KeyEvent.VK_F12;
                    break;

            }
            akcja.maxHpToTrigger = Integer.parseInt(hpField.getText());
            akcja.maxManaToTrigger = Integer.parseInt(manaField.getText());
            akcja.coolDown = Integer.parseInt(cdField.getText());
            if(boolComboBox.getValue().equals("On"))
            {
                akcja.activated = true;
            }else{
                akcja.activated = false;
            }
            tabela.getItems().add(akcja);

            typeComboBox.setItems(null);
            nameField.clear();
            hotkeyComboBox.setItems(null);
            hpField.clear();
            manaField.clear();
            cdField.clear();
            boolComboBox.setItems(null);
        }
    }
}
