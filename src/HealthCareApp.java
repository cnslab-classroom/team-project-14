import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


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

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 아이디 레이블과 필드
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(userLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(userField, gbc);

        // 비밀번호 레이블과 필드
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(passLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(passField, gbc);

        // 로그인, 회원가입 버튼
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.add(loginButton);
        buttonPanel.add(signupButton);
        panel.add(buttonPanel, gbc);

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

      JLabel userLabel = new JLabel("아이디:");
      JLabel passLabel = new JLabel("비밀번호:");
      JTextField userField = new JTextField(15);
      JPasswordField passField = new JPasswordField(15);
      JButton signupButton = new JButton("가입하기");

      signupButton.addActionListener(e -> {
          String username = userField.getText();
          String password = String.valueOf(passField.getPassword());

          if (username.isEmpty() || password.isEmpty()) {
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

      // Set up the layout
      JPanel panel = new JPanel(new GridBagLayout());
      GridBagConstraints gbc = new GridBagConstraints();
      gbc.insets = new Insets(10, 10, 10, 10);
      gbc.fill = GridBagConstraints.HORIZONTAL;

      // 아이디 라벨 및 필드
      gbc.gridx = 0;
      gbc.gridy = 0;
      panel.add(userLabel, gbc);

      gbc.gridx = 1;
      panel.add(userField, gbc);

      // 비밀번호 라벨 및 필드
      gbc.gridx = 0;
      gbc.gridy = 1;
      panel.add(passLabel, gbc);

      gbc.gridx = 1;
      panel.add(passField, gbc);

      // 가입하기 버튼
      gbc.gridx = 0;
      gbc.gridy = 2;
      gbc.gridwidth = 2;
      gbc.anchor = GridBagConstraints.CENTER;
      panel.add(signupButton, gbc);

      // Add panel to frame
      frame.add(panel);
      frame.setSize(300, 200);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setLocationRelativeTo(null); // Center the frame on the screen
      frame.setVisible(true);
  }

  public static void main(String[] args) {
      new SignupScreen();
  }
}




class MainMenuScreen {
    private Recommendation recommendation;

   
    MainMenuScreen(String username) {
        JFrame frame = new JFrame("메인 메뉴 - " + username);
        frame.setLayout(new GridLayout(5, 1, 10, 10)); // 4 -> 5로 변경하여 BMI 버튼 추가

        JButton userInfoButton = new JButton("사용자 정보 입력");
        JButton healthAnalysisButton = new JButton("건강 지표 분석");
        JButton recordButton = new JButton("운동 및 식단 기록");
        JButton recommendationButton = new JButton("운동 및 식단 추천");
        
        // "사용자 정보 입력" 버튼 클릭 시 UserInfoScreen으로 이동
        userInfoButton.addActionListener(e -> new UserInfoScreen());

        // "건강 지표 분석" 버튼 클릭 시 HealthMetricSwing 화면 호출
        healthAnalysisButton.addActionListener(null);

        // "운동 및 식단 기록" 버튼 클릭 시 HealthMetricSwing 화면 호출
        recordButton.addActionListener(null);

        // "운동 및 식단 추천" 버튼 클릭 시 HealthMetricSwing 화면 호출
        recommendationButton.addActionListener(null);

        recommendationButton.addActionListener(e -> {
            // Recommendation 객체 생성
            
            new RecommendationGUI(recommendation);
        });

        

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


class RecommendationGUI {
    private Recommendation recommendation;

    public RecommendationGUI(Recommendation recommendation) {
        this.recommendation = recommendation;
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("운동 및 식단 추천");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JTextArea outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        outputArea.setText("환영합니다! 아래 3개의 버튼 중 하나를 눌러를 원하는 결과를 확인해 보세요!");

        JScrollPane scrollPane = new JScrollPane(outputArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JButton recommendationButton = new JButton("운동 및 식단 추천");
        gbc.gridx = 0;
        gbc.gridy = 0;
        buttonPanel.add(recommendationButton, gbc);

        JButton weeklyReportButton = new JButton("주간 분석");
        gbc.gridx = 1;
        buttonPanel.add(weeklyReportButton, gbc);

        JButton monthlyReportButton = new JButton("월간 분석");
        gbc.gridx = 2;
        buttonPanel.add(monthlyReportButton, gbc);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Add action listeners
        recommendationButton.addActionListener(e -> {
            String result = recommendation.generateRecommendation();
            outputArea.setText(result);
        });

        weeklyReportButton.addActionListener(e -> {
            String result = recommendation.generateWeeklyReportFromStartOfMonth();
            outputArea.setText(result);
        });

        monthlyReportButton.addActionListener(e -> {
            String result = recommendation.generateMonthlyReport();
            outputArea.setText(result);
        });

        frame.add(panel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
