package com.gwg.demo.domain;

import com.gwg.demo.config.exception.Add;
import com.gwg.demo.config.exception.Update;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * Model中添加校验注解
 */
public class Hello {

    @NotNull(message = "id不能为空")
    private Long id;

    /**
     * name不能是null或空，如果只想校验不能是空，则使用@NotNull
     */
    @NotEmpty(message = "名字不能为空", groups = {Update.class})
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 校验age必须大于18
     */
    @Min(value = 18, message = "年龄必须大于18", groups = {Add.class})


    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
