package sample;

public class ProbabilityFirstReturn {
    //Расчет значения вероятности первого возврата в момент k
    public static double probabilityFirstReturn(Service service[], int k){
        double probabilityFirstReturn = 0;
        double multiplierFirst[] = new double[service.length];
        double multiplierSecond[] = new double[service.length];
        double multiplierThird[] = new double[service.length];
        double multiplierFourth[] = new double[service.length];

        for(int i = 0; i < service.length; i++){
            multiplierFirst[i] = BaseMethods.difference(service[i], 0, BaseMethods.countInd(service[i],k-1));
        }
        for(int i = 0; i < service.length; i++){
            multiplierSecond[i] = BaseMethods.sum(service[i],BaseMethods.countInd(service[i], k), k);
        }
        for(int i = 0; i < service.length; i++){
            multiplierThird[i] = BaseMethods.difference(service[i], 0, BaseMethods.countInd(service[i],k));
        }
        for(int i = 0; i < service.length; i++){
            multiplierFourth[i] = BaseMethods.sum(service[i],BaseMethods.countInd(service[i], k+1), k);
        }

        probabilityFirstReturn = BaseMethods.multiplication(multiplierFirst) - BaseMethods.multiplication(multiplierSecond) - BaseMethods.multiplication(multiplierThird) + BaseMethods.multiplication(multiplierFourth);

        return probabilityFirstReturn;
    }

}
