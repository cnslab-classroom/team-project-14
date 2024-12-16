import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ActivityLog {
    // 운동 및 식단 데이터를 저장할 리스트 (배열 형식으로 관리)
    private List<String[]> activities;

    // 생성자
    public ActivityLog() {
        this.activities = new ArrayList<>();
    }

    // 활동 기록 추가
    public void addActivity(String type, String name, int calories, LocalDate date) {
        activities.add(new String[] { type, name, String.valueOf(calories), date.toString() });
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
        activities.forEach(a -> System.out.println(
                String.format("%s: %s, %s칼로리, 날짜: %s", a[0], a[1], a[2], a[3])));
    }

    public static void main(String[] args) {
        ActivityLog log = new ActivityLog();

        // 데이터 추가
        log.addActivity("운동", "달리기", 300, LocalDate.now());
        log.addActivity("식단", "샐러드", 200, LocalDate.now());

        // 총 칼로리 계산
        System.out.println("오늘 운동 소모 칼로리: " + log.getTotalCalories("운동", LocalDate.now()));
        System.out.println("오늘 식단 섭취 칼로리: " + log.getTotalCalories("식단", LocalDate.now()));

        // 배열로 저장 및 불러오기
        String[][] savedData = log.saveToArray();
        System.out.println("배열로 저장된 데이터:");
        for (String[] activity : savedData) {
            System.out.println(String.join(", ", activity));
        }

        log.loadFromArray(savedData);
        System.out.println("배열로부터 불러온 데이터:");
        log.printActivities();
    }
}