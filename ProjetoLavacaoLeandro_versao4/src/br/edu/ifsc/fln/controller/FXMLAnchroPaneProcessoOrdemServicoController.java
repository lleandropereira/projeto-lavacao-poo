/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.exception.DAOException;
import br.edu.ifsc.fln.model.dao.ItemOSDAO;
import br.edu.ifsc.fln.model.dao.OrdemServicoDAO;
import br.edu.ifsc.fln.model.dao.ServicoDAO;
import br.edu.ifsc.fln.model.database.Database;
import br.edu.ifsc.fln.model.database.DatabaseFactory;
import br.edu.ifsc.fln.model.domain.ItemOS;
import br.edu.ifsc.fln.model.domain.OrdemServico;
import br.edu.ifsc.fln.utils.AlertDialog;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableCell;

/**
 * FXML Controller class
 *
 * @author leandropereira
 */
public class FXMLAnchroPaneProcessoOrdemServicoController implements Initializable {
    
    @FXML
    private Button btAlterar;

    @FXML
    private Button btExcluir;

    @FXML
    private Button btInserir;

    @FXML
    private Label lbOrdemServicoAgenda;

    @FXML
    private Label lbOrdemServicoCliente;

    @FXML
    private Label lbOrdemServicoDesconto;

    @FXML
    private Label lbOrdemServicoNumero;

    @FXML
    private Label lbOrdemServicoSituacao;

    @FXML
    private Label lbOrdemServicoTotal;

    @FXML
    private TableColumn<OrdemServico, Integer> tableColumnNumero;

    @FXML
    private TableColumn<OrdemServico, String> tableColumnPlaca;

    @FXML
    private TableColumn<OrdemServico, LocalDate> tableColumnAgenda;
    
    @FXML
    private TableView<OrdemServico> tableView;

    private List<OrdemServico> listaOrdemServico;
    private ObservableList<OrdemServico> observableListOrdemServico;

    //acesso ao banco de dados
    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
    private final OrdemServicoDAO ordemServicoDAO = new OrdemServicoDAO();
    private final ItemOSDAO itemOSDAO = new ItemOSDAO();
    private final ServicoDAO servicoDAO = new ServicoDAO();
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        ordemServicoDAO.setConnection(connection);
        itemOSDAO.setConnection(connection);
        carregarTableView();

        tableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarItemTableView(newValue));
    }    

    public void carregarTableView() {
        DateTimeFormatter myDateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        try {
            listaOrdemServico = ordemServicoDAO.listar(); // Busca a lista atualizada do banco de dados
        } catch(DAOException ex) {
            AlertDialog.exceptionMessage(ex);
        }

        tableColumnNumero.setCellValueFactory(new PropertyValueFactory<>("numero"));
        tableColumnPlaca.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPlacaVeiculo()));
    
        tableColumnAgenda.setCellFactory(column -> {
            return new TableCell<OrdemServico, LocalDate>() {
                @Override
                protected void updateItem(LocalDate item, boolean empty) {
                    super.updateItem(item, empty);

                    if (item == null || empty) {
                        setText(null);
                    } else {
                        setText(myDateFormatter.format(item));
                    }
                }
            };
        });
        tableColumnAgenda.setCellValueFactory(new PropertyValueFactory<>("agenda"));
    
        observableListOrdemServico = FXCollections.observableArrayList(listaOrdemServico);
        tableView.setItems(observableListOrdemServico); // Define a observableListOrdemServico como a fonte de itens da tableView
    }

    
    public void selecionarItemTableView(OrdemServico ordemServico) {
        if (ordemServico != null) {
            lbOrdemServicoNumero.setText(Integer.toString(ordemServico.getNumero()));
            lbOrdemServicoCliente.setText(ordemServico.getVeiculo().getCliente().getNome());
            lbOrdemServicoTotal.setText(String.format("%.2f", ordemServico.getTotal()));
            lbOrdemServicoAgenda.setText(String.valueOf(ordemServico.getAgenda().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
            lbOrdemServicoDesconto.setText((String.format("%.2f", ordemServico.getTaxaDesconto())) + "%");
            lbOrdemServicoSituacao.setText(ordemServico.getStatus().name());
        } else {
            lbOrdemServicoNumero.setText("");
            lbOrdemServicoCliente.setText("");
            lbOrdemServicoTotal.setText("");
            lbOrdemServicoAgenda.setText("");
            lbOrdemServicoDesconto.setText("");
            lbOrdemServicoSituacao.setText("");
        }
    }
    
    @FXML
    void handleBtAlterar(ActionEvent event) throws IOException {
        OrdemServico ordemServico = tableView.getSelectionModel().getSelectedItem();
        if (ordemServico != null) {
            boolean buttonConfirmarClicked = showFXMLAnchorPaneProcessoOrdemServicoDialog(ordemServico);
            if (buttonConfirmarClicked) {
                try {
                    ordemServicoDAO.alterar(ordemServico);
                } catch(DAOException ex) {
                    AlertDialog.exceptionMessage(ex);
                }
                carregarTableView();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha uma ordem de serviço na tabela.");
            alert.show();
        }        
    }

    @FXML
    void handleBtExcluir(ActionEvent event) {
        OrdemServico ordemServico = tableView.getSelectionModel().getSelectedItem();
        if (ordemServico != null) {
            if (AlertDialog.confirmarExclusao("Tem certeza que deseja excluir a venda " + ordemServico.getNumero())) {
                ordemServicoDAO.setConnection(connection);
                try {
                    ordemServicoDAO.remover(ordemServico);
                } catch(DAOException ex) {
                    AlertDialog.exceptionMessage(ex);
                }
                carregarTableView();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Por favor, escolha uma venda na tabela!");
            alert.show();
        }
    }

    @FXML
    void handleBtInserir(ActionEvent event) throws IOException {
        OrdemServico ordemServico = new OrdemServico();
        List<ItemOS> itensOS = new ArrayList<>();
        ordemServico.setItensOS(itensOS);
        boolean buttonConfirmarClicked = showFXMLAnchorPaneProcessoOrdemServicoDialog(ordemServico);
        if (buttonConfirmarClicked) {
            ordemServicoDAO.setConnection(connection);
            try {
                ordemServicoDAO.inserir(ordemServico);
            } catch(DAOException ex) {
                AlertDialog.exceptionMessage(ex);
            }
            carregarTableView();
        }
    }
    
    public boolean showFXMLAnchorPaneProcessoOrdemServicoDialog(OrdemServico ordemServico) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(FXMLAnchorPaneProcessoOrdemServicoDialogController.class.getResource(
                "../view/FXMLAnchorPaneProcessoOrdemServicoDialog.fxml"));
        AnchorPane page = (AnchorPane) loader.load();

        //criando um estágio de diálogo  (Stage Dialog)
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Cadastro de ordem de serviço");
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        //Setando o venda ao controller
        FXMLAnchorPaneProcessoOrdemServicoDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setOrdemServico(ordemServico);

        //Mostra o diálogo e espera até que o usuário o feche
        dialogStage.showAndWait();

        return controller.isButtonConfirmarClicked();
    }
}
