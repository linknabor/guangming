/**
 * 
 */
package com.yumu.hexie.model.commonsupport.info;

import java.io.Serializable;
import java.util.List;

/**
 * @author davidhardson
 *
 */
public class Specification implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2420669006253141454L;
	
	private String name;
	private List<String> values;
	
	/**
	 * 
	 */
	public Specification() {
		// TODO Auto-generated constructor stub
	}
	
	public Specification(String name, List<String> values) {
		super();
		this.name = name;
		this.values = values;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getValues() {
		return values;
	}

	public void setValues(List<String> values) {
		this.values = values;
	}




}
