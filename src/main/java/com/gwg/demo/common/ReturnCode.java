package com.gwg.demo.common;

import lombok.Getter;
import lombok.Setter;

/**
 * 返回码枚举类
 */
public enum ReturnCode {

	SUCCESS("success", "0000"), //
	PARAMETER_IS_EMPTY("parameter is empty", "9562"), //
	PARAMETER_IS_VALIDATE_ERROR("parameter validate error", "9563"), //
	BUSSINESS_ERROR("业务异常", "8000"), //
	ERROR("something is error....", "9999"),
	PHONE_OR_IDNO_EXIST("此手机号码或身份证已存在", "9600");

	@Setter
	@Getter
	private String message;
	@Setter
	@Getter
	private String code;

	private ReturnCode(String message, String code) {
		this.message = message;
		this.code = code;
	}

	public static String getMessage(String code) {
		for (ReturnCode c : ReturnCode.values()) {
			if (c.getCode().equals(code)) {
				return c.message;
			}
		}
		return null;
	}
}
