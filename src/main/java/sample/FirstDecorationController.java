package sample;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class FirstDecorationController  {
    public static ArrayList<Service> services = new ArrayList<Service>();
    @FXML Button buttonMarkov;
    @FXML Button buttonAdd, buttonDel;
    @FXML GridPane gridPane;
    //Добавление поля для ввода временного ограничения
    @FXML
    private void addRow(){
        int indexRow = gridPane.getRowIndex(buttonAdd);
        Label l = new Label("M"+(indexRow+1));
        l.setId("M"+(indexRow+1));
        TextField t = new TextField();
        t.setId("T"+(indexRow+1));
        gridPane.add(l, 0, indexRow);
        gridPane.add(t, 1, indexRow);
        buttonDel.setVisible(true);
        gridPane.getChildren().remove(buttonAdd);
        gridPane.add(buttonAdd, 2, indexRow+1);
        gridPane.getChildren().remove(buttonDel);
        gridPane.add(buttonDel, 2, indexRow);
    }
    //Удаление поля с временным ограничением
    @FXML
    private void deleteRow(){
        int indexRow = gridPane.getRowIndex(buttonDel);
        int to = gridPane.getChildren().size();
        int from = to - 4;
        gridPane.getChildren().remove(from, to);
        gridPane.add(buttonDel, 2, indexRow-1);
        gridPane.add(buttonAdd, 2, indexRow);
        if(to == 6){
            buttonDel.setVisible(false);
        }
    }
    //Запись значений временных ограничений и переход ко второму //окну приложения
    @FXML
    private void timeLimit()throws IOException {
        if (validate()){
            int timeLimit[] = new int[gridPane.getRowIndex(buttonAdd)];
            ObservableList<Node> childrens = gridPane.getChildren();
            TextField result = null;
            int i = 0;
            for(Node node:childrens){
                if (gridPane.getColumnIndex(node)!=null&&gridPane.getColumnIndex(node)==1){
                    result = (TextField) node;
                    String t = result.getText();
                    timeLimit[i] = Integer.valueOf(t);
                    i++;
                }
            }
            for (int j = 0; j < timeLimit.length; j++){
            //    System.out.println(timeLimit.length);
                FirstDecorationController.services.add(new Service(timeLimit[j]));
                System.out.println(services.size());

            }
            FXMLLoader loader = new FXMLLoader(getClass().getResource("secondDecorationController.fxml"));
            Parent root = loader.load();
            SecondDecorationController controller = loader.getController();; //получаем контроллер для второй формы
            controller.showGridPane(); // передаем необходимые параметры
            Stage stage = new Stage();
            stage.setTitle("My New Stage Title");
            stage.setScene(new Scene(root, 500, 500));
            stage.show();
        }
    }
    //Проверка вводимых значений
    private boolean validate(){
        ObservableList<Node> childrens = gridPane.getChildren();
        TextField result = null;
        for(Node node:childrens){
            if (gridPane.getColumnIndex(node)!=null&&gridPane.getColumnIndex(node)==1){
                int res;
                result = (TextField) node;
                String t = result.getText();
                try {
                    res = Integer.valueOf(t);
                }catch(Exception e){

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information");
                    alert.setContentText("Ошибка");
                    alert.showAndWait();
                    return false;
                }
                if (res <= 0){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information");
                    alert.setContentText("Введены неправильные параметры");
                    alert.showAndWait();

                    return false;
                }
            }
        }
        return true;
    }

    public void switchToMarkov(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("marcovChainController.fxml"));
        Parent root = loader.load();
        MarcovChainController controller = loader.getController();; //получаем контроллер для второй формы
        controller.showGridPane(); // передаем необходимые параметры
        Stage stage = new Stage();
        stage.setTitle("My New Stage Title");
        stage.setScene(new Scene(root, 800, 500));
        stage.show();
    }
}
