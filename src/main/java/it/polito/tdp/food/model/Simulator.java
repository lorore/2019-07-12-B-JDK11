package it.polito.tdp.food.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.food.model.Event.EventType;

public class Simulator {

	private Model model;
	private int K;
	private int stazioni;
	private Food partenza;
	private PriorityQueue<Event> queue;
	private Graph<Food, DefaultWeightedEdge> graph;
	private Map<Integer, Food> preparati;
	private int stazioniLibere;
	private double tempoTotale;
	
	public Simulator(int k, Model model, Food f) {
		super();
		this.model = model;
		K = k;
		this.partenza=f;
	}
	
	public void init() {
		List<Successivo> successivi=this.model.getPeggiori(partenza);
		if(successivi.size()<K) {
			//non uso tutte le stazioni
			K=successivi.size();
		}
		graph=this.model.getGraph();
		preparati=new HashMap<>();
		queue=new PriorityQueue<>();
		this.stazioniLibere=0;
		this.tempoTotale=0;
		for(int i=0; i<K; i++) {
			Food f=successivi.get(i).getF();
			double t=successivi.get(i).getPeso();
			preparati.put(f.getFood_code(), f);
			Event e=new Event(t, EventType.FINE_PREPARAZIONE, f);
			queue.add(e);
		}
		
	}
	
	public String sim() {
		while(this.queue.peek()!=null && this.stazioniLibere!=this.K) {
			Event e=this.queue.poll();
			this.processEvent(e);
			
		}
		System.out.println("Num cibi preparati: "+preparati.size()+" tempo totale: "+(this.tempoTotale*60.0)+"minuti");
		String result="Num cibi preparati: "+preparati.size()+" tempo totale: "+(this.tempoTotale*60.0)+"minuti";
		return result;
	}
	
	private void processEvent(Event e) {
		switch(e.getType()) {
		case FINE_PREPARAZIONE:
			//devo scegliere un nuovo cibo
			this.tempoTotale=e.getT();
			List<Successivo> successivi=this.model.getPeggiori(e.getF());
			boolean trovato=false;
			int i=0;
			while(trovato==false && i<successivi.size()) {
				Food f=successivi.get(i).getF();
				if(!this.preparati.containsKey(f.getFood_code())) {
					trovato=true;
					preparati.put(f.getFood_code(), f);
					queue.add(new Event(e.getT()+successivi.get(i).getPeso(), EventType.FINE_PREPARAZIONE, f));
				}
				i++;
			}
			if(trovato==false) {
				this.stazioniLibere++;
				//non mi serve l'evento stazioni libere
			}
			break;
		}
	}
}
