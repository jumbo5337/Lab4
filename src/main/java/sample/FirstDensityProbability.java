package sample;

public class FirstDensityProbability {
    public static double firstDensityProbability(Service service[], int k){
        double firstDensityProbability = 0;

        for(int i = 0; i < service.length; i++){
            double tempMulti = service[i].getF()[k];

            for(int n = 0; n < i; n++){
                tempMulti *= BaseMethods.sum(service[n], BaseMethods.countInd(service[n],k),k-1);
            }

            for(int n = service.length-1; i < n; n--){
                tempMulti *= BaseMethods.sum(service[n], BaseMethods.countInd(service[n],k),k);
            }
            firstDensityProbability += tempMulti;
        }
        return firstDensityProbability;
    }

}
