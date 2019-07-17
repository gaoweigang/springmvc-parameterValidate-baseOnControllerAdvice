package com.gwg.demo.config.exception;

import com.gwg.demo.common.Result;
import com.gwg.demo.common.ReturnCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.naming.SizeLimitExceededException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;
import java.util.logging.LogManager;

/**
 * 全局异常处理
 * 参考：https://www.cnblogs.com/beiyan/p/5946345.html
 */
@Slf4j
@ControllerAdvice
public class ExceptionHandlerAdvice extends ResponseEntityExceptionHandler {//也可以继承ResponseEntityExceptionHandler，根据不同异常类型，返回不同的status

	@Bean
	public MethodValidationPostProcessor methodValidationPostProcessor() {
		return new MethodValidationPostProcessor();
	}


	/**
	 * 校验参数
	 *
	 * 捕捉@Valid 校验注解抛出的异常
	 * 当带有@Valid注解的参数验证失败时，将抛出异常MethodArgumentNotValidException
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(value = HttpStatus.OK) //响应200
	@ResponseBody
	public Result<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex,
			WebRequest request) {
		log.error("WebRequest:" + request.toString());
		log.error("handleMethodArgumentNotValidException", ex);
		BindingResult bindingResult = ex.getBindingResult();
		String errorMesssage = "Invalid Request:";

		for (FieldError fieldError : bindingResult.getFieldErrors()) {
			errorMesssage += "[" + fieldError.getField() + ":" + fieldError.getDefaultMessage() + "];";
		}
		Result<?> result = Result.error(ReturnCode.PARAMETER_IS_EMPTY.getCode(), errorMesssage);
		result.setStatus(false);
		return result;
	}

	/**
	 * 校验参数
	 *
	 * Spring validator 方法级别的校验:
	 * JSR和Hibernate validator的校验只能对Object的属性进行校验，不能对单个的参数进行校验，spring 在此基础上进行了扩展，
	 * 添加了MethodValidationPostProcessor拦截器，可以实现对方法参数的校验，如果异常，则会抛出ConstraintViolationException
	 *
	 * 约束异常 ConstraintViolationException
	 * 参考：https://www.cnblogs.com/beiyan/p/5946345.html
	 */
	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(value = HttpStatus.OK) //响应200
	@ResponseBody
	public Result<?> handleMethodArgumentNotValidException2(ConstraintViolationException ex, WebRequest request) {
		log.error("WebRequest:" + request.toString());
		log.error("handleMethodArgumentNotValidException2", ex);
		Set<ConstraintViolation<?>> bindingResult = ex.getConstraintViolations();
		String errorMesssage = "Invalid Request:\n";

		for (ConstraintViolation<?> fieldError : bindingResult) {
			errorMesssage += fieldError.getMessage() + "\n";
		}
		Result<?> result = Result.error(ReturnCode.PARAMETER_IS_EMPTY.getCode(), errorMesssage);
		result.setStatus(false);
		return result;
	}

	/**
	 * Spring上传文件异常处理
	 * FileUploadBase$SizeLimitExceededException: the request was rejected because its size (17869943) exceeds the configured maximum (10485760)
	 */
	@ExceptionHandler({ SizeLimitExceededException.class, MultipartException.class })
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	public Result<?> handleSizeLimitExceededException(Exception ex, WebRequest request) {
		log.error("WebRequest:" + request.toString());
		log.error("handleSizeLimitExceededException", ex);
		Result<?> result = Result.error(ReturnCode.PARAMETER_IS_EMPTY.getCode(), ex.getMessage());
		result.setStatus(false);
		return result;
	}

	/**
	 * AssertException 断言异常处理
	 * @param request
	 */
	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	public Result<?> handleIllegalArgumentException(IllegalArgumentException ex,WebRequest request) {
		log.error("WebRequest:" + request.toString());
		log.error("handleIllegalArgumentException", ex);
		Result<?> result = Result.error(ReturnCode.PARAMETER_IS_EMPTY.getCode(), ex.getMessage());
		result.setStatus(false);
		return result;
	}


	/**
	 *  处理全局剩余未知异常-eg:ArithmeticException
	 *  当然我们也可以针对每一种具体异常定义一个异常处理器，比如@ExceptionHandler(ArithmeticException.class)来专门处理ArithmeticException异常
	 *
	 *  声明了对 Exception 异常的处理，起到兜底作用，不管 Controller 层执行的代码出现了什么未能考虑到的异常，
	 *  都返回统一的错误提示给客户端。
	 */
	@ExceptionHandler(value = Exception.class) // 处理Exception异常，优先级靠后，因为会先精确匹配
	@ResponseStatus(value = HttpStatus.OK) //响应200
	@ResponseBody
	public Result<?> exception(Exception e, WebRequest request) {
		Result<?> result = Result.error(ReturnCode.ERROR.getCode(), e.getMessage());
		result.setStatus(false);
		log.error("WebRequest:" + request.toString());
		log.error("后台出错", e);
		return result;
	}

}
