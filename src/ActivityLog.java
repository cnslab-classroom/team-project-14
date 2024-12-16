import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

//수정
//수정
//수정

public class ActivityLog {
    // 운동 및 식단 데이터를 저장할 리스트 (배열 형식으로 관리)
    private List<String[]> activities;

    // 생성자
    public ActivityLog() {
        this.activities = new ArrayList<>();
    }

    // HealthMetric 데이터를 추가하는 메서드 (이제 User 객체를 통해 연동)
    public void addHealthMetric(User user, String date) {
        if (user.getHealthMetric() == null) {
            System.out.println("HealthMetric 정보가 없습니다.");
            return; // 혹은 예외 처리
        }
        double bmi = user.getHealthMetric().getBmi();
        double bodyFatPercentage = user.getHealthMetric().getBodyFatPercentage();
        double sleepHours = user.getHealthMetric().getSleepHours();

        activities.add(new String[] {
                "건강",
                "BMI: " + bmi + ", 체지방률: " + bodyFatPercentage + "%, 수면시간: " + sleepHours + "시간",
                "",
                date.toString()
        });
    }

    public boolean isActivityExist(String date) {
        return activities.stream().anyMatch(a -> a[3].equals(date));
    }

    // 활동 추가 메서드 (건강 정보도 함께 기록)
    public void addActivity(User user, String type, String name, int calories, String date) {
        // 활동 기록에 사용자 정보를 포함
        String username = user.getName();
        String userId = user.getUserId();

        // 건강 정보를 활동 기록에 추가
        addHealthMetric(user, date);

        // 운동 정보 및 식단 정보도 추가
        String exerciseLog = String.join(", ", user.getExerciseLog());
        String dietLog = String.join(", ", user.getDietLog());

        // 활동 기록 추가
        activities.add(new String[] {
                type,
                name,
                String.valueOf(calories),
                date.toString(),
                "사용자: " + username + ", ID: " + userId,
                "운동: " + exerciseLog,
                "식단: " + dietLog
        });
    }

    // 특정 날짜의 총 칼로리 소모/섭취 계산
    public int getTotalCalories(String type, LocalDate date) {
        return activities.stream()
                .filter(a -> a[0].equalsIgnoreCase(type) && a[3].equals(date.toString()))
                .mapToInt(a -> Integer.parseInt(a[2]))
                .sum();
    }

    // 기록 배열로 저장
    public String[][] saveToArray() {
        return activities.toArray(new String[0][]);
    }

    // 배열로부터 기록 불러오기
    public void loadFromArray(String[][] data) {
        activities.clear();
        for (String[] activity : data) {
            activities.add(activity);
        }
    }

    // 활동 기록 출력
    public void printActivities() {
        for (String[] activity : activities) {
            System.out.println(String.join(", ", activity));
        }
    }
}