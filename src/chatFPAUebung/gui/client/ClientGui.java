package chatFPAUebung.gui.client;

import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import chatFPAUebung.klassen.Nachricht;

public class ClientGui extends JFrame
{
	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	private JList<Nachricht> list;
	private JButton btnSenden;
	private JTextField textFieldNachricht;
	private JLabel lblFehlermeldung;
	private JScrollPane scrollPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					ClientGui frame = new ClientGui();
					frame.setVisible(true);
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ClientGui()
	{
		initialize();
	}

	private void initialize()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 369, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		contentPane.add(getScrollPane());
		contentPane.add(getBtnSenden());
		contentPane.add(getTextFieldNachricht());
		contentPane.add(getLblFehlermeldung());
	}

	public JList<Nachricht> getList()
	{
		if (list == null)
		{
			list = new JList<Nachricht>();
		}
		return list;
	}

	public JButton getBtnSenden()
	{
		if (btnSenden == null)
		{
			btnSenden = new JButton("Start");
			btnSenden.setBounds(253, 208, 89, 23);
		}
		return btnSenden;
	}

	public JTextField getTextFieldNachricht()
	{
		if (textFieldNachricht == null)
		{
			textFieldNachricht = new JTextField();
			textFieldNachricht.setBounds(10, 208, 86, 20);
			textFieldNachricht.setColumns(10);
		}
		return textFieldNachricht;
	}

	public JLabel getLblFehlermeldung()
	{
		if (lblFehlermeldung == null)
		{
			lblFehlermeldung = new JLabel("");
			lblFehlermeldung.setBounds(10, 239, 428, 14);
		}
		return lblFehlermeldung;
	}

	private JScrollPane getScrollPane()
	{
		if (scrollPane == null)
		{
			scrollPane = new JScrollPane();
			scrollPane.setBounds(10, 11, 332, 186);
			scrollPane.setViewportView(getList());
		}
		return scrollPane;
	}
}
