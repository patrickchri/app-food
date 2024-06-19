package com.example.user_service.services;

import com.example.user_service.Exception.FavouriteAlreadyExistException;
import com.example.user_service.Exception.FavouriteNotFoundException;

import com.example.user_service.Exception.UserAlreadyExistException;
import com.example.user_service.Exception.UserNotFoundException;
import com.example.user_service.domain.Favourite;
import com.example.user_service.domain.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IUserService {
    User register(String userName, String email, String password, MultipartFile image) throws UserAlreadyExistException;

    User addFavourites(Favourite favourite,int userId) throws FavouriteAlreadyExistException,UserNotFoundException;
    User deleteFavourite(int id , int userId) throws UserNotFoundException, FavouriteNotFoundException;
    List<Favourite> getAllFavourite(int userId) throws UserNotFoundException,FavouriteNotFoundException;
}
