package com.yumu.hexie.service.jingdong;

import java.util.List;

import com.yumu.hexie.model.jingdong.getSecurity.JDLoad;
import com.yumu.hexie.model.jingdong.getSecurity.JDSecurity;
import com.yumu.hexie.model.jingdong.getaddress.JDAddress;
import com.yumu.hexie.model.jingdong.getaddress.JDAddressF;
import com.yumu.hexie.model.jingdong.getsku.JDSku;
import com.yumu.hexie.model.jingdong.getsku.JDSkuF;
import com.yumu.hexie.model.jingdong.getskuid.JDSkuID;
import com.yumu.hexie.model.jingdong.getskuid.detail.JDSkuIDF;
import com.yumu.hexie.model.jingdong.getskuid.image.JDSkuIdImageF;
import com.yumu.hexie.model.jingdong.getskuid.price.PriceF;
import com.yumu.hexie.model.jingdong.getskuid.status.JDSkuIDStatusF;
import com.yumu.hexie.model.jingdong.getstock.SkuNums;
import com.yumu.hexie.model.jingdong.getstock.Stock;
import com.yumu.hexie.model.jingdong.getstock.StockF;
import com.yumu.hexie.model.jingdong.gettype.Classification;
import com.yumu.hexie.model.jingdong.limitregion.JDRegion;
import com.yumu.hexie.model.jingdong.limitregion.JDRegionF;
import com.yumu.hexie.model.jingdong.token.JDToken;
import com.yumu.hexie.model.jingdong.token.JDTokenF;

public interface JDService {
	JDSecurity getTokenSafeCode(JDLoad load);//获取安全码
	
	JDTokenF getApiToken(JDToken token);//获取token
	
	JDSkuF getSku(JDSku sku);//获取所有sku
	
	JDSkuIDF skuDetail(JDSkuID skuid);//根据商品id获取详细信息
	
	JDSkuIDStatusF skuState(JDSkuID skuid);//获取商品上下架状态
	
	JDSkuIdImageF skuImage(JDSkuID skuid);//获取所有图片信息 skus最多100个  用,隔开
	
	String ClassificationC(Classification cic);//获取分类列表
	
	JDAddressF GetAdress(JDAddress addre);//获取下级地址列表
	
	StockF GetNewStockById(Stock sto);//批量获取库存接口 
	
	JDRegionF CheckAreaLimit(JDRegion region);//商品购买区域限制查询
	
	PriceF getPrice(JDSkuID sku);//批量查询协议价价格
	
}
