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
		setBounds(100, 100, 367, 189);
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
	public JButton getBtnLogin() {
		if (btnLogin == null) {
			btnLogin = new JButton("Einloggen");
			btnLogin.setBounds(83, 84, 116, 23);
		}
		return btnLogin;
	}
	public JButton getBtnReg() {
		if (btnReg == null) {
			btnReg = new JButton("Registrieren");
			btnReg.setBounds(209, 116, 117, 23);
		}
		return btnReg;
	}
	public JTextField getTextFieldUnameLog() {
		if (textFieldUnameLog == null) {
			textFieldUnameLog = new JTextField("");
			textFieldUnameLog.setBounds(83, 23, 116, 20);
			textFieldUnameLog.setColumns(10);
		}
		return textFieldUnameLog;
	}
	public JTextField getTextFieldPwLog() {
		if (textFieldPwLog == null) {
			textFieldPwLog = new JTextField();
			textFieldPwLog.setBounds(83, 54, 116, 20);
			textFieldPwLog.setColumns(10);
		}
		return textFieldPwLog;
	}
	public JTextField getTextFieldUnameReg() {
		if (textFieldUnameReg == null) {
			textFieldUnameReg = new JTextField();
			textFieldUnameReg.setBounds(209, 23, 117, 20);
			textFieldUnameReg.setColumns(10);
		}
		return textFieldUnameReg;
	}
	public JTextField getTextFieldPwReg() {
		if (textFieldPwReg == null) {
			textFieldPwReg = new JTextField();
			textFieldPwReg.setBounds(209, 54, 117, 20);
			textFieldPwReg.setColumns(10);
		}
		return textFieldPwReg;
	}
	public JTextField getTextFieldPwRepeat() {
		if (textFieldPwRepeat == null) {
			textFieldPwRepeat = new JTextField();
			textFieldPwRepeat.setBounds(209, 85, 117, 20);
			textFieldPwRepeat.setColumns(10);
		}
		return textFieldPwRepeat;
	}
	public JLabel getLblUsername() {
		if (lblUsername == null) {
			lblUsername = new JLabel("Username");
			lblUsername.setBounds(10, 23, 460, 14);
		}
		return lblUsername;
	}
	public JLabel getLblPasswort() {
		if (lblPasswort == null) {
			lblPasswort = new JLabel("Passwort");
			lblPasswort.setBounds(10, 54, 77, 14);
		}
		return lblPasswort;
	}
}
