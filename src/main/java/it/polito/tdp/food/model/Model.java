package it.polito.tdp.food.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.food.db.FoodDao;

public class Model 
{
	
	private Graph<Food, DefaultWeightedEdge> graph;
	private Map<Integer, Food> idMap;
	private FoodDao dao;
	
	public Model() {
		dao=new FoodDao();
	}
	
	
	public String creaGrafo(int k) {
		String result=null;
		graph=new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		idMap=new HashMap<>();
		dao.getVertici(idMap, k);
		Graphs.addAllVertices(graph, idMap.values());
		result="Num vertici: "+this.graph.vertexSet().size();
		List<Adiacenza> archi=dao.getArchi(idMap, k);
		for(Adiacenza a: archi) {
			Food f1=a.getF1();
			Food f2=a.getF2();
			double peso=a.getPeso();
			Graphs.addEdge(graph, f1, f2, peso);
		}
		result+=" num archi: "+this.graph.edgeSet().size();
		System.out.println(result);
		return result;
		
	}
	
	public List<Successivo> getPeggiori(Food f){
		List<Successivo> result=new ArrayList<>();
		for(DefaultWeightedEdge e: graph.outgoingEdgesOf(f)) {
			result.add(new Successivo(graph.getEdgeTarget(e), graph.getEdgeWeight(e) ));
		}
		
		Collections.sort(result);
		
		return result;
	}
	
	public List<Food> getVertici(){
		List<Food> vertici=new ArrayList<>(this.graph.vertexSet());
		Collections.sort(vertici);
		return vertici;
	}
	
	public Graph<Food, DefaultWeightedEdge> getGraph(){
		return this.graph;
	}
	
	public String avviaSimulazione(int k, Food f) {
		Simulator sim=new Simulator(k, this, f);
		sim.init();
		return sim.sim();
	}

}
