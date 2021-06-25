package it.polito.tdp.food.model;

public class Portion 
{
	private Integer portion_id;
	private Double portion_amount;
	private String portion_display_name;
	private Double calories;
	private Double saturated_fats;
	private Integer food_code;
	
	
	public Portion(Integer portion_id, Double portion_amount, String portion_display_name, Double calories,
			Double saturated_fats, Integer food_code) 
	{
		this.portion_id = portion_id;
		this.portion_amount = portion_amount;
		this.portion_display_name = portion_display_name;
		this.calories = calories;
		this.saturated_fats = saturated_fats;
		this.food_code = food_code;
	}
	
	public Integer getPortion_id() 
	{
		return this.portion_id;
	}

	public Double getPortion_amount() 
	{
		return this.portion_amount;
	}

	public String getPortion_display_name() 
	{
		return this.portion_display_name;
	}

	public Double getCalories() 
	{
		return this.calories;
	}

	public Double getSaturated_fats() 
	{
		return this.saturated_fats;
	}

	public Integer getFood_code() 
	{
		return this.food_code;
	}

	@Override
	public int hashCode() 
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.portion_id == null) ? 0 : this.portion_id.hashCode());
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
		Portion other = (Portion) obj;
		if (this.portion_id == null) 
		{
			if (other.portion_id != null)
				return false;
		} 
		else if (!this.portion_id.equals(other.portion_id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "{portion_id=" + this.portion_id + ", portion_amount=" 
				+ this.portion_amount + ", portion_display_name="
				+ this.portion_display_name + ", food_code=" + this.food_code + "}";
	}
}
