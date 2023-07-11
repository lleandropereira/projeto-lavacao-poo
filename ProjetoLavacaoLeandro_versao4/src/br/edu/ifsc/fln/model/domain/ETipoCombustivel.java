/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package br.edu.ifsc.fln.model.domain;

/**
 *
 * @author leandropereira
 */
public enum ETipoCombustivel {
    GASOLINA(1, "Gasolina"), ETANOL(2, "Etanol"), FLEX(3, "Flex"), DIESEL(4, "Diesel"), GNV(5, "GNV"), OUTRO(6, "Outro");
    
    private int id;
    private String descricao;

    private ETipoCombustivel(int id, String descricao) {
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
