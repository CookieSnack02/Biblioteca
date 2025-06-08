package Telas;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.sql.Connection;
import conexaoBD.ConexaoBancoDados;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JTable;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.awt.Component;

public class TelaLivros extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField tituloCampo;
    private JTable tabelaLivros;
    private JComboBox<String> autorSelecionar;
    private JComboBox<String> editoraSelecionar;
    private final String[] nomeColunas = {"Livro", "Autor", "Editora", "Data Publicação", "Emprestado"};
    private JButton botaoRegistros;

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

    
    
    public TelaLivros() {
        setTitle("Tela de Livros");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(550, 245, 800, 600);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        criarComponentes(); 	//Criação dos componentes
        carregarAutores(); 		//Busca banco de dados -> tabela autor
        carregarEditoras(); 	//Busca banco de dados -> tabela editora
    }
    
    private void emprestarLivro() {
        int selectedRow = tabelaLivros.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Selecione um livro na tabela para emprestar",
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        DefaultTableModel model = (DefaultTableModel) tabelaLivros.getModel();
        String nomeLivro = (String) model.getValueAt(selectedRow, 0);
        Boolean emprestado = (Boolean) model.getValueAt(selectedRow, 4);
        
        if (emprestado) {
            JOptionPane.showMessageDialog(this, 
                "Este livro já está emprestado!",
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
            "Confirmar empréstimo do livro: " + nomeLivro + "?",
            "Confirmar Empréstimo", JOptionPane.YES_NO_OPTION);
        
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try (Connection conn = ConexaoBancoDados.createConnectionToMySQL()) {
            // Atualiza no banco de dados
            String sql = "UPDATE livro SET emprestado = TRUE WHERE nome_Livro = ?";
            
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, nomeLivro);
                int rowsAffected = stmt.executeUpdate();
                
                if (rowsAffected > 0) {
                    // Atualiza na tabela
                    model.setValueAt(true, selectedRow, 4);
                    JOptionPane.showMessageDialog(this,
                        "Livro emprestado com sucesso!",
                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                "Erro ao atualizar empréstimo: " + ex.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    

    private void criarComponentes() {
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
        
        autorSelecionar = new JComboBox<>();
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
        
        editoraSelecionar = new JComboBox<>();
        editoraSelecionar.setBounds(599, 26, 114, 22);
        contentPane.add(editoraSelecionar);
        
        JButton botaoBuscar = new JButton("Buscar");
        botaoBuscar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                buscarLivros();
            }
        });
        botaoBuscar.setBackground(new Color(255, 255, 255));
        botaoBuscar.setFont(new Font("Segoe UI Black", Font.PLAIN, 15));
        botaoBuscar.setBounds(269, 69, 227, 37);
        contentPane.add(botaoBuscar);
        
        tabelaLivros = new JTable();
        tabelaLivros.setBounds(10, 125, 764, 381);
        contentPane.add(tabelaLivros);
        
        JButton emprestadoBotao = new JButton("Pegar Emprestado");
        emprestadoBotao.setBackground(new Color(255, 255, 255));
        emprestadoBotao.addActionListener(e -> emprestarLivro());
        emprestadoBotao.setFont(new Font("Segoe UI Black", Font.PLAIN, 15));
        emprestadoBotao.setBounds(288, 517, 173, 33);
        contentPane.add(emprestadoBotao);
        
        botaoRegistros = new JButton("Registros");
        botaoRegistros.setBackground(Color.WHITE);
        botaoRegistros.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		TelaRegistro telaRegistro = new TelaRegistro();
        		telaRegistro.setVisible(true);
        		dispose();
        		
        	}
        });
        botaoRegistros.setFont(new Font("Tahoma", Font.BOLD, 13));
        botaoRegistros.setBounds(640, 524, 114, 23);
        contentPane.add(botaoRegistros);
    }

    private void carregarAutores() {
        try (Connection conn = ConexaoBancoDados.createConnectionToMySQL();
             PreparedStatement stmt = conn.prepareStatement("SELECT id_Autor, nome_Autor FROM autor ORDER BY nome_Autor");
             ResultSet rs = stmt.executeQuery()) {
            
            autorSelecionar.addItem("Todos");
            while (rs.next()) {
                autorSelecionar.addItem(rs.getString("nome_Autor"));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar autores: " + ex.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarEditoras() {
        try (Connection conn = ConexaoBancoDados.createConnectionToMySQL();
             PreparedStatement stmt = conn.prepareStatement("SELECT id_Editora, nome_Editora FROM editora ORDER BY nome_Editora");
             ResultSet rs = stmt.executeQuery()) {
            
            editoraSelecionar.addItem("Todas");
            while (rs.next()) {
                editoraSelecionar.addItem(rs.getString("nome_Editora"));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar editoras: " + ex.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarLivros() {
        String titulo = tituloCampo.getText().trim();
        String autor = autorSelecionar.getSelectedItem().toString();
        String editora = editoraSelecionar.getSelectedItem().toString();
        
        if (titulo.isEmpty() && autor.equals("Todos") && editora.equals("Todas")) {
            JOptionPane.showMessageDialog(this, "Por favor, informe pelo menos um critério de busca.", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try (Connection conn = ConexaoBancoDados.createConnectionToMySQL()) {
            StringBuilder sql = new StringBuilder(
                "SELECT L.nome_Livro, A.nome_Autor, E.nome_Editora, " +
                "P.data_Publicacao, L.emprestado " +
                "FROM livro L " +
                "JOIN autor A ON L.id_Autor = A.id_Autor " +
                "JOIN editora E ON L.id_Editora = E.id_Editora " +
                "JOIN publicacao P ON L.id_Publicacao = P.id_Publicacao " +
                "WHERE 1=1");
            
            if (!titulo.isEmpty()) {
                sql.append(" AND L.nome_Livro LIKE ?");
            }
            if (!autor.equals("Todos")) {
                sql.append(" AND A.nome_Autor = ?");
            }
            if (!editora.equals("Todas")) {
                sql.append(" AND E.nome_Editora = ?");
            }
            sql.append(" ORDER BY L.nome_Livro");
            
            PreparedStatement stmt = conn.prepareStatement(sql.toString());
            
            int paramIndex = 1;
            if (!titulo.isEmpty()) {
                stmt.setString(paramIndex++, "%" + titulo + "%");
            }
            if (!autor.equals("Todos")) {
                stmt.setString(paramIndex++, autor);			//Parâmetro de index
            }
            if (!editora.equals("Todas")) {
                stmt.setString(paramIndex++, editora);
            }				
            
            ResultSet rs = stmt.executeQuery();
            
            DefaultTableModel model = new DefaultTableModel() {
                @Override		//Sobrescrevendo sobre o modelo pré-estabelecido 
                public Class<?> getColumnClass(int columnIndex) { 		//Tipo genérico
                    switch(columnIndex) {
                        case 3: return Date.class;
                        case 4: return Boolean.class;
                        default: return String.class;
                    }
                }
                
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            
            model.addColumn("Livro");
            model.addColumn("Autor");
            model.addColumn("Editora");
            model.addColumn("Publicação");
            model.addColumn("Emprestado?");
            
            int totalResultados = 0;
            while (rs.next()) {
                totalResultados++;
                model.addRow(new Object[]{
                    rs.getString("nome_Livro"),
                    rs.getString("nome_Autor"),
                    rs.getString("nome_Editora"),
                    rs.getDate("data_Publicacao"),
                    rs.getBoolean("emprestado")
                });
            }
            
            tabelaLivros.setModel(model);
            
            // Configura renderizadores personalizados
            tabelaLivros.setDefaultRenderer(Date.class, new DefaultTableCellRenderer() {
                SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
                
                @Override  //Componente do Swing, formatação padrão
                public Component getTableCellRendererComponent(JTable table,
                    Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    if (value instanceof Date) {
                        value = f.format((Date)value);
                    }
                    return super.getTableCellRendererComponent(table, value, isSelected, 
                        hasFocus, row, column);
                }
            });
            
            tabelaLivros.setDefaultRenderer(Boolean.class, new DefaultTableCellRenderer() { //Renderização da tabela com valores de célula booleana
                @Override
                public Component getTableCellRendererComponent(JTable table,
                    Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    if (value instanceof Boolean) {
                        boolean boolValue = (Boolean) value;
                    	value = (Boolean)value ? "Sim" : "Não";
                        
                        if(boolValue){
                        	setForeground(Color.RED);
                        } else {
                        	setForeground(Color.GREEN);
                        }
                        
                        
                    }	//superclasse (super)
                    return super.getTableCellRendererComponent(table, value, isSelected, 
                        hasFocus, row, column);
                }
            });
            
            // Ajuste de colunas
            TableColumnModel columnModel = tabelaLivros.getColumnModel();
            columnModel.getColumn(0).setPreferredWidth(200);
            columnModel.getColumn(1).setPreferredWidth(150);
            columnModel.getColumn(2).setPreferredWidth(150);
            columnModel.getColumn(3).setPreferredWidth(100);
            columnModel.getColumn(4).setPreferredWidth(80);
            
            if (totalResultados > 0) {
                JOptionPane.showMessageDialog(this, 
                    totalResultados + " livro(s) encontrado(s)", 
                    "Resultado da Busca", 
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Nenhum livro encontrado com os critérios informados", 
                    "Resultado da Busca", 
                    JOptionPane.WARNING_MESSAGE);
            }
            
            rs.close();
            stmt.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao buscar livros: " + ex.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}