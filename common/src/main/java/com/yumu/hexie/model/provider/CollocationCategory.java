package com.yumu.hexie.model.provider;

import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class CollocationCategory implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4544309432144180541L;
	
	String firstType;
	List<String> secondTypes;
	public String getFirstType() {
		return firstType;
	}
	public void setFirstType(String firstType) {
		this.firstType = firstType;
	}
	public List<String> getSecondTypes() {
		return secondTypes;
	}
	public void setSecondTypes(List<String> secondTypes) {
		this.secondTypes = secondTypes;
	}
	@Override
	public String toString() {
		return "CollocationCategory [firstType=" + firstType + ", secondTypes="
				+ secondTypes + "]";
	}
	
	
	
	
}
