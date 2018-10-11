package com.gwg.demo.controller;

import com.gwg.demo.common.Result;
import com.gwg.demo.config.exception.Update;
import com.gwg.demo.domain.Hello;
import com.gwg.demo.service.HelloService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@RestController
@Slf4j /*lombok里面的东西*/
@Validated
@RequestMapping("/hello")
public class HelloController {


	/**
	 * 测试服务是否通
	 * @return
	 */
	@RequestMapping(value = "/ok", method = RequestMethod.GET)
	public @ResponseBody String ok(){
		return "ok";
	}


	/**
     * 校验bean-可能会抛出MethodArgumentNotValidException
     * @valid 标识需要校验实体hello
     * @RequestBody 必须，否则无法接受前端json
     */
    @RequestMapping(value = "/queryHelloInfo", method = RequestMethod.POST)
    public @ResponseBody Result queryHelloInfo(@Valid @RequestBody Hello hello){
        log.info("queryHelloInfo..................");
        return Result.success(null);
    }

    /**
     * 校验bean-可能会抛出MethodArgumentNotValidException
     * @Validated 标识需要校验实体hello,Validated注解的功能比Valid注解更强大，Validated支持分组，只校验指定分组的参数
     * @RequestBody 必须，否则无法接受前端json
     *
     * 在这里Validated没有指定分组，因此只校验没有分组的参数
     */
    @RequestMapping(value = "/queryHelloInfo2", method = RequestMethod.POST)
    public @ResponseBody Result queryHelloInfo2(@Validated @RequestBody Hello hello){
        log.info("queryHelloInfo..................");
        return Result.success(null);
    }

    /**
     * 校验bean-可能会抛出MethodArgumentNotValidException
     * Update.calss 分组定义必须是接口类型,在这里指定只校验分组为Update的参数
     * @Validated 标识需要校验实体hello,Validated注解的功能比Valid注解更强大，Validated支持分组，只校验指定分组的参数
     * @RequestBody 必须，否则无法接受前端json
     */
    @RequestMapping(value = "/queryHelloInfo3", method = RequestMethod.POST)
    public @ResponseBody Result queryHelloInfo3(@Validated({Update.class}) @RequestBody Hello hello){
        log.info("queryHelloInfo..................");
        return Result.success(null);
    }



    /**
     * 校验单个参数--可能会有约束异常ConstraintViolationException
     * @Length 该注解必须与@Validated配合使用，否则不生效
     * @return
     */
    @RequestMapping(value = "/queryHelloInfoByName", method = RequestMethod.POST)
    public @ResponseBody Result hello(@Length(min = 2, message = "名字参数长度必须大于2")  String name){
        log.info("queryHelloInfoByName..................");
        return Result.success(null);
    }

    /**
     * 校验单个参数--可能会有约束异常ConstraintViolationException
     * @Length 该注解必须与@Validated配合使用，否则不生效
     * @return
     */
    @RequestMapping(value = "/queryHelloInfoByName2", method = RequestMethod.POST)
    public @ResponseBody Result queryHelloInfoByName2(@NotNull String name){
        log.info("queryHelloInfoByName2..................");
        return Result.success(null);
    }

    /**
     * 校验单个参数--可能会有约束异常ConstraintViolationException
     * @Length 该注解必须与@Validated配合使用，否则不生效
     * @return
     */
    @RequestMapping(value = "/queryHelloInfoByName3", method = RequestMethod.POST)
    public @ResponseBody Result queryHelloInfoByName3(@NotEmpty(message="查询参数不能为空") @Size(min=2,max=5, message="名字长度必须是2到5个字符") String name){
        log.info("queryHelloInfoByName3..................");
        return Result.success(null);
    }


    /**
     * 未知异常--抛出异常ArithmeticException，
     */
    @RequestMapping(value = "/unknownException", method = RequestMethod.GET)
    public @ResponseBody Result unknownException(){
        log.info("unknownException..................");
        int i = 1 / 0;
        return Result.success(null);
    }

    /**
     * 未知异常--抛出异常ArithmeticException，被全局异常处理器@ExceptionHandler(value = Exception.class)捕获
     */
    @RequestMapping(value = "/unknownException2", method = RequestMethod.GET)
    public @ResponseBody Result unknownException2(){
        log.info("unknownException2..................");
        HelloService helloService = new HelloService();
        helloService.unknownException();
        return Result.success(null);
    }






}
