package br.edu.ifsc.fln.model.dao;

import br.edu.ifsc.fln.exception.DAOException;
import br.edu.ifsc.fln.model.domain.Cliente;
import br.edu.ifsc.fln.model.domain.Cor;
import br.edu.ifsc.fln.model.domain.ECategoria;
import br.edu.ifsc.fln.model.domain.ETipoCombustivel;
import br.edu.ifsc.fln.model.domain.Marca;
import br.edu.ifsc.fln.model.domain.Modelo;
import br.edu.ifsc.fln.model.domain.Motor;
import br.edu.ifsc.fln.model.domain.Veiculo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class VeiculoDAO{

    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void inserir(Veiculo veiculo) throws DAOException{
        String sql = "INSERT INTO veiculo(placa, observacao, id_cor, id_modelo, id_cliente) VALUES(?,?,?,?,?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, veiculo.getPlaca());
            stmt.setString(2, veiculo.getObservacao());
            stmt.setInt(3, veiculo.getCor().getId());
            stmt.setInt(4,veiculo.getModelo().getId());
            stmt.setInt(5, veiculo.getCliente().getId());
            stmt.execute();
        } catch (SQLException ex) {
            Logger.getLogger(VeiculoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Não foi possível inserir o veículo no banco de dados!", ex);
        }
    }

    public void alterar(Veiculo veiculo) throws DAOException{
        String sql = "UPDATE veiculo SET placa=?, observacao=?, id_cor=?, id_modelo=?, id_cliente=? WHERE id=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, veiculo.getPlaca());
            stmt.setString(2, veiculo.getObservacao());
            stmt.setInt(3, veiculo.getCor().getId());
            stmt.setInt(4, veiculo.getModelo().getId());
            stmt.setInt(5, veiculo.getCliente().getId());
            stmt.setInt(6, veiculo.getId());
            stmt.execute();
        } catch (SQLException ex) {
            Logger.getLogger(VeiculoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Não foi possível alterar o veículo no banco de dados!", ex);
        }
    }

    public void remover(Veiculo veiculo) throws DAOException{
        String sql = "DELETE FROM veiculo WHERE id=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, veiculo.getId());
            stmt.execute();
        } catch (SQLException ex) {
            Logger.getLogger(VeiculoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Não foi possível remover o veículo no banco de dados!", ex);
        }
    }

    public List<Veiculo> listar() throws DAOException{
        String sql =  "SELECT v.id as id_veiculo, v.placa as veiculo_placa, v.observacao as veiculo_observacao, "
                + "m.id as id_modelo, m.descricao as modelo_descricao, m.id_marca as modelo_idMarca, m.categoria as modelo_categoria, "
                + "mt.situacao as motor_situacao, mt.potencia as motor_potencia, "
                + "c.id as id_cor, c.nome as cor_nome, cl.id as id_cliente "
                + "FROM veiculo v "
                + "INNER JOIN cor c ON c.id = v.id_cor "
                + "INNER JOIN modelo m ON m.id = v.id_modelo "
                + "INNER JOIN motor mt ON mt.id_modelo = m.id "
                + "INNER JOIN cliente cl ON cl.id = v.id_cliente;";
        List<Veiculo> retorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                Veiculo veiculo = populateVO(resultado);
                retorno.add(veiculo);
            }
        } catch (SQLException ex) {
            Logger.getLogger(VeiculoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Não foi possível listar o veículo no banco de dados!", ex);
        }
        return retorno;
    }
    
    public Veiculo buscarPorId(int id) throws DAOException{
        String sql = "SELECT v.id as id_veiculo, v.placa as veiculo_placa, v.observacao as veiculo_observacao, "
                 + "m.id as id_modelo, m.descricao as modelo_descricao, m.id_marca as modelo_idMarca, m.categoria as modelo_categoria, "
                 + "mt.situacao as motor_situacao, mt.potencia as motor_potencia, "
                 + "c.id as id_cor, c.nome as cor_nome, cli.id as id_cliente "
                 + "FROM veiculo v "
                 + "INNER JOIN modelo m ON m.id = v.id_modelo "
                 + "INNER JOIN motor mt ON mt.id_modelo = m.id "
                 + "INNER JOIN cor c ON c.id = v.id_cor "
                 + "INNER JOIN cliente cli ON cli.id = v.id_cliente "
                 + "WHERE v.id = ?";
        Veiculo veiculo = null;
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                veiculo = populateVO(resultado);
            }
        } catch (SQLException ex) {
            Logger.getLogger(VeiculoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Não foi possível buscar o veículo no banco de dados!", ex);
        }
        return veiculo;
    }
    
    public Veiculo buscar(Veiculo veiculo) throws DAOException{
        String sql =  "SELECT v.id as id_veiculo, v.placa as veiculo_placa, v.observacao as veiculo_observacao, "
                + "m.id as id_modelo, m.descricao as modelo_descricao, m.id_marca as modelo_idMarca, m.categoria as modelo_categoria, "
                + "mt.situacao as motor_situacao, mt.potencia as motor_potencia, "
                + "c.id as id_cor, c.nome as cor_nome, cli.id as id_cliente "
                + "FROM veiculo v "
                +"INNER JOIN modelo m ON m.id = v.id_modelo "
                + "INNER JOIN motor mt ON mt.id_modelo = m.id "
                +"INNER JOIN cor c ON c.id = v.id_cor "
                +"INNER JOIN cliente cli on cli.id = v.id_cliente WHERE v.id=?";
        Veiculo retorno = new Veiculo();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, veiculo.getId());
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                retorno = populateVO(resultado);
            }
        } catch (SQLException ex) {
            Logger.getLogger(VeiculoDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Não foi possível buscar o veículo no banco de dados!", ex);
        }
        return retorno;
    }
    
    private Veiculo populateVO(ResultSet rs) throws SQLException, DAOException {
        Veiculo veiculo = new Veiculo();
        Cor cor = new Cor();
        veiculo.setCor(cor);
        Modelo modelo = new Modelo();
        veiculo.setModelo(modelo);
        Marca marca = new Marca();
        
        veiculo.setId(rs.getInt("id_veiculo"));
        veiculo.setPlaca(rs.getString("veiculo_placa"));
        veiculo.setObservacao(rs.getString("veiculo_observacao"));
        
        cor.setId(rs.getInt("id_cor"));
        cor.setNome(rs.getString("cor_nome"));
        
        modelo.setId(rs.getInt("id_modelo"));
        modelo.setDescricao(rs.getString("modelo_descricao"));
        
        marca.setId(rs.getInt("modelo_idMarca"));
        
        MarcaDAO marcaDAO = new MarcaDAO();
        marcaDAO.setConnection(connection);
        marca = marcaDAO.buscar(marca);
        modelo.setMarca(marca);
        modelo.setCategoria(Enum.valueOf(ECategoria.class, rs.getString("modelo_categoria")));
        
        Motor motor = new Motor();
        motor.setSituacao(Enum.valueOf(ETipoCombustivel.class, rs.getString("motor_situacao")));
        motor.setPotencia(rs.getInt("motor_potencia"));
        modelo.setMotor(motor);
        
        int idCliente = rs.getInt("id_cliente");
        ClienteDAO clienteDAO = new ClienteDAO();
        clienteDAO.setConnection(connection);
        Cliente cliente = clienteDAO.buscar(idCliente);
        veiculo.setCliente(cliente);
        return veiculo;
    }    
}
