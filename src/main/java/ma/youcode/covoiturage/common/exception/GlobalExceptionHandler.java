package ma.youcode.covoiturage.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    private ProblemDetail createProblem(HttpStatus status , String title , String detail, HttpServletRequest req){
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, detail);
        problem.setTitle(title);
        problem.setInstance(URI.create(req.getRequestURI()));
        return problem;
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException exception , HttpServletRequest req){
        ProblemDetail problem = createProblem(
                HttpStatus.BAD_REQUEST,
                "Validation failed",
                "One or more feilds are not valide ",
                req);
        Map<String,String> errors = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(er ->
                errors.put(er.getField(),er.getDefaultMessage())
                );
        problem.setProperty("errors",errors);
        return problem;
    }

    @ExceptionHandler(MatriculeNotUniqueException.class)
    public ProblemDetail handleUniqueException(MatriculeNotUniqueException exception , HttpServletRequest req){
        ProblemDetail problemDetail = createProblem(
                HttpStatus.CONFLICT,
                "unique matricule",
                exception.getMessage(),
                req
        );
        return problemDetail;
    }

    @ExceptionHandler(NotYourVehicleException.class)
    public ProblemDetail handleNotYourVehicleException(NotYourVehicleException exception , HttpServletRequest req){
        ProblemDetail problemDetail = createProblem(
                HttpStatus.FORBIDDEN,
                "not authorized",
                exception.getMessage(),
                req
        );
        return problemDetail;
    }


    @ExceptionHandler(InvalidSegmentException.class)
    public ProblemDetail handleInvalidSegmentException(InvalidSegmentException exception , HttpServletRequest req){
        log.warn("rule refused : {}", exception.getMessage());
        return createProblem(
                HttpStatus.BAD_REQUEST,
                "invalid segment",
                exception.getMessage(),
                req
        );
    }

    // ---------- 403
    @ExceptionHandler(ForbiddenActionException.class)
    public ProblemDetail handleForbiddenActionException(ForbiddenActionException exception , HttpServletRequest req){
        log.warn("action refused : {}", exception.getMessage());
        return createProblem(
                HttpStatus.FORBIDDEN,
                "forbidden-action",
                exception.getMessage(),
                req
        );
    }
    // ---------- 404
    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleResourceNotFoundException(ResourceNotFoundException exception , HttpServletRequest req){
        return createProblem(
                HttpStatus.NOT_FOUND,
                "resource-not-found",
                exception.getMessage(),
                req
        );
    }
    // ---------- 409
    @ExceptionHandler(BusinessRuleException.class)
    public ProblemDetail handleBusinessRuleException(BusinessRuleException exception , HttpServletRequest req){
        log.warn("rule refused : {}", exception.getMessage());
        return createProblem(
                HttpStatus.CONFLICT,
                "business-rule-violated",
                exception.getMessage(),
                req
        );
    }
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail handleDataIntegrityViolationException(DataIntegrityViolationException exception , HttpServletRequest req){
        log.warn("rule refused : data integrity violation on {}", req.getRequestURI());
        return createProblem(
                HttpStatus.CONFLICT,
                "data-conflict",
                "A value that must be unique (email, license number, plate number ...) already exists",
                req
        );
    }

    // ---------- 500 (and the errors of Spring itself : unknown url 404, wrong method 405 ...)
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleOthers(Exception exception , HttpServletRequest req){
        if (exception instanceof ErrorResponse errorResponse){
            HttpStatus status = HttpStatus.valueOf(errorResponse.getStatusCode().value());
            String message = Objects.requireNonNullElse(errorResponse.getBody().getDetail(), status.getReasonPhrase());
            return createProblem(status, "request-error", message, req);
        }
        log.error("unexpected error on {}", req.getRequestURI(), exception);
        return createProblem(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "internal-error",
                "An unexpected error occurred",
                req
        );
    }


}
