import java.sql.*;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;

public class HealthMetric {
    private Connection connection;
    private double bmi;
    private double bodyFatPercentage;
    private double sleepHours;

    // 생성자: 데이터베이스 연결 및 초기화
    public HealthMetric() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:health_data.db");
            initializeDatabase();
        } catch (SQLException e) {
            System.err.println("데이터베이스 연결 실패: " + e.getMessage());
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
            System.err.println("테이블 생성 실패: " + e.getMessage());
        }
    }

    // BMI 계산
    public double calculateBMI(double weight, double height) {
        this.bmi = weight / (height * height);
        return this.bmi;
    }

    // 체지방률 계산
    public double calculateBodyFatPercentage(double bmi, int age, String gender) {
        if (gender.equalsIgnoreCase("male")) {
            this.bodyFatPercentage = (1.20 * bmi) + (0.23 * age) - 16.2;
        } else {
            this.bodyFatPercentage = (1.20 * bmi) + (0.23 * age) - 5.4;
        }
        return this.bodyFatPercentage;
    }

    // 수면 시간 계산
    public double calculateSleepHours(String sleepTime, String wakeTime) {
        try {
            LocalTime sleep = LocalTime.parse(sleepTime);
            LocalTime wake = LocalTime.parse(wakeTime);

            long minutesBetween = ChronoUnit.MINUTES.between(sleep, wake);
            if (minutesBetween < 0) {
                minutesBetween += 24 * 60; // 다음 날로 넘어가는 경우 처리
            }

            this.sleepHours = minutesBetween / 60.0;
            return this.sleepHours;
        } catch (Exception e) {
            System.err.println("시간 형식 오류: HH:mm 형식으로 입력하세요.");
            return 0;
        }
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
            System.out.println("데이터가 저장되었습니다.");
        } catch (SQLException e) {
            System.err.println("데이터 저장 실패: " + e.getMessage());
        }
    }

    // 건강 데이터 조회
    public void printAllRecords() {
        String selectQuery = "SELECT * FROM health_metrics";

        try (Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(selectQuery)) {

            System.out.println("\n===== 저장된 데이터 =====");
            while (resultSet.next()) {
                System.out.printf("날짜: %s, BMI: %.2f, 체지방률: %.2f%%, 수면 시간: %.2f시간\n",
                        resultSet.getString("date"),
                        resultSet.getDouble("bmi"),
                        resultSet.getDouble("body_fat"),
                        resultSet.getDouble("sleep_hours"));
            }
        } catch (SQLException e) {
            System.err.println("데이터 조회 실패: " + e.getMessage());
        }
    }

    // 건강 지표 분석
    public void analyzeHealthMetrics(double bmi, double bodyFatPercentage, double sleepHours) {
        System.out.println("\n===== 건강 지표 분석 =====");

        // BMI 분석
        if (bmi < 18.5) {
            System.out.println("- 저체중 (BMI: " + String.format("%.2f", bmi) + ")");
        } else if (bmi < 25) {
            System.out.println("- 정상 체중 (BMI: " + String.format("%.2f", bmi) + ")");
        } else {
            System.out.println("- 과체중 또는 비만 (BMI: " + String.format("%.2f", bmi) + ")");
        }

        // 체지방률 분석
        if (bodyFatPercentage < 10) {
            System.out.println("- 체지방률 낮음 (" + String.format("%.2f", bodyFatPercentage) + "%)");
        } else if (bodyFatPercentage <= 20) {
            System.out.println("- 체지방률 정상 (" + String.format("%.2f", bodyFatPercentage) + "%)");
        } else {
            System.out.println("- 체지방률 높음 (" + String.format("%.2f", bodyFatPercentage) + "%)");
        }

        // 수면 시간 분석
        if (sleepHours < 7) {
            System.out.println("- 수면 부족 (" + String.format("%.2f", sleepHours) + "시간)");
        } else if (sleepHours > 9) {
            System.out.println("- 수면 과다 (" + String.format("%.2f", sleepHours) + "시간)");
        } else {
            System.out.println("- 적정 수면 시간 (" + String.format("%.2f", sleepHours) + "시간)");
        }
    }

    // 데이터베이스 연결 종료
    public void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("데이터베이스 연결 종료 실패: " + e.getMessage());
        }
    }

    // 추가된 메서드들

    // BMI 반환 메서드
    public double getBmi() {
        return this.bmi;
    }

    // 체지방률 반환 메서드
    public double getBodyFatPercentage() {
        return this.bodyFatPercentage;
    }

    // 수면 시간 반환 메서드
    public double getSleepHours() {
        return this.sleepHours;
    }

    // 메인 실행
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // User 객체 생성
        User user = new User("홍길동", 25, 175.0, 70.0, "male");
        HealthMetric healthMetric = new HealthMetric();

        while (true) {
            System.out.println("\n===== 건강 데이터 관리 시스템 =====");
            System.out.println("1. 건강 데이터 추가");
            System.out.println("2. 저장된 데이터 조회");
            System.out.println("3. 종료");
            System.out.print("선택: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("숫자를 입력하세요.");
                continue;
            }

            switch (choice) {
                case 1:
                    try {
                        System.out.print("날짜 (YYYY-MM-DD): ");
                        String date = scanner.nextLine();

                        System.out.print("키(m): ");
                        double height = Double.parseDouble(scanner.nextLine());

                        System.out.print("몸무게(kg): ");
                        double weight = Double.parseDouble(scanner.nextLine());

                        System.out.print("나이: ");
                        int age = Integer.parseInt(scanner.nextLine());

                        System.out.print("성별 (male/female): ");
                        String gender = scanner.nextLine();

                        System.out.print("취침 시간 (HH:mm): ");
                        String sleepTime = scanner.nextLine();

                        System.out.print("기상 시간 (HH:mm): ");
                        String wakeTime = scanner.nextLine();

                        // User 객체의 BMI 계산, 체지방률 계산
                        double bmi = healthMetric.calculateBMI(weight, height);
                        double bodyFat = healthMetric.calculateBodyFatPercentage(bmi, age, gender);
                        double sleepHours = healthMetric.calculateSleepHours(sleepTime, wakeTime);

                        // 건강 기록 추가
                        healthMetric.addRecord(date, bmi, bodyFat, sleepHours);

                        // 건강 분석 결과 출력
                        healthMetric.analyzeHealthMetrics(bmi, bodyFat, sleepHours);
                    } catch (Exception e) {
                        System.out.println("입력 중 오류가 발생했습니다. 다시 시도하세요.");
                    }
                    break;

                case 2:
                    healthMetric.printAllRecords();
                    break;

                case 3:
                    healthMetric.closeConnection();
                    scanner.close();
                    return;

                default:
                    System.out.println("잘못된 입력입니다. 다시 시도하세요.");
                    break;
            }
        }
    }
}
