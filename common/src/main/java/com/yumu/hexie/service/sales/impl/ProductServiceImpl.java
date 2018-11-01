package com.yumu.hexie.service.sales.impl;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.yumu.hexie.model.ModelConstant;
import com.yumu.hexie.model.commonsupport.info.Product;
import com.yumu.hexie.model.commonsupport.info.ProductItem;
import com.yumu.hexie.model.commonsupport.info.ProductItemRepository;
import com.yumu.hexie.model.commonsupport.info.ProductRepository;
import com.yumu.hexie.model.commonsupport.info.Productclassification;
import com.yumu.hexie.model.commonsupport.info.ProductclassificationRepository;
import com.yumu.hexie.model.user.User;
import com.yumu.hexie.service.exception.BizValidateException;
import com.yumu.hexie.service.sales.ProductService;

@Service("productService")
public class ProductServiceImpl implements ProductService {

	@Inject
	private ProductRepository productRepository;
	@Inject
	private ProductItemRepository productItemRepository;
	@Inject
	private ProductclassificationRepository productclassificationRepository;
	
	@Override
	public Product getProduct(long productId) {
		return productRepository.findOne(productId);
	}

	@Override
	public List<Product> getAllProduct() {
		return productRepository.findAll();
	}

	@Override
	public List<ProductItem> getByNameProduct(String name,User user,int pageNow) {
		List<ProductItem> list = productItemRepository.getByNameProduct(user.getProvinceId(), user.getCityId(), user.getCountyId(), user.getXiaoquId(),name,pageNow);
		for (int i = 0; i < list.size(); i++) {
			int a = 0;
			for (int j = 0; j < list.get(i).getProducts().size(); j++) {
				 a = a+list.get(i).getProducts().get(j).getSaledNum();
			}
			list.get(i).setTotalSale(a);
		}
		return list;
	}
	
	@Override
	public void checkSalable(Product product, int count) {
		if(product.getStatus() != ModelConstant.PRODUCT_ONSALE){
			throw new BizValidateException("您晚到了一步，商品已下架！");
		} else if(product.getCanSaleNum()<=0) {
			throw new BizValidateException("您晚到了一步，商品已卖完！");
		} else if(product.getCanSaleNum()<count) {
			throw new BizValidateException("库存不足，请减少购买数量！");
		} else if(product.getEndDate()!=null&&product.getEndDate().getTime()<System.currentTimeMillis()){
			throw new BizValidateException("您晚到了一步，商品已下架！");
		}
	}

	@Override
	public void freezeCount(Product product, int count) {
		product.setFreezeNum(product.getFreezeNum() +count);
		productRepository.save(product);
		
	}

	@Override
	public void saledCount(long productId, int count) {
		Product product = productRepository.findOne(productId);
		product.setSaledNum(product.getSaledNum()+count);
		product.setFreezeNum(product.getFreezeNum()-count);
		productRepository.save(product);
	}

	@Override
	public void unfreezeCount(long productId, int count) {
		Product product = productRepository.findOne(productId);
		product.setFreezeNum(product.getFreezeNum()-count);
		productRepository.save(product);
	}

	@Override
	public List<Product> getProductsByItem(long productItemId) {
		
		ProductItem productItem = productItemRepository.findOne(productItemId); 
		return productRepository.findByProductItemAndStatus(productItem, ModelConstant.PRODUCT_ONSALE);
	}

	@Override
	public ProductItem getProdcutItemById(long productItemId) {

		return productItemRepository.findOne(productItemId);
	}

	@Override
	public List<Productclassification> getParentProductCfi() {
		// TODO Auto-generated method stub
		return productclassificationRepository.getParentProductCfi();
	}

	@Override
	public List<Productclassification> getByParentIDProductCfi(int parentid) {
		// TODO Auto-generated method stub
		return productclassificationRepository.getByParentIDProductCfi(parentid);
	}

	@Override
	public List<ProductItem> getByProductCfiId(int productcfiid,User user,int pageNow) {
		// TODO Auto-generated method stub
		return productItemRepository.getByProductCfiId( user.getProvinceId(), user.getCityId(), user.getCountyId(), user.getXiaoquId(),productcfiid, pageNow);
	}
}
