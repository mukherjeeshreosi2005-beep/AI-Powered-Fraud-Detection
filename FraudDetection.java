import java.util.Scanner;

public class FraudDetection {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("====================================");
        System.out.println("     AI POWERED FRAUD DETECTION");
        System.out.println("====================================");

        System.out.print("Enter transaction amount: ");
        double amount = sc.nextDouble();

        System.out.print("Enter transaction hour (0-23): ");
        int hour = sc.nextInt();

        System.out.print("Enter number of failed attempts: ");
        int failedAttempts = sc.nextInt();

        System.out.print("Is this a new device? (yes/no): ");
        String newDevice = sc.next();

        System.out.print("Is this a new location? (yes/no): ");
        String newLocation = sc.next();

        System.out.print("Enter distance from usual location (km): ");
        double distance = sc.nextDouble();

        System.out.print("Is this an international transaction? (yes/no): ");
        String international = sc.next();

        int riskScore = 0;

        System.out.println("\nAnalyzing transaction...");

        if (amount > 50000) {
            riskScore += 20;
        }

        if (hour < 6 || hour > 23) {
            riskScore += 15;
        }

        if (failedAttempts >= 3) {
            riskScore += 15;
        }

        if (newDevice.equalsIgnoreCase("yes")) {
            riskScore += 10;
        }

        if (newLocation.equalsIgnoreCase("yes")) {
            riskScore += 10;
        }

        if (distance > 500) {
            riskScore += 10;
        }

        if (international.equalsIgnoreCase("yes")) {
            riskScore += 10;
        }

        String riskLevel;

        if (riskScore >= 60) {
            riskLevel = "HIGH";
        } else if (riskScore >= 30) {
            riskLevel = "MEDIUM";
        } else {
            riskLevel = "LOW";
        }

        System.out.println("\n====================================");
        System.out.println("       FRAUD DETECTION RESULT");
        System.out.println("====================================");
        System.out.println("Risk Score       : " + riskScore + "/90");
        System.out.println("Risk Level       : " + riskLevel);
        System.out.println("Fraud Probability: " + Math.min(riskScore, 90) + "%");

        if (riskLevel.equals("HIGH")) {
            System.out.println("Action           : BLOCK TRANSACTION");
        } else if (riskLevel.equals("MEDIUM")) {
            System.out.println("Action           : VERIFY TRANSACTION");
        } else {
            System.out.println("Action           : ALLOW TRANSACTION");
        }

        System.out.println("====================================");

        sc.close();
    }
}