package com.example.UserAuthentication.Controller;


import com.example.UserAuthentication.Exception.InvalidCredentialException;
import com.example.UserAuthentication.Exception.UserAlreadyExistException;
import com.example.UserAuthentication.Security.ISecurityTokenGenerator;
import com.example.UserAuthentication.domain.User;
import com.example.UserAuthentication.service.IUserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1")
public class UserController {
    private IUserService userService;
    private ResponseEntity responseEntity;
    private ISecurityTokenGenerator securityTokenGenerator;
    public UserController(IUserService userService,ISecurityTokenGenerator securityTokenGenerator)
    {
        this.userService=userService;
        this.securityTokenGenerator=securityTokenGenerator;
    }
    @PostMapping("/saveUser")
    public ResponseEntity save(@RequestBody User user) throws UserAlreadyExistException
    {
        try
        {
            responseEntity=new ResponseEntity<>(userService.saveUser(user),HttpStatus.OK);
        }
        catch(UserAlreadyExistException e)
        {
            throw new UserAlreadyExistException();
        }
        catch(Exception e)
        {
            responseEntity=new ResponseEntity(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }

    @PostMapping("/login")
    public ResponseEntity loginUser(@RequestBody User user) throws InvalidCredentialException
    {
        try
        {
            User founUser = userService.findByEmailAndPassword(user.getEmail(),user.getPassword());
            responseEntity=new ResponseEntity<>(securityTokenGenerator.createToken(founUser), HttpStatus.OK);
        }
        catch (InvalidCredentialException e)
        {
            throw new InvalidCredentialException();
        }
        catch(Exception e)
        {
            responseEntity=new ResponseEntity(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }
}
