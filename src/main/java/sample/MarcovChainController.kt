package sample;

import javafx.fxml.FXML
import javafx.geometry.Pos
import javafx.scene.Node
import javafx.scene.chart.LineChart
import javafx.scene.chart.NumberAxis
import javafx.scene.chart.XYChart
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.layout.GridPane
import org.jetbrains.numkt.array
import org.jetbrains.numkt.copy
import org.jetbrains.numkt.core.ExperimentalNumkt
import org.jetbrains.numkt.core.KtNDArray
import org.jetbrains.numkt.core.dot
import org.jetbrains.numkt.eye
import org.jetbrains.numkt.linalg.Linalg.Companion.inv
import org.jetbrains.numkt.linalg.Linalg.Companion.matrixPower
import org.jetbrains.numkt.math.minus
import org.jetbrains.numkt.math.power
import org.jetbrains.numkt.math.sum
import org.jetbrains.numkt.math.times
import org.jetbrains.numkt.ones
import java.lang.Math.*

@ExperimentalNumkt
class MarcovChainController {

    @FXML
    var gridPane: GridPane? = null
    val matrixSize = 5

    @FXML
    lateinit var title: Label
    val inputNodes = mutableListOf<Node>()

    fun showGridPane() {
        for (i in 1..matrixSize) {
            for (j in 1..matrixSize) {
                val label = Label()
                label.id = "LableP$i"
                label.text = "P${j},${i}"
                gridPane!!.addColumn(i * 2, label)
                val text = TextField()
                text.id = "P$i,$j"
                text.setMaxWidth(75.0)
                gridPane!!.addColumn(i * 2 + 1, text)
                inputNodes.add(label)
                inputNodes.add(text)

            }
        }
        val buttonAdd = Button()
        buttonAdd.id = "add"
        buttonAdd.text = "Добавить"
        buttonAdd.setOnAction { actionEvent ->
            val matrix = loadMatrix()
            val results = calculusMarcovChain(matrix)
            drawResults(results)
        }
        gridPane!!.add(buttonAdd, 5, 5)
        inputNodes.add(buttonAdd)
    }

    private fun drawResults(results: Results) {
        inputNodes.forEach { it.visibleProperty().set(false) }
        title.setText("Результаты")
        val k = results.probs.size.toDouble()
        val xAxis = NumberAxis(1.0, k, 1.0)
        xAxis.label = "k"
        val y = results.probs.max()
        val yAxis = NumberAxis(0.0, y!!, 0.2)
        val chart = LineChart(xAxis, yAxis)
        val probSeries = XYChart.Series<Number, Number>()
        var i = 0
        results.probs.forEach {
            probSeries.data.add(
                    XYChart.Data(i++, it)
            )
        }
        gridPane!!.alignment = Pos.BASELINE_LEFT

        chart.data.add(probSeries)
        val label = Label()
        label.id = "chart"
        label.text = "Probability distribution density of discrete time"
        gridPane!!.addRow(1,label)
        gridPane!!.addRow(2,chart)
        val title = Label()
        title.id = "outTitle"
        title.text = "Динамические характеристики качества от 1го до последнего состояния: \n" +
                "- Кол-во допустимых шагов: ${results.steps} \n" +
                "- Матожидание: ${results.eN} \n" +
                "- Дисперсия: ${results.dN} \n"
        gridPane!!.addRow(3, title)


    }

    private fun loadMatrix(): MutableList<MutableList<Double>> {
        val nodes = gridPane!!.children
        val rawMatrix = mutableListOf<Double>()
        for (node in nodes) {
            if (node is TextField) {
                try {
                    rawMatrix.add(node.getText().toDouble())
                } catch (exc: Exception) {
                    val alert = Alert(Alert.AlertType.ERROR)
                    alert.title = "Неправильный ввод данных"
                    alert.contentText = "Уточните ввод"
                    alert.showAndWait()
                    break
                }
            }
        }
        if (rawMatrix.size != pow(matrixSize.toDouble(), 2.0).toInt()) {
            val alert = Alert(Alert.AlertType.ERROR)
            alert.title = "Неправильный ввод данных"
            alert.contentText = "пропущена ячейка"
            alert.showAndWait()
        }
        var doubleMatrix = Array(matrixSize) { DoubleArray(matrixSize) }
        var column = 0
        var row = 0
        rawMatrix.forEach {
            doubleMatrix[column][row] = it
            column++
            if (column > matrixSize - 1) {
                column = 0
                row++
            }
        }
        doubleMatrix.forEach { println(it) }
        val doubleList = doubleMatrix.toMutableList().map { it.toMutableList() }
        checkMatrix(doubleList.toMutableList())
        return doubleList.toMutableList()
    }

    private fun checkMatrix(matrix: MutableList<MutableList<Double>>) {
        for (i in 0..matrixSize - 1) {
            val sum = matrix[i].sum()
            if (sum != 1.0) {
                val alert = Alert(Alert.AlertType.ERROR)
                alert.title = "Неправильный ввод данных"
                alert.contentText = "Сумма вероятностей должна ровняться 1, в строке ${i+1}"
                alert.showAndWait()
                throw RuntimeException()
            }
        }
    }

    private fun calculusMarcovChain(matrix: MutableList<MutableList<Double>>): Results {
        val pMatrix = array<Double>(matrix)

        var pQMatrix = array<Double>(matrix.subList(0, matrixSize - 1).map { it.subList(0, matrixSize - 1) })
        val i = eye<Int>(matrixSize - 1, matrixSize - 1)
        val tMatrix = inv(i.minus(pQMatrix))
        val oVector = ones<Int>(1, matrixSize - 1)
        val tVector = oVector.dot(tMatrix)
        val tZero = tVector[0][0]
        val t0 = copy(tMatrix)
        val tStarArr = power(tMatrix, 2)
        for (i in (0..1)) {
            for (j in (0..1)) {
                if (i != j) {
                    t0.set(intArrayOf(i, j), 0.0)
                }

            }
        }

        val dMatrix = tMatrix.dot(2 * t0 - i).minus(tStarArr)
        val tStar = power(tVector, 2)
        val d = tVector.dot(2 * tMatrix - i) - tStar
        var b = 2
        val probs = mutableListOf<Double>(lastElement(1, pMatrix).toList2d()[0].last())
        val delta = 0.0001
        while (b < 1500) {
            val c = lastElement(b, pMatrix).toList2d()[0].last() - lastElement(b - 1, pMatrix).toList2d()[0].last()
            probs.add(c)
            val del = 1 - sum(array<Double>(probs))
            if (del <= delta) break
            b++
        }
        b = 0

        var eN = 0.0  // calc expectation
        for (i in probs) {
            eN += i * (b + 1)
            b++
        }

        b = 0
        var dN = 0.0 // calc dispersion
        for (i in probs) {
            dN += i * (pow(((b + 1) - eN), 2.0))
            b++
        }
        val results = Results(
                probs.size,
                probs,
                dN,
                eN,
                dMatrix,
                d)
        return results

    }

    private fun lastElement(p: Int, pMatrix: KtNDArray<Double>): KtNDArray<Double> {
        when (p) {
            1 -> return pMatrix
            0 -> return ones(1)
        }
        return matrixPower(pMatrix, p)
    }

}

data class Results(
        val steps: Int,
        val probs: MutableList<Double>,
        val dN: Double,
        val eN: Double,
        val dispArr: KtNDArray<Double>,
        val dispIArr: KtNDArray<Double>
)
