package com.juliuskrah.quartz.helpers;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Getter @Setter
public class ApiResponse {

    private HttpStatus status = null;
    private String message = null;
    private List<String> errors;
    private ArrayList<Object> data = null;
    @JsonFormat(pattern="dd/MM/yyyy HH:mm:ss", timezone="America/Santiago")
    private Date timestamp = null;
    private String path = null;
    private Boolean debug = true;




    public ApiResponse(WebRequest request) {
        HttpServletRequest request1 = ((ServletWebRequest) request).getRequest();
        this.setPath(request1.getRequestURI());
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

    public static ApiResponse success(WebRequest webRequest, ArrayList<Object> data){

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
        ArrayList<Object> list = new ArrayList<>();
        list.add(data);
        response.setData(list);
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

