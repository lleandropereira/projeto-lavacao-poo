/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.ifsc.fln.model.domain;


/**
 *
 * @author leandropereira
 */
public class Modelo {
    
    private int id;
    private String descricao;
    private Marca marca;
    private Motor motor;
    private ECategoria categoria = ECategoria.PADRAO;
    
    public Modelo() {
        this.createMotor();
    }
    
    public Modelo(String descricao, Marca marca) {
        this.descricao = descricao;
        this.marca = marca;
    }
    
    private void createMotor() {
        this.motor = new Motor();
        this.motor.setModelo(this);    
    }    
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Marca getMarca() {
        return marca;
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
    }

    public Motor getMotor() {
        return motor;
    }
    
    public void setMotor(Motor motor) {
        this.motor = motor;
        this.motor.setModelo(this);
    }

    public ECategoria getCategoria() {
        return categoria;
    }

    public void setCategoria(ECategoria categoria) {
        this.categoria = categoria;
    }
    
    public String getDados() {
        StringBuilder sb = new StringBuilder();
        sb.append(descricao).append("\n");
        sb.append("Potencia").append(getMotor().getPotencia());
        return sb.toString();
    }
    
    @Override
    public String toString() {
        return this.descricao;
    }

}
