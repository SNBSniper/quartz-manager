package com.juliuskrah.quartz.helpers;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

public class ApiResponse {

    private HttpStatus status = null;
    private String message = null;
    private List<String> errors;
    private Object[] data = null;
    @JsonFormat(pattern="dd/MM/yyyy HH:mm:ss", timezone="America/Santiago")
    private Date timestamp = null;
    private String path = null;
    private Boolean debug = true;


    public ApiResponse(HttpStatus status, String message) {
        this.status = status;
        this.message = message;


    }

    public ApiResponse(HttpStatus status, String message, List<String> errors) {
        this.status = status;
        this.message = message;
        this.errors = errors;

    }

    public ApiResponse(WebRequest request) {
        HttpServletRequest request1 = ((ServletWebRequest) request).getRequest();
        this.setPath(request1.getRequestURI());
    }

    public ApiResponse(HttpStatus status, String message, String error) {
        this.status = status;
        this.message = message;
        errors = Arrays.asList(error);
    }



//    "timestamp": "2018-01-30T17:16:54.815+0000",
//            "status": 405,
//            "error": "Method Not Allowed",
//            "message": "Request method 'PUT' not supported",
//            "path": "/api/v2/user/"


    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object[] getData() {
        return data;
    }

    public void setData(Object[] data) {
        this.data = data;
    }

    public void setData(Object data) {
        Object[] tmp = new Object[1];
        tmp[0] = data;
        this.data = tmp;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public static ApiResponse error(WebRequest webRequest, HttpStatus errorCode, String message, List<String> error){
        ApiResponse response = new ApiResponse(webRequest);
        response.setStatus(errorCode);
        response.setMessage(message);
        response.setErrors(error);
        response.setTimestamp(new Date());
        response.setData(null);
        return response;
    }

    public static ApiResponse success(WebRequest webRequest, Object[] data){

        ApiResponse response = new ApiResponse(webRequest);
        response.setStatus(HttpStatus.OK);
        response.setTimestamp(new Date());
        response.setData(data);
        return response;
    }

    public static ApiResponse success(WebRequest webRequest, Object data){

        ApiResponse response = new ApiResponse(webRequest);
        response.setStatus(HttpStatus.OK);
        response.setTimestamp(new Date());
        response.setData(data);
        return response;
    }


    public static ApiResponse success(WebRequest webRequest){
        ApiResponse response = new ApiResponse(webRequest);
        response.setStatus(HttpStatus.OK);
        response.setTimestamp(new Date());
        response.setData(null);
        return response;
    }

    public static ApiResponse error(WebRequest webRequest, Map<String, Object> errorAttributes) {
        ApiResponse response = new ApiResponse(webRequest);
        List<String> errors = new ArrayList<String>();
        response.setStatus(HttpStatus.valueOf((Integer) errorAttributes.get("status")));
        response.setTimestamp((Date) errorAttributes.get("timestamp"));
        response.setMessage(errorAttributes.get("error").toString());
        response.setPath(errorAttributes.get("path").toString());

        errors.add(errorAttributes.get("error").toString());
        if(response.debug)
            errors.add(errorAttributes.get("trace").toString());
        response.setErrors(errors);

        return response;

    }
}

