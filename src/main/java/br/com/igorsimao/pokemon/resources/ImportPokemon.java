package br.com.igorsimao.pokemon.resources;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.io.File;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.igorsimao.pokemon.DAO.PokemonDAO;
import br.com.igorsimao.pokemon.model.Pokemon;

public class ImportPokemon {
	
	public void lerJson() {
		//Para alterar o json a ser importado altere a atribuição da variável pokemonJson para o caminho do arquivo.
		
	    String pokemonJson = "C:\\Users\\Igor Simao\\eclipse-workspace\\pokedex.json";

	    ObjectMapper objectMapper = new ObjectMapper();
	
		    try {
				
		    	 PokemonList pokemonsList = objectMapper.readValue(new File(pokemonJson), PokemonList.class);
		    	 System.out.println(pokemonsList);
		    	 
		            List<Pokemon> pokemons = pokemonsList.getPokemons();

		            for (Pokemon pokemon : pokemons) {
		            	
		                PokemonDAO pokemonDAO = new PokemonDAO(null);
		                try {
							pokemonDAO.insertPokemon(pokemon);
						} catch (SQLException e) {
							
							e.printStackTrace();
						}
		                
		               
	            }
		            System.out.println("Importação concluída!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    
	    
	    }
    
   

}