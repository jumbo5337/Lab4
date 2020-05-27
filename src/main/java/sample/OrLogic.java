package sample;
import java.math.BigDecimal;
public class OrLogic {
    public static double orLogic(Service service[], int k){
        double orLogic = 0;
        for(int i = 0; i < service.length; i++){
            double tempMulti = service[i].getF()[k];

            for(int n = 0; n < i; n++){
                tempMulti *= (1 - BaseMethods.sum(service[n], 0,k-1));
            }
            for(int n = service.length-1; i < n; n--){
                tempMulti *= (1 - BaseMethods.sum(service[n], 0, k));
            }
            orLogic += tempMulti;
        }
        orLogic = new BigDecimal(orLogic).setScale(8, BigDecimal.ROUND_HALF_UP).doubleValue();
        return orLogic;
    }

}
