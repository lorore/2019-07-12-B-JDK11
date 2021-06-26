package it.polito.tdp.simulation;

import it.polito.tdp.food.model.Food;

public class CookingEvent implements Comparable<CookingEvent>
{
	private double finishTime;	//hours
	private double hourDuration;
	private int stationNum;
	private Food preparedFood;
	
	
	public CookingEvent(double finishTime, double hourDuration, int stationNum, Food preparedFood)
	{
		this.finishTime = finishTime;
		this.hourDuration = hourDuration;
		this.stationNum = stationNum;
		this.preparedFood = preparedFood;
	}
	
	public double getFinishTime()
	{
		return this.finishTime;
	}
	
	public double getHourDuration()
	{
		return this.hourDuration;
	}
	
	public int getStationNum()
	{
		return this.stationNum;
	}
	
	public Food getPreparedFood()
	{
		return this.preparedFood;
	}

	@Override
	public int compareTo(CookingEvent other)	//cronologically ordered
	{
		return Double.compare(this.finishTime, other.finishTime);
	}
}
