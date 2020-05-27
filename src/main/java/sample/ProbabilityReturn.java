package sample;

import java.util.Arrays;

public class ProbabilityReturn {
    //Определение значений индексов
    private static int[] countIndex(Service service[], int k){

        int countIndex[] = new int[2];
        int m[] =  new int[service.length];

        for(int i = 0; i < service.length; i++){
            m[i] = service[i].getM();
        }
        Arrays.sort(m);
        countIndex[1] = k - m[0] -2;
        countIndex[0] = m[0]+1;
        return countIndex;
    }

    //Вероятность возврата к началу в момент времени k
    public static double probabilityReturn(Service service[], int k){
        double probabilityReturn = 0;

        if(k < 0){
            return probabilityReturn;
        }else{

            if(k == 0){
                return ProbabilityFirstReturn.probabilityFirstReturn(service, k);
            }else{
                int index[] = countIndex(service, k);

                if(index[0] < 0 || index[1] < 0){
                    return ProbabilityFirstReturn.probabilityFirstReturn(service, k);
                }else{
                    double tempSum = 0;
                    for(; index[0] <= index[1]; index[0]++){
                        tempSum += ProbabilityFirstReturn.probabilityFirstReturn(service, index[0])*probabilityReturn(service,k-index[0]);
                    }
                    probabilityReturn = ProbabilityFirstReturn.probabilityFirstReturn(service,k) + tempSum;
                }
            }
        }
        return probabilityReturn;
    }

}
