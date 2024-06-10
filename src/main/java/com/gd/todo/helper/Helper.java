package com.gd.todo.helper;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

@Component
@Aspect
@EnableAspectJAutoProxy
public class Helper {

//    @Before("execution(public org.springframework.http.ResponseEntity<java.util.List<com.gd.todo.model.Todo>> getAllTodo())")
//    public void logs() {
//        System.out.println("Log is Called");
//    }

    @Around("loggableMethod()")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        Object proceed = joinPoint.proceed();

        long executionTime = System.currentTimeMillis() - start;

        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();

        System.out.println("Executing " + className + "." + methodName + " took " + executionTime + "ms");

        return proceed;
    }

    @Pointcut("execution(public * com.gd.todo.*.*.*(..))")
    public void loggableMethod() {}
//
//    @Before("execution(public * com.gd.todo.controller.TodoController.addTodo(..))")
//    public void postArgLogs(JoinPoint jp){
//        String args = jp.getArgs()[0].toString();
//        System.out.println(args);
//    }

//    @Pointcut("execution(public * com.gd.todo.controller.TodoController.getAllTodo())")
//    public void afterReturningPointCut(){}
//
//    @AfterReturning(pointcut = "afterReturningPointCut()",returning = "responseEntity")
//    public void returnLogs(ResponseEntity<List<Todo>> responseEntity){
//        List<Todo> todos = responseEntity.getBody();
//        if (todos != null) {
//            todos.forEach(todo -> System.out.println(todo.toString()));
//        } else {
//            System.out.println("No todos found");
//        }
//    }

}
