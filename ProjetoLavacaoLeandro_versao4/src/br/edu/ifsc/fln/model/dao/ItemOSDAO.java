/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.ifsc.fln.model.dao;

import br.edu.ifsc.fln.exception.DAOException;
import br.edu.ifsc.fln.model.domain.ItemOS;
import br.edu.ifsc.fln.model.domain.OrdemServico;
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
public class ItemOSDAO {
    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public boolean inserir(ItemOS itemOS) throws DAOException{
        String sql = "INSERT INTO item_os(valor_servico, observacao, id_servico, id_ordem_servico) VALUES(?,?,?,?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setBigDecimal(1, itemOS.getValorServico());
            stmt.setString(2, itemOS.getObservacao());
            stmt.setInt(3, itemOS.getServico().getId());
            stmt.setInt(4, itemOS.getOrdemServico().getNumero());
            
            stmt.execute();
        } catch (SQLException ex) {
            Logger.getLogger(ItemOSDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Não foi possível inserir o item da ordem de serviço no banco de dados!", ex);
        }
        return false;
    }

    public boolean alterar(ItemOS itemOS) throws DAOException{
        String sql = "UPDATE item_os SET valor_servico = ?, observacao = ?, id_servico= ?, id_ordem_servico = ? WHERE id = ?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setBigDecimal(1, itemOS.getValorServico());
            stmt.setString(2, itemOS.getObservacao());
            stmt.setInt(3, itemOS.getServico().getId());
            stmt.setInt(4, itemOS.getOrdemServico().getNumero());
            stmt.setInt(5, itemOS.getId());
            stmt.execute();
        } catch (SQLException ex) {
            Logger.getLogger(ItemOSDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Não foi possível alterar o item da ordem de serviço no banco de dados!", ex);
        }
        return false;
    }

    public boolean remover(ItemOS itemOS) throws DAOException{
        String sql = "DELETE FROM item_os WHERE id=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, itemOS.getId());
            stmt.execute();
        } catch (SQLException ex) {
            Logger.getLogger(ItemOSDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Não foi possível remover o item da ordem de serviço no banco de dados!", ex);
        }
        return false;
    }

    public List<ItemOS> listar() throws DAOException{
        String sql = "SELECT * FROM item_os";
        List<ItemOS> retorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                ItemOS itemOS = new ItemOS();
                Servico servico = new Servico();
                OrdemServico ordemServico = new OrdemServico();
                itemOS.setId(resultado.getInt("id"));
                itemOS.setValorServico(resultado.getBigDecimal("valor_servico"));
                itemOS.setObservacao(resultado.getString("observacao"));
                
                servico.setId(resultado.getInt("id_servico"));
                ordemServico.setNumero(resultado.getInt("id_ordem_servico"));
                
                ServicoDAO servicoDAO = new ServicoDAO();
                servicoDAO.setConnection(connection);
                servico = servicoDAO.buscar(servico);
                
                OrdemServicoDAO ordemServicoDAO = new OrdemServicoDAO();
                ordemServicoDAO.setConnection(connection);
                ordemServico = ordemServicoDAO.buscar(ordemServico);
                
                itemOS.setServico(servico);
                itemOS.setOrdemServico(ordemServico);
                
                retorno.add(itemOS);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ItemOSDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Não foi possível listar o item da ordem de serviço no banco de dados!", ex);
        }
        return retorno;
    }
    
    public List<ItemOS> listarPorOrdemServico(OrdemServico ordemServico) throws DAOException{
        String sql = "SELECT * FROM item_os WHERE id_ordem_servico=?";
        List<ItemOS> retorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, ordemServico.getNumero());
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                ItemOS itemOS = new ItemOS();
                Servico servico = new Servico();
                OrdemServico os = new OrdemServico();
                itemOS.setId(resultado.getInt("id"));
                itemOS.setValorServico(resultado.getBigDecimal("valor_servico"));
                itemOS.setObservacao(resultado.getString("observacao"));
                
                servico.setId(resultado.getInt("id_servico"));
                os.setNumero(resultado.getInt("id_ordem_servico"));
                
                ServicoDAO servicoDAO = new ServicoDAO();
                servicoDAO.setConnection(connection);
                servico = servicoDAO.buscar(servico);
                
                itemOS.setServico(servico);
                itemOS.setOrdemServico(os);
                
                retorno.add(itemOS);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ItemOSDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Não foi possível listar o item da ordem de serviço no banco de dados!", ex);
        }
        return retorno;
    }

    public ItemOS buscar(ItemOS itemOS) throws DAOException{
        String sql = "SELECT * FROM item_os WHERE id=?";
        ItemOS retorno = new ItemOS();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, itemOS.getId());
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                Servico servico = new Servico();
                OrdemServico ordemServico = new OrdemServico();
                itemOS.setId(resultado.getInt("id"));
                itemOS.setValorServico(resultado.getBigDecimal("valor_servico"));
                itemOS.setObservacao(resultado.getString("observacao"));
                
                servico.setId(resultado.getInt("id_servico"));
                ordemServico.setNumero(resultado.getInt("id_ordem_servico"));
                
                //Obtendo os dados completos do Cliente associado à Venda
                ServicoDAO servicoDAO = new ServicoDAO();
                servicoDAO.setConnection(connection);
                servico = servicoDAO.buscar(servico);
                
                OrdemServicoDAO ordemServicoDAO = new OrdemServicoDAO();
                ordemServicoDAO.setConnection(connection);
                ordemServico = ordemServicoDAO.buscar(ordemServico);
                
                itemOS.setServico(servico);
                itemOS.setOrdemServico(ordemServico);
                
                retorno = itemOS;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ItemOSDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Não foi possível buscar o item da ordem de serviço no banco de dados!", ex);
        }
        return retorno;
    }
    
    public void alterarTodos(OrdemServico ordemServico) throws DAOException{
        String sql = "UPDATE item_os SET valor_servico = ?, observacao = ?, id_servico= ?, id_ordem_servico = ? WHERE id = ?";
        try {
            
            for (ItemOS itemOS : ordemServico.getItensOS()) 
            {
                PreparedStatement stmt = connection.prepareStatement(sql);
                stmt.setBigDecimal(1, itemOS.getValorServico());
                stmt.setString(2, itemOS.getObservacao());
                stmt.setInt(3, itemOS.getServico().getId());
                stmt.setInt(4, itemOS.getOrdemServico().getNumero());
                stmt.setInt(5, itemOS.getId());
                stmt.execute();
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(ItemOSDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Não foi possível alterar o item da ordem de serviço no banco de dados!", ex);
        }
    }
}
