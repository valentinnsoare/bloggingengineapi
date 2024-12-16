package io.valentinsoare.bloggingengineapi.logging.aop;

import org.aspectj.lang.annotation.Pointcut;

public class AopMapping {
    @Pointcut("execution(* io.valentinsoare.bloggingengineapi.controller.*.*(..))")
    public void methodsExecutionOnControllerLayer() {}

    @Pointcut("execution(* io.valentinsoare.bloggingengineapi.service.*.*(..))")
    public void methodsExecutionOnServiceLayer() {}

    @Pointcut("execution(* io.valentinsoare.bloggingengineapi.repository.*.*(..))")
    public void methodsExecutionOnRepositoryLayer() {}

    @Pointcut("methodsExecutionOnControllerLayer() || methodsExecutionOnServiceLayer() || methodsExecutionOnRepositoryLayer()")
    public void methodsExecutionOnAllLayers() {}
}
