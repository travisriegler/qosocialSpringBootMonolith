package com.qosocial.v1api;

import com.qosocial.v1api.auth.exception.*;
import com.qosocial.v1api.common.dto.ErrorResponseDto;
import com.qosocial.v1api.common.exception.*;
import com.qosocial.v1api.post.exception.*;
import com.qosocial.v1api.profile.exception.*;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.security.authentication.*;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestControllerAdvice
public class GlobalControllerExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalControllerExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        // choosing not to log a validation error
        List<String> errors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> errors.add(error.getDefaultMessage()));
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(errors);
        return new ResponseEntity<>(errorResponseDto, HttpStatus.BAD_REQUEST);
    }


    //--------------------------START AUTH EXCEPTIONS--------------------------------------------
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<Object> emailAlreadyExistsException(EmailAlreadyExistsException ex) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(Collections.singletonList(ex.getMessage()));
        return new ResponseEntity<>(errorResponseDto, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> badCredentialsException(BadCredentialsException ex) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(Collections.singletonList(ex.getMessage()));
        return new ResponseEntity<>(errorResponseDto, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<Object> disabledException(DisabledException ex) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(Collections.singletonList(ex.getMessage()));
        return new ResponseEntity<>(errorResponseDto, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<Object> lockedException(LockedException ex) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(Collections.singletonList(ex.getMessage()));
        return new ResponseEntity<>(errorResponseDto, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(AccountExpiredException.class)
    public ResponseEntity<Object> accountExpiredException(AccountExpiredException ex) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(Collections.singletonList(ex.getMessage()));
        return new ResponseEntity<>(errorResponseDto, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(CredentialsExpiredException.class)
    public ResponseEntity<Object> credentialsExpiredException(CredentialsExpiredException ex) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(Collections.singletonList(ex.getMessage()));
        return new ResponseEntity<>(errorResponseDto, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(GenericRegistrationException.class)
    public ResponseEntity<Object> genericRegistrationException(GenericRegistrationException ex) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(Collections.singletonList(ex.getMessage()));
        return new ResponseEntity<>(errorResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(GenericLoginException.class)
    public ResponseEntity<Object> genericLoginException(GenericLoginException ex) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(Collections.singletonList(ex.getMessage()));
        return new ResponseEntity<>(errorResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidRefreshTokenException.class)
    public ResponseEntity<Object> invalidRefreshTokenException(InvalidRefreshTokenException ex) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(Collections.singletonList(ex.getMessage()));
        return new ResponseEntity<>(errorResponseDto, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(NoRefreshTokenException.class)
    public ResponseEntity<Object> noRefreshTokenException(NoRefreshTokenException ex) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(Collections.singletonList(ex.getMessage()));
        return new ResponseEntity<>(errorResponseDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
    public ResponseEntity<Object> authenticationCredentialsNotFoundException(AuthenticationCredentialsNotFoundException ex) {
        logger.error("The received authentication object was null", ex);
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(Collections.singletonList("Encountered an error with your authentication, please try again"));
        return new ResponseEntity<>(errorResponseDto, HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(InvalidAuthenticationTypeException.class)
    public ResponseEntity<Object> invalidAuthenticationTypeException(InvalidAuthenticationTypeException ex) {
        logger.error("The received authentication object was not an instance of JwtAuthenticationToken", ex);
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(Collections.singletonList("Encountered an error with your authentication, please try again"));
        return new ResponseEntity<>(errorResponseDto, HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(JwtTokenMissingException.class)
    public ResponseEntity<Object> jwtTokenMissingException(JwtTokenMissingException ex) {
        logger.error("The received authentication object contained a null jwt", ex);
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(Collections.singletonList("Encountered an error with your authentication, please try again"));
        return new ResponseEntity<>(errorResponseDto, HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(InvalidJwtIssuerException.class)
    public ResponseEntity<Object> invalidJwtIssuerException(InvalidJwtIssuerException ex) {
        logger.error("The received authentication object contained a jwt with an invalid issuer", ex);
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(Collections.singletonList("Encountered an error with your authentication, please try again"));
        return new ResponseEntity<>(errorResponseDto, HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(InvalidJwtAudienceException.class)
    public ResponseEntity<Object> invalidJwtAudienceException(InvalidJwtAudienceException ex) {
        logger.error("The received authentication object contained a jwt with an invalid audience", ex);
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(Collections.singletonList("Encountered an error with your authentication, please try again"));
        return new ResponseEntity<>(errorResponseDto, HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(InvalidJwtSubjectException.class)
    public ResponseEntity<Object> invalidJwtSubjectException(InvalidJwtSubjectException ex) {
        logger.error("The received authentication object contained a jwt with an invalid subject", ex);
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(Collections.singletonList("Encountered an error with your authentication, please try again"));
        return new ResponseEntity<>(errorResponseDto, HttpStatus.UNAUTHORIZED);
    }

    //--------------------------END AUTH EXCEPTIONS----------------------------------------------

    //--------------------------START PROFILE EXCEPTIONS--------------------------------------------

    @ExceptionHandler(InvalidImageTypeException.class)
    public ResponseEntity<Object> invalidImageTypeException(InvalidImageTypeException ex) {
        logger.warn("An invalid image type was received");
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(Collections.singletonList(ex.getMessage()));
        return new ResponseEntity<>(errorResponseDto, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(InvalidImageException.class)
    public ResponseEntity<Object> invalidImageException(InvalidImageException ex) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(Collections.singletonList(ex.getMessage()));
        return new ResponseEntity<>(errorResponseDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ProfileAlreadyExistsException.class)
    public ResponseEntity<Object> profileAlreadyExistsException(ProfileAlreadyExistsException ex) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(Collections.singletonList(ex.getMessage()));
        return new ResponseEntity<>(errorResponseDto, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<Object> usernameAlreadyExistsException(UsernameAlreadyExistsException ex) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(Collections.singletonList(ex.getMessage()));
        return new ResponseEntity<>(errorResponseDto, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(GenericCreateProfileException.class)
    public ResponseEntity<Object> genericCreateProfileException(GenericCreateProfileException ex) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(Collections.singletonList(ex.getMessage()));
        return new ResponseEntity<>(errorResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(GenericEditProfileException.class)
    public ResponseEntity<Object> genericEditProfileException(GenericEditProfileException ex) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(Collections.singletonList(ex.getMessage()));
        return new ResponseEntity<>(errorResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ProfileNotFoundException.class)
    public ResponseEntity<Object> profileNotFoundException(ProfileNotFoundException ex) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(Collections.singletonList(ex.getMessage()));
        return new ResponseEntity<>(errorResponseDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(GenericGetMyProfileException.class)
    public ResponseEntity<Object> genericGetMyProfileException(GenericGetMyProfileException ex) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(Collections.singletonList(ex.getMessage()));
        return new ResponseEntity<>(errorResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(GenericGetProfileException.class)
    public ResponseEntity<Object> genericGetProfileException(GenericGetProfileException ex) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(Collections.singletonList(ex.getMessage()));
        return new ResponseEntity<>(errorResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //--------------------------END PROFILE EXCEPTIONS----------------------------------------------

    //--------------------------START POST EXCEPTIONS--------------------------------------------
    @ExceptionHandler(GenericCreatePostException.class)
    public ResponseEntity<Object> genericCreatePostException(GenericCreatePostException ex) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(Collections.singletonList(ex.getMessage()));
        return new ResponseEntity<>(errorResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(GenericGetPostsException.class)
    public ResponseEntity<Object> genericGetPostsException(GenericGetPostsException ex) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(Collections.singletonList(ex.getMessage()));
        return new ResponseEntity<>(errorResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(GenericUpdatePostByIdException.class)
    public ResponseEntity<Object> genericUpdatePostByIdException(GenericUpdatePostByIdException ex) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(Collections.singletonList(ex.getMessage()));
        return new ResponseEntity<>(errorResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<Object> postNotFoundException(PostNotFoundException ex) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(Collections.singletonList(ex.getMessage()));
        return new ResponseEntity<>(errorResponseDto, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(UnauthorizedToModifyPostException.class)
    public ResponseEntity<Object> unauthorizedToModifyPostException(UnauthorizedToModifyPostException ex) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(Collections.singletonList(ex.getMessage()));
        return new ResponseEntity<>(errorResponseDto, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(InvalidPostIdException.class)
    public ResponseEntity<Object> invalidPostIdException(InvalidPostIdException ex) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(Collections.singletonList(ex.getMessage()));
        return new ResponseEntity<>(errorResponseDto, HttpStatus.BAD_REQUEST);
    }

    //--------------------------END POST EXCEPTIONS----------------------------------------------

    //--------------------------START MISC EXCEPTIONS--------------------------------------------
    private ResponseEntity<Object> genericBadRequestResponse(Exception ex) {
        logger.error("A bad request exception was caught by the global exception handler", ex);
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(Collections.singletonList("Invalid request, please try again"));
        return new ResponseEntity<>(errorResponseDto, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Object> missingServletRequestParameterException(MissingServletRequestParameterException ex) {
        return genericBadRequestResponse(ex);
    }
    @ExceptionHandler(ServletRequestBindingException.class)
    public ResponseEntity<Object> servletRequestBindingException(ServletRequestBindingException ex) {
        return genericBadRequestResponse(ex);
    }
    @ExceptionHandler(TypeMismatchException.class)
    public ResponseEntity<Object> typeMismatchException(TypeMismatchException ex) {
        return genericBadRequestResponse(ex);
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> httpMessageNotReadableException(HttpMessageNotReadableException ex) {
        return genericBadRequestResponse(ex);
    }
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        return genericBadRequestResponse(ex);
    }
    @ExceptionHandler(MissingPathVariableException.class)
    public ResponseEntity<Object> missingPathVariableException(MissingPathVariableException ex) {
        return genericBadRequestResponse(ex);
    }
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> constraintViolationException(ConstraintViolationException ex) {
        return genericBadRequestResponse(ex);
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> illegalArgumentException(IllegalArgumentException ex) {
        return genericBadRequestResponse(ex);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Object> handleMethodNotAllowedExceptions(HttpRequestMethodNotSupportedException ex) {
        logger.error("A method not allowed exception was caught by the global exception handler", ex);
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(Collections.singletonList("This method is not supported for this endpoint. Please check the HTTP method."));
        return new ResponseEntity<>(errorResponseDto, HttpStatus.METHOD_NOT_ALLOWED);
    }
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<Object> handleUnsupportedMediaTypeExceptions(HttpMediaTypeNotSupportedException ex) {
        logger.error("An unsupported media type exception was caught by the global exception handler", ex);
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(Collections.singletonList("The media type is not supported. Please check your Content-Type header."));
        return new ResponseEntity<>(errorResponseDto, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }
    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public ResponseEntity<Object> handleNotAcceptableMediaTypeExceptions(HttpMediaTypeNotAcceptableException ex) {
        logger.error("A not acceptable media type exception was caught by the global exception handler", ex);
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(Collections.singletonList("Requested media type is not acceptable. Please check your Accept header."));
        return new ResponseEntity<>(errorResponseDto, HttpStatus.NOT_ACCEPTABLE);
    }
    private ResponseEntity<Object> genericInternalServerErrorResponse(Exception ex) {
        logger.error("An internal server exception was caught by the global exception handler", ex);
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(Collections.singletonList("An unexpected error occurred."));
        return new ResponseEntity<>(errorResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(ConversionNotSupportedException.class)
    public ResponseEntity<Object> conversionNotSupportedException(ConversionNotSupportedException ex) {
        return genericInternalServerErrorResponse(ex);
    }
    @ExceptionHandler(HttpMessageNotWritableException.class)
    public ResponseEntity<Object> httpMessageNotWritableException(HttpMessageNotWritableException ex) {
        return genericInternalServerErrorResponse(ex);
    }
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Object> illegalStateException(IllegalStateException ex) {
        return genericInternalServerErrorResponse(ex);
    }


    //--------------------------END MISC EXCEPTIONS----------------------------------------------
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleUnexpectedExceptions(Exception ex) {
        logger.error("An unexpected exception was caught by the global controller exception handler", ex);
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(Collections.singletonList("An unexpected error occurred"));
        return new ResponseEntity<>(errorResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
