package com.yumu.hexie.web.product;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yumu.hexie.model.commonsupport.info.Product;
import com.yumu.hexie.service.sales.ProductService;
import com.yumu.hexie.web.BaseController;

@RestController(value="shangpinController")
public class ProductController extends BaseController{
	
	private static final Logger log = LoggerFactory.getLogger(ProductController.class);
	
	@Inject
	private ProductService product;
	
	/**
	 * 模糊查询商品
	 * @param name
	 * @return
	 */
	@RequestMapping("/search")
	public List<Product> search(String name) {
		
		List<Product> list = product.getByNameProduct(name);
		
		return list;
	}
}
