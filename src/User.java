import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class User {
    private String userId; // 사용자 아이디
    private String name; // 실제 이름
    private int age;
    private double height;
    private double weight;
    private String gender;
    private List<String> exerciseLog; // 운동 기록
    private List<String> dietLog; // 식단 기록

    // HealthMetric 객체 참조
    private HealthMetric healthMetric;

    // ActivityLog 객체 참조 (사용자 활동 기록)
    private ActivityLog activityLog;

    // 생성자
    public User(String name, int age, double height, double weight, String gender) {
        this.userId = userId;
        this.name = name;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.gender = gender;
        this.exerciseLog = new ArrayList<>(); // 운동 기록 리스트 초기화
        this.dietLog = new ArrayList<>(); // 식단 기록 리스트 초기화
        this.healthMetric = new HealthMetric(); // HealthMetric 객체 초기화
        this.activityLog = new ActivityLog(); // ActivityLog 객체 초기화
    }

    public User(String username) {
        this.userId = username;
        this.exerciseLog = new ArrayList<>();
        this.dietLog = new ArrayList<>();
        this.healthMetric = new HealthMetric();
        this.activityLog = new ActivityLog();
    }

    public List<String> getExerciseLog() {
        return exerciseLog;
    }

    public List<String> getDietLog() {
        return dietLog;
    }

    // 예시로 운동과 식단 추가 메서드
    public void addExercise(String exercise) {
        exerciseLog.add(exercise);
    }

    public void addDiet(String diet) {
        dietLog.add(diet);
    }

    // HealthMetric 객체 반환 메서드
    public HealthMetric getHealthMetric() {
        return healthMetric; // HealthMetric 객체 반환
    }

    public String getUserId() {
        return userId;
    }

    // 사용자 정보를 반환하는 메서드
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

    // BMI 계산 메서드 (HealthMetric을 통해 호출)
    public double getBMI() {
        return healthMetric.calculateBMI(weight, height); // height와 weight 전달
    }

    // 체지방률 계산 메서드 (HealthMetric을 통해 호출)
    public double getBodyFatPercentage() {
        return healthMetric.calculateBodyFatPercentage(getBMI(), age, gender); // BMI, 나이, 성별 전달
    }

    // 권장 체중 계산 (BMI 기준 18.5~24.9)
    public double[] calculateIdealWeightRange() {
        double heightInMeters = height / 100.0;
        double minWeight = 18.5 * (heightInMeters * heightInMeters);
        double maxWeight = 24.9 * (heightInMeters * heightInMeters);
        return new double[] { minWeight, maxWeight };
    }

    // 활동 추가 메서드
    public void addActivity(User user, String category, String description, int calories, String date) {
        // activityLog의 addActivity 메서드 호출
        activityLog.addActivity(user, category, description, calories, date); // User 객체를 전달
    }
}

class UserInfoScreen {
    private User user; // User 객체를 멤버 변수로 선언

    UserInfoScreen() {
        JFrame frame = new JFrame("사용자 정보 입력");
        frame.setLayout(new GridLayout(7, 2, 10, 10)); // 7행 2열의 GridLayout

        // 입력 필드 및 라벨
        JLabel nameLabel = new JLabel("이름:");
        JTextField nameField = new JTextField();
        JLabel ageLabel = new JLabel("나이(만):");
        JTextField ageField = new JTextField();
        JLabel heightLabel = new JLabel("키 (cm):");
        JTextField heightField = new JTextField();
        JLabel weightLabel = new JLabel("몸무게 (kg):");
        JTextField weightField = new JTextField();
        JLabel genderLabel = new JLabel("성별:");
        String[] genderOptions = { "Male", "Female" };
        JComboBox<String> genderComboBox = new JComboBox<>(genderOptions);
        JButton submitButton = new JButton("정보 저장");

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

        // 버튼 클릭 이벤트 처리
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                int age = Integer.parseInt(ageField.getText());
                double height = Double.parseDouble(heightField.getText());
                double weight = Double.parseDouble(weightField.getText());
                String gender = (String) genderComboBox.getSelectedItem();

                // User 객체 생성 후 값 설정
                user = new User(name, age, height, weight, gender);
                JOptionPane.showMessageDialog(frame, "정보 저장 완료!");
            }
        });

        // 프레임 설정
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null); // 화면 중앙에 위치
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }

    public User getUser() {
        return user; // User 객체 반환
    }
}
