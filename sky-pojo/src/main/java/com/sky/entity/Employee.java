package com.sky.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data // Lombok 提供的一个组合注解，它会在编译时自动生成类中的一些通用方法，如 toString()、equals()、hashCode()、getter 和 setter 方法等
@Builder //用于为类生成一个建造者模式的构建器。通过在类上添加 @Builder 注解，Lombok会为类生成一个静态内部类，该类包含了针对类的各个字段的 setter 方法，并且可以链式调用。这使得可以更方便地创建对象，特别是在需要大量参数的情况下。
@NoArgsConstructor //自动生成一个无参的构造方法。这在使用一些框架或者序列化时特别有用，因为有些框架或者序列化库需要一个无参构造方法来创建对象。
@AllArgsConstructor //会自动生成一个包含所有字段的构造方法。这个构造方法用于方便地为类的所有字段赋值，尤其是在需要在对象创建时初始化所有字段时非常有用。
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String username;

    private String name;

    private String password;

    private String phone;

    private String sex;

    private String idNumber;

    private Integer status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    private Long createUser;

    private Long updateUser;

}
