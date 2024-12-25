package io.valentinsoare.bloggingengineapi.logging;

import io.valentinsoare.bloggingengineapi.exception.MethodFailedException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.SourceLocation;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Aspect
@Order(2)
@Component
public class AroundLoggingAspect {

    @Around(value = "io.valentinsoare.bloggingengineapi.logging.aop.AopMapping.methodsExecutionOnAllLayers()")
    public Object loggingAfterMethodExecutionWithAnyArguments(ProceedingJoinPoint joinPoint) throws Throwable {
        return logAroundMethodExecution(joinPoint);
    }

    private <T> Object logAroundMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        SourceLocation sourceLocation = joinPoint.getSourceLocation();

        String methodName = joinPoint.getSignature().getName();
        String className = sourceLocation.getWithinType().toString();
        Object[] methodArguments = joinPoint.getArgs();

        long startTime = System.currentTimeMillis();
        long endTime, executionTime;

        try {
            Object resultOfExecution = joinPoint.proceed();
            endTime = System.currentTimeMillis();
            executionTime = endTime - startTime;

            log.info("{} -> returned method {} with arguments {} and result {}. Execution time: {} ms",
                    className, methodName, Arrays.toString(methodArguments), resultOfExecution, executionTime);

            return resultOfExecution;
        } catch (Exception e) {
            endTime = System.currentTimeMillis();
            executionTime = endTime - startTime;

            String exceptionMessage = String.format("%s -> while executing method %s with arguments %s and exception %s. Execution tome: %s ms",
                    joinPoint.getSourceLocation().getWithinType().getName(), methodName, Arrays.toString(methodArguments), e.getMessage(), executionTime);

            log.error(
                    new MethodFailedException(exceptionMessage).toString()
            );

            throw e;
        }
    }
}
