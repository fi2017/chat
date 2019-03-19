package server;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JScrollPane;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.util.EventListener;

import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Gui extends JFrame
{
	private JPanel contentPane;
	private JLabel lblLog;
	private JScrollPane scrollPane;
	private JList<String> listLogMessages;
	private JLabel lblPortnummer;
	private JTextField textFieldPortnummer;
	private JButton btnStart;
	private JLabel lblStatus;
	private DefaultListModel<String> listModelLogMessages;
	private JButton btnStop;

	public Gui()
	{
		initialize();

		listModelLogMessages = new DefaultListModel<>();
		getListLogMessages().setModel(listModelLogMessages);
	}

	private void initialize()
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e)
		{
		}

		setTitle("Server");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 687, 324);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 565, 0, 0 };
		gbl_contentPane.rowHeights = new int[] { 0, 0, 0, 0, 0, 0 };
		gbl_contentPane.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		gbl_contentPane.rowWeights = new double[] { 0.0, 0.0, 1.0, 1.0, 0.0, Double.MIN_VALUE };
		contentPane.setLayout(gbl_contentPane);
		GridBagConstraints gbc_lblLog = new GridBagConstraints();
		gbc_lblLog.insets = new Insets(0, 0, 5, 5);
		gbc_lblLog.gridx = 0;
		gbc_lblLog.gridy = 0;
		contentPane.add(getLblLog(), gbc_lblLog);
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridheight = 3;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 1;
		contentPane.add(getScrollPane(), gbc_scrollPane);
		GridBagConstraints gbc_lblPortnummer = new GridBagConstraints();
		gbc_lblPortnummer.insets = new Insets(0, 0, 5, 0);
		gbc_lblPortnummer.gridx = 1;
		gbc_lblPortnummer.gridy = 0;
		contentPane.add(getLblPortnummer(), gbc_lblPortnummer);
		GridBagConstraints gbc_textFieldPortnummer = new GridBagConstraints();
		gbc_textFieldPortnummer.insets = new Insets(0, 0, 5, 0);
		gbc_textFieldPortnummer.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldPortnummer.gridx = 1;
		gbc_textFieldPortnummer.gridy = 1;
		contentPane.add(getTextFieldPortnummer(), gbc_textFieldPortnummer);
		GridBagConstraints gbc_btnStart = new GridBagConstraints();
		gbc_btnStart.fill = GridBagConstraints.BOTH;
		gbc_btnStart.insets = new Insets(0, 0, 5, 0);
		gbc_btnStart.gridx = 1;
		gbc_btnStart.gridy = 2;
		contentPane.add(getBtnStart(), gbc_btnStart);
		GridBagConstraints gbc_btnStop = new GridBagConstraints();
		gbc_btnStop.fill = GridBagConstraints.BOTH;
		gbc_btnStop.insets = new Insets(0, 0, 5, 0);
		gbc_btnStop.gridx = 1;
		gbc_btnStop.gridy = 3;
		contentPane.add(getBtnStop(), gbc_btnStop);
		GridBagConstraints gbc_lblStatus = new GridBagConstraints();
		gbc_lblStatus.anchor = GridBagConstraints.WEST;
		gbc_lblStatus.gridwidth = 2;
		gbc_lblStatus.gridx = 0;
		gbc_lblStatus.gridy = 4;
		contentPane.add(getLblStatus(), gbc_lblStatus);
	}

	// Komponentenunabhängige Methoden:

	public void addStartListener(ActionListener l)
	{
		getBtnStart().addActionListener(l);
	}

	public void addStopListener(ActionListener l)
	{
		getBtnStop().addActionListener(l);
	}

	public void setStatus(String status)
	{
		getLblStatus().setText(status);
		listModelLogMessages.addElement(status);
		// TODO: Scrolle zum neusten Eintrag
	}

	public int getPortnummer()
	{
		return Integer.parseInt(getTextFieldPortnummer().getText());
	}

	// Generiert:

	private JLabel getLblLog()
	{
		if (lblLog == null)
		{
			lblLog = new JLabel("Log");
		}
		return lblLog;
	}

	private JScrollPane getScrollPane()
	{
		if (scrollPane == null)
		{
			scrollPane = new JScrollPane();
			scrollPane.setViewportView(getListLogMessages());
		}
		return scrollPane;
	}

	private JList<String> getListLogMessages()
	{
		if (listLogMessages == null)
		{
			listLogMessages = new JList<String>();
			listLogMessages.setEnabled(false);
		}
		return listLogMessages;
	}

	private JLabel getLblPortnummer()
	{
		if (lblPortnummer == null)
		{
			lblPortnummer = new JLabel("Portnummer");
		}
		return lblPortnummer;
	}

	private JTextField getTextFieldPortnummer()
	{
		if (textFieldPortnummer == null)
		{
			textFieldPortnummer = new JTextField();
			textFieldPortnummer.setText("8008");
			textFieldPortnummer.setColumns(10);
		}
		return textFieldPortnummer;
	}

	private JButton getBtnStart()
	{
		if (btnStart == null)
		{
			btnStart = new JButton("Start");
		}
		return btnStart;
	}

	private JLabel getLblStatus()
	{
		if (lblStatus == null)
		{
			lblStatus = new JLabel(" ");
			lblStatus.setHorizontalAlignment(SwingConstants.LEFT);
		}
		return lblStatus;
	}

	private JButton getBtnStop()
	{
		if (btnStop == null)
		{
			btnStop = new JButton("Stop");
		}
		return btnStop;
	}
}
