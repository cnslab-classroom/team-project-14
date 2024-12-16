import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class HealthCareApp {
    public static void main(String[] args) {
        new IntroScreen();
    }
}

class IntroScreen {
    IntroScreen() {
        JFrame frame = new JFrame("헬스케어 앱");
        JLabel label = new JLabel("Welcome to HealthCare App!", SwingConstants.CENTER);
        label.setFont(label.getFont().deriveFont(20.0f));

        JButton startButton = new JButton("시작하기");
        startButton.addActionListener(e -> {
            frame.dispose();
            new LoginScreen();
        });

        frame.add(label, BorderLayout.CENTER);
        frame.add(startButton, BorderLayout.SOUTH);
        frame.setSize(400, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); // 화면 중앙에 위치
        frame.setVisible(true);
    }
}

class LoginScreen {
    private static final Map<String, String> userDatabase = new HashMap<>();

    LoginScreen() {
        JFrame frame = new JFrame("로그인");
        JLabel userLabel = new JLabel("아이디:");
        JLabel passLabel = new JLabel("비밀번호:");
        JTextField userField = new JTextField(15);
        JPasswordField passField = new JPasswordField(15);
        JButton loginButton = new JButton("로그인");
        JButton signupButton = new JButton("회원가입");

        loginButton.addActionListener(e -> {
            String username = userField.getText();
            String password = String.valueOf(passField.getPassword());
            if (userDatabase.containsKey(username) && userDatabase.get(username).equals(password)) {
                JOptionPane.showMessageDialog(frame, "로그인 성공!");
                frame.dispose();
                new MainMenuScreen(username);
            } else {
                JOptionPane.showMessageDialog(frame, "로그인 실패: 아이디 또는 비밀번호를 확인하세요.");
            }
        });

        signupButton.addActionListener(e -> {
            frame.dispose();
            new SignupScreen();
        });

        JPanel panel = new JPanel();
        panel.add(userLabel);
        panel.add(userField);
        panel.add(passLabel);
        panel.add(passField);
        panel.add(loginButton);
        panel.add(signupButton);

        frame.add(panel);
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); // 화면 중앙에 위치
        frame.setVisible(true);
    }

    public static void registerUser(String username, String password) {
        userDatabase.put(username, password);
    }

    public static boolean isUserExists(String username) {
        return userDatabase.containsKey(username);
    }
}

class SignupScreen {
    SignupScreen() {
        JFrame frame = new JFrame("회원가입");
        JLabel nameLabel = new JLabel("이름:");
        JLabel userLabel = new JLabel("아이디:");
        JLabel passLabel = new JLabel("비밀번호:");
        JTextField nameField = new JTextField(15);
        JTextField userField = new JTextField(15);
        JPasswordField passField = new JPasswordField(15);
        JButton signupButton = new JButton("가입하기");

        signupButton.addActionListener(e -> {
            String name = nameField.getText();
            String username = userField.getText();
            String password = String.valueOf(passField.getPassword());

            if (username.isEmpty() || password.isEmpty() || name.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "모든 필드를 채워주세요.");
            } else if (LoginScreen.isUserExists(username)) {
                JOptionPane.showMessageDialog(frame, "이미 존재하는 아이디입니다.");
            } else {
                LoginScreen.registerUser(username, password);
                JOptionPane.showMessageDialog(frame, "회원가입 성공! 로그인 화면으로 이동합니다.");
                frame.dispose();
                new LoginScreen();
            }
        });

        JPanel panel = new JPanel();
        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(userLabel);
        panel.add(userField);
        panel.add(passLabel);
        panel.add(passField);
        panel.add(signupButton);

        frame.add(panel);
        frame.setSize(300, 250);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); // 화면 중앙에 위치
        frame.setVisible(true);
    }
}

class MainMenuScreen {
    MainMenuScreen(String username) {
        JFrame frame = new JFrame("메인 메뉴 - " + username);
        frame.setLayout(new GridLayout(4, 1, 10, 10));

        JButton userInfoButton = new JButton("사용자 정보 입력");
        JButton healthAnalysisButton = new JButton("건강 지표 분석");
        JButton recordButton = new JButton("운동 및 식단 기록");
        JButton recommendationButton = new JButton("운동 및 식단 추천");
        
        // "사용자 정보 입력" 버튼 클릭 시 UserInfoScreen으로 이동
        userInfoButton.addActionListener(e -> new UserInfoScreen());

        frame.add(userInfoButton);
        frame.add(healthAnalysisButton);
        frame.add(recordButton);
        frame.add(recommendationButton);

        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); // 화면 중앙에 위치
        frame.setVisible(true);
    }
}
