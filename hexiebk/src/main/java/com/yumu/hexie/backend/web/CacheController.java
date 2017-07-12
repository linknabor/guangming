package com.yumu.hexie.backend.web;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yumu.hexie.backend.web.dto.BaseResult;
import com.yumu.hexie.service.SharedSysConfigService;

@Controller
public class CacheController extends BaseController{

	@Inject
	private SharedSysConfigService sharedSysConfigService;
	
	@RequestMapping(value = "/refreshCache/{bindAppId}/{key}", method = RequestMethod.GET)
	@ResponseBody
    public BaseResult<String> refreshCache(@PathVariable String bindAppId, @PathVariable String key) throws Exception {
		
		sharedSysConfigService.updateCaches(bindAppId, key);
        return BaseResult.successResult("测试成功");
    }
	
	
}
