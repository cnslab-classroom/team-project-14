
import javax.swing.*;
import java.awt.*;

public class HealthMetricSwing {

    public static void showHealthAnalysis(double height, double weight, int age, String gender) {
        JFrame frame = new JFrame("건강 지표 분석");
        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));

        double bmi = calculateBMI(weight, height);
        String bmiCategory = (bmi < 18.5) ? "저체중" : (bmi < 25) ? "정상" : "과체중";

        double bodyFat = calculateBodyFatPercentage(bmi, age, gender);
        String bodyFatCategory = (bodyFat < 10) ? "적음" : (bodyFat <= 20) ? "보통" : "높음";

        JLabel bmiLabel = new JLabel("BMI: " + String.format("%.2f", bmi) + " (" + bmiCategory + ")");
        JLabel bodyFatLabel = new JLabel("체지방률: " + String.format("%.2f", bodyFat) + "% (" + bodyFatCategory + ")");

        panel.add(bmiLabel);
        panel.add(bodyFatLabel);

        JButton closeButton = new JButton("닫기");
        closeButton.addActionListener(e -> frame.dispose());
        panel.add(closeButton);

        frame.add(panel);
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static double calculateBMI(double weight, double heightInCm) {
        double heightInMeters = heightInCm / 100.0;
        return weight / (heightInMeters * heightInMeters);
    }

    private static double calculateBodyFatPercentage(double bmi, int age, String gender) {
        if ("남".equals(gender)) {
            return (1.20 * bmi) + (0.23 * age) - 16.2;
        } else {
            return (1.20 * bmi) + (0.23 * age) - 5.4;
        }
    }
}
