package Main;

import Actions.Action;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

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
    TableView<Action> tabela;

    @FXML
    TableColumn<Action, Action.ActionType> typeColumn;

    @FXML
    TableColumn<Action, String> nameColumn;

    @FXML
    TableColumn<Action, String> hotkeyColumn;

    @FXML
    TableColumn<Action, Integer> maxHpColumn;

    @FXML
    TableColumn<Action, Integer> maxManaColumn;

    @FXML
    TableColumn<Action, Integer> coolDownColumn;

    @FXML
    TableColumn<Action, Boolean> activatedColumn;

    @FXML
    public void initialize(){
        typeColumn.setCellValueFactory(new PropertyValueFactory<Action, Action.ActionType>("actionType"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<Action, String>("name"));
        hotkeyColumn.setCellValueFactory(new PropertyValueFactory<Action, String>("hotKey"));
        maxHpColumn.setCellValueFactory(new PropertyValueFactory<Action, Integer>("maxHpToTrigger"));
        maxManaColumn.setCellValueFactory(new PropertyValueFactory<Action, Integer>("maxManaToTrigger"));
        coolDownColumn.setCellValueFactory(new PropertyValueFactory<Action, Integer>("coolDown"));
        activatedColumn.setCellValueFactory(new PropertyValueFactory<Action, Boolean>("activated"));
        tabela.setItems(getActions());
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

    public ObservableList<Action> getActions()
    {
        ObservableList<Action> actions = FXCollections.observableArrayList();
        for (Action action: Main.actionList) {
            actions.add(action);
        }
        return actions;
    }

    public void addToTable(ActionEvent e){
        if(e.getSource()==addActionButton){
            Action.ActionType actionType = Action.ActionType.none;
            String actionTypeName = typeComboBox.getValue().toString();
            switch (actionTypeName)
            {
                case "Heal" : actionType = Action.ActionType.heal; break;
                case "Attack" : actionType = Action.ActionType.attack; break;
                case "Support" : actionType = Action.ActionType.support; break;
                case "Potion" : actionType = Action.ActionType.potion; break;
                case "None" : break;
            }
            String actionName = nameField.getText();
            if (actionName.length()==0)
                actionName = "Action "+Integer.toString(Main.actionList.size());

            String hotKey = hotkeyComboBox.getValue().toString();

            int maxHpToTrigger, maxManaToTrigger, coolDown;
            maxHpToTrigger = Integer.parseInt(hpField.getText());
            maxManaToTrigger = Integer.parseInt(manaField.getText());
            coolDown = Integer.parseInt(cdField.getText());
            boolean activated;
            if(boolComboBox.getValue().equals("On"))
            {
                activated = true;
            }else{
                activated = false;
            }
            Action akcja = new Action(actionName, hotKey, maxHpToTrigger, maxManaToTrigger, actionType, coolDown, activated);
            Main.actionList.add(akcja);
            tabela.setItems(getActions());
            nameField.clear();
            hpField.clear();
            manaField.clear();
            cdField.clear();
        }
    }

    public void clickRow(){
        Action akcja1 = tabela.getSelectionModel().getSelectedItem();
        nameField.setText(akcja1.getName());
        hpField.setText(Integer.toString(akcja1.getMaxHpToTrigger()));
        manaField.setText(Integer.toString(akcja1.getMaxManaToTrigger()));
        cdField.setText(Integer.toString(akcja1.getCoolDown()));
        typeComboBox.setValue(akcja1.getActionType());
        hotkeyComboBox.setValue(akcja1.getHotKey());
        if(akcja1.isActivated()==true){
            boolComboBox.setValue("On");
        }else{
            boolComboBox.setValue("Off");
        }
    }

    public void deleteSelectedRow(ActionEvent e){
        if(e.getSource()==deleteActionButton){
            Action akcja2 = tabela.getSelectionModel().getSelectedItem();
            tabela.getItems().remove(akcja2);
            Main.actionList.remove(akcja2);
        }
    }
}
