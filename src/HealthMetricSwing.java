import javax.swing.*;
import java.awt.*;

public class HealthMetricSwing {
    public static void showHealthAnalysis(User user) {
        // 사용자 데이터를 가져옴
        double height = user.getHeight();
        double weight = user.getWeight();
        int age = user.getAge();
        String gender = user.getGender();

        // HealthMetric 객체를 통해 BMI와 체지방률 계산
        double bmi = user.getBMI();
        double bodyFatPercentage = user.getBodyFatPercentage();

        // 상태를 판단하는 메서드 호출
        String bmiStatus = getBmiStatus(bmi);
        String bodyFatStatus = getBodyFatStatus(bodyFatPercentage, gender);

        // 결과를 표시할 새로운 창 생성
        JFrame frame = new JFrame("건강 지표 분석");
        frame.setLayout(new GridLayout(5, 1, 10, 10));

        // BMI 및 체지방률과 함께 상태 표시
        JLabel bmiLabel = new JLabel("BMI: " + String.format("%.2f", bmi) + " (" + bmiStatus + ")", JLabel.CENTER);
        JLabel bodyFatLabel = new JLabel("체지방률: " + String.format("%.2f", bodyFatPercentage) + "% (" + bodyFatStatus + ")", JLabel.CENTER);

        // 권장 체중 범위 계산 및 표시
        double[] idealWeightRange = user.calculateIdealWeightRange();
        JLabel idealWeightLabel = new JLabel(
                "권장 체중 범위: " + String.format("%.2f", idealWeightRange[0]) + "kg ~ "
                        + String.format("%.2f", idealWeightRange[1]) + "kg",
                JLabel.CENTER);

        JButton backButton = new JButton("뒤로 가기");
        backButton.addActionListener(e -> frame.dispose());

        // 프레임에 컴포넌트 추가
        frame.add(bmiLabel);
        frame.add(bodyFatLabel);
        frame.add(idealWeightLabel);
        frame.add(backButton);

        // 프레임 설정
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }

    // BMI 상태 판단 메서드
    private static String getBmiStatus(double bmi) {
        if (bmi < 18.5) {
            return "저체중";
        } else if (bmi < 24.9) {
            return "정상";
        } else if (bmi < 29.9) {
            return "과체중";
        } else {
            return "비만";
        }
    }

    // 체지방률 상태 판단 메서드
    private static String getBodyFatStatus(double bodyFatPercentage, String gender) {
        if (gender.equalsIgnoreCase("Male")) {
            if (bodyFatPercentage < 6) {
                return "적음";
            } else if (bodyFatPercentage <= 24) {
                return "보통";
            } else {
                return "많음";
            }
        } else { // Female
            if (bodyFatPercentage < 16) {
                return "적음";
            } else if (bodyFatPercentage <= 30) {
                return "보통";
            } else {
                return "많음";
            }
        }
    }
}

