import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class ProdutosDAO {
    
    Connection conn;
    PreparedStatement prep;
    ResultSet resultset;
    ArrayList<ProdutosDTO> listagem = new ArrayList<>();
    
    public void cadastrarProduto (ProdutosDTO produto){
        
        conn = new conectaDAO().connectDB();
        String sql = "INSERT INTO produtos (nome, valor, status) VALUES (?, ?, ?)";
        
        try {
            // Prepara a estrutura do SQL substituindo as "?" pelos dados reais
            prep = conn.prepareStatement(sql);
            prep.setString(1, produto.getNome());
            prep.setInt(2, produto.getValor());
            prep.setString(3, produto.getStatus());
            
            // Executa fisicamente o comando no banco de dados MySQL
            prep.executeUpdate();
            
        } catch (SQLException erro) {
            // Mostra uma mensagem na tela caso ocorra algum erro de banco de dados
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar na DAO: " + erro.getMessage());
        } finally {
            // Fecha as conexões para evitar travamento do banco
            try {
                if (prep != null) prep.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.out.println("Erro ao fechar conexao: " + e.getMessage());
            }
        }
    }
    
    public ArrayList<ProdutosDTO> listarProdutos(){
        // Limpa a lista para não duplicar dados caso o método seja chamado várias vezes
        listagem.clear(); 
        
        conn = new conectaDAO().connectDB();
        String sql = "SELECT * FROM produtos";
        
        try {
            prep = conn.prepareStatement(sql);
            resultset = prep.executeQuery(); // Executa a busca (SELECT)
            
            // Percorre linha por linha o resultado vindo do MySQL
            while (resultset.next()) {
                ProdutosDTO produto = new ProdutosDTO();
                
                // Pega os dados das colunas do banco e joga no objeto Java
                produto.setId(resultset.getInt("id")); 
                produto.setNome(resultset.getString("nome"));
                produto.setValor(resultset.getInt("valor"));
                produto.setStatus(resultset.getString("status"));
                
                // Adiciona o produto na lista que será exibida na tabela
                listagem.add(produto);
            }
            
        } catch (SQLException erro) {
            JOptionPane.showMessageDialog(null, "Erro ao listar na DAO: " + erro.getMessage());
        } finally {
            try {
                if (resultset != null) resultset.close();
                if (prep != null) prep.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.out.println("Erro ao fechar conexao: " + e.getMessage());
            }
        }
        
        return listagem;
    }
}