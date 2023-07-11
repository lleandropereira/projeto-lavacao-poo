/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.model.dao.ServicoDAO;
import br.edu.ifsc.fln.model.database.Database;
import br.edu.ifsc.fln.model.database.DatabaseFactory;
import br.edu.ifsc.fln.model.domain.ECategoria;
import br.edu.ifsc.fln.model.domain.Servico;
import java.sql.Connection;
import java.net.URL;
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
public class FXMLAnchorPaneCadastroServicoDialogController implements Initializable {

    @FXML
    private Button btCancelar;

    @FXML
    private Button btConfirmar;

    @FXML
    private ComboBox<ECategoria> cbServicoCategoria;

    @FXML
    private TextField tfServicoValor;

    @FXML
    private TextField tfServicoDescricao;
    
    private List<Servico> listaServico;
    private ObservableList<Servico> observableListServico;
    
    //atributos para manipulação de banco de dados
    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
    private final ServicoDAO servicoDAO = new ServicoDAO();
    
    private Stage dialogStage;
    private boolean buttonConfirmarClicked = false;
    private Servico servico;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        servicoDAO.setConnection(connection);
        carregarComboBoxCategoria();
        setFocusLostHandle();
    }    
    
    private void setFocusLostHandle() {
        tfServicoDescricao.focusedProperty().addListener((ov, oldV, newV) -> {
        if (!newV) { // focus lost
                if (tfServicoDescricao.getText() == null || tfServicoDescricao.getText().isEmpty()) {
                    //System.out.println("teste focus lost");
                    tfServicoDescricao.requestFocus();
                }
            }
        });
    }
    
    public void carregarComboBoxCategoria() {
        cbServicoCategoria.setItems( FXCollections.observableArrayList( ECategoria.values()));
    } 

    public Stage getDialogStage() {
        return dialogStage;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public boolean isButtonConfirmarClicked() {
        return buttonConfirmarClicked;
    }

    public void setButtonConfirmarClicked(boolean buttonConfirmarClicked) {
        this.buttonConfirmarClicked = buttonConfirmarClicked;
    }

    public Servico getServico() {
        return servico;
    }

    public void setServico(Servico servico) {
        this.servico = servico;
        this.tfServicoDescricao.setText(servico.getDescricao());
        this.tfServicoValor.setText(Float.toString((float) servico.getValor()));
        this.cbServicoCategoria.getSelectionModel().select(servico.getCategoria());
    }
    
    
    
    @FXML
    void handleBtCancelar(ActionEvent event) {
        dialogStage.close();
    }

    @FXML
    void handleBtConfirmar(ActionEvent event) {
        if (validarEntradaDeDados()) {
            servico.setDescricao(tfServicoDescricao.getText());
            servico.setValor(Float.parseFloat(tfServicoValor.getText()));
            servico.setCategoria(cbServicoCategoria.getSelectionModel().getSelectedItem());
            
            buttonConfirmarClicked = true;
            dialogStage.close();
            
        }
    }
    
    private boolean validarEntradaDeDados() {
        String errorMessage = "";
        int valorMin = 0;
        
        try {
            valorMin = (int) Float.parseFloat(tfServicoValor.getText());
            if(valorMin <= /*potenciaMax*/ 0) {
                errorMessage += "O valor informado deve ser maior que 0";
            }
            
        } catch (NumberFormatException e) {
            errorMessage += "Certifique-se de que o valor foi digitado";
        }
           
        if (cbServicoCategoria.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "Selecione o tipo de categoria do veículo!\n";
        }
        
        if (this.tfServicoDescricao.getText() == null || this.tfServicoDescricao.getText().length() == 0) {
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
