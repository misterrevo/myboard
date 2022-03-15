package com.revo.myboard.exception;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;

/*
 * Created By Revo
 */

@RestControllerAdvice
@Slf4j
public class RestExceptionHandler {

    private static final String EMAIL_SENDING_EXCEPTION = "Error while sending email, exception message: %s";
    private static final String NULL_POINTER_EXCEPTION = "%s not found!";
    private static final String ILLEGAL_ARGUMENT_EXCEPTION = "%s is illegal argument!";

    @ExceptionHandler(value = MissingRequestHeaderException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String missingRequestHeaderHandler(MissingRequestHeaderException exception) {
        return exception.getMessage();
    }

    @ExceptionHandler(value = JWTDecodeException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String tokenExpiredHandler(JWTDecodeException exception) {
        return exception.getMessage();
    }

    @ExceptionHandler(value = TokenExpiredException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String tokenExpiredHandler(TokenExpiredException exception) {
        return exception.getMessage();
    }

    @ExceptionHandler(value = CategoryNameInUseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String argumentInUseHandler(CategoryNameInUseException exception) {
        return exception.getMessage();
    }

    @ExceptionHandler(value = GroupNameInUseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String argumentInUseHandler(GroupNameInUseException exception) {
        return exception.getMessage();
    }

    @ExceptionHandler(value = SectionNameInUseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String argumentInUseHandler(SectionNameInUseException exception) {
        return exception.getMessage();
    }

    @ExceptionHandler(value = PostNameInUseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String argumentInUseHandler(PostNameInUseException exception) {
        return exception.getMessage();
    }

    @ExceptionHandler(value = HasLikeBeforeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String hasLikeBeforeHandler(HasLikeBeforeException exception) {
        return exception.getMessage();
    }

    @ExceptionHandler(value = UserIsOwnerException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String hasLikeBeforeHandler(UserIsOwnerException exception) {
        return exception.getMessage();
    }

    @ExceptionHandler(value = NoPermissionException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String hasLikeBeforeHandler(NoPermissionException exception) {
        return exception.getMessage();
    }

    @ExceptionHandler(value = SameAccessStatusException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String hasLikeBeforeHandler(SameAccessStatusException exception) {
        return exception.getMessage();
    }

    @ExceptionHandler(value = UserIsActiveException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String hasLikeBeforeHandler(UserIsActiveException exception) {
        return exception.getMessage();
    }

    @ExceptionHandler(value = LoginInUseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String hasLikeBeforeHandler(LoginInUseException exception) {
        return exception.getMessage();
    }

    @ExceptionHandler(value = EmailInUseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String hasLikeBeforeHandler(EmailInUseException exception) {
        return exception.getMessage();
    }

    @ExceptionHandler(value = MatchPasswordException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String hasLikeBeforeHandler(MatchPasswordException exception) {
        return exception.getMessage();
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String illegalArgumentHandler(IllegalArgumentException exception) {
        return ILLEGAL_ARGUMENT_EXCEPTION.formatted(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public String handleValidationExceptions(MethodArgumentNotValidException exception) {
        return exception.getMessage();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = ConstraintViolationException.class)
    public String handleValidationExceptions(ConstraintViolationException exception) {
        return exception.getMessage();
    }

    @ExceptionHandler(value = EmailSendingException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public String emailSendingExceptionHandler(EmailSendingException exception) {
        log.warn(EMAIL_SENDING_EXCEPTION.formatted(exception.getMessage()));
        return exception.getMessage();
    }

    @ExceptionHandler(value = UserNotExistsException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String objectNotExistsHandler(UserNotExistsException exception) {
        return exception.getMessage();
    }

    @ExceptionHandler(value = SectionNotExistsException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String objectNotExistsHandler(SectionNotExistsException exception) {
        return exception.getMessage();
    }

    @ExceptionHandler(value = PostNotExistsException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String objectNotExistsHandler(PostNotExistsException exception) {
        return exception.getMessage();
    }

    @ExceptionHandler(value = ReportNotExistsException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String objectNotExistsHandler(ReportNotExistsException exception) {
        return exception.getMessage();
    }

    @ExceptionHandler(value = CategoryNotExistsException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String objectNotExistsHandler(CategoryNotExistsException exception) {
        return exception.getMessage();
    }

    @ExceptionHandler(value = CommentNotExistsException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String objectNotExistsHandler(CommentNotExistsException exception) {
        return exception.getMessage();
    }

    @ExceptionHandler(value = GroupNotExistsException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String objectNotExistsHandler(GroupNotExistsException exception) {
        return exception.getMessage();
    }

    @ExceptionHandler(value = EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String entityNotFoundHandler(EntityNotFoundException exception) {
        return exception.getMessage();
    }

    @ExceptionHandler(value = NullPointerException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String nullPointerExceptionHandler(NullPointerException exception) {
        return NULL_POINTER_EXCEPTION.formatted(exception.getMessage());
    }

}
