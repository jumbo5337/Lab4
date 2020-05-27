package sample;
import java.math.BigDecimal;

public class ExpectedValue {
    //Определение значения математического ожидания
    public static double expectedValue(double probabilityDensity[]){
        double expectedValue = 0;
        for(int i = 0; i < probabilityDensity.length; i++){
            expectedValue += (i+1)*probabilityDensity[i];
        }
        expectedValue = new BigDecimal(expectedValue).setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue();
        return  expectedValue;
    }
    //Определение значения дисперсии
    public static double dispersion(double probabilityDensity[]){
        double dispersion = 0;
        for(int i = 0; i < probabilityDensity.length; i++){
            dispersion += ((i+1)- ExpectedValue.expectedValue(probabilityDensity))*((i+1)- ExpectedValue.expectedValue(probabilityDensity))*probabilityDensity[i];
        }
        dispersion = new BigDecimal(dispersion).setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue();
        return  dispersion;
    }
}
