package com.okiimport.app.mvvm.model;

public class ModeloCombo<T> {

	private String nombre;
	
	private T valor;
	
	public ModeloCombo() {
		// TODO Auto-generated constructor stub
	}

	public String getNombre() {
		return nombre;
	}

	public ModeloCombo(String nombre, T valor) {
		super();
		this.nombre = nombre;
		this.valor = valor;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public T getValor() {
		return valor;
	}

	public void setValor(T valor) {
		this.valor = valor;
	}

}
