/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.exception.DAOException;
import br.edu.ifsc.fln.model.dao.ServicoDAO;
import br.edu.ifsc.fln.model.database.Database;
import br.edu.ifsc.fln.model.database.DatabaseFactory;
import br.edu.ifsc.fln.model.domain.Servico;
import br.edu.ifsc.fln.utils.AlertDialog;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
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
public class FXMLAnchorPaneCadastroServicoController implements Initializable {
    
    @FXML
    private Button btAlterar;

    @FXML
    private Button btExcluir;

    @FXML
    private Button btInserir;

    @FXML
    private Label lbServicoDescricao;

    @FXML
    private Label lbServicoId;

    @FXML
    private Label lbServicoPonto;

    @FXML
    private Label lbServicoValor;
    
    @FXML
    private Label lbServicoCategoria;
    
    @FXML
    private TableColumn<Servico, String> tableColumnServico;

    @FXML
    private TableView<Servico> tableViewServico;
    
        private List<Servico> listaServico;
    private ObservableList<Servico> observableListServico;

    //acesso ao banco de dados
    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
    private final ServicoDAO servicoDAO = new ServicoDAO();
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        servicoDAO.setConnection(connection);

        carregarTableView();

        tableViewServico.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarItemTableView(newValue));
    }    
    
    public void carregarTableView() {
        tableColumnServico.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        try{
            listaServico = servicoDAO.listar();
        } catch(DAOException ex) {
            AlertDialog.exceptionMessage(ex);
        }
        observableListServico = FXCollections.observableArrayList(listaServico);
        tableViewServico.setItems(observableListServico);
    }
    
    public void selecionarItemTableView(Servico servico) {
        if (servico != null) {
            lbServicoId.setText(Integer.toString(servico.getId()));
            lbServicoDescricao.setText(servico.getDescricao());
            lbServicoValor.setText(String.valueOf(servico.getValor()));
            lbServicoPonto.setText(String.valueOf(servico.getPontos()));
            lbServicoCategoria.setText(servico.getCategoria().name());
        } else {
            lbServicoId.setText("");
            lbServicoDescricao.setText("");
            lbServicoValor.setText("");
            lbServicoPonto.setText("");
            lbServicoCategoria.setText("");
        }
    }
    
    @FXML
    void handleBtAlterar(ActionEvent event) throws IOException {
        Servico servico = tableViewServico.getSelectionModel().getSelectedItem();
        if (servico != null) {
            boolean buttonConfirmarClicked = showFXMLAnchorPaneCadastroServicoDialog(servico);
            if (buttonConfirmarClicked) {
                try{
                    servicoDAO.alterar(servico);
                } catch(DAOException ex) {
                    AlertDialog.exceptionMessage(ex);
                }
                carregarTableView();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha um venda na Tabela.");
            alert.show();
        }        
    }

    @FXML
    void handleBtExcluir(ActionEvent event) {
        Servico servico = tableViewServico.getSelectionModel().getSelectedItem();
        if (servico != null) {
            if (AlertDialog.confirmarExclusao("Tem certeza que deseja excluir o serviço " + servico.getId())) {
                servicoDAO.setConnection(connection);
                try{
                    servicoDAO.remover(servico);
                } catch(DAOException ex) {
                    AlertDialog.exceptionMessage(ex);
                }
                carregarTableView();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Por favor, escolha um serviço na tabela!");
            alert.show();
        }
    }

    @FXML
    void handleBtInserir(ActionEvent event) throws IOException {
        Servico servico = new Servico();
        boolean buttonConfirmarClicked = showFXMLAnchorPaneCadastroServicoDialog(servico);
        if (buttonConfirmarClicked) {
            servicoDAO.setConnection(connection);
            try{
                servicoDAO.inserir(servico);
            } catch(DAOException ex) {
                AlertDialog.exceptionMessage(ex);
            }
            carregarTableView();
        }
    }
    
    public boolean showFXMLAnchorPaneCadastroServicoDialog(Servico servico) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(FXMLAnchorPaneCadastroServicoDialogController.class.getResource(
                "../view/FXMLAnchorPaneCadastroServicoDialog.fxml"));
        AnchorPane page = (AnchorPane) loader.load();

        //criando um estágio de diálogo  (Stage Dialog)
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Cadastro de serviços");
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        //Setando o venda ao controller
        FXMLAnchorPaneCadastroServicoDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setServico(servico);

        //Mostra o diálogo e espera até que o usuário o feche
        dialogStage.showAndWait();

        return controller.isButtonConfirmarClicked();
    }
    
}
