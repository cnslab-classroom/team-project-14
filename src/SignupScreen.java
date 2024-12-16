import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
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

      signupButton.addActionListener(e -> JOptionPane.showMessageDialog(frame, "회원가입 성공!"));

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
      frame.setVisible(true);
  }
}
