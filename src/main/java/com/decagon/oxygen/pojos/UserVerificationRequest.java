package com.decagon.oxygen.pojos;

import lombok.Data;

@Data
public class UserVerificationRequest {
    private  String username;
    private  String otp;
}
