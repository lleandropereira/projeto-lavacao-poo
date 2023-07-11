/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.exception.DAOException;
import br.edu.ifsc.fln.model.dao.VeiculoDAO;
import br.edu.ifsc.fln.model.database.Database;
import br.edu.ifsc.fln.model.database.DatabaseFactory;
import br.edu.ifsc.fln.model.domain.Veiculo;
import br.edu.ifsc.fln.utils.AlertDialog;
import java.io.IOException;
import java.sql.Connection;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
public class FXMLAnchorPaneCadastroVeiculoController implements Initializable {

    @FXML
    private Button btAlterar;

    @FXML
    private Button btExlcuir;

    @FXML
    private Button btInserir;

    @FXML
    private Label lbVeiculoCor;

    @FXML
    private Label lbVeiculoId;

    @FXML
    private Label lbVeiculoModelo;

    @FXML
    private Label lbVeiculoObservacao;

    @FXML
    private Label lbVeiculoPlaca;
    
    @FXML
    private Label lbVeiculoMarca;
    
    @FXML
    private Label lbVeiculoCliente;
    
    @FXML
    private TableColumn<Veiculo, String> tableColumnVeiculo;

    @FXML
    private TableView<Veiculo> tableViewVeiculo;

    private List<Veiculo> listaVeiculo;
    private ObservableList<Veiculo> observableListVeiculo;

    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
    private final VeiculoDAO veiculoDAO = new VeiculoDAO();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        veiculoDAO.setConnection(connection);
        carregarTableViewVeiculo();

        tableViewVeiculo.getSelectionModel().selectedItemProperty().
            addListener((observable, oldValue, newValue) ->
                selecionarItemTableViewVeiculo(newValue));

    }    
    
    public void carregarTableViewVeiculo() {
        tableColumnVeiculo.setCellValueFactory(new PropertyValueFactory<>("placa"));
        
        try{
            listaVeiculo = veiculoDAO.listar();
        } catch(DAOException ex) {
            AlertDialog.exceptionMessage(ex);
        }
        
        observableListVeiculo = FXCollections.observableArrayList(listaVeiculo);
        tableViewVeiculo.setItems(observableListVeiculo);
    }

     public void selecionarItemTableViewVeiculo(Veiculo veiculo) {
        if (veiculo != null) {
            lbVeiculoId.setText(String.valueOf(veiculo.getId()));
            lbVeiculoPlaca.setText(veiculo.getPlaca());
            lbVeiculoObservacao.setText(veiculo.getObservacao());
            lbVeiculoCor.setText(veiculo.getCor().getNome());
            lbVeiculoModelo.setText(veiculo.getModelo().getDescricao());
            lbVeiculoMarca.setText(veiculo.getModelo().getMarca().getNome());
            lbVeiculoCliente.setText(veiculo.getCliente().getNome());
            
        } else {
            lbVeiculoId.setText("");
            lbVeiculoPlaca.setText("");
            lbVeiculoObservacao.setText("");
            lbVeiculoCor.setText("");
            lbVeiculoModelo.setText("");
            lbVeiculoMarca.setText("");
            lbVeiculoCliente.setText("");
        }
        
    }

    @FXML
    void handleBrAlterar(ActionEvent event) throws IOException {
        Veiculo veiculo = tableViewVeiculo.getSelectionModel().getSelectedItem();
        if (veiculo != null) {
            boolean btConfirmarClicked = showFXMLAnchorPaneCadastroVeiculoDialog(veiculo);
            if (btConfirmarClicked) {
                try{
                    veiculoDAO.alterar(veiculo);
                } catch(DAOException ex) {
                    AlertDialog.exceptionMessage(ex);
                }
                carregarTableViewVeiculo();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Esta operação requer a seleção \nde um Veículo na tabela ao lado");
            alert.show();
        }

    }

    @FXML
    void handleBtExcluir(ActionEvent event) throws IOException {
        Veiculo veiculo = tableViewVeiculo.getSelectionModel().getSelectedItem();
        if (veiculo != null) {
            try{
                veiculoDAO.remover(veiculo);
            } catch(DAOException ex) {
                AlertDialog.exceptionMessage(ex);
            }
            carregarTableViewVeiculo();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Esta operação requer a seleção \nde um Veículo na tabela ao lado");
            alert.show();
        }

    }

    @FXML
    void handleBtInserir(ActionEvent event) throws IOException {
        Veiculo veiculo = new Veiculo();
        boolean btConfirmarClicked = showFXMLAnchorPaneCadastroVeiculoDialog(veiculo);
        if (btConfirmarClicked) {
            try{
                veiculoDAO.inserir(veiculo);
            } catch(DAOException ex) {
                AlertDialog.exceptionMessage(ex);
            }
            carregarTableViewVeiculo();
        } 

    }
    
    private boolean showFXMLAnchorPaneCadastroVeiculoDialog(Veiculo veiculo) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        
        loader.setLocation(FXMLAnchorPaneCadastroVeiculoController.class.getResource("../view/FXMLAnchorPaneCadastroVeiculoDialog.fxml"));
        AnchorPane page = (AnchorPane) loader.load();
        
        //criação de um estágio de diálogo (StageDialog)
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Cadastro de Veículo");
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);
        
        //enviando o objeto categoria para o controller
        FXMLAnchorPaneCadastroVeiculoDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setVeiculo(veiculo);
        
        //apresenta o diálogo e aguarda a confirmação do usuário
        dialogStage.showAndWait();
        
        return controller.isBtConfirmarClicked();
    }   
}
