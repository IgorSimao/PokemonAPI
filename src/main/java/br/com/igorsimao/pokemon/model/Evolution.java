package br.com.igorsimao.pokemon.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Evolution {
	private Integer id;
    private Integer num;
    private String name;

	
    
    public Evolution() {
		
	}

	public Evolution(Integer num, String name) {
		
        this.num = num;
        this.name = name;
    }

    public Integer getNum() {
        return num;
    }
    public void setNum(Integer num) {
        this.num = num;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    
    public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String toJson() throws JsonProcessingException {
    	ObjectMapper objectMapper = new ObjectMapper();
    	
			String evolutionJson = objectMapper.writeValueAsString(Evolution.this);
			
			return evolutionJson;
				
    }

    @Override
    public String toString() {
        return "Evolution [num=" + num + ", name=" + name + "]";
    }
    
}