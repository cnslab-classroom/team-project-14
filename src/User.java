import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class User {
  // 사용자 기본 정보
  private String name;
  private int age;
  private double height; // 단위: cm
  private double weight; // 단위: kg
  private String gender;

  // 생성자
  public User(String name, int age, double height, double weight, String gender) {
      this.name = name;
      this.age = age;
      this.height = height;
      this.weight = weight;  //(허은빈) 체지방량 기준 판단하료면 성별 필요해서 성별도 추가했어용 
      this.gender = gender;   //일요일에 회의하면서 다시 수정해보아요 

  }

  // getter와 setter
  public String getName() {
      return name;
  }

  public void setName(String name) {
      this.name = name;
  }

  public int getAge() {
      return age;
  }

  public void setAge(int age) {
      this.age = age;
  }


  public double getHeight() {
      return height;
  }

  public void setHeight(double height) {
      this.height = height;
  }

  public double getWeight() {
      return weight;
  }

  public void setWeight(double weight) {
      this.weight = weight;
  }

  public String getGender() {
    return gender;
}

public void setGender(String gender) {
    this.gender = gender;
}

  

  // BMI 계산 메서드
  public double calculateBMI() {
      double heightInMeters = height / 100.0; // cm를 m로 변환
      return weight / (heightInMeters * heightInMeters);
  }

  // 체지방률 계산 메서드
  // 남성: 체지방률 = (1.20 × BMI) + (0.23 × 나이) − 16.2
  // 여성: 체지방률 = (1.20 × BMI) + (0.23 × 나이) − 5.4
  public double calculateBodyFatPercentage() {
      double bmi = calculateBMI();
      if (gender == "Male") {
          return (1.20 * bmi) + (0.23 * age) - 16.2;
      } else {
          return (1.20 * bmi) + (0.23 * age) - 5.4;
      }
  }

  // 권장 체중 계산 (BMI 기준 18.5~24.9)
  public double[] calculateIdealWeightRange() {
      double heightInMeters = height / 100.0;
      double minWeight = 18.5 * (heightInMeters * heightInMeters);
      double maxWeight = 24.9 * (heightInMeters * heightInMeters);
      return new double[] { minWeight, maxWeight };
  }

  // 목표 체중 안내 메서드
  public void printGoalInfo(double goalWeight) {
      double[] idealRange = calculateIdealWeightRange();
      double weightToLose = weight - goalWeight;
      System.out.println("현재 BMI: " + String.format("%.2f", calculateBMI()));
      System.out.println("권장 체중 범위: " + String.format("%.2f", idealRange[0]) + "kg ~ " + String.format("%.2f", idealRange[1]) + "kg");

      if (goalWeight < idealRange[0] || goalWeight > idealRange[1]) {
          System.out.println("경고: 목표 체중이 권장 범위를 벗어났습니다.");
      }

      System.out.println("목표 체중까지 감량해야 할 무게: " + String.format("%.2f", weightToLose) + "kg");
      System.out.println("일일 500칼로리 적자 기준 감량 예상 소요 시간: 약 " + (weightToLose * 7700 / 500) + "일");
  }
}

class UserInfoScreen {
    UserInfoScreen() {
        JFrame frame = new JFrame("사용자 정보 입력");
        frame.setLayout(new GridLayout(7, 2, 10, 10)); // 7행 2열의 GridLayout

        // 입력 필드 및 라벨
        JLabel nameLabel = new JLabel("이름:");
        JTextField nameField = new JTextField();

        JLabel ageLabel = new JLabel("나이:");
        JTextField ageField = new JTextField();

        JLabel heightLabel = new JLabel("키 (cm):");
        JTextField heightField = new JTextField();

        JLabel weightLabel = new JLabel("몸무게 (kg):");
        JTextField weightField = new JTextField();

        JLabel genderLabel = new JLabel("성별:");
        String[] genderOptions = { "Male", "Female" };
        JComboBox<String> genderComboBox = new JComboBox<>(genderOptions);

        JButton submitButton = new JButton("정보 입력");
        JLabel resultLabel = new JLabel("결과가 여기에 표시됩니다.", SwingConstants.CENTER);

        // 버튼 클릭 시 동작 정의
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // 사용자 입력값 가져오기
                    String name = nameField.getText();
                    int age = Integer.parseInt(ageField.getText());
                    double height = Double.parseDouble(heightField.getText());
                    double weight = Double.parseDouble(weightField.getText());
                    String gender = (String) genderComboBox.getSelectedItem();

                    // User 객체 생성 및 계산
                    User user = new User(name, age, height, weight, gender);
                    double bmi = user.calculateBMI();
                    double bodyFatPercentage = user.calculateBodyFatPercentage();

                    // 결과 표시
                    resultLabel.setText("<html>이름: " + name + "<br>"
                            + "BMI: " + String.format("%.2f", bmi) + "<br>"
                            + "체지방률: " + String.format("%.2f", bodyFatPercentage) + "%</html>");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "올바른 값을 입력해주세요.", "입력 오류", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // 컴포넌트 배치
        frame.add(nameLabel);
        frame.add(nameField);
        frame.add(ageLabel);
        frame.add(ageField);
        frame.add(heightLabel);
        frame.add(heightField);
        frame.add(weightLabel);
        frame.add(weightField);
        frame.add(genderLabel);
        frame.add(genderComboBox);
        frame.add(submitButton);
        frame.add(resultLabel);

        // 프레임 설정
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null); // 화면 중앙에 위치
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }
}
