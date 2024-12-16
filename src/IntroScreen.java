import javax.swing.*;

public class IntroScreen {
    public static void main(String[] args) {
        JFrame frame = new JFrame("헬스케어 앱");
        JLabel label = new JLabel("Welcome to HealthCare App!", SwingConstants.CENTER);
        label.setFont(label.getFont().deriveFont(20.0f));
        
        JButton startButton = new JButton("시작하기");
        startButton.addActionListener(e -> {
            frame.dispose(); // 인트로 화면 종료
            new LoginScreen(); // 로그인 화면 열기
        });

        frame.add(label, "Center");
        frame.add(startButton, "South");
        frame.setSize(400, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
