package it.polito.tdp.simulation;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.food.model.Adjacence;
import it.polito.tdp.food.model.Food;
import it.polito.tdp.food.model.Model;

public class Simulator implements SimulationResult
{
	private Graph<Food, DefaultWeightedEdge> graph;
	private int activeStations;
	
	private PriorityQueue<CookingEvent> eventsQueue;
	
	private Set<Food> preparedFoods;
	private Map<Integer, Food> foodInProgressByStationId;
	private double time;
	
	
	public void initialize(Graph<Food, DefaultWeightedEdge> foodGraph, int maxStations, Food startFood)
	{
		this.graph = foodGraph;
		this.preparedFoods = new HashSet<>();
		this.foodInProgressByStationId = new HashMap<>();
		this.eventsQueue = new PriorityQueue<>();
		this.time = 0.0;
		
		int numAdjacences = this.graph.outgoingEdgesOf(startFood).size();
		this.activeStations = Math.min(maxStations, numAdjacences);
		
		Collection<Adjacence> minimumAdjacences = 
				Model.getMinimumAdjacences(this.graph, startFood, this.activeStations);
		
		int count = 0;
		for(Adjacence a : minimumAdjacences)
		{
			int station = ++count;
			Food foodToPrepare = a.getFood2();
			double hourDuration = a.getWeight();
			
			this.foodInProgressByStationId.put(station, foodToPrepare);
			CookingEvent newEvent = new CookingEvent(hourDuration, hourDuration, station, foodToPrepare);
			this.eventsQueue.add(newEvent);
		}
	}
	
	public SimulationResult run()
	{
		CookingEvent currentEvent;
		
		while((currentEvent = this.eventsQueue.poll()) != null)
		{
			this.time = currentEvent.getFinishTime();
			
			//updates
			int station = currentEvent.getStationNum();
			this.foodInProgressByStationId.put(station, null);
			
			Food preparedFood = currentEvent.getPreparedFood();
			this.preparedFoods.add(preparedFood);
			
			//compute next food
			List<Adjacence> minimumOrderedAdjacences = Model.getMinimumAdjacences(
										this.graph, preparedFood, Integer.MAX_VALUE);
			
			Food nextFood = null;
			double hourDuration = Double.NaN; 
			for(Adjacence a : minimumOrderedAdjacences)
			{
				Food adjacentFood = a.getFood2();
				
				if(!this.preparedFoods.contains(adjacentFood) //food has not been already prepared
						&& !this.foodInProgressByStationId.containsValue(adjacentFood))	//food is not in progress
				{
					nextFood = adjacentFood;
					hourDuration = a.getWeight();
					break;
				}
			}
			
			if(nextFood == null)
				continue;	//there aren't adjacent foods anymore
			
			this.foodInProgressByStationId.put(station, nextFood);
			
			CookingEvent newEvent = new CookingEvent(this.time + hourDuration, hourDuration, station, nextFood);
			this.eventsQueue.add(newEvent);
		}
		
		return this;
	}

	@Override
	public int getNumOfPreparedFoods()
	{
		return this.preparedFoods.size();
	}

	@Override
	public double getHourEndTime()
	{
		return this.time;
	}
}


















