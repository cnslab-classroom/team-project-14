
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class Recommendation {

    private Connection connection;
    private User user;
    private HealthMetric healthMetric;
    private ActivityLog activityLog;

    private double[] idealWeightRange;
    private double minWeight;
    private double maxWeight;

    public Recommendation(User user, ActivityLog activityLog, HealthMetric healthMetric) {
        this.user = user;
        this.activityLog = activityLog;
        this.healthMetric = healthMetric;

        // 권장 몸무게 범위 계산
        this.idealWeightRange = user.calculateIdealWeightRange();
        this.minWeight = idealWeightRange[0];
        this.maxWeight = idealWeightRange[1];
    }

    String generateRecommendation() {
        StringBuilder recommendation = new StringBuilder();
        int bodyFatStatus = checkBodyFatPercentage();
        int weightStatus = checkIdealWeight();

        if (bodyFatStatus == 1 && weightStatus == 1) {
            recommendation.append("감량이 필요합니다! 고강도 운동과 함께 저칼로리 식단을 유지하세요.\n");
        } else if (bodyFatStatus == 2 && weightStatus == 2) {
            recommendation.append("잘하고 있습니다! 현재 상태를 유지하기 위해 균형 잡힌 식단과 가벼운 운동을 병행하세요.\n");
        } else if (bodyFatStatus == 3 && weightStatus == 3) {
            recommendation.append("체중 증가가 필요합니다. 고단백 식단과 근력 운동을 추가하세요.\n");
        } else if (bodyFatStatus == 2 && weightStatus == 1) {
            recommendation.append("체지방률을 유지하며 체중을 줄여야 합니다. 유산소 운동과 근력 운동을 병행하세요.\n");
        } else if (bodyFatStatus == 2 && weightStatus == 3) {
            recommendation.append("체지방률을 유지하며 체중을 늘려야 합니다. 고칼로리 식단과 근력 운동을 병행하세요.\n");
        } else if (bodyFatStatus == 1 && weightStatus == 2) {
            recommendation.append("체지방을 줄이고 체중을 유지해야 합니다. 고단백 저탄수 식단과 고강도 운동을 병행하세요.\n");
        } else if (bodyFatStatus == 3 && weightStatus == 1) {
            recommendation.append("건강한 상태를 유지하고 있습니다. 현재 상태를 유지하기 위해 노력하세요!\n");
        }
        return recommendation.toString();
    }

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

    public String generateWeeklyReportFromStartOfMonth() {
        StringBuilder weeklyReport = new StringBuilder("===== 주간 리포트 =====\n");
        LocalDate startDate = LocalDate.now().withDayOfMonth(1);
        LocalDate endDate = startDate.plusDays(7);
    
        // HealthMetric 데이터 추가
        String query = """
            SELECT date, bmi, body_fat, sleep_hours
            FROM health_metrics
            WHERE date >= ? AND date < ?
            ORDER BY date ASC;
            """;
    
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, startDate.toString());
            preparedStatement.setString(2, endDate.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
    
            int recordCount = 0;
            while (resultSet.next()) {
                recordCount++;
                weeklyReport.append(String.format("날짜: %s, BMI: %.2f, 체지방률: %.2f%%, 수면 시간: %.2f시간\n",
                        resultSet.getString("date"),
                        resultSet.getDouble("bmi"),
                        resultSet.getDouble("body_fat"),
                        resultSet.getDouble("sleep_hours")));
            }
    
            if (recordCount < 7) {
                return "주간 리포트를 생성할 데이터가 부족합니다 (7일 필요).\n";
            }
    
            // ActivityLog 데이터 추가
            weeklyReport.append("\n--- 활동 기록 ---\n");
            for (String[] activity : activityLog.getActivities()) {
                LocalDate activityDate = LocalDate.parse(activity[2]);
                if (!activityDate.isBefore(startDate) && activityDate.isBefore(endDate)) {
                    weeklyReport.append(String.format("날짜: %s - 활동 유형: %s, 이름: %s\n",
                            activity[2], activity[0], activity[1]));
                }
            }
    
        } catch (SQLException e) {
            e.printStackTrace();
            return "주간 리포트 생성 중 오류가 발생했습니다.";
        }
    
        return weeklyReport.toString();
    }
    

public String generateMonthlyReport() {
    StringBuilder monthlyReport = new StringBuilder("===== 월간 리포트 =====\n");
    LocalDate startDate = LocalDate.now().withDayOfMonth(1);
    LocalDate endDate = startDate.plusMonths(1).withDayOfMonth(1);

    // HealthMetric 데이터 추가
    String query = """
            SELECT date, bmi, body_fat, sleep_hours
            FROM health_metrics
            WHERE date >= ? AND date < ?
            ORDER BY date ASC;
            """;

    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
        preparedStatement.setString(1, startDate.toString());
        preparedStatement.setString(2, endDate.toString());
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            monthlyReport.append(String.format("날짜: %s, BMI: %.2f, 체지방률: %.2f%%, 수면 시간: %.2f시간\n",
                    resultSet.getString("date"),
                    resultSet.getDouble("bmi"),
                    resultSet.getDouble("body_fat"),
                    resultSet.getDouble("sleep_hours")));
        }

        // ActivityLog 데이터 추가
        monthlyReport.append("\n--- 활동 기록 ---\n");
        for (String[] activity : activityLog.getActivities()) {
            LocalDate activityDate = LocalDate.parse(activity[2]);
            if (!activityDate.isBefore(startDate) && activityDate.isBefore(endDate)) {
                monthlyReport.append(String.format("날짜: %s - 활동 유형: %s, 이름: %s\n",
                        activity[2], activity[0], activity[1]));
            }
        }

    } catch (SQLException e) {
        e.printStackTrace();
        return "월간 리포트 생성 중 오류가 발생했습니다.";
    }

    return monthlyReport.toString();
}


    
    public int checkBodyFatPercentage() {
        int age = user.getAge();
        double bodyFatPercentage = user.getBodyFatPercentage();

        if (user.getGender().equals("male")) { // 성별: 남성
            if (age >= 18 && age <= 39) {
                if (bodyFatPercentage > 20) {
                    return 1; // 체지방량 높음
                } else if (bodyFatPercentage >= 9 && bodyFatPercentage <= 20) {
                    return 2; // 체지방량 정상
                } else {
                    return 3; // 체지방량 낮음
                }
            } else if (age >= 40 && age <= 59) {
                if (bodyFatPercentage > 22) {
                    return 1; // 체지방량 높음
                } else if (bodyFatPercentage >= 12 && bodyFatPercentage <= 22) {
                    return 2; // 체지방량 정상
                } else {
                    return 3; // 체지방량 낮음
                }
            } else {
                return 2; // 나이가 많으면 정상범위는 넓어진다.
            }
        } else if (user.getGender().equals("female")) { // 성별: 여성
            if (age >= 18 && age <= 39) {
                if (bodyFatPercentage > 32) {
                    return 1; // 체지방량 높음
                } else if (bodyFatPercentage >= 21 && bodyFatPercentage <= 32) {
                    return 2; // 체지방량 정상
                } else {
                    return 3; // 체지방량 낮음
                }
            } else if (age >= 40 && age <= 59) {
                if (bodyFatPercentage > 34) {
                    return 1; // 체지방량 높음
                } else if (bodyFatPercentage >= 23 && bodyFatPercentage <= 34) {
                    return 2; // 체지방량 정상
                } else {
                    return 3; // 체지방량 낮음
                }
            } else {
                return 2; // 나이가 많으면 정상범위는 넓어진다.
            }
        }
        return 2;
    }

    public int checkIdealWeight() {
        double weight = user.getWeight();

        if (weight < minWeight) {
            return 1; // 체중 부족
        } else if (weight >= minWeight && weight <= maxWeight) {
            return 2; // 체중 정상
        } else {
            return 3; // 체중 과다
        }
    }


    public void debugLogs() {
        System.out.println("===== Debugging ActivityLog =====");
        for (String[] activity : activityLog.getActivities()) {
            System.out.println(String.join(", ", activity));
        }
    
        System.out.println("\n===== Debugging HealthMetric Database =====");
        healthMetric.printAllRecords();
    }
    
}