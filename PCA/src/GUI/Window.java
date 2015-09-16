package GUI;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.GridBagLayout;
import java.awt.Dimension;
import javax.swing.JTabbedPane;
import java.awt.GridBagConstraints;
import java.awt.Font;

public class Window {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Window window = new Window();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Window() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setMaximumSize(new Dimension(1024, 768));
		frame.getContentPane().setMinimumSize(new Dimension(1024, 768));
		frame.getContentPane().setPreferredSize(new Dimension(1024, 768));
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		frame.getContentPane().setLayout(gridBagLayout);
		
		JTabbedPane jtpPane = new JTabbedPane(JTabbedPane.TOP);
		jtpPane.setFont(new Font("Tahoma", Font.PLAIN, 14));
		GridBagConstraints gbc_jtpPane = new GridBagConstraints();
		gbc_jtpPane.fill = GridBagConstraints.BOTH;
		gbc_jtpPane.gridx = 0;
		gbc_jtpPane.gridy = 0;
		frame.getContentPane().add(jtpPane, gbc_jtpPane);
		frame.setJMenuBar(new ApplicationMenu(jtpPane));
		frame.pack();
	}

}
