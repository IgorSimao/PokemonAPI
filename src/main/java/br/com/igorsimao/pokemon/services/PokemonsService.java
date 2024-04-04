package br.com.igorsimao.pokemon.services;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.ws.rs.QueryParam;

import br.com.igorsimao.pokemon.DAO.PokemonDAO;
import br.com.igorsimao.pokemon.model.Pokemon;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.ResponseBuilder;

@Path("/pokemons")
public class PokemonsService {
	
	PokemonDAO pokemonDAO = new PokemonDAO(null);
	private final ObjectMapper objectMapper = new ObjectMapper();
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllPokemons() {

		List<Pokemon> pokemons = pokemonDAO.getAll();	
		
		try {
            String json = objectMapper.writeValueAsString(pokemons);
            return Response.ok(json).build();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
	}
	
	@GET()
	@Path("/{type}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPokemonByType(@PathParam("type") String type) {
		String formattedType = type.substring(0, 1).toUpperCase() + type.substring(1).toLowerCase();
		List<Pokemon> pokemons = pokemonDAO.getPokemonByType(formattedType);
		try {
            String json = objectMapper.writeValueAsString(pokemons);
            return Response.ok(json).build();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
	}
	
	@GET
	@Path("/{page}/{quantity}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPokemonByQuantityAndPage(@PathParam("page") Integer page, @PathParam("quantity") Integer quantity) {
		
		List<Pokemon> pokemons = pokemonDAO.getPokemonByQuantityAndPage(page, quantity);

		try {
            String json = objectMapper.writeValueAsString(pokemons);
            return Response.ok(json).build();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
		
	}
	
}