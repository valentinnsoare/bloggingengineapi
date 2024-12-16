package io.valentinsoare.bloggingengineapi.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.SourceLocation;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Aspect
@Order(1)
@Component
public class BeforeLoggingAspect {

    @Before("io.valentinsoare.bloggingengineapi.logging.aop.AopMapping.methodsExecutionOnSecurityLayer()")
    public void loggingBeforeSecurityLayer(JoinPoint joinPoint) {
        logBeforeMethodExecution("Security layer", joinPoint);
    }

    @Before("io.valentinsoare.bloggingengineapi.logging.aop.AopMapping.methodsExecutionOnControllerLayer()")
    public void loggingBeforeControllerLayer(JoinPoint joinPoint) {
        logBeforeMethodExecution("Controller layer", joinPoint);
    }

    @Before("io.valentinsoare.bloggingengineapi.logging.aop.AopMapping.methodsExecutionOnServiceLayer()")
    public void loggingBeforeServiceLayer(JoinPoint joinPoint) {
        logBeforeMethodExecution("Service layer", joinPoint);
    }

    @Before("io.valentinsoare.bloggingengineapi.logging.aop.AopMapping.methodsExecutionOnRepositoryLayer()")
    public void loggingBeforeRepositoryLayer(JoinPoint joinPoint) {
        logBeforeMethodExecution("Repository layer", joinPoint);
    }

    private <T> void logBeforeMethodExecution(String layer, JoinPoint joinPoint) {
        SourceLocation sourceLocation = joinPoint.getSourceLocation();

        String methodName = joinPoint.getSignature().getName();
        String className = sourceLocation.getWithinType().toString();
        Object[] methodArguments = joinPoint.getArgs();

        log.info("{} -> executing method {} from {} with arguments {}",
                layer, methodName, className, Arrays.toString(methodArguments));
    }
}
