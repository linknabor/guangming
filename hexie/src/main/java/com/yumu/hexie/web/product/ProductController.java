package com.yumu.hexie.web.product;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yumu.hexie.common.Constants;
import com.yumu.hexie.model.commonsupport.info.Product;
import com.yumu.hexie.model.commonsupport.info.ProductItem;
import com.yumu.hexie.model.user.User;
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
	public List<ProductItem> search(String name,@ModelAttribute(Constants.USER)User user,@RequestParam(required=false,defaultValue="0")int pageNow)  throws Exception{
		
		List<ProductItem> list = product.getByNameProduct(name,user,pageNow);

		return list;
	}
	
	@RequestMapping(value = "/getByproductCfi", method = RequestMethod.GET)
	@ResponseBody
	public List<ProductItem> getByproductCfi(int productcfiid,@ModelAttribute(Constants.USER)User user,@RequestParam(required=false,defaultValue="0") int pageNow){
		return product.getByProductCfiId(productcfiid,user, pageNow);
	}
	
}
