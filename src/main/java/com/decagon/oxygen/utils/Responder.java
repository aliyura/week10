package com.decagon.oxygen.utils;

import com.decagon.oxygen.pojos.APIResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class Responder<T> {

    public ResponseEntity<APIResponse> Okay(T response){
        return  new  ResponseEntity<>(new APIResponse("Request Successful", true, response), HttpStatus.OK);
    }

    public ResponseEntity<APIResponse> NotFound(){
        return  new ResponseEntity<>(new APIResponse("Request Not Found", true, null), HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<APIResponse> AlreadyExist(String message){
        return  new ResponseEntity<>(new APIResponse(message, true, null), HttpStatus.CONFLICT);
    }

    public ResponseEntity<APIResponse> UnAuthorize(String message){
        return  new ResponseEntity<>(new APIResponse(message, true, null), HttpStatus.UNAUTHORIZED);
    }

}
