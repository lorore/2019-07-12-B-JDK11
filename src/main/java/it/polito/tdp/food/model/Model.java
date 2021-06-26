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

public class Model 
{
	private Graph<Food, DefaultWeightedEdge> graph;
	private final FoodDao dao;
	private Map<Integer, Food> foodIdMap;
	
	
	public Model()
	{
		this.dao = new FoodDao();
		this.foodIdMap = new HashMap<>();
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

	public List<Adjacence> getMinimumAdjacentFoodsOf(Food selectedFood)
	{
		if(this.graph == null || this.graph.vertexSet().size() == 0)
			return null;
		
		List<Adjacence> minimumAdjacences = new ArrayList<>();
		
		for(var edge : this.graph.outgoingEdgesOf(selectedFood))
		{
			double weight = this.graph.getEdgeWeight(edge);
			Food adjacentFood = this.graph.getEdgeTarget(edge);
			Adjacence a = new Adjacence(selectedFood, adjacentFood, weight);
			minimumAdjacences.add(a);
		}
		
		minimumAdjacences.sort((a1,a2) -> Double.compare(a1.getWeight(), a2.getWeight()));
		
		if(minimumAdjacences.size() > 5)
		{
			int toRemove = minimumAdjacences.size() - 5;
			for(int i=0; i<toRemove; i++)
				minimumAdjacences.remove(minimumAdjacences.size() - 1); //remove last element n times
		}
		
		return minimumAdjacences;
	}

}
