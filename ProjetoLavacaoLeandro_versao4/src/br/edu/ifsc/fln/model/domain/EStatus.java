/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package br.edu.ifsc.fln.model.domain;

/**
 *
 * @author leandropereira
 */
public enum EStatus {
    ABERTA(1, "Aberta"), FECHADA(2, "Fechada"), CANCELADA(3, "Cancelada");
    
    private int id;
    private String descricao;
    
    private EStatus(int id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }
    
    public int getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }
}
