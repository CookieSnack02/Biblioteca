package Telas;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import conexaoBD.ConexaoBancoDados;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.awt.event.ActionEvent;

public class TelaLivros extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textField;
    private JTable table;
    private JComboBox<String> comboBoxAutor;
    private JComboBox<String> comboBoxEditora;
    private JButton registroBotao;

    /**
     * Método principal para iniciar a aplicação
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
     * Construtor da tela de livros
     */
    public TelaLivros() {
        // Configurações básicas da janela
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(600, 300, 750, 550);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // Configuração dos componentes da interface
        configurarComponentes();
        
        // Carrega os dados nos ComboBoxes
        carregarAutores();
        carregarEditoras();
    }

    /**
     * Configura todos os componentes da interface gráfica
     */
    private void configurarComponentes() {
        // Label e campo de texto para título
        JLabel lblTitulo = new JLabel("Titulo");
        lblTitulo.setBounds(10, 23, 46, 14);
        contentPane.add(lblTitulo);
        
        textField = new JTextField();
        textField.setBounds(66, 20, 112, 20);
        contentPane.add(textField);
        textField.setColumns(10);
        
        // ComboBox para autores
        comboBoxAutor = new JComboBox<>();
        comboBoxAutor.setBounds(308, 19, 112, 22);
        comboBoxAutor.addItem("Escolha"); // Item padrão
        contentPane.add(comboBoxAutor);
        
        // ComboBox para editoras
        comboBoxEditora = new JComboBox<>();
        comboBoxEditora.setBounds(552, 19, 112, 22);
        comboBoxEditora.addItem("Escolha"); // Item padrão
        contentPane.add(comboBoxEditora);
        
        // Labels para os ComboBoxes
        JLabel lblAutor = new JLabel("Autor");
        lblAutor.setBounds(251, 23, 46, 14);
        contentPane.add(lblAutor);
        
        JLabel lblEditora = new JLabel("Editora");
        lblEditora.setBounds(496, 23, 46, 14);
        contentPane.add(lblEditora);
        
        // Tabela para exibir os resultados
        table = new JTable();
        table.setBounds(10, 96, 714, 367);
        contentPane.add(table);
        
        // Botão de busca
        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                buscarLivros();
            }
        });
        btnBuscar.setBounds(318, 62, 89, 23);
        contentPane.add(btnBuscar);
        
        // Botão para pegar emprestado (funcionalidade a implementar)
        JButton btnEmprestimo = new JButton("Pegar Emprestado");
        btnEmprestimo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int linhaSelecionada = table.getSelectedRow();
                
                if (linhaSelecionada == -1) {
                    JOptionPane.showMessageDialog(null, "Selecione um livro na tabela!");
                    return;
                }

                String nomeLivro = table.getValueAt(linhaSelecionada, 0).toString();
                String nomeAutor = table.getValueAt(linhaSelecionada, 1).toString();

                Connection conexao = null;
                PreparedStatement stmt = null;
                ResultSet rs = null;

                try {
                    conexao = ConexaoBancoDados.createConnectionToMySQL();

                    // Verifica se o livro está disponível
                    String sqlVerifica = "SELECT id_Livro, emprestado FROM livro L " +
                                         "JOIN autor A ON A.id_Autor = L.id_Autor " +
                                         "WHERE L.nome_Livro = ? AND A.nome_Autor = ?";

                    stmt = conexao.prepareStatement(sqlVerifica);
                    stmt.setString(1, nomeLivro);
                    stmt.setString(2, nomeAutor);
                    rs = stmt.executeQuery();

                    if (rs.next()) {
                        int idLivro = rs.getInt("id_Livro");
                        boolean emprestado = rs.getBoolean("emprestado");

                        if (emprestado) {
                            JOptionPane.showMessageDialog(null, "Este livro já está emprestado!");
                            return;
                        }

                        // Atualiza o livro com status, usuário e datas
                        String sqlAtualiza = "UPDATE livro SET emprestado = true, id_Usuario = ?, " +
                                             "data_emprestimo = CURDATE(), data_devolucao = DATE_ADD(CURDATE(), INTERVAL 7 DAY) " +
                                             "WHERE id_Livro = ?";
                        
                        try (PreparedStatement stmtAtualiza = conexao.prepareStatement(sqlAtualiza)) {
                            stmtAtualiza.setInt(1, 1); // ID do usuário que está pegando emprestado
                            stmtAtualiza.setInt(2, idLivro);
                            int linhasAfetadas = stmtAtualiza.executeUpdate();

                            if (linhasAfetadas > 0) {
                                JOptionPane.showMessageDialog(null, "Livro emprestado com sucesso!");
                                buscarLivros();
                            } else {
                                JOptionPane.showMessageDialog(null, "Erro ao emprestar livro!");
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Livro não encontrado!");
                    }

                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Erro: " + ex.getMessage());
                } finally {
                    try {
                        if (rs != null) rs.close();
                        if (stmt != null) stmt.close();
                        if (conexao != null) conexao.close();
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, "Erro ao fechar conexão: " + ex.getMessage());
                    }
                }
            }
        });

        btnEmprestimo.setBounds(299, 474, 121, 23);
        contentPane.add(btnEmprestimo);
        
        registroBotao = new JButton("Registros");
        registroBotao.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        	TelaRegistros telaRegistros = new TelaRegistros(1);
        	telaRegistros.setVisible(true);
        	dispose();
        	
        	}
        });
        registroBotao.setBounds(615, 474, 89, 23);
        contentPane.add(registroBotao);
    }

    /**
     * Carrega os autores no ComboBox
     */
    private void carregarAutores() {
        Connection conexao = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conexao = ConexaoBancoDados.createConnectionToMySQL();
            String sql = "SELECT nome_Autor FROM autor ORDER BY nome_Autor";
            stmt = conexao.prepareStatement(sql);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                comboBoxAutor.addItem(rs.getString("nome_Autor"));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao carregar autores: " + ex.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conexao != null) conexao.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Erro ao fechar conexão: " + ex.getMessage());
            }
        }
    }

    /**
     * Carrega as editoras no ComboBox
     */
    private void carregarEditoras() {
        Connection conexao = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conexao = ConexaoBancoDados.createConnectionToMySQL();
            String sql = "SELECT nome_Editora FROM editora ORDER BY nome_Editora";
            stmt = conexao.prepareStatement(sql);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                comboBoxEditora.addItem(rs.getString("nome_Editora"));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao carregar editoras: " + ex.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conexao != null) conexao.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Erro ao fechar conexão: " + ex.getMessage());
            }
        }
    }

    /**
     * Realiza a busca de livros com base nos filtros selecionados
     */
    private void buscarLivros() {
        Connection conexao = null;
        PreparedStatement sts = null;
        ResultSet rs = null;
        
        try {
            conexao = ConexaoBancoDados.createConnectionToMySQL();
            
            // Construção da query SQL
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT L.nome_Livro, A.nome_Autor, E.nome_Editora ")
               .append("FROM livro L ")
               .append("JOIN autor A ON A.id_Autor = L.id_Autor ")
               .append("JOIN editora E ON E.id_Editora = L.id_Editora ")
               .append("JOIN publicacao P ON P.id_Publicacao = L.id_Publicacao ")
               .append("WHERE 1=1 ");
            
            ArrayList<String> parametros = new ArrayList<>();
            
            // Filtro por título
            if(!textField.getText().trim().isEmpty()) {
                sql.append("AND L.nome_Livro LIKE ? ");
                parametros.add("%" + textField.getText().trim() + "%");
            }
            
            // Filtro por autor
            if(comboBoxAutor.getSelectedItem() != null && !comboBoxAutor.getSelectedItem().equals("Escolha")) {
                sql.append("AND A.nome_Autor = ? ");
                parametros.add(comboBoxAutor.getSelectedItem().toString());
            }
            
            // Filtro por editora
            if(comboBoxEditora.getSelectedItem() != null && !comboBoxEditora.getSelectedItem().equals("Escolha")) {
                sql.append("AND E.nome_Editora = ? ");
                parametros.add(comboBoxEditora.getSelectedItem().toString());
            }
            
            // Prepara e executa a query
            sts = conexao.prepareStatement(sql.toString());
            
            for(int i = 0; i < parametros.size(); i++) {
                sts.setString(i+1, parametros.get(i));
            }
            
            rs = sts.executeQuery();
            
            // Configura o modelo da tabela
            DefaultTableModel model = new DefaultTableModel(
                new Object[][]{},
                new String[]{"Título", "Autor", "Editora"}
            );
            table.setModel(model);
            model.setRowCount(0);

            // Preenche a tabela com os resultados
            while (rs.next()) { 
                model.addRow(new Object[] {
                    rs.getString("nome_Livro"),
                    rs.getString("nome_Autor"),
                    rs.getString("nome_Editora")
                });
            }
            
        } catch(SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na busca: " + ex.getMessage());
        } finally {
            // Fecha os recursos
            try {
                if(rs != null) rs.close();
                if(sts != null) sts.close();
                if(conexao != null) conexao.close();
            } catch(SQLException ex) {
                JOptionPane.showMessageDialog(null, "Erro ao fechar conexão: " + ex.getMessage());
            }
        }
    }
}