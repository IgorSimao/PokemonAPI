package br.com.igorsimao.pokemon.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Pokemon {
	private Integer id = 0;
	private Integer num;
	private String name;
	private List<String> type = new ArrayList<String>();
	@JsonProperty("prev_evolution")
	private List<Evolution> prevEvolution = new ArrayList<Evolution>();
	@JsonProperty("next_evolution")
	private List<Evolution> nextEvolution = new ArrayList<Evolution>();

	
	public Pokemon() {
		
	}
	
	public Pokemon(Integer num, String name, List<String> type, List<Evolution> prevEvolution,
			List<Evolution> nextEvolution) {
		
		this.num = num;
		this.name = name;
		this.type = type;
		this.prevEvolution = prevEvolution;
		this.nextEvolution = nextEvolution;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setNum(Integer num) {
		this.num = num;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public Integer getNum() {
		return num;
	}

	public String getName() {
		return name;
	}

	public List<String> getType() {
		return type;
	}

	public void setType(List<String> type) {
		this.type = type;
	}

	public List<Evolution> getPrevEvolution() {
		return prevEvolution;
	}

	public void setPrevEvolution(List<Evolution> prevEvolution) {
		this.prevEvolution = prevEvolution;
	}

	public List<Evolution> getNextEvolution() {
		return nextEvolution;
	}

	public void setNextEvolution(List<Evolution> nextEvolution) {
		this.nextEvolution = nextEvolution;
	}


	@Override
	public String toString() {
		return "Pokemon [num=" + num + ", name=" + name + ", type=" + type + ", prevEvolution=" + prevEvolution
				+ ", nextEvolution=" + nextEvolution + "]";
	}
	
	
	
}