/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.exception.DAOException;
import br.edu.ifsc.fln.model.dao.ClienteDAO;
import br.edu.ifsc.fln.model.dao.PontuacaoDAO;
import br.edu.ifsc.fln.model.database.Database;
import br.edu.ifsc.fln.model.database.DatabaseFactory;
import br.edu.ifsc.fln.model.domain.Cliente;
import br.edu.ifsc.fln.model.domain.PessoaFisica;
import br.edu.ifsc.fln.model.domain.PessoaJuridica;
import br.edu.ifsc.fln.model.domain.Pontuacao;
import br.edu.ifsc.fln.utils.AlertDialog;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
import javafx.scene.control.ChoiceDialog;
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
public class FXMLAnchorPaneCadastroClienteController implements Initializable {
    
     @FXML
    private Button btAlterar;

    @FXML
    private Button btExcluir;

    @FXML
    private Button btInserir;

    @FXML
    private Label lbClienteCelular;

    @FXML
    private Label lbClienteDataCadastro;

    @FXML
    private Label lbClienteEmail;

    @FXML
    private Label lbClienteId;

    @FXML
    private Label lbClienteNome;

     @FXML
    private Label lbInscricaoEstadual;

    @FXML
    private Label lbRegistro;

    @FXML
    private Label lbTipoCliente;
    
    @FXML
    private Label lbDataNascimento;
    
    @FXML
    private TableColumn<Cliente, String> tableColumnCliente;

    @FXML
    private TableView<Cliente> tableViewCliente;

    private List<Cliente> listaCliente;
    private ObservableList<Cliente> observableListCliente;
    
    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final PontuacaoDAO pontuacaoDAO = new PontuacaoDAO();
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        clienteDAO.setConnection(connection);
        pontuacaoDAO.setConnection(connection);
        carregarTableViewCliente();
        
        tableViewCliente.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarItemTableViewCliente(newValue));
    }
    
    public void carregarTableViewCliente() {
        tableColumnCliente.setCellValueFactory(new PropertyValueFactory<>("nome"));
        try {
            listaCliente = clienteDAO.listar();
        } catch (DAOException ex) {
            AlertDialog.exceptionMessage(ex);
        }
        
        
        observableListCliente = FXCollections.observableArrayList(listaCliente);
        tableViewCliente.setItems(observableListCliente);
    }
    
    public void selecionarItemTableViewCliente(Cliente cliente) {
        if (cliente != null) {
            lbClienteId.setText(String.valueOf(cliente.getId())); 
            lbClienteNome.setText(cliente.getNome());
            lbClienteCelular.setText(cliente.getCelular());
            lbClienteEmail.setText(cliente.getEmail());
            lbClienteDataCadastro.setText(String.valueOf(cliente.getDataCadastro().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
            if (cliente instanceof PessoaFisica) {
                lbTipoCliente.setText("Pessoa Física");
                lbRegistro.setText(((PessoaFisica)cliente).getCpf());
                lbDataNascimento.setText(String.valueOf(
                    ((PessoaFisica) cliente).getDataNascimento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
                lbInscricaoEstadual.setText("");
            } else {
                lbTipoCliente.setText("Pessoa Jurídica");
                lbRegistro.setText(((PessoaJuridica)cliente).getCnpj());
                lbDataNascimento.setText("");
                lbInscricaoEstadual.setText(((PessoaJuridica)cliente).getInscricaoEstadual());
            }
        } else {
            lbClienteId.setText(""); 
            lbClienteNome.setText("");
            lbClienteCelular.setText("");
            lbClienteEmail.setText("");
            lbClienteDataCadastro.setText("");
            lbTipoCliente.setText("");
            lbRegistro.setText("");
            lbDataNascimento.setText("");
            lbInscricaoEstadual.setText("");
        }
        
    }
    
    @FXML
    void handleBtAlterar(ActionEvent event) throws IOException {
        Cliente cliente = tableViewCliente.getSelectionModel().getSelectedItem();
        if (cliente != null) {
            boolean btConfirmarClicked = showFXMLAnchorPaneCadastroClienteDialog(cliente);
            if (btConfirmarClicked) {
                try {
                    clienteDAO.alterar(cliente);
                } catch (DAOException ex){
                    AlertDialog.exceptionMessage(ex);
                }
                
                carregarTableViewCliente();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Esta operação requer a seleção \nde um cliente na tabela ao lado");
            alert.show();
        }
    }

    @FXML
    void handleBtExcluir(ActionEvent event) {
        Cliente cliente = tableViewCliente.getSelectionModel().getSelectedItem();
        if (cliente != null) {
            if (AlertDialog.confirmarExclusao("Tem certeza que deseja excluir o cliente " + cliente.getNome())) {
                try {
                    clienteDAO.remover(cliente);
                } catch(DAOException ex) {
                    AlertDialog.exceptionMessage(ex);
                }
                
                carregarTableViewCliente();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Esta operação requer a seleção \nde um cliente na tabela ao lado");
            alert.show();
        }
    }

    @FXML
    void handleBtInserir(ActionEvent event) throws IOException {
        Cliente cliente = getTipoCliente();
        Pontuacao pontuacao = new Pontuacao();
        pontuacao.setQuantidade(0);
        
        cliente.setPontuacao(pontuacao);
        pontuacao.setCliente(cliente);
        
//        if (cliente != null ) {
            boolean btConfirmarClicked = showFXMLAnchorPaneCadastroClienteDialog(cliente);
            if (btConfirmarClicked) {
                try {
                    clienteDAO.inserir(cliente);
                } catch(DAOException ex) {
                    AlertDialog.exceptionMessage(ex);
                }
                try {
                    cliente.getPontuacao().getCliente().setId(clienteDAO.getClienteAutoID(cliente));
                } catch (DAOException ex) {
                    AlertDialog.exceptionMessage(ex);
                }
                try {
                    pontuacaoDAO.inserir(cliente.getPontuacao());
                } catch (DAOException ex) {
                    AlertDialog.exceptionMessage(ex);
                }
                
                carregarTableViewCliente();
            }
//        }
    }
    
    private Cliente getTipoCliente() {
        List<String> opcoes = new ArrayList<>();
        opcoes.add("Pessoa Física");
        opcoes.add("Pessoa Jurídica");
        ChoiceDialog<String> dialog = new ChoiceDialog<>("Pessoa Física", opcoes);
        dialog.setTitle("Dialogo de Opções");
        dialog.setHeaderText("Escolha o tipo de cliente");
        dialog.setContentText("Tipo de cliente: ");
        Optional<String> escolha = dialog.showAndWait();
        if (escolha.isPresent()) {
            if (escolha.get().equalsIgnoreCase("Pessoa Física")) 
                return new PessoaFisica();
            else 
                return new PessoaJuridica();
        } else {
            return null;
        }
    }
    
    private boolean showFXMLAnchorPaneCadastroClienteDialog(Cliente cliente) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(FXMLAnchorPaneCadastroClienteController.class.getResource("../view/FXMLAnchorPaneCadastroClienteDialog.fxml"));
        AnchorPane page = (AnchorPane) loader.load();
        
        //criação de um estágio de diálogo (StageDialog)
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Cadastro de cliente");
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);
        
        //enviando o obejto fornecedor para o controller
        FXMLAnchorPaneCadastroClienteDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setCliente(cliente);
        
        //apresenta o diálogo e aguarda a confirmação do usuário
        dialogStage.showAndWait();
        
        return controller.isBtConfirmarClicked();
    }
    
}
