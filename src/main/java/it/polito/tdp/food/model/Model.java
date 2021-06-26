package it.polito.tdp.food.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.builder.GraphTypeBuilder;

import it.polito.tdp.food.db.FoodDao;
import it.polito.tdp.simulation.SimulationResult;
import it.polito.tdp.simulation.Simulator;

public class Model 
{
	private Graph<Food, DefaultWeightedEdge> graph;
	private final FoodDao dao;
	private final Map<Integer, Food> foodIdMap;
	private final Simulator simulator;
	
	
	public Model()
	{
		this.dao = new FoodDao();
		this.foodIdMap = new HashMap<>();
		this.simulator = new Simulator();
	}

	public void createGraph(int numMinPortions)
	{
		this.graph = GraphTypeBuilder.<Food, DefaultWeightedEdge>directed()
									.allowingMultipleEdges(false)
									.allowingSelfLoops(false)
									.weighted(true)
									.edgeClass(DefaultWeightedEdge.class)
									.buildGraph();
		
		Collection<Food> vertices = this.dao.getFoods(numMinPortions, this.foodIdMap);
		Graphs.addAllVertices(this.graph, vertices);
		
		Collection<Adjacence> adjacences = this.dao.getFoodAdjacences(numMinPortions, this.foodIdMap);
		
		for(Adjacence adjacence : adjacences)
		{
			if(adjacence.getWeight() > 0)
				Graphs.addEdge(this.graph, adjacence.getFood1(), adjacence.getFood2(), adjacence.getWeight());
			else if(adjacence.getWeight() < 0)
				Graphs.addEdge(this.graph, adjacence.getFood2(), adjacence.getFood1(), -adjacence.getWeight());
		}
	}

	public int getNumVertices() { return this.graph.vertexSet().size(); }
	public int getNumEdges() { return this.graph.edgeSet().size(); }

	public List<Food> getOrderedFoods()
	{
		if(this.graph == null || this.graph.vertexSet().size() == 0)
			return null;
		
		List<Food> orderedFoods = new ArrayList<>(this.graph.vertexSet());
		orderedFoods.sort((f1,f2) -> f1.getDisplay_name().compareTo(f2.getDisplay_name()));
		return orderedFoods;
	}

	public static List<Adjacence> getMinimumAdjacences(Graph<Food,DefaultWeightedEdge> graph, 
			Food selectedFood, int maxNum)
	{
		if(graph == null || graph.vertexSet().size() == 0)
			return null;
		
		List<Adjacence> minimumAdjacences = new ArrayList<>();
		
		for(var edge : graph.outgoingEdgesOf(selectedFood))
		{
			double weight = graph.getEdgeWeight(edge);
			Food adjacentFood = graph.getEdgeTarget(edge);
			Adjacence a = new Adjacence(selectedFood, adjacentFood, weight);
			minimumAdjacences.add(a);
		}
		
		minimumAdjacences.sort((a1,a2) -> Double.compare(a1.getWeight(), a2.getWeight()));
		
		if(minimumAdjacences.size() > maxNum)
		{
			int toRemove = minimumAdjacences.size() - maxNum;
			for(int i=0; i<toRemove; i++)
				minimumAdjacences.remove(minimumAdjacences.size() - 1); //remove last element n times
		}
		
		return minimumAdjacences;
	}
	
	public List<Adjacence> getMinimumAdjacentFoodsOf(Food selectedFood)
	{
		return getMinimumAdjacences(this.graph, selectedFood, 5);
	}
	
	public SimulationResult runSimulation(int maxStations, Food startFood)
	{
		this.simulator.initialize(this.graph, maxStations, startFood);
		
		SimulationResult result = this.simulator.run();
		return result;
	}

}
