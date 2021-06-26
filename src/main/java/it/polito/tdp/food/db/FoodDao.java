package it.polito.tdp.food.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import it.polito.tdp.food.model.Adjacence;
import it.polito.tdp.food.model.Condiment;
import it.polito.tdp.food.model.Food;

public class FoodDao 
{
	public List<Food> listAllFoods()
	{
		String sql = "SELECT * FROM food" ;
		try 
		{
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql) ;
			List<Food> list = new ArrayList<>() ;
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) 
			{
				try 
				{
					list.add(new Food(res.getInt("food_code"),
							res.getString("display_name")));
				} 
				catch (Throwable t) 
				{
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null ;
		}

	}
	
	public List<Condiment> listAllCondiments()
	{
		String sql = "SELECT * FROM condiment" ;
		try 
		{
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			List<Condiment> list = new ArrayList<>();
			ResultSet res = st.executeQuery();
			
			while(res.next()) 
			{
				try 
				{
					list.add(new Condiment(res.getInt("condiment_code"),
							res.getString("display_name"),
							res.getDouble("condiment_calories"), 
							res.getDouble("condiment_saturated_fats")
							));
				} 
				catch (Throwable t) 
				{
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list;
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null ;
		}
	}

	public Collection<Food> getFoods(int numMinPortions, Map<Integer, Food> foodIdMap)
	{
		String sqlQuery = String.format("%s %s %s %s %s",
							"SELECT f.food_code AS code, f.display_name AS name, COUNT(*) AS occurrencies",
							"FROM food f, `portion` p",
							"WHERE f.food_code = p.food_code",
							"GROUP BY f.food_code, f.display_name",
							"HAVING occurrencies >= ?");
		
		Collection<Food> foods = new HashSet<>();
		
		try 
		{	
			Connection connection = DBConnect.getConnection();
			PreparedStatement statement = connection.prepareStatement(sqlQuery);
			statement.setInt(1, numMinPortions);
			ResultSet queryResult = statement.executeQuery();
			
			while(queryResult.next()) 
			{
				try 
				{
					int code = queryResult.getInt("code");
					
					if(!foodIdMap.containsKey(code))
						foodIdMap.put(code, new Food(code, queryResult.getString("name")));
					
					foods.add(foodIdMap.get(code));
				} 
				catch (Throwable t) 
				{
					t.printStackTrace();
				}
			}
			
			queryResult.close();
			statement.close();
			connection.close();
			return foods;
		} 
		catch (SQLException sqle) 
		{
			sqle.printStackTrace();
			throw new RuntimeException("Dao error in getFoods()", sqle);
		}
	}

	public Collection<Adjacence> getFoodAdjacences(int numMinPortions, Map<Integer, Food> foodIdMap)
	{
		final String sqlQuery = String.format("%s %s %s %s %s %s %s %s %s %s %s %s %s %s %s %s",
									"SELECT F1.food_code AS code1, F2.food_code AS code2, (F1.fats - F2.fats) AS diff",
									"FROM (SELECT food_code, AVG(saturated_fats) AS fats",
										  "FROM `portion`",
										  "GROUP BY food_code) AS F1,",
										 "(SELECT food_code, AVG(saturated_fats) AS fats",
										  "FROM `portion`",
										  "GROUP BY food_code) AS F2",
									"WHERE F1.food_code IN (SELECT food_code",
										  					"FROM `portion`",
										  					"GROUP BY food_code",
										  					"HAVING COUNT(*) >= ?)",
										  "AND F2.food_code IN (SELECT food_code",
										  					"FROM `portion`",
										  					"GROUP BY food_code",
										  					"HAVING COUNT(*) >= ?)",
										  "AND F1.food_code < F2.food_code",
										  "AND diff <> 0");
		
		Collection<Adjacence> adjacences = new HashSet<>();
		
		try
		{
			Connection connection = DBConnect.getConnection();
			PreparedStatement statement = connection.prepareStatement(sqlQuery);
			statement.setInt(1, numMinPortions);
			statement.setInt(2, numMinPortions);
			ResultSet queryResult = statement.executeQuery();
			
			while(queryResult.next()) 
			{
				try 
				{
					int code1 = queryResult.getInt("code1");
					int code2 = queryResult.getInt("code2");
					
					if(!foodIdMap.containsKey(code1) || !foodIdMap.containsKey(code2))
						throw new RuntimeException("Food code not found in FoodIdMap");

					Food food1 = foodIdMap.get(code1);
					Food food2 = foodIdMap.get(code2);
					double diff = queryResult.getDouble("diff");
					
					Adjacence newAdjacence = new Adjacence(food1, food2, diff);
					adjacences.add(newAdjacence);
				} 
				catch (Throwable t) 
				{
					t.printStackTrace();
				}
			}
			
			queryResult.close();
			statement.close();
			connection.close();
			return adjacences;
		}
		catch(SQLException sqle)
		{
			sqle.printStackTrace();
			throw new RuntimeException("Dao error in getFoodAdjacences()", sqle);
		}
	}
}
