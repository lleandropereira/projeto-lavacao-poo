/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.ifsc.fln.model.dao;

import br.edu.ifsc.fln.exception.DAOException;
import br.edu.ifsc.fln.model.domain.ECategoria;
import br.edu.ifsc.fln.model.domain.Servico;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author leandropereira
 */
public class ServicoDAO {
    
    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void inserir(Servico servico) throws DAOException{
        String sql = "INSERT INTO servico(descricao, valor, ponto, categoria) VALUES(?,?,?,?)";
        
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, servico.getDescricao());
            stmt.setFloat(2, (float) servico.getValor());
            stmt.setInt(3, servico.getPontos());
            stmt.setString(4, servico.getCategoria().name());
            stmt.execute();
        } catch (SQLException ex) {
            Logger.getLogger(ServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Não foi possível inserir o serviço no banco de dados!", ex);
        }
    }

    public void alterar(Servico servico) throws DAOException{
        String sql = "UPDATE servico SET descricao=?, valor=?, ponto=?, categoria=? WHERE id=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, servico.getDescricao());
            stmt.setFloat(2, (float) servico.getValor());
            stmt.setInt(3, servico.getPontos());
            stmt.setString(4, servico.getCategoria().name());
            stmt.setInt(5, servico.getId());
            stmt.execute();
        } catch (SQLException ex) {
            Logger.getLogger(ServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Não foi possível alterar o serviço no banco de dados!", ex);
        }
    }

    public void remover(Servico servico) throws DAOException{
        String sql = "DELETE FROM servico WHERE id=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, servico.getId());
            stmt.execute();
        } catch (SQLException ex) {
            Logger.getLogger(ModeloDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Não foi possível remover o serviço no banco de dados!", ex);
        }
    }

    public List<Servico> listar() throws DAOException{
        String sql =  "SELECT s.id as id, s.descricao as descricao, s.valor as valor, s.categoria as categoria "
                + "FROM servico s ";
        List<Servico> retorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                Servico servico = populateVO(resultado);
                retorno.add(servico);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServicoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Não foi possível listar o serviço no banco de dados!", ex);
        }
        return retorno;
    }
    
    public Servico buscar(Servico servico) throws DAOException{
//        String sql =  "SELECT model.id as id_modelo, model.descricao as modelo_descricao, "
//                + "m.id as marca_id, m.nome as marca_nome "
//                  + "mt.potencia as motor_potencia, mt.situacao as motor_situacao "
//                + "FROM modelo model "
//                  + "INNER JOIN marca m ON m.id = model.id_marca WHERE model.id = ? "
//                  + "INNER JOIN motor mo ON mo.id = modelo.id_motor WHERE modelo.id =?;";
        
        String sql = "SELECT * FROM servico WHERE id=?";
        Servico retorno = new Servico();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, servico.getId());
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                retorno = populateVO(resultado);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ModeloDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Não foi possível buscar o serviço no banco de dados!", ex);
        }
        return retorno;
    }
    
    private Servico populateVO(ResultSet rs) throws SQLException {
        Servico servico = new Servico();
        
        
        servico.setId(rs.getInt("id"));
        servico.setDescricao(rs.getString("descricao"));
        servico.setValor(rs.getFloat("valor"));
        servico.setCategoria(Enum.valueOf(ECategoria.class, rs.getString("categoria")));
        return servico;
    }    
}
