package Model;

public class GovernmentIds {

    private final String sssNumber;
    private final String philhealthNumber;
    private final String tinNumber;
    private final String pagibigNumber;

    public GovernmentIds(String sssNumber, String philhealthNumber, String tinNumber, String pagibigNumber) {
        this.sssNumber = sssNumber;
        this.philhealthNumber = philhealthNumber;
        this.tinNumber = tinNumber;
        this.pagibigNumber = pagibigNumber;
    }

    public String getSssNumber() {
        return sssNumber;
    }

    public String getPhilhealthNumber() {
        return philhealthNumber;
    }

    public String getTinNumber() {
        return tinNumber;
    }

    public String getPagibigNumber() {
        return pagibigNumber;
    }

}
