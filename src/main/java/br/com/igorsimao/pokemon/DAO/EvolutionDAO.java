package br.com.igorsimao.pokemon.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.com.igorsimao.pokemon.model.Evolution;
import br.com.igorsimao.pokemon.util.DBUtil;


public class EvolutionDAO {
	private Connection connection;
	
	
    public EvolutionDAO(Connection connection) {
		this.connection = connection;
	}

    
	public Integer insertEvolution(Evolution evolution, String table, Integer fk_pokemon) {
        String sql = "INSERT INTO "+ table +" (num, name, fk_pokemon) VALUES (?, ?, ?)";
        connection = DBUtil.DbConnect();
        try {
        	PreparedStatement preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, evolution.getNum());
            preparedStatement.setString(2, evolution.getName());
            preparedStatement.setInt(3, fk_pokemon);
            preparedStatement.execute();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();

            if (generatedKeys.next()) {
                Integer id = generatedKeys.getInt(1);
                evolution.setId(id);
                return id;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
        	
			DBUtil.closeConnetion(connection);
		}
        return null;
    }

    public Evolution getEvolutionById(Integer id, String table) {
        String sql = "SELECT * FROM "+ table +" WHERE id = ?";
        connection = DBUtil.DbConnect();
        
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Evolution evolution = new Evolution();
                evolution.setId(resultSet.getInt("id"));
                evolution.setNum(resultSet.getInt("num"));
                evolution.setName(resultSet.getString("name"));

                return evolution;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
        	
			DBUtil.closeConnetion(connection);
		}
        return null;
    }

    public Evolution getEvolutionByNum(Integer num, String table) {
        String sql = "SELECT * FROM "+ table +" WHERE id = ?";
        connection = DBUtil.DbConnect();
        
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, num);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Evolution evolution = new Evolution();
                evolution.setId(resultSet.getInt("id"));
                evolution.setNum(resultSet.getInt("num"));
                evolution.setName(resultSet.getString("name"));

                return evolution;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
        	
			DBUtil.closeConnetion(connection);
		}
        return null;
    }

    public List<Evolution> getEvolutionByFkPokemon(Integer fkPokemon, String table) {
        String sql = "SELECT * FROM " + table + " WHERE fk_pokemon = ?";
        connection = DBUtil.DbConnect();
        
        List<Evolution> evolutionsByFkPokemon = new ArrayList<>();
        
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, fkPokemon);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Evolution evolution = new Evolution();
                evolution.setId(resultSet.getInt("id"));
                evolution.setNum(resultSet.getInt("num"));
                evolution.setName(resultSet.getString("name"));

                evolutionsByFkPokemon.add(evolution);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
        	
			DBUtil.closeConnetion(connection);
		}
        return evolutionsByFkPokemon;
    }

    public Boolean deleteEvolutionsByPokemonId(Integer fk_pokemon) {
    	connection = DBUtil.DbConnect();
    	
    	String deletePrevEvolutionsQuery = "DELETE FROM prev_evolutions WHERE fk_pokemon = ?";
        String deleteNextEvolutionsQuery = "DELETE FROM next_evolutions WHERE fk_pokemon = ?";
        try {
        	PreparedStatement deletePrevEvolutionsStatement = connection.prepareStatement(deletePrevEvolutionsQuery);
            PreparedStatement deleteNextEvolutionsStatement = connection.prepareStatement(deleteNextEvolutionsQuery);
            deletePrevEvolutionsStatement.setInt(1, fk_pokemon);
            deleteNextEvolutionsStatement.setInt(1, fk_pokemon);
            deletePrevEvolutionsStatement.executeUpdate();
            deleteNextEvolutionsStatement.executeUpdate();
        	
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}finally {
			DBUtil.closeConnetion(connection);
		}
        return true;
    }
}
