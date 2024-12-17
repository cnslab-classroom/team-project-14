
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Recommendation {

   
    private User user;
    
    private ActivityLog activityLog;

    private double[] idealWeightRange;
    private double minWeight;
    private double maxWeight;

    public Recommendation(User user, ActivityLog activityLog) {
        this.user = user;
        this.activityLog = activityLog;
     

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


    public String generateWeeklyReport() {
        List<String[]> activities = activityLog.getActivities(); // 모든 기록을 가져옴
        Map<String, List<String>> groupedRecords = new TreeMap<>(); // 날짜별 기록을 저장하기 위한 Map
        int[] weekRanges = {7, 14, 21, 28}; // 주간 범위의 끝 숫자 설정
    
        // 날짜별로 기록 분류
        for (String[] activity : activities) {
            String date = (activity[0].equals("식단")) ? activity[3] : activity[2]; // 식단 날짜는 activity[3], 운동 날짜는 activity[2]
            groupedRecords.putIfAbsent(date, new ArrayList<>());
    
            if (activity[0].equals("식단")) { // 식단 기록
                groupedRecords.get(date).add(String.format("  - 아침: %s - 점심: %s - 저녁: %s", activity[1], activity[2], activity[6]));
            } else { // 운동 기록
                groupedRecords.get(date).add(String.format("  - 운동 이름(운동 종류): %s (%s)", activity[1], activity[0]));
            }
        }
    
        // 가장 최근 완성된 주간 리포트를 찾기
        StringBuilder report = new StringBuilder("===== 주간 리포트 =====\n");
        boolean reportGenerated = false;
    
        for (int i = weekRanges.length - 1; i >= 0; i--) {
            int range = weekRanges[i];
            List<String> selectedDates = new ArrayList<>();
    
            // 해당 주차 날짜만 선택
            for (String date : groupedRecords.keySet()) {
                int day = Integer.parseInt(date.substring(8));
                if (day <= range) {
                    selectedDates.add(date);
                }
            }
    
            if (selectedDates.size() == range) { // 해당 주차의 모든 데이터를 가져왔을 때만 리포트 생성
                for (String date : selectedDates) {
                    report.append(String.format("날짜: %s\n", date));
                    for (String detail : groupedRecords.get(date)) {
                        report.append(detail).append("\n");
                    }
                }
                reportGenerated = true;
                break; // 가장 최근 완성된 주간 리포트만 출력
            }
        }
    
        if (!reportGenerated) {
            return "아직 주간 리포트를 생성할 데이터가 부족합니다. 모든 데이터를 입력하세요.";
        }
    
        return report.toString();
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

    private Map<LocalDate, List<String[]>> groupActivitiesByDate(List<String[]> activities) {
        Map<LocalDate, List<String[]>> grouped = new TreeMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
        for (String[] activity : activities) {
            // activity[2]는 날짜 (예: "2024-12-01")입니다.
            LocalDate date = LocalDate.parse(activity[2], formatter);
            grouped.computeIfAbsent(date, k -> new ArrayList<>()).add(activity);
        }
    
        return grouped;
    }
    
    public String debugActivities() {
        StringBuilder debugLog = new StringBuilder("===== Debugging Activities =====\n");
        for (String[] activity : activityLog.getActivities()) {
            debugLog.append(String.format("Type: %s, Name: %s, Date: %s\n", activity[0], activity[1], activity[2]));
        }
        return debugLog.toString();
    }

    public String generateDailySummary() {
        List<String[]> activities = activityLog.getActivities(); // 모든 기록을 가져옴
        Map<String, StringBuilder> groupedRecords = new TreeMap<>(); // 날짜별 기록을 저장하기 위한 Map
    
        // 날짜별로 기록 분류
        for (String[] activity : activities) {
            String date = (activity[0].equals("식단")) ? activity[3] : activity[2]; // 식단 날짜는 activity[3], 운동 날짜는 activity[2]
            groupedRecords.putIfAbsent(date, new StringBuilder());
    
            if (activity[0].equals("식단")) { // 식단 기록
                groupedRecords.get(date).append(String.format(
                    "아침: %s - 점심: %s - 저녁: %s\n", activity[1], activity[2], activity[6]
                ));
            } else { // 운동 기록
                groupedRecords.get(date).append(String.format(
                    "운동 이름(운동 종류): %s (%s)\n", activity[1], activity[0]
                ));
            }
        }
    
        // 최종 출력 문자열 생성
        StringBuilder result = new StringBuilder("===== 월간 리포트 =====\n");
        for (Map.Entry<String, StringBuilder> entry : groupedRecords.entrySet()) {
            result.append(String.format("날짜: %s\n", entry.getKey()));
            result.append(entry.getValue());
            result.append("\n");
        }
    
        if (result.toString().equals("===== 월간 리포트 =====\n")) {
            return "아직 입력된 기록이 없습니다.";
        }
    
        return result.toString();
    }
    
    


    
    
}