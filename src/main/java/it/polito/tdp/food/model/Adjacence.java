package it.polito.tdp.food.model;

public class Adjacence
{
	private final Food food1;
	private final Food food2;
	private final double weight;
	
	
	public Adjacence(Food food1, Food food2, double weight)
	{
		this.food1 = food1;
		this.food2 = food2;
		this.weight = weight;
	}

	public Food getFood1()
	{
		return this.food1;
	}

	public Food getFood2()
	{
		return this.food2;
	}

	public double getWeight()
	{
		return this.weight;
	}
	
	@Override
	public String toString()
	{
		return String.format("%s  -  %s  (%.3f)", this.food1.toString(), this.food2.toString(), this.weight);
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((food1 == null) ? 0 : food1.hashCode());
		result = prime * result + ((food2 == null) ? 0 : food2.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Adjacence other = (Adjacence) obj;
		if (food1 == null)
		{
			if (other.food1 != null)
				return false;
		}
		else
			if (!food1.equals(other.food1))
				return false;
		if (food2 == null)
		{
			if (other.food2 != null)
				return false;
		}
		else
			if (!food2.equals(other.food2))
				return false;
		return true;
	}
	
	
}
