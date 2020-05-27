package sample;

import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.layout.GridPane

class MarcovChainController {
//    private val pMatrix = Double[4][4]()
    @FXML
    var gridPane: GridPane? = null
    fun showGridPane() {
        for (i in 1..4) {
            for (j in 1..4) {
                val label = Label()
                label.id = "LableP$i"
                label.text = "P" + (i + 1) + "[k=1]"

                gridPane!!.addColumn(i * 2, label)
                val text = TextField()
                text.id = "F$i"
                gridPane!!.addColumn(i * 2 + 1, text)
            }
        }
        val buttonAdd = Button()
        buttonAdd.id = "add"
        buttonAdd.text = "Добавить"
        gridPane!!.add(buttonAdd, 5, 5)
    }

    fun loadMatrix(){

    }
}