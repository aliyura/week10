package com.decagon.oxygen.pojos;

import lombok.Data;

@Data
public class ResetPasswordRequest{
    private String username;
    private  String newPassword;
    private  String otp;
}
