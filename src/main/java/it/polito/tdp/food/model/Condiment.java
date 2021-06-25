package it.polito.tdp.food.model;

public class Condiment 
{
	private Integer condiment_code;
	private String display_name;
	private Double condiment_calories;
	private Double condiment_saturated_fats;
	
	
	public Condiment(Integer condiment_code, String display_name, Double condiment_calories,
			Double condiment_saturated_fats) 
	{
		this.condiment_code = condiment_code;
		this.display_name = display_name;
		this.condiment_calories = condiment_calories;
		this.condiment_saturated_fats = condiment_saturated_fats;
	}
	
	public Integer getCondiment_code() 
	{
		return this.condiment_code;
	}

	public String getDisplay_name() 
	{
		return this.display_name;
	}

	public Double getCondiment_calories()
	{
		return this.condiment_calories;
	}
	
	public Double getCondiment_saturated_fats() 
	{
		return this.condiment_saturated_fats;
	}

	@Override
	public int hashCode() 
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.condiment_code == null) ? 0 
										: this.condiment_code.hashCode());
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
		Condiment other = (Condiment) obj;
		if (this.condiment_code == null) 
		{
			if (other.condiment_code != null)
				return false;
		} 
		else if (!this.condiment_code.equals(other.condiment_code))
			return false;
		return true;
	}

	@Override
	public String toString() 
	{
		return "{condiment_code=" + this.condiment_code + ", display_name=" + this.display_name + "}";
	}	
}
