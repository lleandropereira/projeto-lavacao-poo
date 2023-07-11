package br.edu.ifsc.fln.model.dao;

import br.edu.ifsc.fln.exception.DAOException;
import br.edu.ifsc.fln.model.domain.ECategoria;
import br.edu.ifsc.fln.model.domain.ETipoCombustivel;
import br.edu.ifsc.fln.model.domain.Marca;
import br.edu.ifsc.fln.model.domain.Modelo;
import br.edu.ifsc.fln.model.domain.Motor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ModeloDAO{

    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public boolean inserir(Modelo modelo) throws DAOException{
        String sql = "INSERT INTO modelo(descricao, categoria, id_marca) VALUES(?,?,?)";
        final String sqlMotor = "INSERT INTO motor(id_modelo, potencia, situacao) VALUES((SELECT max(id) FROM modelo), ?,?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, modelo.getDescricao());
            stmt.setString(2, modelo.getCategoria().getDescricao());
            stmt.setInt(3, modelo.getMarca().getId());
            stmt.execute();
            stmt = connection.prepareStatement(sqlMotor);
            stmt.setInt(1, modelo.getMotor().getPotencia());
            stmt.setString(2, modelo.getMotor().getSituacao().getDescricao());
            stmt.execute();
        } catch (SQLException ex) {
            Logger.getLogger(ModeloDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Não foi possível inserir o modelo no banco de dados!", ex);
        }
        return false;
    }

    public boolean alterar(Modelo modelo) throws DAOException{
        String sql = "UPDATE modelo SET descricao=?, categoria=?, id_marca=? WHERE id=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, modelo.getDescricao());
            stmt.setString(2, modelo.getCategoria().getDescricao());
            stmt.setInt(3, modelo.getMarca().getId());
            stmt.setInt(4, modelo.getId());
            stmt.execute();
        } catch (SQLException ex) {
            Logger.getLogger(ModeloDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Não foi possível alterar o modelo no banco de dados!", ex);
        }
        return false;
    }

    public boolean remover(Modelo modelo) throws DAOException{
        String sql = "DELETE FROM modelo WHERE id=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, modelo.getId());
            stmt.execute();
        } catch (SQLException ex) {
            Logger.getLogger(ModeloDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Não foi possível remover o modelo no banco de dados!", ex);
        }
        return false;
    }

    public List<Modelo> listar() throws DAOException{
        String sql =  "SELECT model.id as id_modelo, model.descricao as modelo_descricao, model.categoria as categoria, "
                + "m.id as marca_id, m.nome as marca_nome, "
                + "mt.potencia as motor_potencia, mt.situacao as motor_situacao "
                + "FROM modelo model "
                + "INNER JOIN marca m ON m.id = model.id_marca "
                + "INNER JOIN motor mt ON mt.id_modelo = model.id;";
        List<Modelo> retorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                Modelo modelo = populateVO(resultado);
                retorno.add(modelo);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ModeloDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Não foi possível listar o modelo no banco de dados!", ex);
        }
        return retorno;
    }
    
    public Modelo buscar(Modelo modelo) throws DAOException{
//        String sql =  "SELECT model.id as id_modelo, model.descricao as modelo_descricao, "
//                + "m.id as marca_id, m.nome as marca_nome "
//                  + "mt.potencia as motor_potencia, mt.situacao as motor_situacao "
//                + "FROM modelo model "
//                  + "INNER JOIN marca m ON m.id = model.id_marca WHERE model.id = ? "
//                  + "INNER JOIN motor mo ON mo.id = modelo.id_motor WHERE modelo.id =?;";
        
        String sql = "SELECT * FROM modelo WHERE id=?";
        Modelo retorno = new Modelo();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, modelo.getId());
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                retorno = populateVO(resultado);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ModeloDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Não foi possível buscar o modelo no banco de dados!", ex);
        }
        return retorno;
    }
    
    private Modelo populateVO(ResultSet rs) throws SQLException {
        Modelo modelo = new Modelo();
        Marca marca = new Marca();
        modelo.setMarca(marca);
//        MarcaDAO marcaDAO = new MarcaDAO();
//        marcaDAO.setConnection(connection);
//        marca = marcaDAO.buscar(marca);

//        MotorDAO motorDAO = new MotorDAO();
//        motorDAO.setConnection(connection);
//        modelo.setMotor(motorDAO.buscar(modelo));
        
        Motor motor = new Motor();
        modelo.setMotor(motor);
        
        modelo.setId(rs.getInt("id_modelo"));
        modelo.setDescricao(rs.getString("modelo_descricao"));
        modelo.setCategoria(Enum.valueOf(ECategoria.class, rs.getString("categoria")));
        marca.setId(rs.getInt("marca_id"));
        marca.setNome(rs.getString("marca_nome"));
        motor.setPotencia(rs.getInt("motor_potencia"));
        motor.setSituacao(Enum.valueOf(ETipoCombustivel.class, rs.getString("motor_situacao")));
        
        return modelo;
    }    
}
