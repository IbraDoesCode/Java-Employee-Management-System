import java.util.Map;
import java.util.TreeMap;

public class Main {

    public static void main(String[] args) {

        Map<Double, Double> sss = initializeSssTable();

        for (double i : sss.keySet()) {
            System.out.println(i + " Amount: " + sss.get(i));
        }

    }

    private static Map<Double, Double> initializeSssTable() {

        Map<Double, Double> sssTable = new TreeMap<>();

        double base_contribution = 135;
        double increment = 22.50;
        int range = 44;

        for (int i = 0; i < range; i++) {
            double compensationRange = 2750 + (i * 500);
            double contribution = base_contribution + i * increment;
            sssTable.put(compensationRange, contribution);
        }

        return sssTable;
    }

}
