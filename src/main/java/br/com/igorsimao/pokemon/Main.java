package br.com.igorsimao.pokemon;

import java.io.IOException;
import java.net.URI;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import br.com.igorsimao.pokemon.resources.ImportPokemon;

public class Main {
    
    public static final String BASE_URI = "http://localhost:8080/";

   
    public static HttpServer startServer() {

        final ResourceConfig rc = new ResourceConfig().packages("br.com.igorsimao.pokemon");

    
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    
    @SuppressWarnings("deprecation")
	public static void main(String[] args) throws IOException {
    	//Para importar um novo json de Pokemons descomente as duas linhas abaixo:
    	//ImportPokemon importPokemon = new ImportPokemon();
    	//importPokemon.lerJson();
    	
        final HttpServer server = startServer();
        System.out.println(String.format("Jersey app started with endpoints available at "
                + "%s%nHit Ctrl-C to stop it...", BASE_URI));
        System.in.read();
        server.stop();
    }
}