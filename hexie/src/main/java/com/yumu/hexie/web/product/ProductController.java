package com.yumu.hexie.web.product;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yumu.hexie.model.commonsupport.info.Product;
import com.yumu.hexie.service.sales.ProductService;
import com.yumu.hexie.web.BaseController;

@Controller
@RequestMapping(value="productCotroller")
public class ProductController extends BaseController{
	
	private static final Logger log = LoggerFactory.getLogger(ProductController.class);
	
	@Inject
	private ProductService product;
	
	/**
	 * 模糊查询商品
	 * @param name
	 * @return
	 */
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	@ResponseBody
	public List<Product> search(String name)  throws Exception{
		
		List<Product> list = product.getByNameProduct(name);
		
		return list;
	}
	
}
