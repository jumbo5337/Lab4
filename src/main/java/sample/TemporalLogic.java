package sample;
import java.math.BigDecimal;
public class TemporalLogic {
    //Вероятность успешного объединения группы сервисов в момент времени k с учетом темпоральной логики
    public static double temporalLogic(Service service[], int k){
        double tempSum = 0;
        for(int j = 0; j <= k-2; j++){
            tempSum += FirstDensityProbability.firstDensityProbability(service, j)* ProbabilityReturn.probabilityReturn(service, k-j);
        }
        double temporalLogic = FirstDensityProbability.firstDensityProbability(service, k) + tempSum;
        temporalLogic = new BigDecimal(temporalLogic).setScale(8, BigDecimal.ROUND_HALF_UP).doubleValue();

        return temporalLogic;
    }

}
