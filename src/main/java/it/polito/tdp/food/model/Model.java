package it.polito.tdp.food.model;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.food.db.FoodDao;

public class Model 
{
	private Graph<Food, DefaultWeightedEdge> graph;
	@SuppressWarnings("unused")
	private final FoodDao dao;
	
	
	public Model()
	{
		this.dao = new FoodDao();
	}

	public void createGraph(int numPortions)
	{
		// TODO Auto-generated method stub
		
	}

	public int getNumVertices() { return this.graph.vertexSet().size(); }
	public int getNumEdges() { return this.graph.edgeSet().size(); }

}
