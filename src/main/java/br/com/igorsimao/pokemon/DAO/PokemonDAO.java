package br.com.igorsimao.pokemon.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.sql.Statement;

import br.com.igorsimao.pokemon.model.Evolution;
import br.com.igorsimao.pokemon.model.Pokemon;
import br.com.igorsimao.pokemon.util.DBUtil;

public class PokemonDAO {
	private Connection connection;

	
    public PokemonDAO(Connection connection) {
		this.connection = connection;
	}

	public Pokemon insertPokemon(Pokemon pokemon) throws SQLException {
        List<String> nextEvolutionsId = new ArrayList<>();
        List<String> prevEvolutionsId = new ArrayList<>();

        String sql = "INSERT INTO pokemons (num, name, type) VALUES (?, ?, ?)";

        Integer idNewPokemon = 1;

        try {
        	connection = DBUtil.DbConnect();
        	
        	PreparedStatement preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, pokemon.getNum());
            preparedStatement.setString(2, pokemon.getName());
            preparedStatement.setString(3, String.join(",", pokemon.getType()));
            preparedStatement.execute();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();

            if (generatedKeys.next()) {
                Integer id = generatedKeys.getInt(1);
                pokemon.setId(id);
                idNewPokemon = id;
            }

        } catch (SQLException e) {
        	if(e.getMessage().contains("unique_num")) {
        		throw new SQLException("Já existe um pokemon com o campo num informado!");
        	}
            e.printStackTrace();
        }finally {
        	
			DBUtil.closeConnetion(connection);
		}
        
        try {
            List<Evolution> nextEvolutions = pokemon.getNextEvolution();
            List<Evolution> prevEvolutions = pokemon.getPrevEvolution();

            EvolutionDAO evolutionDAO = new EvolutionDAO(null);
            
            if(nextEvolutions.size() > 0 ) {
            	for (Evolution evolution : nextEvolutions) {
                    Integer id = evolutionDAO.insertEvolution(evolution, "next_evolutions", idNewPokemon);
                    nextEvolutionsId.add(id.toString());
                }
            }

            if(prevEvolutions.size() > 0 ) {
            	for (Evolution evolution : prevEvolutions) {
                    Integer id = evolutionDAO.insertEvolution(evolution, "prev_evolutions", idNewPokemon);
                    prevEvolutionsId.add(id.toString());
                }
            }
            

        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return pokemon;
    }

    public List<Pokemon> getAll() {
        List<Pokemon> pokemons = new ArrayList<>();

        String sql = "SELECT "
                + "    pokemons.id,"
                + "    pokemons.num,"
                + "    pokemons.name,"
                + "    pokemons.type,"
                + "    string_agg(next_evolutions.id::text, ', ') AS next_evolutions_id,"
                + "    string_agg(next_evolutions.num::text, ', ') AS next_evolutions_num,"
                + "    string_agg(next_evolutions.name, ', ') AS next_evolutions_name,"
                + "    string_agg(prev_evolutions.id::text, ', ') AS prev_evolutions_id,"
                + "    string_agg(prev_evolutions.num::text, ', ') AS prev_evolutions_num,"
                + "    string_agg(prev_evolutions.name, ', ') AS prev_evolutions_name "
                + "FROM "
                + "    pokemons "
                + "LEFT JOIN "
                + "    next_evolutions ON pokemons.id = next_evolutions.fk_pokemon "
                + "LEFT JOIN "
                + "    prev_evolutions ON pokemons.id = prev_evolutions.fk_pokemon "
                + "GROUP BY "
                + "    pokemons.id, pokemons.name, pokemons.num "
                + "ORDER BY "
                + "    pokemons.id;";
        
        connection = DBUtil.DbConnect();
        
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                Pokemon pokemon = new Pokemon();
                pokemon.setId(resultSet.getInt("id"));
                pokemon.setNum(resultSet.getInt("num"));
                pokemon.setName(resultSet.getString("name"));
                String[] types = resultSet.getString("type").split(",");
                pokemon.setType(Arrays.asList(types));
                if (resultSet.getString("next_evolutions_id") != null) {
                	
                	String[] nextEvolutionIds = resultSet.getString("next_evolutions_id").split(", ");
                	String[] nextEvolutionNum = resultSet.getString("next_evolutions_num").split(", ");
                    String[] nextEvolutionNames = resultSet.getString("next_evolutions_name").split(", ");
                    List<Evolution> nextEvolutions = new ArrayList<>();
                    for (int i = 0; i < nextEvolutionIds.length; i++) {
                        int nextId = Integer.parseInt(nextEvolutionIds[i]);
                        int nextnum = Integer.parseInt(nextEvolutionNum[i]);
                        Evolution evolution = new Evolution();
                        evolution.setId(nextId);
                        evolution.setNum(nextnum);
                        evolution.setName(nextEvolutionNames[i]);
                        nextEvolutions.add(evolution);
                    }
                    pokemon.setNextEvolution(nextEvolutions);
                }
                
                if (resultSet.getString("prev_evolutions_id") != null) {
                	String[] prevEvolutionIds = resultSet.getString("prev_evolutions_id").split(", ");
                	String[] prevEvolutionNum = resultSet.getString("prev_evolutions_num").split(", ");
                    String[] prevEvolutionNames = resultSet.getString("prev_evolutions_name").split(", ");
                    List<Evolution> prevEvolutions = new ArrayList<>();
                    for (int i = 0; i < prevEvolutionIds.length; i++) {
                        int prevId = Integer.parseInt(prevEvolutionIds[i]);
                        int prevNum = Integer.parseInt(prevEvolutionNum[i]);
                        Evolution evolution = new Evolution();
                        evolution.setId(prevId);
                        evolution.setNum(prevNum);
                        evolution.setName(prevEvolutionNames[i]);
                        prevEvolutions.add(evolution);
                    }
                    pokemon.setPrevEvolution(prevEvolutions);
                }
                
                pokemons.add(pokemon);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeConnetion(connection);
        }

        return pokemons;
    }

    public Pokemon getPokemonByNum(Integer num) {

        String sql = "SELECT "
        		+ "    pokemons.id,"
                + "    pokemons.num,"
                + "    pokemons.name,"
                + "    pokemons.type,"
                + "    string_agg(next_evolutions.id::text, ', ') AS next_evolutions_id,"
                + "    string_agg(next_evolutions.num::text, ', ') AS next_evolutions_num,"
                + "    string_agg(next_evolutions.name, ', ') AS next_evolutions_name,"
                + "    string_agg(prev_evolutions.id::text, ', ') AS prev_evolutions_id,"
                + "    string_agg(prev_evolutions.num::text, ', ') AS prev_evolutions_num,"
                + "    string_agg(prev_evolutions.name, ', ') AS prev_evolutions_name "
        		+ "FROM pokemons LEFT JOIN next_evolutions ON pokemons.id = next_evolutions.fk_pokemon "
        		+ "LEFT JOIN prev_evolutions ON pokemons.id = prev_evolutions.fk_pokemon "
        		+ "WHERE pokemons.num = ? "
        		+ "GROUP BY pokemons.id, pokemons.name, pokemons.num ORDER BY pokemons.id;";
        
        connection = DBUtil.DbConnect();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, num);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
            	Pokemon pokemon = new Pokemon();
                pokemon.setId(resultSet.getInt("id"));
                pokemon.setNum(resultSet.getInt("num"));
                pokemon.setName(resultSet.getString("name"));
                String[] types = resultSet.getString("type").split(",");
                pokemon.setType(Arrays.asList(types));
                if (resultSet.getString("next_evolutions_id") != null) {
                	
                	String[] nextEvolutionIds = resultSet.getString("next_evolutions_id").split(", ");
                	String[] nextEvolutionNum = resultSet.getString("next_evolutions_num").split(", ");
                    String[] nextEvolutionNames = resultSet.getString("next_evolutions_name").split(", ");
                    List<Evolution> nextEvolutions = new ArrayList<>();
                    for (int i = 0; i < nextEvolutionIds.length; i++) {
                        int nextId = Integer.parseInt(nextEvolutionIds[i]);
                        int nextnum = Integer.parseInt(nextEvolutionNum[i]);
                        Evolution evolution = new Evolution();
                        evolution.setId(nextId);
                        evolution.setNum(nextnum);
                        evolution.setName(nextEvolutionNames[i]);
                        nextEvolutions.add(evolution);
                    }
                    pokemon.setNextEvolution(nextEvolutions);
                }
                
                if (resultSet.getString("prev_evolutions_id") != null) {
                	String[] prevEvolutionIds = resultSet.getString("prev_evolutions_id").split(", ");
                	String[] prevEvolutionNum = resultSet.getString("prev_evolutions_num").split(", ");
                    String[] prevEvolutionNames = resultSet.getString("prev_evolutions_name").split(", ");
                    List<Evolution> prevEvolutions = new ArrayList<>();
                    for (int i = 0; i < prevEvolutionIds.length; i++) {
                        int prevId = Integer.parseInt(prevEvolutionIds[i]);
                        int prevNum = Integer.parseInt(prevEvolutionNum[i]);
                        Evolution evolution = new Evolution();
                        evolution.setId(prevId);
                        evolution.setNum(prevNum);
                        evolution.setName(prevEvolutionNames[i]);
                        prevEvolutions.add(evolution);
                    }
                    pokemon.setPrevEvolution(prevEvolutions);
                }

                return pokemon;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    
    public Pokemon getPokemonById(Integer id) {
        EvolutionDAO evolutionDAO = new EvolutionDAO(null);

        String sql = "SELECT "
        		+ "    pokemons.id,"
                + "    pokemons.num,"
                + "    pokemons.name,"
                + "    pokemons.type,"
                + "    string_agg(next_evolutions.id::text, ', ') AS next_evolutions_id,"
                + "    string_agg(next_evolutions.num::text, ', ') AS next_evolutions_num,"
                + "    string_agg(next_evolutions.name, ', ') AS next_evolutions_name,"
                + "    string_agg(prev_evolutions.id::text, ', ') AS prev_evolutions_id,"
                + "    string_agg(prev_evolutions.num::text, ', ') AS prev_evolutions_num,"
                + "    string_agg(prev_evolutions.name, ', ') AS prev_evolutions_name "
        		+ "FROM pokemons LEFT JOIN next_evolutions ON pokemons.id = next_evolutions.fk_pokemon "
        		+ "LEFT JOIN prev_evolutions ON pokemons.id = prev_evolutions.fk_pokemon "
        		+ "WHERE pokemons.num = ? "
        		+ "GROUP BY pokemons.id, pokemons.name, pokemons.num ORDER BY pokemons.id;";
        
        connection = DBUtil.DbConnect();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
            	Pokemon pokemon = new Pokemon();
                pokemon.setId(resultSet.getInt("id"));
                pokemon.setNum(resultSet.getInt("num"));
                pokemon.setName(resultSet.getString("name"));
                String[] types = resultSet.getString("type").split(",");
                pokemon.setType(Arrays.asList(types));
                if (resultSet.getString("next_evolutions_id") != null) {
                	
                	String[] nextEvolutionIds = resultSet.getString("next_evolutions_id").split(", ");
                	String[] nextEvolutionNum = resultSet.getString("next_evolutions_num").split(", ");
                    String[] nextEvolutionNames = resultSet.getString("next_evolutions_name").split(", ");
                    List<Evolution> nextEvolutions = new ArrayList<>();
                    for (int i = 0; i < nextEvolutionIds.length; i++) {
                        int nextId = Integer.parseInt(nextEvolutionIds[i]);
                        int nextnum = Integer.parseInt(nextEvolutionNum[i]);
                        Evolution evolution = new Evolution();
                        evolution.setId(nextId);
                        evolution.setNum(nextnum);
                        evolution.setName(nextEvolutionNames[i]);
                        nextEvolutions.add(evolution);
                    }
                    pokemon.setNextEvolution(nextEvolutions);
                }
                
                if (resultSet.getString("prev_evolutions_id") != null) {
                	String[] prevEvolutionIds = resultSet.getString("prev_evolutions_id").split(", ");
                	String[] prevEvolutionNum = resultSet.getString("prev_evolutions_num").split(", ");
                    String[] prevEvolutionNames = resultSet.getString("prev_evolutions_name").split(", ");
                    List<Evolution> prevEvolutions = new ArrayList<>();
                    for (int i = 0; i < prevEvolutionIds.length; i++) {
                        int prevId = Integer.parseInt(prevEvolutionIds[i]);
                        int prevNum = Integer.parseInt(prevEvolutionNum[i]);
                        Evolution evolution = new Evolution();
                        evolution.setId(prevId);
                        evolution.setNum(prevNum);
                        evolution.setName(prevEvolutionNames[i]);
                        prevEvolutions.add(evolution);
                    }
                    pokemon.setPrevEvolution(prevEvolutions);
                }

                return pokemon;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    
    
    public List<Pokemon> getPokemonByType(String type) {
    	List<Pokemon> pokemons = new ArrayList<>();
    	
    	String sql = "SELECT "
    			+ "    pokemons.id, "
    			+ "    pokemons.num, "
    			+ "    pokemons.name, "
    			+ "    pokemons.type,"
    			+ "    string_agg(next_evolutions.id::text, ', ') AS next_evolutions_id,"
    			+ "    string_agg(next_evolutions.num::text, ', ') AS next_evolutions_num,"
    			+ "    string_agg(next_evolutions.name, ', ') AS next_evolutions_name,"
    			+ "    string_agg(prev_evolutions.id::text, ', ') AS prev_evolutions_id,"
    			+ "    string_agg(prev_evolutions.num::text, ', ') AS prev_evolutions_num,"
    			+ "    string_agg(prev_evolutions.name, ', ') AS prev_evolutions_name "
    			+ "FROM pokemons "
    			+ "LEFT JOIN next_evolutions ON pokemons.id = next_evolutions.fk_pokemon "
    			+ "LEFT JOIN prev_evolutions ON pokemons.id = prev_evolutions.fk_pokemon "
    			+ "WHERE pokemons.type LIKE ? "
    			+ "GROUP BY pokemons.id, pokemons.name, pokemons.num "
    			+ "ORDER BY pokemons.id;";
    	connection = DBUtil.DbConnect();
    	
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, "%" + type + "%");

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Pokemon pokemon = new Pokemon();
                pokemon.setId(resultSet.getInt("id"));
                pokemon.setNum(resultSet.getInt("num"));
                pokemon.setName(resultSet.getString("name"));
                String[] types = resultSet.getString("type").split(",");
                pokemon.setType(Arrays.asList(types));
                if (resultSet.getString("next_evolutions_id") != null) {
                	
                	String[] nextEvolutionIds = resultSet.getString("next_evolutions_id").split(", ");
                	String[] nextEvolutionNum = resultSet.getString("next_evolutions_num").split(", ");
                    String[] nextEvolutionNames = resultSet.getString("next_evolutions_name").split(", ");
                    List<Evolution> nextEvolutions = new ArrayList<>();
                    for (int i = 0; i < nextEvolutionIds.length; i++) {
                        int nextId = Integer.parseInt(nextEvolutionIds[i]);
                        int nextnum = Integer.parseInt(nextEvolutionNum[i]);
                        Evolution evolution = new Evolution();
                        evolution.setId(nextId);
                        evolution.setNum(nextnum);
                        evolution.setName(nextEvolutionNames[i]);
                        nextEvolutions.add(evolution);
                    }
                    pokemon.setNextEvolution(nextEvolutions);
                }
                
                if (resultSet.getString("prev_evolutions_id") != null) {
                	String[] prevEvolutionIds = resultSet.getString("prev_evolutions_id").split(", ");
                	String[] prevEvolutionNum = resultSet.getString("prev_evolutions_num").split(", ");
                    String[] prevEvolutionNames = resultSet.getString("prev_evolutions_name").split(", ");
                    List<Evolution> prevEvolutions = new ArrayList<>();
                    for (int i = 0; i < prevEvolutionIds.length; i++) {
                        int prevId = Integer.parseInt(prevEvolutionIds[i]);
                        int prevNum = Integer.parseInt(prevEvolutionNum[i]);
                        Evolution evolution = new Evolution();
                        evolution.setId(prevId);
                        evolution.setNum(prevNum);
                        evolution.setName(prevEvolutionNames[i]);
                        prevEvolutions.add(evolution);
                    }
                    pokemon.setPrevEvolution(prevEvolutions);
                }
                
                pokemons.add(pokemon);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeConnetion(connection);
        }

        return pokemons;
    }
    
    public Boolean editPokemon(Integer num, Pokemon updatePokemon) throws SQLException {
        Connection connection = null;
        Pokemon currentPokemon = getPokemonByNum(num);
        
        try {
            connection = DBUtil.DbConnect();
            connection.setAutoCommit(false);

            // Atualiza os dados do Pokemon
            String updatePokemonQuery = "UPDATE pokemons SET name = ?, type = ? WHERE num = ?";
            PreparedStatement updatePokemonStatement = connection.prepareStatement(updatePokemonQuery);
            updatePokemonStatement.setString(1, updatePokemon.getName());
            updatePokemonStatement.setString(2, String.join(",", updatePokemon.getType()));
            updatePokemonStatement.setInt(3, num);
            updatePokemonStatement.executeUpdate();

            // Atualiza as evoluções do Pokemon
            EvolutionDAO evolutionDAO = new EvolutionDAO(connection);

            // Remoção de evoluções existentes
            String deletePrevEvolutionsQuery = "DELETE FROM prev_evolutions WHERE fk_pokemon = ?";
            String deleteNextEvolutionsQuery = "DELETE FROM next_evolutions WHERE fk_pokemon = ?";
            PreparedStatement deletePrevEvolutionsStatement = connection.prepareStatement(deletePrevEvolutionsQuery);
            PreparedStatement deleteNextEvolutionsStatement = connection.prepareStatement(deleteNextEvolutionsQuery);
            deletePrevEvolutionsStatement.setInt(1, currentPokemon.getId());
            deleteNextEvolutionsStatement.setInt(1, currentPokemon.getId());
            deletePrevEvolutionsStatement.executeUpdate();
            deleteNextEvolutionsStatement.executeUpdate();

            // Adição de novas evoluções e atualização de evoluções existentes
            if (updatePokemon.getPrevEvolution() != null) {
                for (Evolution prevEvolution : updatePokemon.getPrevEvolution()) {
                    evolutionDAO.insertEvolution(prevEvolution, "prev_evolutions", currentPokemon.getId());
                }
            }

            if (updatePokemon.getNextEvolution() != null) {
                for (Evolution nextEvolution : updatePokemon.getNextEvolution()) {
                    evolutionDAO.insertEvolution(nextEvolution, "next_evolutions", currentPokemon.getId());
                }
            }

            connection.commit();
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    throw new SQLException("Não foi possível realizar o roolback no banco!");
                    
                }
            }
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        
        return true;
    }
    
    public Pokemon deletePokemonByNum(Integer num) throws SQLException {
        Connection connection = null;
        Pokemon deletedPokemon = null;
        
        Pokemon currentDeletePokemon = getPokemonByNum(num);
        
        try {
            connection = DBUtil.DbConnect();
            connection.setAutoCommit(false);
            
            if(currentDeletePokemon == null) {
            	throw new SQLException("O num informado para Deleção não existe no banco!");
            }
            EvolutionDAO evolutionDAO = new EvolutionDAO(connection);
            evolutionDAO.deleteEvolutionsByPokemonId(currentDeletePokemon.getId());

            
            String deletePokemonQuery = "DELETE FROM pokemons WHERE num = ?";
            PreparedStatement deletePokemonStatement = connection.prepareStatement(deletePokemonQuery, Statement.RETURN_GENERATED_KEYS);
            deletePokemonStatement.setInt(1, num);
            int affectedRows = deletePokemonStatement.executeUpdate();

            
            if (affectedRows > 0) {
                ResultSet generatedKeys = deletePokemonStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    deletedPokemon = getPokemonById(id);
                }
            }

            connection.commit();
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return currentDeletePokemon;
    }
    
    public List<Pokemon> getPokemonByQuantityAndPage(Integer page, Integer quantityOfPage){
    	List<Pokemon> pokemons = new ArrayList<>();
    	String sql = "SELECT "
    			+ "    pokemons.id, "
    			+ "    pokemons.num, "
    			+ "    pokemons.name, "
    			+ "    pokemons.type, "
    			+ "    string_agg(next_evolutions.id::text, ', ') AS next_evolutions_id, "
    			+ "    string_agg(next_evolutions.num::text, ', ') AS next_evolutions_num, "
    			+ "    string_agg(next_evolutions.name, ', ') AS next_evolutions_name, "
    			+ "    string_agg(prev_evolutions.id::text, ', ') AS prev_evolutions_id, "
    			+ "    string_agg(prev_evolutions.num::text, ', ') AS prev_evolutions_num, "
    			+ "    string_agg(prev_evolutions.name, ', ') AS prev_evolutions_name "
    			+ "FROM "
    			+ "    pokemons "
    			+ "LEFT JOIN "
    			+ "    next_evolutions ON pokemons.id = next_evolutions.fk_pokemon "
    			+ "LEFT JOIN"
    			+ "    prev_evolutions ON pokemons.id = prev_evolutions.fk_pokemon "
    			+ "GROUP BY "
    			+ "    pokemons.id, "
    			+ "    pokemons.num, "
    			+ "    pokemons.name, "
    			+ "    pokemons.type "
    			+ "ORDER BY "
    			+ "    pokemons.num "
    			+ "OFFSET ((? - 1) * ?) "
    			+ "LIMIT ?;";
    	
    	 connection = DBUtil.DbConnect();
         
         try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
        	 preparedStatement.setInt(1, page);
        	 preparedStatement.setInt(2, quantityOfPage);
        	 preparedStatement.setInt(3, quantityOfPage);
        	 ResultSet resultSet = preparedStatement.executeQuery();

             while (resultSet.next()) {
                 Pokemon pokemon = new Pokemon();
                 pokemon.setId(resultSet.getInt("id"));
                 pokemon.setNum(resultSet.getInt("num"));
                 pokemon.setName(resultSet.getString("name"));
                 String[] types = resultSet.getString("type").split(",");
                 pokemon.setType(Arrays.asList(types));
                 if (resultSet.getString("next_evolutions_id") != null) {
                 	
                 	String[] nextEvolutionIds = resultSet.getString("next_evolutions_id").split(", ");
                 	String[] nextEvolutionNum = resultSet.getString("next_evolutions_num").split(", ");
                     String[] nextEvolutionNames = resultSet.getString("next_evolutions_name").split(", ");
                     List<Evolution> nextEvolutions = new ArrayList<>();
                     for (int i = 0; i < nextEvolutionIds.length; i++) {
                         int nextId = Integer.parseInt(nextEvolutionIds[i]);
                         int nextnum = Integer.parseInt(nextEvolutionNum[i]);
                         Evolution evolution = new Evolution();
                         evolution.setId(nextId);
                         evolution.setNum(nextnum);
                         evolution.setName(nextEvolutionNames[i]);
                         nextEvolutions.add(evolution);
                     }
                     pokemon.setNextEvolution(nextEvolutions);
                 }
                 
                 if (resultSet.getString("prev_evolutions_id") != null) {
                 	String[] prevEvolutionIds = resultSet.getString("prev_evolutions_id").split(", ");
                 	String[] prevEvolutionNum = resultSet.getString("prev_evolutions_num").split(", ");
                     String[] prevEvolutionNames = resultSet.getString("prev_evolutions_name").split(", ");
                     List<Evolution> prevEvolutions = new ArrayList<>();
                     for (int i = 0; i < prevEvolutionIds.length; i++) {
                         int prevId = Integer.parseInt(prevEvolutionIds[i]);
                         int prevNum = Integer.parseInt(prevEvolutionNum[i]);
                         Evolution evolution = new Evolution();
                         evolution.setId(prevId);
                         evolution.setNum(prevNum);
                         evolution.setName(prevEvolutionNames[i]);
                         prevEvolutions.add(evolution);
                     }
                     pokemon.setPrevEvolution(prevEvolutions);
                 }
                 
                 pokemons.add(pokemon);
             }

         } catch (SQLException e) {
             e.printStackTrace();
         } finally {
             DBUtil.closeConnetion(connection);
         }

         return pokemons;
     }
    			
}

