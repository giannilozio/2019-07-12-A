package it.polito.tdp.food.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.food.model.Condiment;
import it.polito.tdp.food.model.Food;
import it.polito.tdp.food.model.PD;
import it.polito.tdp.food.model.Portion;

public class FoodDao {
	public List<Food> listAllFoods(){
		String sql = "SELECT * FROM food" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Food> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Food(res.getInt("food_code"),
							res.getString("display_name")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}

	}
	
	public List<Condiment> listAllCondiments(){
		String sql = "SELECT * FROM condiment" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Condiment> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Condiment(res.getInt("condiment_code"),
							res.getString("display_name"),
							res.getDouble("condiment_calories"), 
							res.getDouble("condiment_saturated_fats")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Portion> listAllPortions(){
		String sql = "SELECT * FROM portion" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Portion> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Portion(res.getInt("portion_id"),
							res.getDouble("portion_amount"),
							res.getString("portion_display_name"), 
							res.getDouble("calories"),
							res.getDouble("saturated_fats"),
							res.getInt("food_code")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}

	}

	public List<Food> getVertici(int porzioni, Map<Integer, Food> idMap) {
		
		String sql = "SELECT f.food_code, f.display_name " + 
					 "FROM portion p, food f " + 
					 "WHERE f.food_code=p.food_code " + 
					 "GROUP BY f.food_code, f.display_name " + 
					 "HAVING COUNT(p.food_code)<= ?";
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, porzioni);
			List<Food> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					
					Food f = new Food(res.getInt("food_code"),res.getString("display_name"));
					idMap.put(res.getInt("food_code"),f);
					
					list.add(f);
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
		
	}

	public List<PD> getPesi(int porzioni, Map<Integer, Food> idMap) {
		String sql = "SELECT f1.food_code, f2.food_code, AVG (c.condiment_calories) as peso " + 
				 	 "FROM condiment c, food_condiment f1, food_condiment f2, portion p1, portion p2 " + 
				 	 "WHERE f1.condiment_code=f2.condiment_code " + 
				 	 "AND c.condiment_code= f2.food_code " + 
				 	 "AND p1.food_code=f1.food_code " + 
				 	 "AND p2.food_code=f2.food_code " + 
				 	 "AND f2.food_code<> f1.food_code " + 
				 	 "GROUP BY f1.food_code, f2.food_code " + 
				 	 "HAVING COUNT(p1.food_code AND p2.food_code)<=?";
	try {
		Connection conn = DBConnect.getConnection() ;

		PreparedStatement st = conn.prepareStatement(sql) ;
		st.setInt(1, porzioni);
		List<PD> list = new ArrayList<>() ;
		
		ResultSet res = st.executeQuery() ;
		
		while(res.next()) {
			try {
				
				Food f1 = idMap.get(res.getInt("f1.food_code"));
				Food f2 = idMap.get(res.getInt("f2.food_code"));
				
				PD p = new PD(f1,f2,res.getDouble("peso"));
				
				list.add(p);
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
		
		conn.close();
		return list ;

	} catch (SQLException e) {
		e.printStackTrace();
		return null ;
	}
	
		
	}
}
