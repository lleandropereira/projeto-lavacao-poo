/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.exception.DAOException;
import br.edu.ifsc.fln.model.dao.ServicoDAO;
import br.edu.ifsc.fln.model.dao.VeiculoDAO;
import br.edu.ifsc.fln.model.database.Database;
import br.edu.ifsc.fln.model.database.DatabaseFactory;
import br.edu.ifsc.fln.model.domain.EStatus;
import br.edu.ifsc.fln.model.domain.ItemOS;
import br.edu.ifsc.fln.model.domain.OrdemServico;
import br.edu.ifsc.fln.model.domain.Servico;
import br.edu.ifsc.fln.model.domain.Veiculo;
import br.edu.ifsc.fln.utils.AlertDialog;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author leandropereira
 */
public class FXMLAnchorPaneProcessoOrdemServicoDialogController implements Initializable {
    
    @FXML
    private Button btAdicionar;

    @FXML
    private Button btCancelar;

    @FXML
    private Button btConfirmar;

    @FXML
    private ComboBox<Servico> cbServico;

    @FXML
    private ComboBox<EStatus> cbStatus;

    @FXML
    private ComboBox<Veiculo> cbVeiculo;

    @FXML
    private ContextMenu contextMenu;

    @FXML
    private DatePicker dpAgenda;

    @FXML
    private TableColumn<ItemOS, String> tableColumnObservacao;

    @FXML
    private TableColumn<ItemOS, Servico> tableColumnServico;

    @FXML
    private TableColumn<ItemOS, BigDecimal> tableColumnValor;

    @FXML
    private TableView<ItemOS> tableView;

    @FXML
    private TextField tfCliente;

    @FXML
    private TextField tfDesconto;

    @FXML
    private TextField tfObservacao;

    @FXML
    private TextField tfValor;
    
    @FXML
    private MenuItem contextMenuItemAtualizarObs;

    @FXML
    private MenuItem contextMenuItemRemoverItem;

    private List<Veiculo> listaVeiculo;
    private List<Servico> listaServico;
    private ObservableList<Veiculo> observableListVeiculo;
    private ObservableList<Servico> observableListServico;
    private ObservableList<ItemOS> observableListItemOS;

    //atributos para manipulação de banco de dados
    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
    private final VeiculoDAO veiculoDAO = new VeiculoDAO();
    private final ServicoDAO servicoDAO = new ServicoDAO();

    private Stage dialogStage;
    private boolean buttonConfirmarClicked = false;
    private OrdemServico ordemServico;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        veiculoDAO.setConnection(connection);
        servicoDAO.setConnection(connection);
        carregarComboBoxVeiculo();
        carregarComboBoxServico();
        carregarComboBoxStatus();
        setFocusLostHandle();
        tableColumnServico.setCellValueFactory(new PropertyValueFactory<>("servico"));
        tableColumnObservacao.setCellValueFactory(new PropertyValueFactory<>("observacao"));
        tableColumnValor.setCellValueFactory(new PropertyValueFactory<>("valorServico"));
    }    
    
    private void carregarComboBoxVeiculo() {
        try {
            listaVeiculo = veiculoDAO.listar();
        } catch(DAOException ex) {
            AlertDialog.exceptionMessage(ex);
        }
        observableListVeiculo = FXCollections.observableArrayList(listaVeiculo);
        cbVeiculo.setItems(observableListVeiculo);
        
        cbVeiculo.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                tfCliente.setText(newValue.getCliente().getNome());
            }
        });
    }

    private void carregarComboBoxServico() {
        try {
            listaServico = servicoDAO.listar();
        } catch(DAOException ex) {
            AlertDialog.exceptionMessage(ex);
        }
        observableListServico = FXCollections.observableArrayList(listaServico);
        cbServico.setItems(observableListServico);
    }
    
    
    public void carregarComboBoxStatus() {
        cbStatus.setItems(FXCollections.observableArrayList(EStatus.values()));
    }

    private void setFocusLostHandle() {
        tfDesconto.focusedProperty().addListener((ov, oldV, newV) -> {
        if (!newV) { // focus lost
                if (tfDesconto.getText() != null && !tfDesconto.getText().isEmpty()) {
                    //System.out.println("teste focus lost");
                    ordemServico.setTaxaDesconto(Double.parseDouble(tfDesconto.getText()));
                    tfValor.setText(ordemServico.getTotal().toString());
                    
                }
            }
        });
    }
    
    public Stage getDialogStage() {
        return dialogStage;
    }

    /**
     * @param dialogStage the dialogStage to set
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * @return the buttonConfirmarClicked
     */
    public boolean isButtonConfirmarClicked() {
        return buttonConfirmarClicked;
    }

    /**
     * @param buttonConfirmarClicked the buttonConfirmarClicked to set
     */
    public void setButtonConfirmarClicked(boolean buttonConfirmarClicked) {
        this.buttonConfirmarClicked = buttonConfirmarClicked;
    }

    public OrdemServico getOrdemServico() {
        return ordemServico;
    }

    
    public void setOrdemServico(OrdemServico ordemServico) {
        this.ordemServico = ordemServico;
        if (ordemServico.getNumero()!= 0) { 
            cbVeiculo.getSelectionModel().select(this.ordemServico.getVeiculo());
            dpAgenda.setValue(this.ordemServico.getAgenda());
            observableListItemOS = FXCollections.observableArrayList(
                    this.ordemServico.getItensOS());
            tableView.setItems(observableListItemOS);
            tfValor.setText(String.format("%.2f", this.ordemServico.getTotal()));
            tfDesconto.setText(String.format("%.2f", this.ordemServico.getTaxaDesconto()));
            
            cbStatus.getSelectionModel().select(this.ordemServico.getStatus());
        }
    }
    
    @FXML
    public void handleBtAdicionar() {
        Servico servico;
        ItemOS itemOS = new ItemOS();
        if (cbServico.getSelectionModel().getSelectedItem() != null) {
            //o comboBox possui dados sintetizados de Produto para evitar carga desnecessária de informação
            servico = cbServico.getSelectionModel().getSelectedItem();
            //a instrução a seguir busca detalhes do produto selecionado
            try {
                servico = servicoDAO.buscar(servico);
            } catch(DAOException ex) {
                AlertDialog.exceptionMessage(ex);
            }
            
            itemOS.setServico(servico);
            itemOS.setObservacao(tfObservacao.getText());
            itemOS.setValorServico(BigDecimal.valueOf(servico.getValor()));
            itemOS.setOrdemServico(ordemServico);
            ordemServico.getItensOS().add(itemOS);
            observableListItemOS = FXCollections.observableArrayList(ordemServico.getItensOS());
            tableView.setItems(observableListItemOS);
            tfValor.setText(String.format("%.2f", ordemServico.getTotal()));
            
        }
    }

    @FXML
    void handleBtCancelar(ActionEvent event) {
        dialogStage.close();
    }

    @FXML
    void handleBtConfirmar(ActionEvent event) {
        if (validarEntradaDeDados()) {
            ordemServico.setVeiculo(cbVeiculo.getSelectionModel().getSelectedItem());
            ordemServico.setAgenda(dpAgenda.getValue());
            ordemServico.setTaxaDesconto(Double.parseDouble(tfDesconto.getText()));
            ordemServico.setStatus((EStatus)cbStatus.getSelectionModel().getSelectedItem());
            ordemServico.setItensOS(observableListItemOS);
            buttonConfirmarClicked = true;
            dialogStage.close();
        }
    }
    
    @FXML
    void handleTableViewMouseClicked(MouseEvent event) {
        ItemOS itemOS
                = tableView.getSelectionModel().getSelectedItem();
        if (itemOS == null) {
            contextMenuItemAtualizarObs.setDisable(true);
            contextMenuItemRemoverItem.setDisable(true);
        } else {
            contextMenuItemAtualizarObs.setDisable(false);
            contextMenuItemRemoverItem.setDisable(false);
        }

    } 
    
    @FXML
    private void handleContextMenuItemAtualizarObs() {
        ItemOS itemOS = tableView.getSelectionModel().getSelectedItem();
        int index = tableView.getSelectionModel().getSelectedIndex();
        
        String obsAtualizada = inputDialog(itemOS.getObservacao());
//        int qtdAtualizada = Integer.parseInt(inputDialog(itemDeVenda.getQuantidade()));
//        String obsAtualizada = inputDialog(itemOS.getObservacao());
        if (obsAtualizada != "Cancelado") {
            itemOS.setObservacao(obsAtualizada);
        }
        ordemServico.getItensOS().set(index, itemOS);
        tableView.refresh();
        tfValor.setText(String.format("%.2f", ordemServico.getTotal()));        
    }
    
    private String inputDialog(String value) {
        TextInputDialog dialog = new TextInputDialog(value);
        dialog.setTitle("Entrada de dados.");
        dialog.setHeaderText("Atualização do campo de observação");
        dialog.setContentText("Observação: ");

        // Traditional way to get the response value.
        Optional<String> result = dialog.showAndWait();
        
        if(result.isPresent()) {
            return result.get();
        } else {
            return "Cancelado";
        }
    }
    
    @FXML
    private void handleContextMenuItemRemoverItem() {
        ItemOS itemOS
                = tableView.getSelectionModel().getSelectedItem();
        int index = tableView.getSelectionModel().getSelectedIndex();
        ordemServico.getItensOS().remove(index);
        observableListItemOS = FXCollections.observableArrayList(ordemServico.getItensOS());
        tableView.setItems(observableListItemOS);

        tfValor.setText(String.format("%.2f", ordemServico.getTotal()));
    }
    
    //validar entrada de dados do cadastro
    private boolean validarEntradaDeDados() {
        String errorMessage = "";

        if (cbVeiculo.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "Veículo inválido!\n";
        }

        if (dpAgenda.getValue() == null) {
            errorMessage += "Data inválida!\n";
        }

        if (observableListItemOS == null) {
            errorMessage += "Itens de ordem de serviço inválidos!\n";
        }
        
        DecimalFormat df = new DecimalFormat("0.00");
        try {
            tfDesconto.setText(df.parse(tfDesconto.getText()).toString());
        } catch (ParseException ex) {
            errorMessage += "A taxa de desconto está incorreta! Use \",\" como ponto decimal.\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro no cadastro");
            alert.setHeaderText("Campos inválidos, por favor corrija...");
            alert.setContentText(errorMessage);
            alert.show();
            return false;
        }
    }
}
