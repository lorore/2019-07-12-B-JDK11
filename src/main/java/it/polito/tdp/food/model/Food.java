package it.polito.tdp.food.model;

public class Food 
{
	private Integer food_code;
	private String display_name;
	
	
	public Food(Integer food_code, String display_name) 
	{
		this.food_code = food_code;
		this.display_name = display_name;
	}
	
	public Integer getFood_code() 
	{
		return this.food_code;
	}

	public String getDisplay_name() 
	{
		return this.display_name;
	}

	@Override
	public int hashCode() 
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.food_code == null) ? 0 : this.food_code.hashCode());
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
		Food other = (Food) obj;
		if (this.food_code == null) 
		{
			if (other.food_code != null)
				return false;
		} 
		else if (!this.food_code.equals(other.food_code))
			return false;
		return true;
	}

	@Override
	public String toString() 
	{
		return this.display_name;
	}

	
	
}
