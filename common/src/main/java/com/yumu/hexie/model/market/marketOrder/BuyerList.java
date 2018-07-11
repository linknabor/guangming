package com.yumu.hexie.model.market.marketOrder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.yumu.hexie.model.user.Address;

public class BuyerList implements Serializable{

	private static final long serialVersionUID = 1L;

	private Address addr;
	
	List<BuyerCart> buyerCart = new ArrayList<BuyerCart>();

	public Address getAddr() {
		return addr;
	}

	public void setAddr(Address addr) {
		this.addr = addr;
	}

	public List<BuyerCart> getBuyerCart() {
		return buyerCart;
	}

	public void setBuyerCart(List<BuyerCart> buyerCart) {
		this.buyerCart = buyerCart;
	}
	
	
}
