/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.ifsc.fln.model.domain;

/**
 *
 * @author leandropereira
 */
public class PessoaJuridica extends Cliente{
    private String cnpj;
    private String inscricaoEstadual;

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getInscricaoEstadual() {
        return inscricaoEstadual;
    }

    public void setInscricaoEstadual(String incricaoEstadual) {
        this.inscricaoEstadual = incricaoEstadual;
    }
    
    @Override
    public String getDados() {
        StringBuilder sb = new StringBuilder();
//        sb.append(super.getDados()).append("\n");
        sb.append("Dados do fornecedor ").append(this.getClass().getSimpleName()).append("\n");
        sb.append("Nome......: ").append(PessoaJuridica.super.getNome()).append("\n");
        sb.append("Celular......: ").append(PessoaJuridica.super.getCelular()).append("\n");
        sb.append("Email.....: ").append(PessoaJuridica.super.getEmail()).append("\n");
        sb.append("Data Cadastro.....: ").append(PessoaJuridica.super.getDataCadastro()).append("\n");
        sb.append("CNPJ.......: ").append(cnpj).append("\n");
        sb.append("Inscrição Estadual......: ").append(inscricaoEstadual).append("\n");
        return sb.toString();
    }   
    
    @Override
    public String getDados(String observacao) {
        StringBuilder sb = new StringBuilder();
//        sb.append(super.getDados()).append("\n");
        sb.append("Dados do fornecedor ").append(this.getClass().getSimpleName()).append("\n");
        sb.append("Nome......: ").append(PessoaJuridica.super.getNome()).append("\n");
        sb.append("Celular......: ").append(PessoaJuridica.super.getCelular()).append("\n");
        sb.append("Email.....: ").append(PessoaJuridica.super.getEmail()).append("\n");
        sb.append("Data Cadastro.....: ").append(PessoaJuridica.super.getDataCadastro()).append("\n");
        sb.append("CNPJ.......: ").append(cnpj).append("\n");
        sb.append("Inscrição Estadual......: ").append(inscricaoEstadual).append("\n");
        return sb.toString();
    }
    
}
