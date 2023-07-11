/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.ifsc.fln.model.domain;

import java.time.LocalDate;
import java.util.Date;

/**
 *
 * @author leandropereira
 */
public class PessoaFisica extends Cliente{
    private String cpf;
    private LocalDate dataNascimento;

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }
    
    @Override
    public String getDados() {
        StringBuilder sb = new StringBuilder();
//        sb.append(super.getDados()).append("\n");
        sb.append("*** PESSOA FÍSICA ***").append("\n");
        sb.append("Nome......: ").append(PessoaFisica.super.getNome()).append("\n");
        sb.append("Celular......: ").append(PessoaFisica.super.getCelular()).append("\n");
        sb.append("Email.....: ").append(PessoaFisica.super.getEmail()).append("\n");
        sb.append("Data Cadastro.....: ").append(PessoaFisica.super.getDataCadastro()).append("\n");
        sb.append("CPF......: ").append(cpf).append("\n");
        sb.append("Data Nascimento......: ").append(dataNascimento).append("\n");
        return sb.toString();
    }

    @Override
    public String getDados(String observacao) {
        StringBuilder sb = new StringBuilder();
        sb.append("*** PESSOA FÍSICA ***").append("\n");
        sb.append("Nome......: ").append(PessoaFisica.super.getNome()).append("\n");
        sb.append("Celular......: ").append(PessoaFisica.super.getCelular()).append("\n");
        sb.append("Email.....: ").append(PessoaFisica.super.getEmail()).append("\n");
        sb.append("Data Cadastro.....: ").append(PessoaFisica.super.getDataCadastro()).append("\n");
        sb.append("CPF......: ").append(cpf).append("\n");
        sb.append("Data Nascimento......: ").append(dataNascimento).append("\n");
        sb.append("Observações..........: ").append(observacao).append("\n");
        return sb.toString();
    }
}
