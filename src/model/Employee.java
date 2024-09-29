package model;

import java.time.LocalDate;

public record Employee(
        int employeeId,
        String firstName,
        String lastName,
        LocalDate birthday,
        String address,
        String phoneNumber,
        LocalDate hireDate,
        String position,
        String department,
        String immediateSupervisor,
        String status,
        String sssNumber,
        String philhealthNumber,
        String tinNumber,
        String pagibigNumber,
        double basicSalary,
        double riceSubsidy,
        double phoneAllowance,
        double clothingAllowance,
        double hourlyRate

) {

//    public String[] toArray() {
//        return new String[]{
//                String.valueOf(employeeId),
//                firstName,
//                lastName,
//                String.valueOf(birthday),
//                address,
//                phoneNumber,
//                String.valueOf(hireDate),
//                position,
//                department,
//                immediateSupervisor,
//                status,
//                sssNumber,
//                philhealthNumber,
//                tinNumber,
//                pagibigNumber,
//                String.valueOf(basicSalary),
//                String.valueOf(riceSubsidy),
//                String.valueOf(phoneAllowance),
//                String.valueOf(clothingAllowance),
//                String.valueOf(hourlyRate)
//        };
//    }
}
