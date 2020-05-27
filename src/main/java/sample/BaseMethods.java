package sample;
public class BaseMethods {
    //Суммирование плотностей распределения вероятностей от j до k
    public static double sum(Service services, int j, int k) {
        double sum = 0;
        if (k >= 0 && j >= 0) {
            for (; j <= k; j++) {
                sum += services.getF()[j];
            }
        }
        if (k >= 0 && j < 0){
            j = 0;
            for (; j <= k; j++) {
                sum += services.getF()[j];
            }
        }
        return sum;
    }
    //Разница еденицы и суммы плотностей распределения вероятностей //от j до k
    public static double difference(Service services, int j, int k){
        double diff = 1;
        if(k >= 0 && j >= 0){
            diff -= sum(services, j, k);
        }
        if (k >= 0 && j < 0){
            j = 0;
            for (; j <= k; j++) {
                diff -= services.getF()[j];
            }
        }
        return diff;
    }

    //Расчет значения индекса
    public static int countInd(Service services, int ind){
        int index = ind - services.getM()+1;
        return index;
    }

    //Произведение значений массива
    public static double multiplication(double multiplier[]){
        double multiplication = 1;
        for(int i = 0; i < multiplier.length; i++){
            multiplication *= multiplier[i];
        }
        return multiplication;
    }

}
