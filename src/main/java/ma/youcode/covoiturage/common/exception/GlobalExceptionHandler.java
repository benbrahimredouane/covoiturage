package ma.youcode.covoiturage.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

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


}
