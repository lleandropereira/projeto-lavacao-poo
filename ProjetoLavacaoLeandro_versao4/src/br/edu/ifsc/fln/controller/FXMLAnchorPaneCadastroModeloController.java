/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.exception.DAOException;
import br.edu.ifsc.fln.model.dao.ModeloDAO;
import br.edu.ifsc.fln.model.dao.MotorDAO;
import br.edu.ifsc.fln.model.database.Database;
import br.edu.ifsc.fln.model.database.DatabaseFactory;
import br.edu.ifsc.fln.model.domain.Modelo;
import br.edu.ifsc.fln.utils.AlertDialog;
import java.io.IOException;
import java.sql.Connection;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author leandropereira
 */
public class FXMLAnchorPaneCadastroModeloController implements Initializable {

    @FXML
    private Button btAlterar;

    @FXML
    private Button btExcluir;

    @FXML
    private Button btInserir;

    @FXML
    private Label lbModeloDescricao;

    @FXML
    private Label lbModeloId;
    
    @FXML
    private Label lbModeloMarca;
    
    @FXML
    private Label lbModeloCategoria;
    
    //APLICACAO DO MOTOR
    @FXML
    private Label lbMotorCombustivel;
    //APLICACAO DO MOTOR
    @FXML
    private Label lbMotorPotencia;

    @FXML
    private TableColumn<Modelo, String> tableColumnModeloDescricao;

    @FXML
    private TableView<Modelo> tableViewModelo;

    private List<Modelo> listaModelo;
    private ObservableList<Modelo> observableListModelo;
    
    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
    private final ModeloDAO modeloDAO = new ModeloDAO();
    private final MotorDAO motorDAO = new MotorDAO();
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        modeloDAO.setConnection(connection);
        motorDAO.setConnection(connection);
        carregarTableViewModelo();

        tableViewModelo.getSelectionModel().selectedItemProperty().
            addListener((observable, oldValue, newValue) ->
                selecionarItemTableViewModelo(newValue));
    }
    
    public void carregarTableViewModelo() {
        tableColumnModeloDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        try{
            listaModelo = modeloDAO.listar();
        } catch(DAOException ex) {
            AlertDialog.exceptionMessage(ex);
        }
        observableListModelo = FXCollections.observableArrayList(listaModelo);
        tableViewModelo.setItems(observableListModelo);
        
    }

    public void selecionarItemTableViewModelo(Modelo modelo) {
        if (modelo != null) {
            lbModeloId.setText(String.valueOf(modelo.getId())); 
            lbModeloDescricao.setText(modelo.getDescricao());
            lbModeloCategoria.setText(modelo.getCategoria().name());
            lbModeloMarca.setText(modelo.getMarca().getNome());
            lbMotorPotencia.setText(Integer.toString(modelo.getMotor().getPotencia()));
            lbMotorCombustivel.setText(modelo.getMotor().getSituacao().name());
        } else {
            lbModeloId.setText(""); 
            lbModeloDescricao.setText("");
            lbModeloCategoria.setText("");
            lbModeloMarca.setText("");
            lbMotorPotencia.setText("");
            lbMotorCombustivel.setText("");
        }
        
    }

    @FXML 
    public void handleBtAlterar() throws IOException {
        Modelo modelo = tableViewModelo.getSelectionModel().getSelectedItem();
        if (modelo != null) {
            modelo.getMotor().setModelo(modelo);
            boolean btConfirmarClicked = showFXMLAnchorPaneCadastroModeloDialog(modelo);
            if (btConfirmarClicked) {
                try {
                    modeloDAO.alterar(modelo);
                    motorDAO.alterar(modelo.getMotor());
                } catch(DAOException ex) {
                    AlertDialog.exceptionMessage(ex);
                }
                
                carregarTableViewModelo();
            }
            
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Esta operação requer a seleção \nde uma Marca na tabela ao lado");
            alert.show();
        }
        
    }

    @FXML
    public void handleBtExcluir() throws IOException {
        Modelo modelo = tableViewModelo.getSelectionModel().getSelectedItem();
        if (modelo != null) {
            try{
                modeloDAO.remover(modelo);
            } catch(DAOException ex) {
                AlertDialog.exceptionMessage(ex);
            }
            carregarTableViewModelo();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Esta operação requer a seleção \nde uma Marca na tabela ao lado");
            alert.show();
        }
    }

    @FXML
    public void handleBtInserir() throws IOException {
        Modelo modelo = new Modelo();
        boolean btConfirmarClicked = showFXMLAnchorPaneCadastroModeloDialog(modelo);
        if (btConfirmarClicked) {
            try{
                modeloDAO.inserir(modelo);
            } catch(DAOException ex) {
                AlertDialog.exceptionMessage(ex);
            }
            carregarTableViewModelo();
        }
    }

    private boolean showFXMLAnchorPaneCadastroModeloDialog(Modelo modelo) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        
        loader.setLocation(FXMLAnchorPaneCadastroModeloDialogController.class.getResource("../view/FXMLAnchorPaneCadastroModeloDialog.fxml"));
        AnchorPane page = (AnchorPane) loader.load();
        
        //criação de um estágio de diálogo (StageDialog)
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Cadastro de Modelo");
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);
        
        
        //enviando o objeto modelo para o controller
        FXMLAnchorPaneCadastroModeloDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setModelo(modelo);
        
        
        
        //apresenta o diálogo e aguarda a confirmação do usuário
        dialogStage.showAndWait();
        
        return controller.isBtConfirmarClicked();
    }
}
