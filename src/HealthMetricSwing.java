import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class HealthMetricSwing {
    private Connection connection;
    private JFrame frame;
    private DefaultTableModel tableModel;

    // 생성자: 데이터베이스 연결 및 초기화
    public HealthMetricSwing() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:health_data.db");
            initializeDatabase();
            initializeGUI();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 데이터베이스 초기화
    private void initializeDatabase() {
        String createTableQuery = """
            CREATE TABLE IF NOT EXISTS health_metrics (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                date TEXT NOT NULL,
                bmi REAL NOT NULL,
                body_fat REAL NOT NULL,
                sleep_hours REAL NOT NULL
            );
        """;

        try (Statement statement = connection.createStatement()) {
            statement.execute(createTableQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // BMI 계산
    public double calculateBMI(double weight, double height) {
        return weight / (height * height);
    }

    // 체지방률 계산
    public double calculateBodyFatPercentage(double bmi, int age, String gender) {
        if (gender.equalsIgnoreCase("male")) {
            return (1.20 * bmi) + (0.23 * age) - 16.2;
        } else {
            return (1.20 * bmi) + (0.23 * age) - 5.4;
        }
    }

    // 수면 시간 계산
    public double calculateSleepHours(String sleepTime, String wakeTime) {
        LocalTime sleep = LocalTime.parse(sleepTime);
        LocalTime wake = LocalTime.parse(wakeTime);

        long minutesBetween = ChronoUnit.MINUTES.between(sleep, wake);
        if (minutesBetween < 0) {
            minutesBetween += 24 * 60; // 다음 날로 넘어가는 경우 처리
        }

        return minutesBetween / 60.0;
    }

    // 건강 데이터 추가
    public void addRecord(String date, double bmi, double bodyFatPercentage, double sleepHours) {
        String insertQuery = "INSERT INTO health_metrics (date, bmi, body_fat, sleep_hours) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setString(1, date);
            preparedStatement.setDouble(2, bmi);
            preparedStatement.setDouble(3, bodyFatPercentage);
            preparedStatement.setDouble(4, sleepHours);
            preparedStatement.executeUpdate();

            // 테이블 갱신
            tableModel.addRow(new Object[]{date, bmi, bodyFatPercentage, sleepHours});
            JOptionPane.showMessageDialog(frame, "데이터가 저장되었습니다.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // GUI 초기화
    private void initializeGUI() {
        frame = new JFrame("건강 데이터 관리 시스템");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        // 메인 패널
        JPanel mainPanel = new JPanel(new BorderLayout());

        // 데이터 입력 패널
        JPanel inputPanel = new JPanel(new GridLayout(8, 2));
        inputPanel.setBorder(BorderFactory.createTitledBorder("건강 데이터 입력"));

        JTextField dateField = new JTextField();
        JTextField heightField = new JTextField();
        JTextField weightField = new JTextField();
        JTextField ageField = new JTextField();
        JTextField genderField = new JTextField();
        JTextField sleepTimeField = new JTextField();
        JTextField wakeTimeField = new JTextField();

        inputPanel.add(new JLabel("날짜 (YYYY-MM-DD):"));
        inputPanel.add(dateField);
        inputPanel.add(new JLabel("키 (m):"));
        inputPanel.add(heightField);
        inputPanel.add(new JLabel("몸무게 (kg):"));
        inputPanel.add(weightField);
        inputPanel.add(new JLabel("나이:"));
        inputPanel.add(ageField);
        inputPanel.add(new JLabel("성별 (male/female):"));
        inputPanel.add(genderField);
        inputPanel.add(new JLabel("취침 시간 (HH:mm):"));
        inputPanel.add(sleepTimeField);
        inputPanel.add(new JLabel("기상 시간 (HH:mm):"));
        inputPanel.add(wakeTimeField);

        JButton addButton = new JButton("데이터 추가");
        inputPanel.add(addButton);

        mainPanel.add(inputPanel, BorderLayout.NORTH);

        // 데이터 표시 테이블
        tableModel = new DefaultTableModel(new String[]{"날짜", "BMI", "체지방률", "수면 시간"}, 0);
        JTable table = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.setBorder(BorderFactory.createTitledBorder("저장된 데이터"));

        mainPanel.add(tableScrollPane, BorderLayout.CENTER);

        // 분석 버튼
        JButton analyzeButton = new JButton("분석하기");
        mainPanel.add(analyzeButton, BorderLayout.SOUTH);

        frame.add(mainPanel);

        // 데이터 추가 버튼 이벤트
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String date = dateField.getText();
                    double height = Double.parseDouble(heightField.getText());
                    double weight = Double.parseDouble(weightField.getText());
                    int age = Integer.parseInt(ageField.getText());
                    String gender = genderField.getText();
                    String sleepTime = sleepTimeField.getText();
                    String wakeTime = wakeTimeField.getText();

                    double bmi = calculateBMI(weight, height);
                    double bodyFat = calculateBodyFatPercentage(bmi, age, gender);
                    double sleepHours = calculateSleepHours(sleepTime, wakeTime);

                    addRecord(date, bmi, bodyFat, sleepHours);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "입력값을 확인하세요: " + ex.getMessage());
                }
            }
        });

        // 분석 버튼 이벤트
        analyzeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StringBuilder analysis = new StringBuilder("===== 건강 지표 분석 =====\n");

                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    double bmi = (double) tableModel.getValueAt(i, 1);
                    double bodyFat = (double) tableModel.getValueAt(i, 2);
                    double sleepHours = (double) tableModel.getValueAt(i, 3);

                    // BMI 분석
                    if (bmi < 18.5) {
                        analysis.append("- 저체중 (BMI: ").append(bmi).append(")\n");
                    } else if (bmi < 25) {
                        analysis.append("- 정상 체중 (BMI: ").append(bmi).append(")\n");
                    } else {
                        analysis.append("- 과체중 또는 비만 (BMI: ").append(bmi).append(")\n");
                    }

                    // 체지방률 분석
                    if (bodyFat < 10) {
                        analysis.append("- 체지방률 낮음 (").append(bodyFat).append("%)\n");
                    } else if (bodyFat <= 20) {
                        analysis.append("- 체지방률 정상 (").append(bodyFat).append("%)\n");
                    } else {
                        analysis.append("- 체지방률 높음 (").append(bodyFat).append("%)\n");
                    }

                    // 수면 시간 분석
                    if (sleepHours < 7) {
                        analysis.append("- 수면 부족 (").append(sleepHours).append("시간)\n");
                    } else if (sleepHours > 9) {
                        analysis.append("- 수면 과다 (").append(sleepHours).append("시간)\n");
                    } else {
                        analysis.append("- 적정 수면 시간 (").append(sleepHours).append("시간)\n");
                    }
                }

                JOptionPane.showMessageDialog(frame, analysis.toString(), "건강 지표 분석 결과", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        frame.setVisible(true);
    }

    // 데이터베이스 연결 종료
    public void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 메인 실행
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            HealthMetricSwing app = new HealthMetricSwing();
            Runtime.getRuntime().addShutdownHook(new Thread(app::closeConnection));
        });
    }
}
