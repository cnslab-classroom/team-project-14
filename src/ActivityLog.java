import java.util.ArrayList;
import java.util.List;

public class ActivityLog {
    private List<String[]> activities;

    public ActivityLog() {
        this.activities = new ArrayList<>();
    }

    public void addActivity(User user, String type, String name, String date) {
        activities.add(new String[] {
                type,
                name,
                date,
                user.getName(),
                user.getUserId()
        });
    }

    public void addDiet(User user, String breakfast, String lunch, String dinner, String date) {
        activities.add(new String[] {
                "식단",
                breakfast,
                lunch,
                date,
                user.getName(),
                user.getUserId(),
                dinner
        });
    }

    public List<String[]> viewActivities(User user) {
        List<String[]> userActivities = new ArrayList<>();
        for (String[] activity : activities) {
            if (activity[4].equals(user.getName())) {
                userActivities.add(activity);
            }
        }
        return userActivities;
    }

    // 모든 활동 기록을 반환하는 메서드 추가
    public List<String[]> getActivities() {
        return activities;
    }
}