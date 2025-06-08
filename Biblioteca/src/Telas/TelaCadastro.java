package Telas;

import java.awt.EventQueue;

import java.sql.*;

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
import java.awt.event.ActionEvent;

public class TelaCadastro extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField nomeCampo;
	private JTextField sobrenomeCampo;
	private JTextField usuarioCampo;
	private JPasswordField senhaCampo;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TelaCadastro frame = new TelaCadastro();
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
	public TelaCadastro() {
		setTitle("Tela de Cadastro");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(400, 400, 600, 400); 					//X-axis, Y-axis, weight, height 
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		nomeCampo = new JTextField();
		nomeCampo.setBounds(221, 66, 125, 20);
		contentPane.add(nomeCampo);
		nomeCampo.setColumns(10);
		
		sobrenomeCampo = new JTextField();
		sobrenomeCampo.setBounds(221, 131, 125, 20);
		contentPane.add(sobrenomeCampo);
		sobrenomeCampo.setColumns(10);
		
		usuarioCampo = new JTextField();
		usuarioCampo.setBounds(221, 199, 125, 20);
		contentPane.add(usuarioCampo);
		usuarioCampo.setColumns(10);
		
		senhaCampo = new JPasswordField();
		senhaCampo.setBounds(221, 270, 125, 20);
		contentPane.add(senhaCampo);
		
		JLabel nomeTexto = new JLabel("Nome");
		nomeTexto.setFont(new Font("Tahoma", Font.BOLD, 12));
		nomeTexto.setHorizontalAlignment(SwingConstants.CENTER);
		nomeTexto.setBounds(221, 41, 125, 14);
		contentPane.add(nomeTexto);
		
		JLabel sobrenomeTexto = new JLabel("Sobrenome");
		sobrenomeTexto.setFont(new Font("Tahoma", Font.BOLD, 12));
		sobrenomeTexto.setHorizontalAlignment(SwingConstants.CENTER);
		sobrenomeTexto.setBounds(221, 106, 125, 14);
		contentPane.add(sobrenomeTexto);
		
		JLabel usuarioTexto = new JLabel("Usuário");
		usuarioTexto.setFont(new Font("Tahoma", Font.BOLD, 12));
		usuarioTexto.setHorizontalAlignment(SwingConstants.CENTER);
		usuarioTexto.setBounds(221, 174, 125, 14);
		contentPane.add(usuarioTexto);
		
		JLabel senhaTexto = new JLabel("Senha");
		senhaTexto.setFont(new Font("Tahoma", Font.BOLD, 12));
		senhaTexto.setHorizontalAlignment(SwingConstants.CENTER);
		senhaTexto.setBounds(221, 245, 125, 14);
		contentPane.add(senhaTexto);
		
		JButton botaoCadastrar = new JButton("Cadastrar");
		botaoCadastrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ConexaoBancoDados conexao = new ConexaoBancoDados();
				String nome = nomeCampo.getText(); 
				String sobrenome = sobrenomeCampo.getText();
				String usuario = usuarioCampo.getText();
				String senha = new String(senhaCampo.getPassword());
	
				try {
					Connection conn = ConexaoBancoDados.createConnectionToMySQL();					
					String sql = "INSERT INTO usuario(nome, sobrenome, nome_Usuario, senha_Usuario) VALUES (?, ?, ?, ?)";
					
					PreparedStatement sts = conn.prepareStatement(sql);
			        
					sts.setString(1, nome);
					sts.setString(2, sobrenome);
					sts.setString(3, usuario);
					sts.setString(4, senha);
					
					sts.execute();
					JOptionPane.showMessageDialog(null, "Dados cadastrados com sucesso! ");
					sts.close();
					
					TelaEntrada telaEntrada = new TelaEntrada();
					telaEntrada.setVisible(true);
					dispose();
					
				}
				
				catch(Exception f){
					JOptionPane.showMessageDialog(null, f);
				}
				
				
			}
		});
		botaoCadastrar.setBackground(Color.WHITE);
		botaoCadastrar.setFont(new Font("Segoe UI Black", Font.PLAIN, 13));
		botaoCadastrar.setBounds(221, 316, 125, 34);
		contentPane.add(botaoCadastrar);
	}
}
