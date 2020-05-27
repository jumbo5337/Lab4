package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.fxml.Initializable;

import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javax.swing.JTable;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ThirdDecorationController {
    @FXML
    GridPane gridPane;    //Вывод полей для ввода значений
    @FXML
    AnchorPane schedule;
    @FXML
    GridPane inputInfo;

    //Формирования массива сервисов
    private Service[] modifiedListServices(){
        Service arrayServices[] = new Service[FirstDecorationController.services.size()];
        for(int i = 0; i < arrayServices.length; i++){
            arrayServices[i] = FirstDecorationController.services.get(i);
        }
        return arrayServices;
    }

    //Формирование массива значений результатов для логики //объединения согласно функции «И»
    private double[] andLogic(Service service[]){
        double andLogic[] = new double[FirstDecorationController.services.get(0).getF().length];

        for (int i = 0; i < andLogic.length; i++){
            andLogic[i] = AndLogic.andLogic(service,i);
        }
        return andLogic;
    }

    //Формирование массива значений результатов для логики //объединения согласно функции «ИЛИ»
    private double[] orLogic(Service service[]){
        double orLogic[] = new double[FirstDecorationController.services.get(0).getF().length];

        for (int i = 0; i < orLogic.length; i++){
            orLogic[i] = OrLogic.orLogic(service, i);
        }
        return orLogic;
    }
    //Формирование массива значений результатов для темпоральной //логики объединения
    private double[] tempLogic(Service service[]){
        double tempLogic[] = new double[FirstDecorationController.services.get(0).getF().length];

        for (int i = 0; i < tempLogic.length; i++){
            tempLogic[i] = TemporalLogic.temporalLogic(service, i);
        }
        return tempLogic;
    }
    @FXML
    //Установка и заполнение таблицы результатов
    private void setGridPane(double array[], int indexColum) {
        for(int i = 0; i < array.length; i++){
            Label l = new Label();
            l.setId("L"+i);
            l.setText(Double.toString(array[i]));
            gridPane.add(l, indexColum, i+1);
        }
        Label l = new Label();
        l.setText("---");
        gridPane.add(l, indexColum, array.length+2);
        Label eX = new Label();
        eX.setText(Double.toString(ExpectedValue.expectedValue(array)));
        gridPane.add(eX, indexColum, array.length+3);
        Label d = new Label();
        d.setText(Double.toString(ExpectedValue.dispersion(array)));
        gridPane.add(d, indexColum, array.length+4);

    }
    @FXML
    //Представление результатов в виде графика
    private void showSchedule(double sAnd[], double sOr[], double sTemp[]){
        int k = sAnd.length;
        NumberAxis xAxis = new NumberAxis(1, k, 1);
        xAxis.setLabel("k");
        NumberAxis yAxis = new NumberAxis(0,1,0.2);
        yAxis.setLabel("f[k]");
        LineChart lineChart = new LineChart(xAxis,yAxis);
        XYChart.Series series[] = new XYChart.Series[3];
        series[0] = new XYChart.Series();
        series[0].setName("AND");
        series[1] = new XYChart.Series();
        series[1].setName("OR");
        series[2] = new XYChart.Series();
        series[2].setName("TEMP");
        for(int i = 0; i < sAnd.length; i++){
            series[0].getData().add(new XYChart.Data(i+1, sAnd[i]));
            series[1].getData().add(new XYChart.Data(i+1, sOr[i]));
            series[2].getData().add(new XYChart.Data(i+1, sTemp[i]));
        }

        lineChart.getData().addAll(series);
        schedule.getChildren().setAll(lineChart);
    }
    //Представление таблицы исходных параметров
    private void showInputInfo(){
        Service service[] = modifiedListServices();
        for(int i = 0; i < service.length; i++){

            Label labelM = new Label();
            labelM.setText("M");
            inputInfo.addColumn(i+1,labelM);

            for(int j = 0; j < service[i].getF().length; j++){
                if(i == 0){
                    Label k = new Label();
                    k.setText("k = "+(1+j));
                    inputInfo.add(k,0,1+j);

                    Label l = new Label();
                    l.setText("Mi");
                    inputInfo.add(l,0,0);
                }
                Label labelK =new Label();
                labelK.setText(Double.toString(service[i].getF()[j]));
                inputInfo.add(labelK,i+1,1+j);
            }
        }
    }

    // Переключение на результат в виде графика
    @FXML
    private void visibleSchedule(){
        gridPane.setVisible(false);
        schedule.setVisible(true);
        inputInfo.setVisible(false);
    }
    //Переключение на результат в виде таблицы
    @FXML
    private void visibleResult(){
        gridPane.setVisible(true);
        schedule.setVisible(false);
        inputInfo.setVisible(false);
    }
    //Переключение на исходные значения
    @FXML
    private void visibleInput(){
        gridPane.setVisible(false);
        schedule.setVisible(false);
        inputInfo.setVisible(true);
    }
    //Вывод результатов в табличном виде
    @FXML
    public void showResult(){
        Service service[] = modifiedListServices();
        double andLogic[] = andLogic(service);
        setGridPane(andLogic, 0);
        double orLogic[] = orLogic(service);
        setGridPane(orLogic,1);
        double tempLogic[] = tempLogic(service);
        setGridPane(tempLogic,2);
        showSchedule(andLogic,orLogic,tempLogic);
        showInputInfo();
        inputInfo.setVisible(false);
        schedule.setVisible(false);
    }
}
