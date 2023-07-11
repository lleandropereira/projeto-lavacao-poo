/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.exception.DAOException;
import br.edu.ifsc.fln.model.dao.MarcaDAO;
import br.edu.ifsc.fln.model.dao.ModeloDAO;
import br.edu.ifsc.fln.model.dao.MotorDAO;
import br.edu.ifsc.fln.model.database.Database;
import br.edu.ifsc.fln.model.database.DatabaseFactory;
import br.edu.ifsc.fln.model.domain.ECategoria;
import br.edu.ifsc.fln.model.domain.ETipoCombustivel;
import br.edu.ifsc.fln.model.domain.Marca;
import br.edu.ifsc.fln.model.domain.Modelo;
import br.edu.ifsc.fln.model.domain.Motor;
import br.edu.ifsc.fln.utils.AlertDialog;
import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author leandropereira
 */
public class FXMLAnchorPaneCadastroModeloDialogController implements Initializable {

     @FXML
    private Button btCancelar;

    @FXML
    private Button btConfirmar;

    @FXML
    private ComboBox<Marca> cbMarca;
    
    @FXML
    private ComboBox<ECategoria> cbCategoria;
    
    @FXML
    private TextField tfDescricao;
    
    @FXML
    private Label lbModeloDescricaoDialog;

    @FXML
    private ComboBox<ETipoCombustivel> cbMotorCombustivel;

    @FXML
    private TextField tfMotorPotencia;
    
    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
    private final ModeloDAO modeloDAO = new ModeloDAO();
    private final MarcaDAO marcaDAO = new MarcaDAO();
    private final MotorDAO motorDAO = new MotorDAO();
    
    private Stage dialogStage;
    private boolean btConfirmarClicked = false;
    private Modelo modelo;
    private Motor motor;

    private List<Marca> listaMarcas;
    private ObservableList<Marca> observableListMarcas; 
    
     /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        marcaDAO.setConnection(connection);
        modeloDAO.setConnection(connection);
        motorDAO.setConnection(connection);
        carregarComboBoxMarca();
        carregarComboBoxMotor();
        carregarComboBoxCategoria();
        setFocusLostHandle();
    } 
    
    private void setFocusLostHandle() {
        tfDescricao.focusedProperty().addListener((ov, oldV, newV) -> {
        if (!newV) { // focus lost
                if (tfDescricao.getText() == null || tfDescricao.getText().isEmpty()) {
                    //System.out.println("teste focus lost");
                    tfDescricao.requestFocus();
                }
            }
        });
    }
    
    public void carregarComboBoxMarca() {
        try {
            listaMarcas = marcaDAO.listar();
        } catch(DAOException ex) {
            AlertDialog.exceptionMessage(ex);
        }
        
        observableListMarcas = 
                FXCollections.observableArrayList(listaMarcas);
        cbMarca.setItems(observableListMarcas);
    }    
    
    public void carregarComboBoxMotor() {
        cbMotorCombustivel.setItems( FXCollections.observableArrayList( ETipoCombustivel.values()));
    } 
    
    public void carregarComboBoxCategoria() {
        cbCategoria.setItems(FXCollections.observableArrayList(ECategoria.values()));
    }
    
    public Stage getDialogStage() {
        return dialogStage;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
        
    }

    public boolean isBtConfirmarClicked() {
        return btConfirmarClicked;
    }

    public void setBtConfirmarClicked(boolean btConfirmarClicked) {
        this.btConfirmarClicked = btConfirmarClicked;
    }

    public Modelo getModelo() {
        return modelo;
    }

    public void setModelo(Modelo modelo) {
        this.modelo = modelo;
        
        this.tfDescricao.setText(modelo.getDescricao());
        this.cbCategoria.getSelectionModel().select(modelo.getCategoria());
        this.cbMarca.getSelectionModel().select(modelo.getMarca());
        
        this.tfMotorPotencia.setText(Integer.toString(modelo.getMotor().getPotencia()));
        this.cbMotorCombustivel.getSelectionModel().select(modelo.getMotor().getSituacao());
        
    }

    
    
    
    @FXML
    void handleBtCancelar(ActionEvent event) {
        dialogStage.close();
    }

    @FXML
    void handleBtConfirmar(ActionEvent event) {
    if (validarEntradaDeDados()) {
            modelo.setDescricao(tfDescricao.getText());
            modelo.setCategoria(cbCategoria.getSelectionModel().getSelectedItem());
            modelo.setMarca(cbMarca.getSelectionModel().getSelectedItem());
            
            modelo.getMotor().setPotencia(Integer.parseInt(tfMotorPotencia.getText()));
            modelo.getMotor().setSituacao(cbMotorCombustivel.getSelectionModel().getSelectedItem());
            
            btConfirmarClicked = true;
            dialogStage.close();
            
        }

    }
    
    //método para validar a entrada de dados
    private boolean validarEntradaDeDados() {
        String errorMessage = "";
        int potenciaMin = 0;
        
        try {
            potenciaMin = Integer.parseInt(tfMotorPotencia.getText());
            if(potenciaMin <= /*potenciaMax*/ 0) {
                errorMessage += "O valor da potência informada deve ser maior que 0";
            }
            
        } catch (NumberFormatException e) {
            errorMessage += "Certifique-se de que a potência foi digitada";
        }
           
        if (cbMotorCombustivel.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "Selecione o tipo de combustível do veículo!\n";
        }
        
        if (this.tfDescricao.getText() == null || this.tfDescricao.getText().length() == 0) {
            errorMessage += "Descrição inválida.\n";
        }
        
        if (errorMessage.length() == 0) {
            return true;
        } else {
            //exibindo uma mensagem de erro
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro no cadastro");
            alert.setHeaderText("Corrija os campos inválidos!");
            alert.setContentText(errorMessage);
            alert.show();
            return false;
        }
        
    }
}
