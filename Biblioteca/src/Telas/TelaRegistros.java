package Telas;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import conexaoBD.ConexaoBancoDados;

public class TelaRegistros extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable tabelaEmprestimo;
    private int usuarioId; // ID do usuário logado

    /**
     * Construtor com ID do Usuário
     */
    public TelaRegistros(int usuarioId) {
        this.usuarioId = usuarioId;

        setTitle("Registros de Empréstimos");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(300, 300, 800, 550);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(null);
        setContentPane(contentPane);

        JLabel lblTitulo = new JLabel("Relatório de Empréstimos Ativos");
        lblTitulo.setFont(new Font("Segoe UI Black", Font.BOLD, 18));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setBounds(10, 11, 764, 31);
        contentPane.add(lblTitulo);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 54, 764, 402);
        contentPane.add(scrollPane);

        tabelaEmprestimo = new JTable();
        scrollPane.setViewportView(tabelaEmprestimo);

        JButton btnProrrogar = new JButton("PRORROGAR");
        btnProrrogar.setFont(new Font("Arial Black", Font.PLAIN, 15));
        btnProrrogar.setBackground(new Color(255, 204, 102));
        btnProrrogar.setBounds(86, 467, 152, 35);
        btnProrrogar.addActionListener(this::prorrogarEmprestimo);
        contentPane.add(btnProrrogar);

        JButton btnDevolver = new JButton("DEVOLVER");
        btnDevolver.setFont(new Font("Arial Black", Font.BOLD, 15));
        btnDevolver.setBackground(new Color(51, 204, 102));
        btnDevolver.setBounds(331, 467, 143, 35);
        btnDevolver.addActionListener(this::devolverLivro);
        contentPane.add(btnDevolver);

        JButton btnAtualizar = new JButton("ATUALIZAR");
        btnAtualizar.setFont(new Font("Arial Black", Font.PLAIN, 15));
        btnAtualizar.setBackground(new Color(102, 153, 255));
        btnAtualizar.setBounds(584, 467, 152, 35);
        btnAtualizar.addActionListener(e -> carregarEmprestimosAtivos());
        contentPane.add(btnAtualizar);

        carregarEmprestimosAtivos();
    }

    /**
     * Carrega os empréstimos ativos do usuário logado
     */
    private void carregarEmprestimosAtivos() {
        try (Connection conn = ConexaoBancoDados.createConnectionToMySQL()) {
            String sql = """
                SELECT L.id_Livro, L.nome_Livro, A.nome_Autor, E.nome_Editora, 
                       U.nome AS nome_usuario, L.data_emprestimo, L.data_devolucao
                FROM livro L
                JOIN autor A ON L.id_Autor = A.id_Autor
                JOIN editora E ON L.id_Editora = E.id_Editora
                JOIN usuario U ON L.id_Usuario = U.id_Usuario
                WHERE L.emprestado = true AND L.id_Usuario = ?
                """;

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, usuarioId);

                try (ResultSet rs = stmt.executeQuery()) {
                    DefaultTableModel modelo = new DefaultTableModel(
                        new String[]{"ID", "Livro", "Autor", "Editora", "Usuário", "Data Empréstimo", "Data Devolução"}, 0
                    ) {
                        @Override
                        public boolean isCellEditable(int row, int column) {
                            return false;
                        }
                    };

                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

                    while (rs.next()) {
                        modelo.addRow(new Object[]{
                            rs.getInt("id_Livro"),
                            rs.getString("nome_Livro"),
                            rs.getString("nome_Autor"),
                            rs.getString("nome_Editora"),
                            rs.getString("nome_usuario"),
                            sdf.format(rs.getDate("data_emprestimo")),
                            sdf.format(rs.getDate("data_devolucao"))
                        });
                    }

                    tabelaEmprestimo.setModel(modelo);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar empréstimos: " + ex.getMessage());
        }
    }

    /**
     * Prorroga o prazo de devolução do livro em 7 dias
     */
    private void prorrogarEmprestimo(ActionEvent e) {
        int linha = tabelaEmprestimo.getSelectedRow();

        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um livro para prorrogar!");
            return;
        }

        int idLivro = (int) tabelaEmprestimo.getValueAt(linha, 0);

        try (Connection conn = ConexaoBancoDados.createConnectionToMySQL()) {
            String sql = """
                UPDATE livro 
                SET data_devolucao = DATE_ADD(data_devolucao, INTERVAL 7 DAY)
                WHERE id_Livro = ? AND id_Usuario = ? AND emprestado = true
                """;

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, idLivro);
                stmt.setInt(2, usuarioId);

                int afetadas = stmt.executeUpdate();

                if (afetadas > 0) {
                    JOptionPane.showMessageDialog(this, "Prazo prorrogado com sucesso por mais 7 dias!");
                    carregarEmprestimosAtivos();
                } else {
                    JOptionPane.showMessageDialog(this, "Erro ao prorrogar o empréstimo.");
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
        }
    }

    /**
     * Devolve o livro selecionado
     */
    private void devolverLivro(ActionEvent e) {
        int linha = tabelaEmprestimo.getSelectedRow();

        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um livro para devolver!");
            return;
        }

        int idLivro = (int) tabelaEmprestimo.getValueAt(linha, 0);
        String nomeLivro = (String) tabelaEmprestimo.getValueAt(linha, 1);

        int confirmar = JOptionPane.showConfirmDialog(
            this,
            "Deseja devolver o livro '" + nomeLivro + "'?",
            "Confirmar Devolução",
            JOptionPane.YES_NO_OPTION
        );

        if (confirmar != JOptionPane.YES_OPTION) return;

        try (Connection conn = ConexaoBancoDados.createConnectionToMySQL()) {
            String sql = """
                UPDATE livro 
                SET emprestado = false, id_Usuario = null, data_emprestimo = null, data_devolucao = null
                WHERE id_Livro = ?
                """;

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, idLivro);

                int afetadas = stmt.executeUpdate();

                if (afetadas > 0) {
                    JOptionPane.showMessageDialog(this, "Livro devolvido com sucesso!");
                    carregarEmprestimosAtivos();
                } else {
                    JOptionPane.showMessageDialog(this, "Erro ao devolver o livro.");
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
        }
    }

    /**
     * Método principal (teste isolado com ID fixo)
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                TelaRegistros frame = new TelaRegistros(1); // Teste com ID 1
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
