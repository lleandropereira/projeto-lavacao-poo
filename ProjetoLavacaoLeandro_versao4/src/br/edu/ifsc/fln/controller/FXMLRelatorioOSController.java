/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.exception.DAOException;
import br.edu.ifsc.fln.model.dao.OrdemServicoDAO;
import br.edu.ifsc.fln.model.dao.VeiculoDAO;
import br.edu.ifsc.fln.model.database.Database;
import br.edu.ifsc.fln.model.database.DatabaseFactory;
import br.edu.ifsc.fln.model.domain.OrdemServico;
import br.edu.ifsc.fln.model.domain.Veiculo;
import br.edu.ifsc.fln.utils.AlertDialog;
import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 * FXML Controller class
 *
 * @author leandropereira
 */
public class FXMLRelatorioOSController implements Initializable {

    @FXML
    private Button btImprimir;

    @FXML
    private TableColumn<OrdemServico, Date> tableColumnAgenda;

    @FXML
    private TableColumn<OrdemServico, Double> tableColumnDesconto;

    @FXML
    private TableColumn<OrdemServico, Integer> tableColumnNumero;

    @FXML
    private TableColumn<OrdemServico, String> tableColumnStatus;

    @FXML
    private TableColumn<OrdemServico, Double> tableColumnTotal;

    @FXML
    private TableColumn<OrdemServico, String> tableColumnVeiculo;

    @FXML
    private TableView<OrdemServico> tableView;

    private List<OrdemServico> listaOrdemServico;
    private ObservableList<OrdemServico> observableListOrdemServico;
    
    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
    private final OrdemServicoDAO ordemServicoDAO = new OrdemServicoDAO();
    private final VeiculoDAO veiculoDAO = new VeiculoDAO();
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        ordemServicoDAO.setConnection(connection);
        veiculoDAO.setConnection(connection);
        carregarTableView();
    }    
    
    private void carregarTableView() {
        try {
            listaOrdemServico = ordemServicoDAO.listar();
        } catch (Exception ex) {
            Logger.getLogger(FXMLRelatorioOSController.class.getName()).log(Level.SEVERE, null, ex);
            listaOrdemServico = new ArrayList<>(); // Inicializa a lista vazia para evitar problemas futuros
        }

        observableListOrdemServico = FXCollections.observableArrayList(listaOrdemServico);

        tableColumnNumero.setCellValueFactory(new PropertyValueFactory<>("numero"));
        tableColumnAgenda.setCellValueFactory(new PropertyValueFactory<>("agenda"));
        tableColumnDesconto.setCellValueFactory(new PropertyValueFactory<>("taxaDesconto"));
        tableColumnTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        tableColumnStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        

  
        tableColumnVeiculo.setCellValueFactory(cellData -> {
            Veiculo veiculo = cellData.getValue().getVeiculo();
            String placa = "";
    
            if (veiculo != null) {
                int idVeiculo = veiculo.getId();
                try {
                    Veiculo veiculoCompleto = veiculoDAO.buscarPorId(idVeiculo);
                    placa = (veiculoCompleto != null) ? veiculoCompleto.getPlaca() : "";
                } catch(DAOException ex) {
                    AlertDialog.exceptionMessage(ex);
                }
                
            }
    
            return new SimpleStringProperty(placa);
        });

        tableView.setItems(observableListOrdemServico);
    }
    
    @FXML
    void handleBbtImprimir(ActionEvent event) throws JRException {
        URL url = getClass().getResource("../report/Relatorio_Lavacao.jasper");
        JasperReport jasperReport = (JasperReport)JRLoader.loadObject(url);
        
        //null: caso não existam filtros
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, connection);
        
        //false: não deixa fechar a aplicação principal
        JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);
        jasperViewer.setVisible(true);  
    }
}
