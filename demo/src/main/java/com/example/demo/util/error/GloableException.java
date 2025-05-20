package com.example.demo.util.error;



import com.example.demo.dto.response.RestResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice //@ControllerAdvice + @ReponseBody
public class GloableException {
//    handle all exception
    @ExceptionHandler(Exception.class)
    public ResponseEntity<RestResponse<Object>> handleAllException(Exception ex) {
        RestResponse<Object> res = new RestResponse<Object>();
        res.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        res.setMessage(ex.getMessage());
        res.setError("Internal Server Error");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
    }
    @ExceptionHandler(value = {
                                UsernameNotFoundException.class,
                                BadCredentialsException.class,
    IdInvalidException.class,})
    public  ResponseEntity<RestResponse<Object>> handleIdInvalidException(Exception ex) {
      RestResponse<Object> restResponse = new RestResponse<Object>();
      restResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
      restResponse.setMessage(ex.getMessage());
      restResponse.setError("Exception occrus..");
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(restResponse);
    }
    @ExceptionHandler(value = {
            NoResourceFoundException.class,
    })
    public ResponseEntity<RestResponse<Object>> handleNotFoundException(Exception ex) {
        RestResponse<Object> res = new RestResponse<>();
        res.setStatusCode(HttpStatus.NOT_FOUND.value());
        res.setError("404 Not Found. URL may be not exit");
        res.setMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RestResponse<Object>> validationError(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        final List<FieldError> fileErrors = result.getFieldErrors();

        RestResponse<Object> res = new RestResponse<Object>();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setError(ex.getBody().getDetail());

        List<String> errors = fileErrors.stream().map(f->f.getDefaultMessage()).collect(Collectors.toList());
        res.setMessage(errors.size() > 1 ? errors : errors.get(0));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }
// file
    @ExceptionHandler(value = {
            StorageException.class})
    public  ResponseEntity<RestResponse<Object>>handleFileUploadException(Exception ex) {
        RestResponse<Object> restResponse = new RestResponse<Object>();
        restResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
        restResponse.setError("Exception upload");
        restResponse.setMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(restResponse);
    }
    @ExceptionHandler(value = {
            PermissionExceeption.class})
    public  ResponseEntity<RestResponse<Object>>handlePermissionException(Exception ex) {
        RestResponse<Object> restResponse = new RestResponse<Object>();
        restResponse.setStatusCode(HttpStatus.FORBIDDEN.value());
        restResponse.setMessage(ex.getMessage());
        restResponse.setError("Forbidden ");

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(restResponse);
    }
}
