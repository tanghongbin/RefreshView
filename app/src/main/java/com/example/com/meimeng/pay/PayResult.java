package com.example.com.meimeng.pay;

import android.text.TextUtils;
import android.util.Log;

public class PayResult {
	private String resultStatus;
	private String result;
	private String memo;

	public PayResult(String rawResult) {

		if (TextUtils.isEmpty(rawResult))
			return;

		String[] resultParams = rawResult.split(";");
		for (String resultParam : resultParams) {
			if (resultParam.startsWith("resultStatus")) {
				resultStatus = gatValue(resultParam, "resultStatus");
			}
			if (resultParam.startsWith("result")) {
				result = gatValue(resultParam, "result");
			}
			if (resultParam.startsWith("memo")) {
				memo = gatValue(resultParam, "memo");
			}
		}
	}

	@Override
	public String toString() {
		return "resultStatus={" + resultStatus + "};memo={" + memo
				+ "};result={" + result + "}";
	}

	private String gatValue(String content, String key) {
		String prefix = key + "={";
		return content.substring(content.indexOf(prefix) + prefix.length(),
				content.lastIndexOf("}"));
	}

	/**
	 * @return the resultStatus
	 */
	public String getResultStatus() {
		return resultStatus;
	}

	/**
	 * @return the memo
	 */
	public String getMemo() {
		return memo;
	}

	/**
	 * @return the result
	 */
	public String getResult() {
		return result;
	}

	/**
	 * 得到订单号
	 * @return 类型是字符串
	 */
	public String getOrderInfo(){
		String[] resultParams = result.split("&");
		for (String resultParam : resultParams) {
			if (resultParam.startsWith("out_trade_no")) {
				String orderNum = getOrderInfoNum(resultParam, "out_trade_no");
				Log.e("订单号：", orderNum);

				return orderNum;
			}

		}
		return "";
	}

	/**
	 * 得到订单号
	 * @param content
	 * @param key
	 * @return
	 */
	private String getOrderInfoNum(String content,String key){
		String prefix = key + "=\"";
		return content.substring(content.indexOf(prefix) + prefix.length(),
				content.lastIndexOf("\""));
	}
}
