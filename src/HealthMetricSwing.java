import javax.swing.*;
import java.awt.*;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class HealthMetricSwing {

    public static void showHealthInputScreen(double height, double weight, int age, String gender) {
        JFrame inputFrame = new JFrame("수면 시간 입력");
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));

        JLabel sleepTimeLabel = new JLabel("취침 시간 (HH:mm):");
        JTextField sleepTimeField = new JTextField();

        JLabel wakeTimeLabel = new JLabel("기상 시간 (HH:mm):");
        JTextField wakeTimeField = new JTextField();

        JButton submitButton = new JButton("분석하기");
        JButton cancelButton = new JButton("취소");

        submitButton.addActionListener(e -> {
            String sleepTime = sleepTimeField.getText();
            String wakeTime = wakeTimeField.getText();
            try {
                // Validate time format
                LocalTime.parse(sleepTime);
                LocalTime.parse(wakeTime);

                // Close input frame and show analysis
                inputFrame.dispose();
                showHealthAnalysis(height, weight, age, gender, sleepTime, wakeTime);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(inputFrame, "올바른 시간 형식을 입력해주세요. (예: 22:30)", "입력 오류", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> inputFrame.dispose());

        panel.add(sleepTimeLabel);
        panel.add(sleepTimeField);
        panel.add(wakeTimeLabel);
        panel.add(wakeTimeField);
        panel.add(submitButton);
        panel.add(cancelButton);

        inputFrame.add(panel);
        inputFrame.setSize(400, 200);
        inputFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        inputFrame.setLocationRelativeTo(null);
        inputFrame.setVisible(true);
    }

    public static void showHealthAnalysis(double height, double weight, int age, String gender, String sleepTime, String wakeTime) {
        JFrame frame = new JFrame("건강 지표 분석");
        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));

        double bmi = calculateBMI(weight, height);
        String bmiCategory = (bmi < 18.5) ? "저체중" : (bmi < 25) ? "정상" : "과체중";

        double bodyFat = calculateBodyFatPercentage(bmi, age, gender);
        String bodyFatCategory = (bodyFat < 10) ? "적음" : (bodyFat <= 20) ? "보통" : "높음";

        double sleepHours = calculateSleepHours(sleepTime, wakeTime);
        String sleepCategory = (sleepHours < 7) ? "적음" : (sleepHours > 9) ? "많음" : "적당";

        JLabel bmiLabel = new JLabel("BMI: " + String.format("%.2f", bmi) + " (" + bmiCategory + ")");
        JLabel bodyFatLabel = new JLabel("체지방률: " + String.format("%.2f", bodyFat) + "% (" + bodyFatCategory + ")");
        JLabel sleepLabel = new JLabel("수면 시간: " + String.format("%.2f", sleepHours) + "시간 (" + sleepCategory + ")");

        panel.add(bmiLabel);
        panel.add(bodyFatLabel);
        panel.add(sleepLabel);

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

    private static double calculateSleepHours(String sleepTime, String wakeTime) {
        try {
            LocalTime sleep = LocalTime.parse(sleepTime);
            LocalTime wake = LocalTime.parse(wakeTime);
            long minutesBetween = ChronoUnit.MINUTES.between(sleep, wake);
            if (minutesBetween < 0) {
                minutesBetween += 24 * 60; // 다음 날로 넘어가는 경우 처리
            }
            return minutesBetween / 60.0;
        } catch (Exception e) {
            return 0.0;
        }
    }
}

