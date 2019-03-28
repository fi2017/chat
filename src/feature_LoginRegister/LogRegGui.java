package feature_LoginRegister;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JLabel;

public class LogRegGui extends JFrame
{

	private JPanel contentPane;
	private JButton btnLogin;
	private JButton btnReg;
	private JTextField textFieldUnameLog;
	private JTextField textFieldPwLog;
	private JTextField textFieldUnameReg;
	private JTextField textFieldPwReg;
	private JTextField textFieldPwRepeat;
	private JLabel lblUsername;
	private JLabel lblPasswort;
	

	/**
	 * Create the frame.
	 */
	public LogRegGui()
	{
		initialize();
	}
	private void initialize() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 335, 189);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		contentPane.add(getBtnLogin());
		contentPane.add(getBtnReg());
		contentPane.add(getTextFieldUnameLog());
		contentPane.add(getTextFieldPwLog());
		contentPane.add(getTextFieldUnameReg());
		contentPane.add(getTextFieldPwReg());
		contentPane.add(getTextFieldPwRepeat());
		contentPane.add(getLblUsername());
		contentPane.add(getLblPasswort());
	}
	private JButton getBtnLogin() {
		if (btnLogin == null) {
			btnLogin = new JButton("Einloggen");
			btnLogin.setBounds(61, 81, 116, 23);
		}
		return btnLogin;
	}
	private JButton getBtnReg() {
		if (btnReg == null) {
			btnReg = new JButton("Registrieren");
			btnReg.setBounds(187, 113, 117, 23);
		}
		return btnReg;
	}
	private JTextField getTextFieldUnameLog() {
		if (textFieldUnameLog == null) {
			textFieldUnameLog = new JTextField("");
			textFieldUnameLog.setBounds(61, 20, 116, 20);
			textFieldUnameLog.setColumns(10);
		}
		return textFieldUnameLog;
	}
	private JTextField getTextFieldPwLog() {
		if (textFieldPwLog == null) {
			textFieldPwLog = new JTextField();
			textFieldPwLog.setBounds(61, 51, 116, 20);
			textFieldPwLog.setColumns(10);
		}
		return textFieldPwLog;
	}
	private JTextField getTextFieldUnameReg() {
		if (textFieldUnameReg == null) {
			textFieldUnameReg = new JTextField();
			textFieldUnameReg.setBounds(187, 20, 117, 20);
			textFieldUnameReg.setColumns(10);
		}
		return textFieldUnameReg;
	}
	private JTextField getTextFieldPwReg() {
		if (textFieldPwReg == null) {
			textFieldPwReg = new JTextField();
			textFieldPwReg.setBounds(187, 51, 117, 20);
			textFieldPwReg.setColumns(10);
		}
		return textFieldPwReg;
	}
	private JTextField getTextFieldPwRepeat() {
		if (textFieldPwRepeat == null) {
			textFieldPwRepeat = new JTextField();
			textFieldPwRepeat.setBounds(187, 82, 117, 20);
			textFieldPwRepeat.setColumns(10);
		}
		return textFieldPwRepeat;
	}
	private JLabel getLblUsername() {
		if (lblUsername == null) {
			lblUsername = new JLabel("Username");
			lblUsername.setBounds(10, 23, 68, 14);
		}
		return lblUsername;
	}
	private JLabel getLblPasswort() {
		if (lblPasswort == null) {
			lblPasswort = new JLabel("Passwort");
			lblPasswort.setBounds(10, 54, 46, 14);
		}
		return lblPasswort;
	}
}
