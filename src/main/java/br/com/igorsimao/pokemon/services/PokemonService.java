package br.com.igorsimao.pokemon.services;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.igorsimao.pokemon.DAO.PokemonDAO;
import br.com.igorsimao.pokemon.model.Pokemon;
import br.com.igorsimao.pokemon.util.JsonErrorMessage;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Request;
import jakarta.ws.rs.core.Response;


@Path("/pokemon")
public class PokemonService {
	PokemonDAO pokemonDAO = new PokemonDAO(null);
	private final ObjectMapper objectMapper = new ObjectMapper();
	
		
	@POST()
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response insertPokemon(String pokemonJson) {
		System.out.println(pokemonJson);
		Pokemon pokemon = new Pokemon();
		if(pokemonJson != null) {
			
			try {
	            pokemon = objectMapper.readValue(pokemonJson, Pokemon.class);
	            Pokemon newPokemon = pokemonDAO.insertPokemon(pokemon);
	            if (newPokemon != null) {
	                String json = objectMapper.writeValueAsString(newPokemon);
	                return Response.status(Response.Status.CREATED).entity(json).build();
	            } else {
	                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(JsonErrorMessage.getJsonErrorMessage("Ocorreu um erro interno ao inserir o pokemon!")).build();
	            }
	        } catch (JsonMappingException e) {
	            e.printStackTrace();
	            return Response.status(Response.Status.BAD_REQUEST).entity(JsonErrorMessage.getJsonErrorMessage("Erro ao mapear o JSON para o objeto Pokemon.")).build();
	        } catch (JsonProcessingException e) {
	            e.printStackTrace();
	            return Response.status(Response.Status.BAD_REQUEST).entity(JsonErrorMessage.getJsonErrorMessage("Erro ao processar o JSON.")).build();
	        } catch (SQLException e) {
	        	e.printStackTrace();
	            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(JsonErrorMessage.getJsonErrorMessage(e.getMessage())).build();
	        }
	    } else {
	        return Response.status(Response.Status.BAD_REQUEST).entity(JsonErrorMessage.getJsonErrorMessage("O pokemon precisa ser enviado no formato Json")).build();
	    }
	}
	
	@GET()
	@Path("/{num}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPokemonByNum(@PathParam("num") Integer num) {
		if(num != null) {
			Pokemon pokemon = pokemonDAO.getPokemonByNum(num);
			if(pokemon != null) {
				String json;
				try {
					json = objectMapper.writeValueAsString(pokemon);
					return Response.status(Response.Status.OK).entity(json).build();
				} catch (JsonProcessingException e) {
					
					e.printStackTrace();
				}
				
			}
			
		}
		return Response.status(Response.Status.BAD_REQUEST).entity(JsonErrorMessage.getJsonErrorMessage("O Num informado do Pokemon não exite ou é nulo")).build();
	}
	
	
	@PUT
    @Path("/{num}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	public Response updatePokemon(@PathParam("num") Integer num, String pokemonJson) {
        try {
            Pokemon pokemon = objectMapper.readValue(pokemonJson, Pokemon.class);
            boolean updated = pokemonDAO.editPokemon(num, pokemon);
            if (updated) {
                return Response.ok().build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity(JsonErrorMessage.getJsonErrorMessage("Pokemon não encontrado para atualização.")).build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (e.getMessage().contains("because \"currentPokemon\" is null")) {
            	return Response.serverError().entity(JsonErrorMessage.getJsonErrorMessage("Erro ao atualizar o pokemon. O Num informado não existe!")).build();
			}
            return Response.serverError().entity(JsonErrorMessage.getJsonErrorMessage("Erro ao atualizar o pokemon.")).build();
        }
    }
	
	@DELETE()
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{num}")
	public Response deletePokemon(@PathParam("num") Integer num) {
		if(num == null) {
			return Response.status(Response.Status.BAD_REQUEST).entity(JsonErrorMessage.getJsonErrorMessage("Informe o Num do Pokemon!")).build();
		}
		
		Pokemon pokemon;
		try {
			pokemon = pokemonDAO.deletePokemonByNum(num);
			
			if(pokemon != null) {
				try {
					String json = objectMapper.writeValueAsString(pokemon);
					return Response.status(Response.Status.OK).entity(json).build();
				} catch (JsonProcessingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		} catch (SQLException e) {
			
			e.printStackTrace();
			return Response.status(Response.Status.NOT_FOUND).entity(JsonErrorMessage.getJsonErrorMessage(e.getMessage())).build();
		}
		
		return Response.status(Response.Status.NOT_FOUND).entity(JsonErrorMessage.getJsonErrorMessage("Pokemon não encontrado para Exclusão!")).build();
		
	}
}
