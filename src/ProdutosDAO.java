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
            prep = conn.prepareStatement(sql);
            prep.setString(1, produto.getNome());
            prep.setInt(2, produto.getValor());
            prep.setString(3, produto.getStatus());
            
            prep.executeUpdate();
            
        } catch (SQLException erro) {
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar na DAO: " + erro.getMessage());
        } finally {
            
            try {
                if (prep != null) prep.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.out.println("Erro ao fechar conexao: " + e.getMessage());
            }
        }
    }
    
    public void venderProduto(int id) {
        String sql = "UPDATE produtos SET status = 'Vendido' WHERE id = ?";
        
        try {
            conn = new conectaDAO().connectDB();
            prep = conn.prepareStatement(sql);
            prep.setInt(1, id);
            
            prep.executeUpdate();          
            
            
    } catch (Exception e) {        
        System.out.println("Erro ao atualizar o status do produto: " + e.getMessage());
    } finally{
            
    }
}
    
    public ArrayList<ProdutosDTO> listarProdutosVendidos() {
        ArrayList<ProdutosDTO> listagem = new ArrayList<>();
        
        String sql = "SELECT * FROM produtos WHERE status = 'Vendido'";
        
    try {
        conn = new conectaDAO().connectDB();
        prep = conn.prepareStatement(sql);
        resultset = prep.executeQuery();
        
        while (resultset.next()) {
            ProdutosDTO produto = new ProdutosDTO();
            produto.setId(resultset.getInt("id"));
            produto.setNome(resultset.getString("nome"));
            produto.setValor(resultset.getInt("valor"));
            produto.setStatus(resultset.getString("status"));
            listagem.add(produto);
        }
    } catch (Exception e) {
        System.out.println("Erro ao listar vendidos: " + e.getMessage());
    }
    return listagem;
}
    
    public ArrayList<ProdutosDTO> listarProdutos(){
        listagem.clear(); 
        
        conn = new conectaDAO().connectDB();
        String sql = "SELECT * FROM produtos";
        
        try {
            prep = conn.prepareStatement(sql);
            resultset = prep.executeQuery(); // Executa a busca (SELECT)
            
            while (resultset.next()) {
                ProdutosDTO produto = new ProdutosDTO();
                
                produto.setId(resultset.getInt("id")); 
                produto.setNome(resultset.getString("nome"));
                produto.setValor(resultset.getInt("valor"));
                produto.setStatus(resultset.getString("status"));
                
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