package it.polito.tdp.food.model;

public class Successivo implements Comparable<Successivo>{
	
	private Food f; 
	private double peso;
	public Successivo(Food f, double peso) {
		super();
		this.f = f;
		this.peso = peso;
	}
	public Food getF() {
		return f;
	}
	public void setF(Food f) {
		this.f = f;
	}
	public double getPeso() {
		return peso;
	}
	public void setPeso(double peso) {
		this.peso = peso;
	}
	@Override
	public int compareTo(Successivo o) {
		
		return Double.compare(peso, o.getPeso());
	}
	@Override
	public String toString() {
		return f.getDisplay_name()+" "+peso;
	}
	
	

}
