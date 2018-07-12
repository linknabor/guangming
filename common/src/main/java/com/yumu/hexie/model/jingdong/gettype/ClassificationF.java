package com.yumu.hexie.model.jingdong.gettype;

public class ClassificationF {
	private String resultCode;//响应状态码
	private String resultMessage;//resultCode的说明
	private ClassificationResult result;
	
	
	public String getResultCode() {
		return resultCode;
	}
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	public String getResultMessage() {
		return resultMessage;
	}
	public void setResultMessage(String resultMessage) {
		this.resultMessage = resultMessage;
	}
	public ClassificationResult getResult() {
		return result;
	}
	public void setResult(ClassificationResult result) {
		this.result = result;
	}
	
	
}
