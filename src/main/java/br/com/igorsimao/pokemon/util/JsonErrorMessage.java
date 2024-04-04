package br.com.igorsimao.pokemon.util;


import javax.enterprise.inject.New;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonErrorMessage {
	private final static ObjectMapper objectMapper = new ObjectMapper();

	public static String getJsonErrorMessage(String message) {
		try {
			
			String json = objectMapper.writeValueAsString(new ErrorResponse("error", message));
			return json;
		} catch (JsonProcessingException e) {
			
			e.printStackTrace();
		}
		return null;
	}
	
	private static class ErrorResponse {
	    private String error;
	    private String message;

	    public ErrorResponse(String error, String message) {
	        this.error = error;
	        this.message = message;
	    }

	    public String getError() {
	        return error;
	    }

	    public void setError(String error) {
	        this.error = error;
	    }

	    public String getMessage() {
	        return message;
	    }

	    public void setMessage(String message) {
	        this.message = message;
	    }
	}
}

