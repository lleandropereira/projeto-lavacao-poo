/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.exception.DAOException;
import br.edu.ifsc.fln.model.dao.ClienteDAO;
import br.edu.ifsc.fln.model.dao.CorDAO;
import br.edu.ifsc.fln.model.dao.ModeloDAO;
import br.edu.ifsc.fln.model.database.Database;
import br.edu.ifsc.fln.model.database.DatabaseFactory;
import br.edu.ifsc.fln.model.domain.Cliente;
import br.edu.ifsc.fln.model.domain.Cor;
import br.edu.ifsc.fln.model.domain.Modelo;
import br.edu.ifsc.fln.model.domain.Veiculo;
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
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author leandropereira
 */
public class FXMLAnchorPaneCadastroVeiculoDialogController implements Initializable {

    @FXML
    private Button btCancelar;

    @FXML
    private Button btConfirmar;

    @FXML
    private ComboBox<Cor> cbCor;

    @FXML
    private ComboBox<Modelo> cbModelo;
    
    @FXML
    private ComboBox<Cliente> cbCliente;

    @FXML
    private TextField tfObservacao;

    @FXML
    private TextField tfPlaca;

    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
    private final CorDAO corDAO = new CorDAO();
    private final ModeloDAO modeloDAO = new ModeloDAO();
    private final ClienteDAO clienteDAO = new ClienteDAO();
    
            
    private List<Cor> listaCor;
    private ObservableList<Cor> observableListCor; 
    
    public void carregarComboBoxCor() {
        try{
            listaCor = corDAO.listar();
        } catch(DAOException ex) {
            AlertDialog.exceptionMessage(ex);
        }
        observableListCor = 
                FXCollections.observableArrayList(listaCor);
        cbCor.setItems(observableListCor);
    }
    
    private List<Modelo> listaModelo;
    private ObservableList<Modelo> observableListModelo;
    
    public void carregarComboBoxModelo() {
        try{
            listaModelo = modeloDAO.listar();
        } catch(DAOException ex) {
            AlertDialog.exceptionMessage(ex);
        }
        observableListModelo = 
                FXCollections.observableArrayList(listaModelo);
        cbModelo.setItems(observableListModelo);
    }    
    
    private List<Cliente> listaCliente;
    private ObservableList<Cliente> observableListCliente; 
    
    public void carregarComboBoxCliente() {
        try{
            listaCliente = clienteDAO.listar();
        } catch(DAOException ex) {
            AlertDialog.exceptionMessage(ex);
        }
        observableListCliente = 
                FXCollections.observableArrayList(listaCliente);
        cbCliente.setItems(observableListCliente);
    }
    
    private Stage dialogStage;
    private boolean btConfirmarClicked = false;
    private Veiculo veiculo;

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

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
        
        
        tfPlaca.setText(veiculo.getPlaca());
        tfObservacao.setText(veiculo.getObservacao());
        cbCor.getSelectionModel().select(veiculo.getCor());
        cbModelo.getSelectionModel().select(veiculo.getModelo());
        cbCliente.getSelectionModel().select(veiculo.getCliente());
        veiculo.setCliente(
                    cbCliente.getSelectionModel().getSelectedItem());
    }
    
    
    @FXML
    void handleBtCancelar(ActionEvent event) {
        dialogStage.close();
    }

    @FXML
    void handleBtConfirmar(ActionEvent event) {
        if (validarEntradaDeDados()) {
            veiculo.setPlaca(tfPlaca.getText());
            veiculo.setObservacao(tfObservacao.getText());
            veiculo.setCor(cbCor.getSelectionModel().getSelectedItem());
            veiculo.setModelo(cbModelo.getSelectionModel().getSelectedItem());
            veiculo.setCliente(cbCliente.getSelectionModel().getSelectedItem());

            btConfirmarClicked = true;
            dialogStage.close();
        }

    }
    
    private boolean validarEntradaDeDados() {
        String errorMessage = "";
        
        if (tfPlaca.getText() == null || tfPlaca.getText().isEmpty()) {
            errorMessage += "Placa inválido!\n";
        }
        
        if (cbCor.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "Selecione uma cor!\n";
        }
        
        if (cbModelo.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "Selecione um modelo!\n";
        }
        
        if (cbCliente.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "Selecione um Cliente!\n";
        }
        
        if (errorMessage.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro no cadastro");
            alert.setHeaderText("Campo(s) inválido(s), por favor corrija...");
            alert.setContentText(errorMessage);
            alert.show();
            return false;
        }
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        corDAO.setConnection(connection);
        carregarComboBoxCor();
        modeloDAO.setConnection(connection);
        carregarComboBoxModelo();
        clienteDAO.setConnection(connection);
        carregarComboBoxCliente();
        setFocusLostHandle();
    }    
    
    private void setFocusLostHandle() {
        tfPlaca.focusedProperty().addListener((ov, oldV, newV) -> {
        if (!newV) { // focus lost
                if (tfPlaca.getText() == null || tfPlaca.getText().isEmpty()) {
                    //System.out.println("teste focus lost");
                    tfPlaca.requestFocus();
                }
            }
        });
    }
    
}
