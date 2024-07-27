package Service;

import Model.Employee;

import java.util.Map;
import java.util.TreeMap;

public class PayrollService {

    private static final double PHILHEALTH_RATE = 0.025;

    private static final double PAGIBIG_RATE_BELOW_1500 = 0.01;

    private static final double PAGIBIG_RATE_ABOVE_1500 = 0.02;

    private static final double PAGIBIG_MAX_CONTRIBUTION = 200;

    private static final double OVERTIME_RATE = 0.25;

    private static final double[][] taxBrackets = {
            {20_832, 0, 0}, // No withholding tax for 20,832 and below
            {33_333, 20_833, 0.20}, // 20% in excess of 20,833
            {66_667, 33_333, 0.25, 2500}, // 2,500 plus 25% in excess of 33,333
            {166_667, 66_667, 0.30, 10_833}, // 10,833 plus 30% in excess of 66,667
            {666_667, 166_667, 0.32, 40_833.33}, // 40,833.33 plus 32% in excess of 166,667
            {Double.MAX_VALUE, 666_667, 0.35, 200_833.33} // 200,833.33 plus 35% in excess of 666,667
    };

    private static final TreeMap<Double, Double> sssContributionTable = initializeSssTable();

    // Properties
    private final Employee employee;
    
    private final double hourlyRate;

    private final double riceSubsidy;

    private final double clothingAllowance;

    private final double phoneAllowance;

    private final double hoursWorked;

    private final double overTimeHours;

    public PayrollService(Employee employee, Double hoursWorked, double overTimeHours) {

        this.employee = employee;
        this.hourlyRate = employee.getHourlyRate();
        this.riceSubsidy = employee.getRiceSubsidy();
        this.clothingAllowance = employee.getClothingAllowance();
        this.phoneAllowance = employee.getPhoneAllowance();
        this.hoursWorked = hoursWorked;
        this.overTimeHours = overTimeHours;

    }
    
    public double basicSalary() {
        return hourlyRate * hoursWorked;
    }

    public double overTimePay() {
        return (OVERTIME_RATE * hourlyRate) * overTimeHours;
    }

    private static TreeMap<Double, Double> initializeSssTable() {

       TreeMap<Double, Double> sssTable = new TreeMap<>();

       int range = 45;
       double baseContribution = 135;
       double increment = 22.50;

       for (int i = 0; i < range; i++) {
           double contributionRange = 2750 + (i * 500);
           double contributionAmount = baseContribution + (i * increment);
           sssTable.put(contributionRange, contributionAmount);
       }

       return sssTable;
    }

    public double calculateSssContribution() {
        Map.Entry<Double, Double> entry = sssContributionTable.floorEntry(basicSalary());
        return entry.getValue();
    }

    public double calculatePagIbigContribution() {
        double rate = (basicSalary() >= 1500) ? basicSalary() * PAGIBIG_RATE_ABOVE_1500 : PAGIBIG_RATE_BELOW_1500;
        return Math.min(basicSalary() * rate, PAGIBIG_MAX_CONTRIBUTION);
    }

    public double calculatePhilhealthContribution() {
        return basicSalary() * PHILHEALTH_RATE;
    }

    public double calculateTotalAllowances() {
        return riceSubsidy + clothingAllowance + phoneAllowance;
    }

    public double calculateGrossPay() {
        return basicSalary() + calculateTotalAllowances();
    }
    
    public double taxableIncome() {
        return calculateGrossPay() - calculatePartialDeductions();
    }

    public double calculateWithholdingTax() {
        for (double[] taxBracket : taxBrackets) {
            if (taxableIncome() <= taxBracket[0]) {
                return taxBracket.length == 3 ? taxBracket[2] * (taxableIncome() - taxBracket[1]) :
                        taxBracket[3] + taxBracket[2] * (taxableIncome() - taxBracket[1]);
            }
        }
        return 0;
    }

    public double calculatePartialDeductions() {
        return calculateSssContribution() + calculatePhilhealthContribution() + calculatePagIbigContribution();
    }

    public double calculateTotalDeductions() {
        return calculatePartialDeductions() + calculateWithholdingTax();
    }
    
    public double calculateNetPay() {
        return calculateGrossPay() - calculateTotalDeductions() + overTimePay();
    }

}

