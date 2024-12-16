import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.time.LocalDate;

public class Recommendation{

    
    private Connection connection;
    private User user;
    private HealthMetric healthMetric;
    private ActivityLog activityLog;

    double[] idealWeightRange;
    double minWeight;
    double maxWeight;

    public Recommendation(User user, ActivityLog activityLog, HealthMetric healthMetric) {
        this.user = user;
        this.activityLog = activityLog;
        this.healthMetric = healthMetric;

        // 권장 몸무게 범위 계산
        this.idealWeightRange = user.calculateIdealWeightRange();
        this.minWeight = idealWeightRange[0];
        this.maxWeight = idealWeightRange[1];
    }



    private String generateRecommendation() {
        // printRecommend 메서드를 String으로 변환하여 호출
        StringBuilder recommendation = new StringBuilder();
        if (checkBodyFatPercentage() == 1 && checkIdealWeight() == 1) //체지방률 높 ,권장몸무게 높
        {
            recommendation.append("감량이 필요합니다! 식단과 병행하는 고강도 운동이 필요합니다.일일 권장 칼로리에 300~500kcal를\r\n" + //
                    "줄인 고단백 저탄수 식단을 섭취해 주세요.\r\n" + //
                    "고강도 유산소운동과 근력 운동을 병행해 주세요");
        } else if (checkBodyFatPercentage() == 2 && checkIdealWeight() == 2)//체지방률 o ,권장몸무게 o
        {
            recommendation.append("잘하고 있어요! 현재 상태를 유지하기 위해 일일 권장 칼로리를 섭취하고, \r\n" + //
                    "    가벼운 생활 운동을 병행하도록 해요.");
        } else if (checkBodyFatPercentage() == 3 && checkIdealWeight() == 3)//체지방률 낮 ,권장몸무게 낮
        {
            recommendation.append("체중 증가와 근력 향상이 필요합니다. 일일 권장 칼로리에 300~500kcal를\r\n" + //
                    "추가하여 고단백 고열량 식단을 섭취해 주세요.\r\n" + //
                    "근력 운동을 중심으로 운동해 주세요.\r\n" + //
                    "");
        } else if (checkBodyFatPercentage() == 2 && checkIdealWeight() == 1)//체지방률 o ,권장몸무게 높
        {
            recommendation.append("현재 체지방률을 유지하며  체중을 줄입시다!\r\n" + //
                    "저칼로리 식단과 함께 유산소 운동, 근력 운동을 병행합시다.");
        } else if (checkBodyFatPercentage() == 2 && checkIdealWeight() == 3)//체지방률 o ,권장몸무게 낮
        {
            recommendation.append("현재 체지방률을 유지하며 체중 증가가 필요합니다.\r\n" + //
                    "고칼로리 식단과 함께 근력 운동 위주의 운동과 함께 가벼운 유산소 운동을 병행합시다.");
        } else if (checkBodyFatPercentage() == 1 && checkIdealWeight() == 2)//체지방률 높 ,권장몸무게 o
        {
            recommendation.append("체지방을 줄이고 체중을 유지합시다!\r\n" + //
                    "저탄수 고단백 식단과 함께 고강도 유산소 운동과 근력 운동을 병행합시다!");
        } else if (checkBodyFatPercentage() == 3 && checkIdealWeight() == 1)//체지방률 낮 ,권장몸무게 높
        {
            recommendation.append("아주 건강한 몸을 잘 유지하고 계시는 군요! 현재 상태를 유지하기 위해 노력합시다!");
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
        String query = """
        SELECT date, bmi, body_fat, sleep_hours
        FROM health_metrics
        WHERE date >= date('now', 'start of month')
          AND date < date('now', 'start of month', '+7 days')
        ORDER BY date ASC;
        """;

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

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

            weeklyReport.append("\n--- 활동 기록 ---\n");
            for (int i = 0; i < 7; i++) {
                LocalDate date = LocalDate.now().minusDays(7 - i);
                int burnedCalories = activityLog.getTotalCalories("운동", date);
                int consumedCalories = activityLog.getTotalCalories("식단", date);
                weeklyReport.append(String.format("날짜: %s - 운동 소모 칼로리: %d, 식단 섭취 칼로리: %d\n", date, burnedCalories, consumedCalories));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "주간 리포트 생성 중 오류가 발생했습니다.";
        }

        return weeklyReport.toString();
    }

    public String generateMonthlyReport() {
        String today = LocalDate.now().toString();
        String endOfMonthQuery = """
        SELECT date('now', 'start of month', '+1 month', '-1 day') AS end_of_month;
        """;

        String endOfMonth = "";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(endOfMonthQuery)) {
            if (resultSet.next()) {
                endOfMonth = resultSet.getString("end_of_month");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "월말 체크 중 오류 발생.";
        }

        if (!today.equals(endOfMonth)) {
            return "월간 리포트는 월말에만 생성됩니다.\n";
        }

        StringBuilder monthlyReport = new StringBuilder("===== 월간 리포트 =====\n");
        String query = """
        SELECT date, bmi, body_fat, sleep_hours
        FROM health_metrics
        WHERE date >= date('now', 'start of month')
          AND date <= date('now', 'start of month', '+1 month', '-1 day')
        ORDER BY date ASC;
        """;

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                monthlyReport.append(String.format("날짜: %s, BMI: %.2f, 체지방률: %.2f%%, 수면 시간: %.2f시간\n",
                        resultSet.getString("date"),
                        resultSet.getDouble("bmi"),
                        resultSet.getDouble("body_fat"),
                        resultSet.getDouble("sleep_hours")));
            }

            monthlyReport.append("\n--- 활동 기록 ---\n");
            for (LocalDate date = LocalDate.now().withDayOfMonth(1); date.isBefore(LocalDate.now().plusMonths(1).withDayOfMonth(1)); date = date.plusDays(1)) {
                int burnedCalories = activityLog.getTotalCalories("운동", date);
                int consumedCalories = activityLog.getTotalCalories("식단", date);
                monthlyReport.append(String.format("날짜: %s - 운동 소모 칼로리: %d, 식단 섭취 칼로리: %d\n", date, burnedCalories, consumedCalories));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "월간 리포트 생성 중 오류가 발생했습니다.";
        }

        return monthlyReport.toString();
    }

    public int checkBodyFatPercentage() {
        int age = user.getAge();
        double bodyFatPercentage = user.calculateBodyFatPercentage();

        if (user.getGender() == "male") { // 성별: 남성
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
            } else if (age >= 60) {
                if (bodyFatPercentage > 24) {
                    return 1; // 체지방량 높음
                } else if (bodyFatPercentage >= 14 && bodyFatPercentage <= 24) {
                    return 2; // 체지방량 정상
                } else {
                    return 3; // 체지방량 낮음
                }
            }
        } else if (user.getGender() == "female") { // 성별: 여성
            if (age >= 18 && age <= 39) {
                if (bodyFatPercentage > 30) {
                    return 1; // 체지방량 높음
                } else if (bodyFatPercentage >= 19 && bodyFatPercentage <= 30) {
                    return 2; // 체지방량 정상
                } else {
                    return 3; // 체지방량 낮음
                }
            } else if (age >= 40 && age <= 59) {
                if (bodyFatPercentage > 32) {
                    return 1; // 체지방량 높음
                } else if (bodyFatPercentage >= 21 && bodyFatPercentage <= 32) {
                    return 2; // 체지방량 정상
                } else {
                    return 3; // 체지방량 낮음
                }
            } else if (age >= 60) {
                if (bodyFatPercentage > 34) {
                    return 1; // 체지방량 높음
                } else if (bodyFatPercentage >= 23 && bodyFatPercentage <= 34) {
                    return 2; // 체지방량 정상
                } else {
                    return 3; // 체지방량 낮음
                }
            }
        }

        return 0; // 기본값: 유효하지 않은 경우
    }


    //권장몸무게보다 현 사용자의 몸무게가 무거운지 가벼운지 일치하나 구분
    public int checkIdealWeight() {


        if (user.getWeight() > maxWeight) {
            return 1; //권장 몸무게보다 무거움 -> 체중 감량 필요
        } else if (user.getWeight() >= minWeight && user.getWeight() <= maxWeight) {
            return 2; //권장 몸무게 범위 내
        } else if (user.getWeight() < minWeight) {
            return 3; //권장 몸무게보다 가벼움 -> 체중 증량 필요
        } else {
            return 0; // 기본값: 유효하지 않은 경우
        }
    }


    //사용자의 현상황에 적합한 제안 출력



}











