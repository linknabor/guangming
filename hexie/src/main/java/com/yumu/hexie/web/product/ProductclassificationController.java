package com.yumu.hexie.web.product;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yumu.hexie.model.commonsupport.info.Productclassification;
import com.yumu.hexie.service.sales.ProductService;
import com.yumu.hexie.web.BaseController;
@Controller
@RequestMapping(value="ProductclassificationController")
public class ProductclassificationController extends BaseController{
	private static final Logger log = LoggerFactory.getLogger(ProductclassificationController.class);
	
	@Inject
	private ProductService productService;
	
	@RequestMapping(value = "/getParent", method = RequestMethod.GET)
	@ResponseBody
	public List<Productclassification> getAll(){
		return productService.getParentProductCfi();
	}
	
	@RequestMapping(value = "/getByParentId", method = RequestMethod.GET)
	@ResponseBody
	public List<Productclassification> getByParentId(int parentid){
		return productService.getByParentIDProductCfi(parentid);
	}
}
