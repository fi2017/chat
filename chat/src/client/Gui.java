package client;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridBagLayout;
import javax.swing.JTextField;
import javax.swing.UIManager;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;

public class Gui extends JFrame
{
	private JPanel contentPane;
	private JTextField txtPort;
	private JTextField txtAdresse;
	private JButton btnVerbinden;
	private JTextArea txtrNachricht;
	private JButton btnSenden;
	private JLabel lblLog;
	private JScrollPane scrollPane;
	private JList<String> listLogMessages;
	private DefaultListModel<String> listModelLogMessages;
	private JLabel lblStatus;

	public Gui()
	{
		initialize();

		listModelLogMessages = new DefaultListModel<>();
		getListLogMessages().setModel(listModelLogMessages);
		getBtnSenden().addActionListener(e -> entferneNachricht());
	}

	private void initialize()
	{
		setTitle("Client");
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e)
		{
		}

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 488, 424);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 0, 0, 0, 0 };
		gbl_contentPane.rowHeights = new int[] { 0, 56, 0, 0, 17, 0 };
		gbl_contentPane.columnWeights = new double[] { 1.0, 1.0, 0.0, Double.MIN_VALUE };
		gbl_contentPane.rowWeights = new double[] { 0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE };
		contentPane.setLayout(gbl_contentPane);
		GridBagConstraints gbc_txtAdresse = new GridBagConstraints();
		gbc_txtAdresse.insets = new Insets(0, 0, 5, 5);
		gbc_txtAdresse.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtAdresse.gridx = 0;
		gbc_txtAdresse.gridy = 0;
		contentPane.add(getTxtAdresse(), gbc_txtAdresse);
		GridBagConstraints gbc_txtPort = new GridBagConstraints();
		gbc_txtPort.insets = new Insets(0, 0, 5, 5);
		gbc_txtPort.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtPort.gridx = 1;
		gbc_txtPort.gridy = 0;
		contentPane.add(getTxtPort(), gbc_txtPort);
		GridBagConstraints gbc_btnVerbinden = new GridBagConstraints();
		gbc_btnVerbinden.insets = new Insets(0, 0, 5, 0);
		gbc_btnVerbinden.gridx = 2;
		gbc_btnVerbinden.gridy = 0;
		contentPane.add(getBtnVerbinden(), gbc_btnVerbinden);
		GridBagConstraints gbc_txtrNachricht = new GridBagConstraints();
		gbc_txtrNachricht.gridwidth = 2;
		gbc_txtrNachricht.insets = new Insets(0, 0, 5, 5);
		gbc_txtrNachricht.fill = GridBagConstraints.BOTH;
		gbc_txtrNachricht.gridx = 0;
		gbc_txtrNachricht.gridy = 1;
		contentPane.add(getTxtrNachricht(), gbc_txtrNachricht);
		GridBagConstraints gbc_btnSenden = new GridBagConstraints();
		gbc_btnSenden.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnSenden.insets = new Insets(0, 0, 5, 0);
		gbc_btnSenden.gridx = 2;
		gbc_btnSenden.gridy = 1;
		contentPane.add(getBtnSenden(), gbc_btnSenden);
		GridBagConstraints gbc_lblLog = new GridBagConstraints();
		gbc_lblLog.gridwidth = 2;
		gbc_lblLog.insets = new Insets(0, 0, 5, 5);
		gbc_lblLog.gridx = 0;
		gbc_lblLog.gridy = 2;
		contentPane.add(getLblLog(), gbc_lblLog);
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridwidth = 2;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 3;
		contentPane.add(getScrollPane(), gbc_scrollPane);
		GridBagConstraints gbc_lblStatus = new GridBagConstraints();
		gbc_lblStatus.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblStatus.insets = new Insets(0, 0, 0, 5);
		gbc_lblStatus.gridx = 0;
		gbc_lblStatus.gridy = 4;
		contentPane.add(getLblStatus(), gbc_lblStatus);
	}

	// Öffentliche, Komponentenunabhängige Methoden:

	public void addVerbindenListener(ActionListener l)
	{
		getBtnVerbinden().addActionListener(l);
	}

	public void addSendenListener(ActionListener l)
	{
		getBtnSenden().addActionListener(l);
	}

	public String getNachricht()
	{
		return getTxtrNachricht().getText();
	}

	public void setStatus(String status)
	{
		getLblStatus().setText(status);
		listModelLogMessages.addElement(status);
		getListLogMessages().ensureIndexIsVisible(listModelLogMessages.getSize() - 1); //Scrolle zum neusten Eintrag
	}

	public int getPortnummer()
	{
		return Integer.parseInt(getTxtPort().getText());
	}

	public String getHost()
	{
		return getTxtAdresse().getText();
	}

	// Andere Methoden:

	private void entferneNachricht()
	{
		getTxtrNachricht().setText("");
	}

	// Generiert:

	private JTextField getTxtPort()
	{
		if (txtPort == null)
		{
			txtPort = new JTextField();
			txtPort.setText("8008");
			txtPort.setColumns(10);
		}
		return txtPort;
	}

	private JTextField getTxtAdresse()
	{
		if (txtAdresse == null)
		{
			txtAdresse = new JTextField();
			txtAdresse.setText("localhost");
			txtAdresse.setColumns(10);
		}
		return txtAdresse;
	}

	private JButton getBtnVerbinden()
	{
		if (btnVerbinden == null)
		{
			btnVerbinden = new JButton("Verbinden");
		}
		return btnVerbinden;
	}

	private JTextArea getTxtrNachricht()
	{
		if (txtrNachricht == null)
		{
			txtrNachricht = new JTextArea();
			txtrNachricht.setLineWrap(true);
		}
		return txtrNachricht;
	}

	private JButton getBtnSenden()
	{
		if (btnSenden == null)
		{
			btnSenden = new JButton("Senden");
		}
		return btnSenden;
	}

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

	private JLabel getLblStatus()
	{
		if (lblStatus == null)
		{
			lblStatus = new JLabel("");
		}
		return lblStatus;
	}
}
