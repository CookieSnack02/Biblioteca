package Telas;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import conexaoBD.ConexaoBancoDados;

import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.event.ActionEvent;

public class TelaEntrada extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField usuarioCampo;
	private JPasswordField senhaCampo;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TelaEntrada frame = new TelaEntrada();
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
	public TelaEntrada() {
		setTitle("Tela de Entrada");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(400, 400, 600, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		usuarioCampo = new JTextField();
		usuarioCampo.setBounds(228, 116, 112, 20);
		contentPane.add(usuarioCampo);
		usuarioCampo.setColumns(10);
		
		senhaCampo = new JPasswordField();
		senhaCampo.setBounds(228, 220, 112, 20);
		contentPane.add(senhaCampo);
		
		JLabel senhaTexto = new JLabel("Senha");
		senhaTexto.setFont(new Font("Tahoma", Font.BOLD, 12));
		senhaTexto.setHorizontalAlignment(SwingConstants.CENTER);
		senhaTexto.setBounds(228, 176, 112, 14);
		contentPane.add(senhaTexto);
		
		JLabel usuarioTexto = new JLabel("Usuário");
		usuarioTexto.setFont(new Font("Tahoma", Font.BOLD, 12));
		usuarioTexto.setHorizontalAlignment(SwingConstants.CENTER);
		usuarioTexto.setBounds(228, 91, 112, 14);
		contentPane.add(usuarioTexto);
		
		JButton btnNewButton = new JButton("Entrar");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String usuario = usuarioCampo.getText();
				String senha = new String(senhaCampo.getText());
				
				try {

					Connection conn = ConexaoBancoDados.createConnectionToMySQL();
					String sql = "SELECT nome_Usuario, senha_Usuario FROM usuario WHERE nome_Usuario =? AND senha_Usuario=?";
					
					
					
					PreparedStatement sts = conn.prepareStatement(sql);
					
					
					sts.setString(1, usuario);
					sts.setString(2, senha);
					
					ResultSet rs = sts.executeQuery();
					
					if(rs.next()) {
						JOptionPane.showMessageDialog(null, "Acesso liberado!");
						
						TelaLivros telaLivros = new TelaLivros();
						telaLivros.setVisible(true);
						dispose();
						
					}
					else{
						JOptionPane.showMessageDialog(null, "Usuário ou senha inválidos");
					}
					
					
				} catch (Exception f) {
					f.printStackTrace();
				}
			}
		});
		btnNewButton.setBackground(Color.WHITE);
		btnNewButton.setFont(new Font("Segoe UI Black", Font.PLAIN, 13));
		btnNewButton.setBounds(228, 300, 112, 36);
		contentPane.add(btnNewButton);
	}

}
