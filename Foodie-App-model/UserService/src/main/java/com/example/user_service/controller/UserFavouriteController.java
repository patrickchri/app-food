package com.example.user_service.controller;


import com.example.user_service.Exception.FavouriteAlreadyExistException;


import com.example.user_service.Exception.FavouriteNotFoundException;

import com.example.user_service.Exception.UserNotFoundException;
import com.example.user_service.domain.Favourite;

import com.example.user_service.services.IUserService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v2/")
public class UserFavouriteController {
    private IUserService iUserService;
    private ResponseEntity responseEntity;
    public UserFavouriteController(IUserService iUserService)
    {
        this.iUserService=iUserService;
    }

    @PostMapping("/user/addFavourite")
    public ResponseEntity addFavourite(@RequestBody Favourite favourite,HttpServletRequest request) throws UserNotFoundException, FavouriteAlreadyExistException {
        try
        {
            responseEntity=new ResponseEntity<>(iUserService.addFavourites(favourite,getIdFromClaims(request)),HttpStatus.OK);
        }
        catch(UserNotFoundException e)
        {
            throw new FavouriteAlreadyExistException();
        }
        catch (Exception e)
        {
            responseEntity=get500Response(e);
        }
        return responseEntity;
    }

    @PostMapping("users/deleteFavourites{id}")
    public  ResponseEntity deleteFavourites(@RequestBody int id,HttpServletRequest request) throws UserNotFoundException, FavouriteNotFoundException
    {
        try
        {
            responseEntity=new ResponseEntity<>(iUserService.deleteFavourite(id,getIdFromClaims(request)),HttpStatus.OK);
        }
        catch(UserNotFoundException e)
        {
            throw new UserNotFoundException();
        }
        catch(FavouriteNotFoundException e)
        {
            throw new FavouriteNotFoundException();
        }
        catch(Exception e)
        {
            responseEntity=get500Response(e);
        }
        return responseEntity;
    }

    @GetMapping("/users/favourites")
    public ResponseEntity getAllFavourites(HttpServletRequest request) throws UserNotFoundException,FavouriteNotFoundException
    {
        try
        {
            responseEntity=new ResponseEntity<>(iUserService.getAllFavourite(getIdFromClaims(request)),HttpStatus.OK);
        }
        catch(UserNotFoundException e)
        {
            throw new UserNotFoundException();
        }
        catch(FavouriteNotFoundException e)
        {
            throw new FavouriteNotFoundException();
        }
        catch(Exception e)
        {
            responseEntity=get500Response(e);
        }
        return responseEntity;
    }
    private int getIdFromClaims(HttpServletRequest request) {
        Claims claims = (Claims) request.getAttribute("claims");
        return Integer.parseInt(claims.getSubject());
    }

    private ResponseEntity<String> get500Response(Exception ex) {
        return new ResponseEntity<>(ex.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
