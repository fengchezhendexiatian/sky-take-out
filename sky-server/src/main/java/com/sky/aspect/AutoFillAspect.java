package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MemberSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * 自定义切面，实现公共字段自动填充处理逻辑
 */
@Aspect //声明一个类是一个切面
@Component //于将类标识为组件的注解。组件是Spring中的基本构建块，用于实现松耦合和模块化开发。通过@Component注解，Spring容器会自动扫描并将标记为组件的类实例化，并将其纳入到应用程序的上下文中，以便后续进行依赖注入、AOP等操作。
@Slf4j //自动生成一个名为log的类成员变量，用于日志输出，可以通过该成员变量直接进行日志记录而不需要手动创建Logger对象
public class AutoFillAspect {

    /**
     * 切入点
     */
    //* 所有的  ，mapper.*.*(..)匹配所有的类，方法以及方法中的参数，Pointcut是一个用于定义切点的注解
    //拦截的方法中应包括加入autofill注解
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void autoFillPointCut(){}


    /**
     * 前置通知，在通知中进行公共字段的赋值
     */

    @Before("autoFillPointCut()")
    public  void autoFill(JoinPoint joinPoint) {
        ////////重要////////////
        //可进行调试，是否能进入该方法，提前在mapper方法添加AutoFill注解
        log.info("开始进行公共字段自动填充...");


        //获取到当前被拦截的方法上的数据库操作类型
        //在AspectJ或Spring AOP中，使用 joinPoint.getSignature() 获取连接点的签名信息时，通常会选择具体的签名类型，如 MethodSignature、ConstructorSignature 等，而不是更通用的 Signature 接口。这是因为不同类型的连接点可能具有不同的签名信息，而具体的签名类型提供了更丰富的方法来访问这些信息。
        //反射操作，如下
        MethodSignature signature = (MethodSignature) joinPoint.getSignature(); //方法签名对象   Signature转到membersignature，接口转型
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);//获得方法上的注解对象
        OperationType operationType = autoFill.value(); //获得数据库操作类型

        //获取到当前被拦截的方法的参数-- 实体对象
        Object[] args = joinPoint.getArgs();
        if (args == null || args.length == 0) {
            return;
        }

        Object entity = args[0];//这里定义对象得是Object，不能是Employee，可能还有其他实体对象例如Category...

        //准备赋值的数据，针对当前Time,User..CREATE_USER(thread_local)
        LocalDateTime now = LocalDateTime.now();
        Long currentId = BaseContext.getCurrentId();

        //根据当前不同的操作类型，为对应的属性通过反射来赋值
        if (operationType == OperationType.INSERT) {
            //为4个公共字段赋值
            try {
                Method setCreateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                Method setCreateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

                //通过反射为对象属性赋值
                setCreateTime.invoke(entity, now);
                setCreateUser.invoke(entity, currentId);
                setUpdateTime.invoke(entity, now);
                setUpdateUser.invoke(entity, currentId);
            } catch (
                    Exception e) { //NoSuchMethodException 是一种特定的异常，用于表示在使用反射机制时尝试访问不存在方法时的情况；而 Exception 则是所有异常的通用父类，用于捕获各种类型的异常
                e.printStackTrace();
//                throw new RuntimeException(e);
            }


        } else if (operationType == OperationType.UPDATE) {
            //为2个公共字段赋值
            try {
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

                //通过反射为对象属性赋值

                setUpdateTime.invoke(entity, now);
                setUpdateUser.invoke(entity, currentId);
            } catch (
                    Exception e) { //NoSuchMethodException 是一种特定的异常，用于表示在使用反射机制时尝试访问不存在方法时的情况；而 Exception 则是所有异常的通用父类，用于捕获各种类型的异常
                e.printStackTrace();
//                throw new RuntimeException(e);
            }
        }
    }
}
