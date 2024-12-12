package io.valentinsoare.bloggingengineapi.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class BeforeMethodExecution {

    @Before("execution(* io.valentinsoare.bloggingengineapi.service.AuthorService.createAuthor(..))")
    public void beforeCreateAuthor() {
        log.info("Creating author.");
    }
}
