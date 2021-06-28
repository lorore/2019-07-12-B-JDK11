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

import it.polito.tdp.food.model.Adiacenza;
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
	
	public void getVertici(Map<Integer, Food> idMap, int k) {
		String sql="SELECT * "
				+ "FROM food f "
				+ "WHERE f.food_code IN( "
				+ "SELECT p.food_code "
				+ "FROM `portion` p "
				+ "GROUP BY p.food_code "
				+ "HAVING COUNT(*)>=?) ";
		Connection conn = DBConnect.getConnection() ;
		

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, k);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Food f=new Food(res.getInt("food_code"),res.getString("display_name"));
				if(!idMap.containsKey(f.getFood_code())) {
					idMap.put(f.getFood_code(), f);
				}
			

			}
			conn.close();
			
			
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		
	}
	
	public List<Adiacenza> getArchi(Map<Integer, Food> idMap, int k){
		String sql="SELECT t1.food_code AS f1, t2.food_code AS f2, (t1.media-t2.media) AS peso "
				+ "FROM ( "
				+ "SELECT p.food_code, AVG(saturated_fats) AS media "
				+ "FROM `portion` p "
				+ "GROUP BY p.food_code "
				+ "HAVING COUNT(*)>=?) AS t1, "
				+ "(SELECT p.food_code, AVG(saturated_fats) AS media "
				+ "FROM `portion` p "
				+ "GROUP BY p.food_code "
				+ "HAVING COUNT(*)>=?) AS t2 "
				+ "WHERE t1.food_code<>t2.food_code "
				+ "GROUP BY t1.food_code, t2.food_code "
				+ "HAVING peso>0 ";
			Connection conn = DBConnect.getConnection() ;
			List<Adiacenza> result=new ArrayList<>();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, k);
			st.setInt(2, k);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				if(idMap.containsKey(res.getInt("f1")) && idMap.containsKey(res.getInt("f2"))) {
					Food f1=idMap.get(res.getInt("f1"));
					Food f2=idMap.get(res.getInt("f2"));
					double peso=res.getDouble("peso");
					result.add(new Adiacenza(f1, f2, peso));
				}
			

			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	
}
