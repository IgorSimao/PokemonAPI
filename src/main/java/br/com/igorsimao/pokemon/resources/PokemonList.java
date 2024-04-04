package br.com.igorsimao.pokemon.resources;

import java.util.List;

import br.com.igorsimao.pokemon.model.Pokemon;

public class PokemonList {
	private List<Pokemon> pokemons;
	
	public List<Pokemon> getPokemons(){
		return pokemons;
	}
	
	public void setPokemon(List<Pokemon> pokemons) {
		this.pokemons = pokemons;
	}

	@Override
	public String toString() {
		String result = "";
		for (Pokemon pokemon : pokemons) {
			result += "PokemonList [pokemons=" + pokemon + "]";
		}
		
		return result;
		
	}
	
	
}
