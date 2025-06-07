package Telas;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JTable;
import java.awt.Color;

public class TelaRegistro extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable tabelaRegistro;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TelaRegistro frame = new TelaRegistro();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public TelaRegistro() {
		setTitle("Tela de Registro");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(550, 245, 700, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel registroTexto = new JLabel("Registros");
		registroTexto.setFont(new Font("Tahoma", Font.BOLD, 16));
		registroTexto.setHorizontalAlignment(SwingConstants.CENTER);
		registroTexto.setBounds(208, 13, 274, 25);
		contentPane.add(registroTexto);
		
		JButton botaoProrrogar = new JButton("Prorrogar");
		botaoProrrogar.setBackground(Color.WHITE);
		botaoProrrogar.setFont(new Font("Segoe UI Black", Font.BOLD, 15));
		botaoProrrogar.setBounds(96, 415, 135, 35);
		contentPane.add(botaoProrrogar);
		
		JButton botaoDevolver = new JButton("Devolver");
		botaoDevolver.setBackground(Color.WHITE);
		botaoDevolver.setFont(new Font("Segoe UI Black", Font.PLAIN, 15));
		botaoDevolver.setBounds(475, 415, 124, 35);
		contentPane.add(botaoDevolver);
		
		tabelaRegistro = new JTable();
		tabelaRegistro.setBounds(10, 49, 664, 355);
		contentPane.add(tabelaRegistro);
	}

}
