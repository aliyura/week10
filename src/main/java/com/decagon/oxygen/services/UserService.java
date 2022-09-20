package com.decagon.oxygen.services;

import com.decagon.oxygen.config.MemcachedConfig;
import com.decagon.oxygen.entities.User;
import com.decagon.oxygen.pojos.*;
import com.decagon.oxygen.repositories.UserRepository;
import com.decagon.oxygen.security.JwtService;
import com.decagon.oxygen.utils.Responder;
import com.decagon.oxygen.utils.Utility;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.ArrayList;

@Service
@AllArgsConstructor
public class UserService {


    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;
    private final UserRepository userRepository;

    private  final  SmsService smsService;
    private  final Utility util;
    private final Responder responder;

    private final PasswordEncoder passwordEncoder;

    private  final MemcachedService memcached;


    public ResponseEntity<APIResponse> createUser(User user) {
        User existingUser = userRepository.findByUsername(user.getUsername()).orElse(null);
        if(existingUser==null) { // if user does not exist
            user.setUuid(util.generateUniqueId()); //generate unique id of user
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            String otp = String.valueOf((Math.random() * (10000 - 9999 + 1) + 9999));
            memcached.save(user.getUsername(),otp); //saving OTP in memory

            //sending same otp to user
            smsService.sendSms(existingUser.getPhoneNumber(), "Your Oxygen OTP is "+otp);

            User result = userRepository.save(user);
            return responder.Okay(result);
        }else{
            return responder.AlreadyExist("User Already Exist");
        }
    }

    public ResponseEntity<APIResponse> authenticate(AuthRequest request){
        System.out.println("I am here !!!!!!!!!!!!!!!!!!");
        Authentication auth= authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        if(auth.isAuthenticated()){
           String token="Bearer "+jwtService.generateToken(new org.springframework.security.core.userdetails.User(request.getUsername(),request.getPassword(),new ArrayList<>()));
           return  responder.Okay(token);
        }else{
            return  responder.UnAuthorize("Authentication Failed");
        }
    }
    public ResponseEntity<APIResponse> authenticateOauth2(GoogleOauth2User authPrincipal){
        User existingUser= userRepository.findByUsername(authPrincipal.getEmail()).orElse(null);
        if(existingUser==null){
            String name=authPrincipal.getFirstName()+" "+authPrincipal.getLastName();
            User newUser = new User();
            newUser.setName(name);
            newUser.setUsername(authPrincipal.getEmail());
            newUser.setUuid(util.generateUniqueId());
            userRepository.save(newUser);
        }

        String token="Bearer "+jwtService.generateToken(new org.springframework.security.core.userdetails.User(authPrincipal.getEmail(),authPrincipal.getFirstName(),new ArrayList<>()));
        return  responder.Okay(token);
    }

    public ResponseEntity<APIResponse> verifyUser(UserVerificationRequest request){
        User existingUser= userRepository.findByUsername(request.getUsername()).orElse(null);
        if(existingUser==null) {

            String givenOTP= request.getOtp();
            String systemOTP=memcached.getValueByKey(request.getOtp()); //getting saved OTP

            if(givenOTP.equals(systemOTP)){ //comapre system otp and the otp that user provide
             return  responder.Okay("Verified");
            }else{
                return  responder.Okay("Invalid OTP");
            }
        }else{
            return responder.UnAuthorize("User not found");
        }
    }
    public ResponseEntity<APIResponse> forgotPassword(ForgotPasswordRequest request){
        User existingUser= userRepository.findByUsername(request.getUsername()).orElse(null);
        if(existingUser==null) {
            String otp = String.valueOf((Math.random() * (10000 - 9999 + 1) + 9999));
            memcached.save(existingUser.getUsername(), otp);// savibg otp to memory
            smsService.sendSms(existingUser.getPhoneNumber(), "Your Oxygen OTP is "+otp);
            return responder.Okay("#####This is the list of the users............");
        }else{
            return responder.UnAuthorize("User not found");
        }
    }

    public ResponseEntity<APIResponse> resetPassword(ResetPasswordRequest request){
        User existingUser= userRepository.findByUsername(request.getUsername()).orElse(null);
        if(existingUser==null) {

            String givenOTP= request.getOtp();
            String systemOTP=memcached.getValueByKey(request.getOtp()); //getting saved OTP

            if(givenOTP.equals(systemOTP)){ //comapre system otp and the otp that user provide

                String newPassword=passwordEncoder.encode(request.getNewPassword());
                existingUser.setPassword(newPassword); //modifying the user in the database with the new password

              userRepository.save(existingUser);
                return responder.Okay("Password Changed Successfully");

            }else{
                return  responder.Okay("Invalid OTP");
            }


        }else{
            return responder.UnAuthorize("User not found");
        }
    }


    public ResponseEntity<APIResponse> getUsers(){
        return responder.Okay("#####This is the list of the users............");
    }

}
