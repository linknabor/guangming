package com.yumu.hexie.backend.web.common;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

/**
 * Created by Administrator on 2015/4/10.
 */
public class BaseController {
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(
				new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"), true));
		binder.registerCustomEditor(int.class, new CustomNumberEditor(
				Integer.class, true));
		binder.registerCustomEditor(long.class, new CustomNumberEditor(
				Long.class, true));
		binder.registerCustomEditor(double.class, new CustomNumberEditor(
				Double.class, true));
		binder.registerCustomEditor(float.class, new CustomNumberEditor(
				Float.class, true));
	}
}
