package sample;
import sample.Service;

import java.math.BigDecimal;
public class AndLogic {
    public static double andLogic(Service service[], int k){
        double andLogic = 0;

        for(int i = 0; i < service.length; i++){
            double tempMulti = service[i].getF()[k];

            for(int n = 0; n < i; n++){
                tempMulti *= BaseMethods.sum(service[n], 0,k-1);
            }

            for(int n = service.length-1; i < n; n--){
                tempMulti *= BaseMethods.sum(service[n], 0, k);
            }
            andLogic += tempMulti;
        }
        andLogic = new BigDecimal(andLogic).setScale(8, BigDecimal.ROUND_HALF_UP).doubleValue();
        return andLogic;
    }
}
