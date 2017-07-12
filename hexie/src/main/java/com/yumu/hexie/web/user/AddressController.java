package com.yumu.hexie.web.user;

import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yumu.hexie.common.Constants;
import com.yumu.hexie.model.user.Address;
import com.yumu.hexie.model.user.User;
import com.yumu.hexie.model.user.Xiaoqu;
import com.yumu.hexie.service.user.AddressService;
import com.yumu.hexie.service.user.PointService;
import com.yumu.hexie.service.user.UserService;
import com.yumu.hexie.service.user.req.AddressReq;
import com.yumu.hexie.web.BaseController;
import com.yumu.hexie.web.BaseResult;

@Controller(value = "addressController")
public class AddressController extends BaseController{

	@Inject
	private AddressService addressService;
	@Inject
	private PointService pointService;
	@Inject
	private UserService userService;


    @RequestMapping(value = "/saveAddressWithXiaoqu", method = RequestMethod.POST)
    @ResponseBody
    public BaseResult<Address> saveWithXiaoqu(HttpSession session,@ModelAttribute(Constants.USER)User user,@RequestBody AddressReq req) throws Exception {
        Address address = addressService.saveAddress(req, user);
        pointService.addZhima(user, 50, "zhima-address-"+user.getId()+"-"+address.getId());
        if(user.getCurrentAddrId() == 0) {
            session.setAttribute(Constants.USER, userService.getById(user.getId()));
        }
        return new BaseResult<Address>().success(address);
    }
    @RequestMapping(value = "/queryXiaoqus", method = RequestMethod.POST)
    @ResponseBody
    public BaseResult<List<Xiaoqu>> queryXiaoqus(){
        return new BaseResult<List<Xiaoqu>>().success(addressService.queryXiaoqu());
    }
    
    
	@RequestMapping(value = "/address/delete/{addressId}", method = RequestMethod.POST)
	@ResponseBody
    public BaseResult<String> deleteAddress(@ModelAttribute(Constants.USER)User user,@PathVariable long addressId) throws Exception {
		addressService.deleteAddress(addressId, user.getId());
        return BaseResult.successResult("删除地址成功");
    }

	@RequestMapping(value = "/address/query/{addressId}", method = RequestMethod.GET)
	@ResponseBody
	public BaseResult<Address> queryAddressById(@ModelAttribute(Constants.USER)User user,@PathVariable long addressId) throws Exception {
		return BaseResult.successResult(addressService.queryAddressById(addressId));
	}

	@RequestMapping(value = "/address/default/{addressId}", method = RequestMethod.POST)
	@ResponseBody
    public BaseResult<String> defaultAddress(HttpSession session,@ModelAttribute(Constants.USER)User user,@PathVariable long addressId) throws Exception {
		boolean r = addressService.configDefaultAddress(user, addressId);
        if(!r) {
        	BaseResult.fail("设置默认地址失败！");
        }
        session.setAttribute(Constants.USER, user);
		return BaseResult.successResult("设置默认地址成功");
    }

	@RequestMapping(value = "/addresses", method = RequestMethod.GET)
	@ResponseBody
    public BaseResult<List<Address>> queryAddressList(@ModelAttribute(Constants.USER)User user) throws Exception {
		List<Address> addresses = addressService.queryAddressByUser(user.getId());
		BaseResult<List<Address>> r = BaseResult.successResult(addresses);
		return r;
    }
	
//	@RequestMapping(value = "/addAddress", method = RequestMethod.POST)
//	@ResponseBody
//    public BaseResult<Address> save(HttpSession session,@ModelAttribute(Constants.USER)User user,@RequestBody Address address) throws Exception {
//		if(StringUtil.isEmpty(address.getCity()) || StringUtil.isEmpty(address.getProvince()) || StringUtil.isEmpty(address.getCounty())){
//			return BaseResult.fail("请重新选择所在区域");
//		}
//		if(StringUtil.isEmpty(address.getXiaoquName()) || StringUtil.isEmpty(address.getDetailAddress())){
//			return BaseResult.fail("请重新填写小区和详细地址");
//		}
//		if (StringUtil.isEmpty(address.getReceiveName()) || StringUtil.isEmpty(address.getTel())) {
//			return BaseResult.fail("请检查真实姓名和手机号码是否正确");
//		}
//		address.setUserId(user.getId());
//		address = addressService.addAddress(address);
//		//本方法内调用无法异步
//		addressService.fillAmapInfo(address);
//		if(user.getCurrentAddrId() == 0||addressService.queryAddressByUser(user.getId()).size()==1) {
//			addressService.configDefaultAddress(user, address.getId());
//			session.setAttribute(Constants.USER, user);
//		}
//		pointService.addZhima(user, 50, "zhima-address-"+user.getId()+"-"+address.getId());
//		BaseResult<Address> r = (BaseResult<Address>)BaseResult.successResult(address);
//		return r;
//    }
//	@RequestMapping(value = "/regions/{type}/{parentId}", method = RequestMethod.GET)
//	@ResponseBody
//	public BaseResult<List<Region>> queryRegions(@PathVariable int type,@PathVariable long parentId){
//		List<Region> regions = addressService.queryRegions(type, parentId);
//		return BaseResult.successResult(regions);
//	}
//	
//	//add by zhangxiaonan for amap
//	@RequestMapping(value = "/amap/{city}/{keyword}", method = RequestMethod.GET)
//	@ResponseBody
//	public BaseResult<List<AmapAddress>> queryAmapYuntuLocal(@PathVariable String city,@PathVariable String keyword){
//		return BaseResult.successResult(addressService.queryAmapYuntuLocal(city, keyword));
//	}
//	
//	@RequestMapping(value = "/amap/{longitude}/{latitude}/around/", method = RequestMethod.GET)
//	@ResponseBody
//	public BaseResult<List<AmapAddress>> queryAround(@PathVariable double longitude, @PathVariable double latitude){
//		return BaseResult.successResult(addressService.queryAroundByCoordinate(longitude, latitude));
//	}
}
