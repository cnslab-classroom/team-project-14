import javax.swing.*;
class LoginScreen {
  LoginScreen() {
      JFrame frame = new JFrame("로그인");
      JLabel userLabel = new JLabel("아이디:");
      JLabel passLabel = new JLabel("비밀번호:");
      JTextField userField = new JTextField(15);
      JPasswordField passField = new JPasswordField(15);
      JButton loginButton = new JButton("로그인");
      JButton signupButton = new JButton("회원가입");

      loginButton.addActionListener(e -> JOptionPane.showMessageDialog(frame, "로그인 성공!"));
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
      frame.setVisible(true);
  }
}
