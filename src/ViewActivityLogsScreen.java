import javax.swing.*;
import java.awt.*;
import java.util.List;

class ViewActivityLogsScreen {
    ViewActivityLogsScreen(String username, List<String[]> activityLogs) {
        JFrame frame = new JFrame("운동 기록 확인 - " + username);
        frame.setLayout(new BorderLayout());

        // 제목 레이블
        JLabel titleLabel = new JLabel("운동 기록", SwingConstants.CENTER);
        frame.add(titleLabel, BorderLayout.NORTH);

        // 운동 기록을 보여주는 텍스트 영역
        JTextArea logsArea = new JTextArea();
        logsArea.setEditable(false); // 편집 불가능하게 설정
        for (String[] log : activityLogs) {
            logsArea.append(String.join(", ", log) + "\n\n"); // 각 기록을 텍스트 영역에 추가
        }

        // 스크롤 패널에 추가
        JScrollPane scrollPane = new JScrollPane(logsArea);
        frame.add(scrollPane, BorderLayout.CENTER);

        // 닫기 버튼
        JButton closeButton = new JButton("닫기");
        closeButton.addActionListener(e -> {
            frame.dispose(); // 창 닫기
        });

        frame.add(closeButton, BorderLayout.SOUTH);

        // 화면 설정
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null); // 화면 중앙에 위치
        frame.setVisible(true);
    }
}