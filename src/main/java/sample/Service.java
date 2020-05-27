package sample;

public class Service {

        private int m;  //Временное ограничение
        private double f[];  //Плотности распределения вероятностей
        public Service(){}
        public Service(int m){
            this.m = m;
        }
        public Service (int m, double f[]){
            this.m = m;
            this.f = f;
        }
        public int getM() {
            return m;
        }
        public double[] getF() {
            return f;
        }
        public void setF(double[] f) {this.f = f;}
        public void setM(int m) {this.m = m;}
    }

