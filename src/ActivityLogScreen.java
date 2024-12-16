import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ActivityLogScreen {
    private ArrayList<String> activityLogs = new ArrayList<>();
    private ActivityLog activityLog;

    public ActivityLogScreen(User user, ActivityLog activityLog) {
        this.activityLog = activityLog;

        JFrame frame = new JFrame("운동 및 식단 기록 - " + user);
        frame.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("운동 및 식단 기록", SwingConstants.CENTER);
        frame.add(titleLabel, BorderLayout.NORTH);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(8, 2, 10, 10));

        JLabel activityTypeLabel = new JLabel("운동 종류:");
        JLabel nameLabel = new JLabel("운동 이름:");
        JLabel dateLabel = new JLabel("날짜 (YYYY-MM-DD):");
        JLabel breakfastLabel = new JLabel("아침:");
        JLabel lunchLabel = new JLabel("점심:");
        JLabel dinnerLabel = new JLabel("저녁:");

        JTextField activityTypeField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField dateField = new JTextField();
        JTextField breakfastField = new JTextField();
        JTextField lunchField = new JTextField();
        JTextField dinnerField = new JTextField();

        inputPanel.add(activityTypeLabel);
        inputPanel.add(activityTypeField);
        inputPanel.add(nameLabel);
        inputPanel.add(nameField);
        inputPanel.add(dateLabel);
        inputPanel.add(dateField);
        inputPanel.add(breakfastLabel);
        inputPanel.add(breakfastField);
        inputPanel.add(lunchLabel);
        inputPanel.add(lunchField);
        inputPanel.add(dinnerLabel);
        inputPanel.add(dinnerField);

        frame.add(inputPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton saveButton = new JButton("기록 저장");
        JButton cancelButton = new JButton("취소");
        JButton viewActivityButton = new JButton("운동 기록 확인");
        JButton viewDietButton = new JButton("식단 기록 확인");
        JButton viewHealthMetricsButton = new JButton("건강 지표 확인");

        // 기록 저장 버튼의 이벤트 리스너
        saveButton.addActionListener(e -> {
            // 운동 기록 저장
            String activityType = activityTypeField.getText();
            String name = nameField.getText();
            String date = dateField.getText();
            String breakfast = breakfastField.getText();
            String lunch = lunchField.getText();
            String dinner = dinnerField.getText();

            // ActivityLog에 기록 추가
            activityLog.addActivity(user, activityType, name, date);
            activityLog.addDiet(user, breakfast, lunch, dinner, date);

            JOptionPane.showMessageDialog(frame, "기록이 저장되었습니다.");
        });

        // 취소 버튼의 이벤트 리스너
        cancelButton.addActionListener(e -> {
            // 입력 필드 초기화
            activityTypeField.setText("");
            nameField.setText("");
            dateField.setText("");
            breakfastField.setText("");
            lunchField.setText("");
            dinnerField.setText("");
        });

        // 운동 기록 확인 버튼의 이벤트 리스너
        viewActivityButton.addActionListener(e -> {
            // 운동 기록 확인 로직 추가
            StringBuilder activityRecords = new StringBuilder();
            for (String[] activity : activityLog.getActivities()) {
                if (!activity[0].equals("식단") && !activity[0].equals("건강")) {
                    activityRecords.append(
                            String.format("날짜: %s - 운동 종류: %s - 운동 이름: %s\n", activity[2], activity[0], activity[1]));
                }
            }
            JOptionPane.showMessageDialog(frame, activityRecords.toString(), "운동 기록", JOptionPane.INFORMATION_MESSAGE);
        });

        // 식단 기록 확인 버튼의 이벤트 리스너
        viewDietButton.addActionListener(e -> {
            // 식단 기록 확인 로직 추가
            StringBuilder dietRecords = new StringBuilder();
            for (String[] activity : activityLog.getActivities()) {
                if (activity[0].equals("식단")) {
                    dietRecords.append(String.format("날짜: %s - 아침: %s - 점심: %s - 저녁: %s\n", activity[3], activity[1],
                            activity[2], activity[6]));
                }
            }
            JOptionPane.showMessageDialog(frame, dietRecords.toString(), "식단 기록", JOptionPane.INFORMATION_MESSAGE);
        });

        // 건강 지표 확인 버튼의 이벤트 리스너
        viewHealthMetricsButton.addActionListener(e -> {
            // 건강 지표 확인 로직 추가
            StringBuilder healthMetrics = new StringBuilder();
            for (String[] activity : activityLog.getActivities()) {
                if (activity[0].equals("건강")) {
                    healthMetrics.append(activity[1]).append("\n");
                }
            }
            JOptionPane.showMessageDialog(frame, healthMetrics.toString(), "건강 지표", JOptionPane.INFORMATION_MESSAGE);
        });

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(viewActivityButton);
        buttonPanel.add(viewDietButton);
        buttonPanel.add(viewHealthMetricsButton);

        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}