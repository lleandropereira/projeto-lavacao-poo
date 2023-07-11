/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package br.edu.ifsc.fln.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author leandropereira
 */
public class FXMLVBoxMainAppController implements Initializable {

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private MenuItem menuItemCadastroCliente;

    @FXML
    private MenuItem menuItemCadastroCor;

    @FXML
    private MenuItem menuItemCadastroMarca;

    @FXML
    private MenuItem menuItemCadastroModelo;

    @FXML
    private MenuItem menuItemCadastroVeiculo;
    
    @FXML
    private MenuItem menuItemCadastroServico;

    @FXML
    private MenuItem menuItemGraficoOrdemDeServicoMes;

    @FXML
    private MenuItem menuItemProcessoOrdemDeServico;

    @FXML
    private MenuItem menuItemRelatorioDeServico;

    @FXML
    void handleMenuItemCadastroCliente(ActionEvent event) throws IOException {
        AnchorPane a = 
            (AnchorPane) FXMLLoader.load(getClass().
                getResource("../view/FXMLAnchorPaneCadastroCliente.fxml"));
        anchorPane.getChildren().setAll(a);
    }

    @FXML
    void handleMenuItemCadastroCor(ActionEvent event) 
            throws IOException {
        AnchorPane a = 
            (AnchorPane) FXMLLoader.load(getClass().
                getResource("../view/FXMLAnchorPaneCadastroCor.fxml"));
        anchorPane.getChildren().setAll(a);

    }

    @FXML
    public void handleMenuItemCadastroMarca(ActionEvent event) 
            throws IOException {
        AnchorPane a = 
            (AnchorPane) FXMLLoader.load(getClass().
                getResource("../view/FXMLAnchorPaneCadastroMarca.fxml"));
        anchorPane.getChildren().setAll(a);

    }
    

    @FXML
    void handleMenuItemCadastroModelo(ActionEvent event) throws IOException {
        AnchorPane a = (AnchorPane) FXMLLoader.load(getClass().getResource("../view/FXMLAnchorPaneCadastroModelo.fxml"));
        anchorPane.getChildren().setAll(a);

    }

    @FXML
    void handleMenuItemCadastroVeiculo(ActionEvent event) throws IOException {
        AnchorPane a = (AnchorPane) FXMLLoader.load(getClass().getResource("../view/FXMLAnchorPaneCadastroVeiculo.fxml"));
        anchorPane.getChildren().setAll(a);

    }
    
    @FXML
    void handleMenuItemCadastroServico(ActionEvent event) throws IOException {
        AnchorPane a = (AnchorPane) FXMLLoader.load(getClass().getResource("../view/FXMLAnchorPaneCadastroServico.fxml"));
        anchorPane.getChildren().setAll(a);
    }

    @FXML
    void handleMenuItemGraficoOrdemDeServicoMes(ActionEvent event) {

    }

    @FXML
    void handleMenuItemProcessoOrdemDeServico(ActionEvent event) throws IOException {
        AnchorPane a = (AnchorPane) FXMLLoader.load(getClass().getResource("../view/FXMLAnchroPaneProcessoOrdemServico.fxml"));
        anchorPane.getChildren().setAll(a);
    }

    @FXML
    void handleMenuItemRelatorioDeServico(ActionEvent event) throws IOException {
        AnchorPane a = (AnchorPane) FXMLLoader.load(getClass().getResource("../view/FXMLRelatorioOS.fxml"));
        anchorPane.getChildren().setAll(a);
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}
