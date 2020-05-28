package sample;

import javafx.collections.ObservableList;
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
import java.math.BigDecimal;

public class SecondDecorationController {
    @FXML
    GridPane gridPane;


//    Button buttonAdd = new Button();
//    Button buttonDel = new Button();




//Вывод полей для ввода значений
    public void showGridPane() {
        for (int i = 1; i < FirstDecorationController.services.size(); i++) {
            Label label = new Label();
            label.setId("LableF" + i);
            label.setText("F" + (i + 1) + "[k=1]");
            gridPane.addColumn((i * 2), label);
            TextField text = new TextField();
            text.setId("F" + i);
            gridPane.addColumn((i * 2 + 1), text);
        }

        Button buttonAdd = new Button();
        buttonAdd.setId("add");
        buttonAdd.setText("Добавить");
        gridPane.add(buttonAdd, FirstDecorationController.services.size() * 2 + 1, 1);
        buttonAdd.setOnAction((btn) -> addRow());

        Button buttonDel = new Button();
        buttonDel.setId("del");
        buttonDel.setText("Удалить");
        gridPane.add(buttonDel, FirstDecorationController.services.size() * 2 + 1, 0);
        buttonDel.setVisible(false);
        buttonDel.setOnAction((btn) -> deleteRow());
    }

    //Добавление дополнительных полей для ввода значений
    @FXML
    private void addRow() {
        Button buttonAdd = new Button();
        Button buttonDel = new Button();

        ObservableList<Node> childrens = gridPane.getChildren();

        for (Node node : childrens) {
            if (node.getId() == "add") {
                buttonAdd = (Button) node;
            }
            if (node.getId() == "del") {
                buttonDel = (Button) node;
            }
        }

        int indexRow = gridPane.getRowIndex(buttonAdd);
        int indexCol = gridPane.getColumnIndex(buttonAdd);

        for (int i = 0; i < indexCol / 2; i++) {
            Label l = new Label();
            l.setId("LableF" + i);
            l.setText("F" + (i + 1) + "[k=" + (indexRow + 1) + "]");

            TextField t = new TextField();
            t.setId("F" + i);

            gridPane.add(l, i * 2, indexRow);
            gridPane.add(t, (i * 2 + 1), indexRow);
        }

        buttonDel.setVisible(true);
        gridPane.getChildren().remove(buttonAdd);
        gridPane.add(buttonAdd, indexCol, indexRow + 1);
        gridPane.getChildren().remove(buttonDel);
        gridPane.add(buttonDel, indexCol, indexRow);
    }

    //Удаление строк
    @FXML
    private void deleteRow() {
        Button buttonAdd = new Button();
        Button buttonDel = new Button();

        ObservableList<Node> childrens = gridPane.getChildren();
        for (Node node : childrens) {
            if (node.getId() == "add") {
                buttonAdd = (Button) node;
            }
            if (node.getId() == "del") {
                buttonDel = (Button) node;
            }
        }

        int indexRow = gridPane.getRowIndex(buttonAdd);
        int indexCol = gridPane.getColumnIndex(buttonAdd);

        int to = gridPane.getChildren().size();
        int from = to - indexCol - 1;

        gridPane.getChildren().remove(from, to);
        gridPane.add(buttonDel, indexCol, indexRow - 2);
        gridPane.add(buttonAdd, indexCol, indexRow - 1);
        if (gridPane.getChildren().size() - 1 == indexCol) {
            buttonDel.setVisible(false);
        }
    }

    @FXML
    private void probabilityDistribution() throws IOException{
        if(validate()){
            int sizeServices = FirstDecorationController.services.size();
            int indexRow = (gridPane.getChildren().size() - 2) / (sizeServices * 2);

            for (int i = 0; i < sizeServices; i++) {
                double f[] = new double[indexRow];
                for (int j = 0; j < indexRow; j++) {
                    ObservableList<Node> childrens = gridPane.getChildren();

                    for (Node node : childrens) {
                        if (gridPane.getColumnIndex(node) != null && gridPane.getColumnIndex(node) == i*2+1 && (gridPane.getRowIndex(node) == null || gridPane.getRowIndex(node) == j)) {
                            TextField result = (TextField) node;
                            try {
                                f[j] = Double.valueOf(result.getText());
                            }catch (Exception e){
                            }
                        }
                    }
                }
                FirstDecorationController.services.get(i).setF(f);
            }
            if(sumProbabilityDensity()){
               // this.mainApp.setThirdDecoration();
                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("thirdDecorationController.fxml"));
                Parent root = loader.load();
                ThirdDecorationController controller = loader.getController();; //получаем контроллер для второй формы
                controller.showResult(); // передаем необходимые параметры
                Stage stage = new Stage();
                stage.setTitle("My New Stage Title");
                stage.setScene(new Scene(root, 500, 500));
                stage.show();
            }else{
            }
        }
    }

    private boolean sumProbabilityDensity(){
        for(int i = 0; i < FirstDecorationController.services.size(); i++){
            double tempSum = 0;
            double f[] = FirstDecorationController.services.get(i).getF();
            for(int j = 0; j <f.length; j++){
                tempSum += f[j];
            }
            tempSum = new BigDecimal(tempSum).setScale(8, BigDecimal.ROUND_HALF_UP).doubleValue();
            if(tempSum != 1){
               // mainApp.alert("Неверный формат параметров", "Ошибка ввода данных!", "Сумма плотностей распределения вероятностей для каждого сервиса должна быть равной единеце");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information");
                alert.setContentText("Сумма плотностей распределения вероятностей для каждого сервиса должна быть равной единеце");
                alert.showAndWait();
                return false;
            }
        }
        return true;
    }

    //Проверка введённых значений
    private boolean validate(){
        ObservableList<Node> children = gridPane.getChildren();
        TextField result = null;

        for(Node node:children){

            if (node!=null && node instanceof TextField){
                result = (TextField) node;
                String t = result.getText();
                try {
                    Double.valueOf(t);
                }catch(Exception e){

                 //   mainApp.alert("Неверный формат параметров", "Ошибка ввода данных!", "Введите значения плотностей распределения вероятностей для каждого момента времени");
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information");
                    alert.setContentText("Введите значения плотностей распределения вероятностей для каждого момента времени");
                    alert.showAndWait();
                    return false;
                }
            }
        }
        return true;
    }

    //Возврат к предыдущему окну приложения
    @FXML
    private void back()throws IOException{
        FirstDecorationController.services.clear();
        //this.mainApp.setFirstDecoration();
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("firstDecorationController.fxml"));
        Parent root = loader.load();
       // FirstDecorationController controller = loader.getController();; //получаем контроллер для второй формы
        Stage stage = new Stage();
        stage.setTitle("My New Stage Title");
        stage.setScene(new Scene(root, 500, 500));
        stage.show();
    }

}




