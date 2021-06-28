package it.polito.tdp.food.model;

public class Event implements Comparable<Event>{
	
	public enum EventType{
		STAZIONE_LIBERA,
		FINE_PREPARAZIONE
		
	}
	
	private double t;
	private EventType type;
	private Food f;
	public Event(double t, EventType type, Food f) {
		super();
		this.t = t;
		this.type = type;
		this.f = f;
	}
	public double getT() {
		return t;
	}
	public void setT(double t) {
		this.t = t;
	}
	public EventType getType() {
		return type;
	}
	public void setType(EventType type) {
		this.type = type;
	}
	public Food getF() {
		return f;
	}
	public void setF(Food f) {
		this.f = f;
	}
	@Override
	public int compareTo(Event o) {
		return Double.compare(t, o.t);
	}
	
	

}
