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

    @Before("io.valentinsoare.bloggingengineapi.logging.aop.AopMapping.methodsExecutionOnAllLayers()")
    public void loggingBeforeControllerLayer(JoinPoint joinPoint) {
        logBeforeMethodExecution(joinPoint);
    }

    private <T> void logBeforeMethodExecution(JoinPoint joinPoint) {
        SourceLocation sourceLocation = joinPoint.getSourceLocation();

        String methodName = joinPoint.getSignature().getName();
        String className = sourceLocation.getWithinType().toString();
        Object[] methodArguments = joinPoint.getArgs();

        log.info("{} -> executing method {} with arguments {}",
                className, methodName, Arrays.toString(methodArguments));
    }
}
