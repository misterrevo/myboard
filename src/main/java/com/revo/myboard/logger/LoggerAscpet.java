package com.revo.myboard.logger;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;
import java.util.stream.Stream;

/*
 * Created By Revo
 */

@Aspect
@Component
@Slf4j
public class LoggerAscpet {

    private static final String UNKNOWN_IP = "unknown";
    private static final String ARGUMENT = "Type: %s | Value: %s";
    private static final String BEFORE_MESSAGE = "User with ip: %s invoke method: %s with details: %s";
    private static final String AFTER_THROW_MESSAGE = "User with ip: %s get exception while invoking method: %s with details: %s";
    private static final String TOKEN_PREFIX = "Bearer";
    private static final String PROTECTED = "PROTECTED";

    private final String CONTROLLERS_PACKAGES = "execution(* com.revo.myboard.category.CategoryController.*(..))"
            + " || execution(* com.revo.myboard.comment.CommentController.*(..))"
            + " || execution(* com.revo.myboard.group.GroupController.*(..))"
            + " || execution(* com.revo.myboard.post.PostController.*(..))"
            + " || execution(* com.revo.myboard.report.ReportController.*(..))"
            + " || execution(* com.revo.myboard.section.SectionController.*(..))"
            + " || execution(* com.revo.myboard.security.AuthController.*(..))"
            + " || execution(* com.revo.myboard.user.UserController.*(..))";

    private final String[] HEADERS_TO_TRY = {"X-Forwarded-For", "Proxy-Client-IP", "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR", "HTTP_X_FORWARDED", "HTTP_X_CLUSTER_CLIENT_IP", "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR", "HTTP_FORWARDED", "HTTP_VIA", "REMOTE_ADDR"};

    public String getIp(HttpServletRequest request) {
        return Stream.of(HEADERS_TO_TRY)
                .map(request::getHeader)
                .filter(Objects::nonNull)
                .filter(Predicate.not(String::isEmpty))
                .filter(header -> !Objects.equals(UNKNOWN_IP, header))
                .findFirst()
                .orElse(getRemoteAddr(request)!= null ? getRemoteAddr(request) : UNKNOWN_IP);
    }

    //TEST

    private String getRemoteAddr(HttpServletRequest request){
        return request.getRemoteAddr();
    }

    @Before(CONTROLLERS_PACKAGES)
    public void logBeforeStartProccessingRequest(JoinPoint joinPoint) {
        AtomicReference<String> ip = new AtomicReference<>(UNKNOWN_IP);
        List<String> arguments = new ArrayList<>();
        configureArguments(ip, arguments, joinPoint);
        log.info(BEFORE_MESSAGE.formatted(ip.get(), joinPoint.getSignature().getName(), arguments));
    }

    private void configureArguments(AtomicReference<String> ip, List<String> arguments, JoinPoint joinPoint){
        Stream.of(joinPoint.getArgs()).forEach(arg -> {
            if (arg instanceof HttpServletRequest) {
                ip.set(getIp((HttpServletRequest) arg));
            } else {
                arguments.add(ARGUMENT.formatted(arg.getClass().getTypeName(), arg.toString().startsWith(TOKEN_PREFIX) ? PROTECTED : arg));
            }
        });
    }

    @AfterThrowing(CONTROLLERS_PACKAGES)
    public void logAfterEndProccessingRequest(JoinPoint joinPoint) {
        AtomicReference<String> ip = new AtomicReference<>(UNKNOWN_IP);
        List<String> arguments = new ArrayList<>();
        configureArguments(ip, arguments, joinPoint);
        log.info(AFTER_THROW_MESSAGE.formatted(ip.get(), joinPoint.getSignature().getName(), arguments));
    }
}
