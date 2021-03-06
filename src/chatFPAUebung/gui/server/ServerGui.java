package chatFPAUebung.gui.server;

import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class ServerGui extends JFrame
{
	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	private JButton btnStart;
	private JButton btnStop;
	private JTextField txtPortnr;
	private JLabel lblPortnummer;
	private JLabel lblFehlermeldung;
	private JButton btnWartung;
	private JTextField txtWartung;

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
					ServerGui frame = new ServerGui();
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
	public ServerGui()
	{
		initialize();
	}

	private void initialize()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 166);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		contentPane.add(getBtnStart());
		contentPane.add(getBtnStop());
		contentPane.add(getTxtPortnr());
		contentPane.add(getLblPortnummer());
		contentPane.add(getLblFehlermeldung());
		this.setTitle("Server");
		contentPane.add(getBtnWartung());
		contentPane.add(getTxtWartung());
	}

	public JButton getBtnStart()
	{
		if (btnStart == null)
		{
			btnStart = new JButton("Start");
			btnStart.setBounds(10, 43, 89, 23);
		}
		return btnStart;
	}

	public JButton getBtnStop()
	{
		if (btnStop == null)
		{
			btnStop = new JButton("Stop");
			btnStop.setBounds(335, 43, 89, 23);
		}
		return btnStop;
	}

	public JTextField getTxtPortnr()
	{
		if (txtPortnr == null)
		{
			txtPortnr = new JTextField();
			txtPortnr.setBounds(335, 12, 86, 20);
			txtPortnr.setColumns(10);
		}
		return txtPortnr;
	}

	private JLabel getLblPortnummer()
	{
		if (lblPortnummer == null)
		{
			lblPortnummer = new JLabel("Portnummer:");
			lblPortnummer.setBounds(10, 15, 313, 14);
		}
		return lblPortnummer;
	}

	public JLabel getLblFehlermeldung()
	{
		if (lblFehlermeldung == null)
		{
			lblFehlermeldung = new JLabel("");
			lblFehlermeldung.setBounds(10, 111, 414, 14);
		}//iidid
		return lblFehlermeldung;
	}
	private JButton getBtnWartung() {
		if (btnWartung == null) {
			btnWartung = new JButton("Wartung ");
			btnWartung.setBounds(130, 79, 89, 23);
		}
		return btnWartung;
	}
	private JTextField getTxtWartung() {
		if (txtWartung == null) {
			txtWartung = new JTextField();
			txtWartung.setBounds(10, 80, 96, 20);
			txtWartung.setColumns(10);
		}
		return txtWartung;
	}
}
