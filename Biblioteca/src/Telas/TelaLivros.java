package Telas;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JTable;
import java.awt.Color;

public class TelaLivros extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField tituloCampo;
	private JTable tabelaLivros;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TelaLivros frame = new TelaLivros();
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
	public TelaLivros() {
		setTitle("Tela de Livros");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(550, 245, 800, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel tituloLivro = new JLabel("Título");
		tituloLivro.setHorizontalAlignment(SwingConstants.CENTER);
		tituloLivro.setFont(new Font("Tahoma", Font.BOLD, 15));
		tituloLivro.setBounds(10, 28, 74, 14);
		contentPane.add(tituloLivro);
		
		tituloCampo = new JTextField();
		tituloCampo.setHorizontalAlignment(SwingConstants.CENTER);
		tituloCampo.setBounds(94, 28, 114, 19);
		contentPane.add(tituloCampo);
		tituloCampo.setColumns(10);
		
		JComboBox autorSelecionar = new JComboBox();
		autorSelecionar.setBounds(337, 26, 114, 22);
		contentPane.add(autorSelecionar);
		
		JLabel autorLivro = new JLabel("Autor");
		autorLivro.setHorizontalAlignment(SwingConstants.CENTER);
		autorLivro.setFont(new Font("Tahoma", Font.BOLD, 15));
		autorLivro.setBounds(253, 30, 74, 14);
		contentPane.add(autorLivro);
		
		JLabel editoraLivro = new JLabel("Editora");
		editoraLivro.setFont(new Font("Tahoma", Font.BOLD, 15));
		editoraLivro.setHorizontalAlignment(SwingConstants.CENTER);
		editoraLivro.setBounds(515, 30, 74, 14);
		contentPane.add(editoraLivro);
		
		JComboBox editoraSelecionar = new JComboBox();
		editoraSelecionar.setBounds(599, 26, 114, 22);
		contentPane.add(editoraSelecionar);
		
		JButton botaoBuscar = new JButton("Buscar");
		botaoBuscar.setBackground(new Color(255, 255, 255));
		botaoBuscar.setFont(new Font("Segoe UI Black", Font.PLAIN, 15));
		botaoBuscar.setBounds(269, 69, 227, 37);
		contentPane.add(botaoBuscar);
		
		tabelaLivros = new JTable();
		tabelaLivros.setBounds(10, 125, 764, 381);
		contentPane.add(tabelaLivros);
		
		JButton emprestadoBotao = new JButton("Pegar Emprestado");
		emprestadoBotao.setBackground(new Color(255, 255, 255));
		emprestadoBotao.setFont(new Font("Segoe UI Black", Font.PLAIN, 15));
		emprestadoBotao.setBounds(288, 517, 173, 33);
		contentPane.add(emprestadoBotao);
	}
}
