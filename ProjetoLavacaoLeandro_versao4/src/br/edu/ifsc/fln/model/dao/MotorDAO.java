package br.edu.ifsc.fln.model.dao;

import br.edu.ifsc.fln.exception.DAOException;
import br.edu.ifsc.fln.model.domain.ETipoCombustivel;
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


public class MotorDAO{

    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

     public boolean inserir(Motor motor) throws DAOException{
        final String sql = "INSERT INTO motor(id_modelo) VALUES(?);";
          
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, motor.getModelo().getId());
            stmt.execute();
            alterar(motor);
        } catch (SQLException ex) {
            Logger.getLogger(MotorDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Não foi possível inserir o motor no banco de dados!", ex);
        }
        return false;
    }
    
    public boolean alterar(Motor motor) throws DAOException{
        String sql = "UPDATE motor SET potencia=?, situacao=? WHERE id_modelo=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, motor.getPotencia());
            stmt.setString(2, motor.getSituacao().name());
            stmt.setInt(3, motor.getModelo().getId());
            stmt.execute();
        } catch (SQLException ex) {
            Logger.getLogger(MotorDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Não foi possível alterar o motor no banco de dados!", ex);
        }
        return false;
    }
    
    public void remover(Motor motor) throws DAOException{
        String sql = "DELETE FROM motor WHERE id_modelo=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, motor.getModelo().getId());
            stmt.execute();
        } catch (SQLException ex) {
            Logger.getLogger(MotorDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Não foi possível remover o motor no banco de dados!", ex);
        }
    }
   
    public List<Motor> listar() throws DAOException{
        String sql = "SELECT * FROM motor";
        List<Motor> retorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                Motor motor = new Motor();
                motor.getModelo().setId(resultado.getInt("id_modelo"));
                motor.setPotencia(resultado.getInt("potencia"));
                motor.setSituacao(Enum.valueOf(ETipoCombustivel.class, resultado.getString("situacao")));
//                Motor motor = populateVO(resultado);
                retorno.add(motor);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MotorDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Não foi possível listar o motor no banco de dados!", ex);
        }
        return retorno;
    }
    
    public Motor buscar(Modelo modelo) throws DAOException{
        String sql = "SELECT * FROM motor WHERE id_modelo=?";
        Motor retorno = new Motor();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, modelo.getId());
            ResultSet resultado = stmt.executeQuery();
            if(resultado.next()) {
                retorno = populateVO(resultado);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ModeloDAO.class.getName()).log(Level.SEVERE, null, ex);
            throw new DAOException("Não foi possível buscar o motor no banco de dados!", ex);
        }
        return retorno;
    }
    
//    public List<Modelo> listarPorMotor(Motor motor) {
//        String sql = "SELECT * FROM motor mo INNER JOIN modelo m ON m.id = mo.id_modelo WHERE mo.id_modelo = ?";
//        List<Modelo> retorno = new ArrayList<>();
//        try {
//            PreparedStatement stmt = connection.prepareStatement(sql);
//            stmt.setInt(1, motor.getModelo().getId());
//            ResultSet resultado = stmt.executeQuery();
//            while (resultado.next()) {
//                Modelo modelo = populateVO(resultado);
//                retorno.add(modelo);
//            }
//        } catch (SQLException ex) {
//            Logger.getLogger(MotorDAO.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return retorno;
//    }

    private Motor populateVO(ResultSet rs) throws SQLException {
        Motor motor = new Motor();
        
        motor.setPotencia(rs.getInt("potencia"));
        motor.setSituacao(Enum.valueOf(ETipoCombustivel.class, rs.getString("situacao")));
        return motor;
    }   
    

}
