package com.example.user_service.services;

import com.example.user_service.Exception.FavouriteAlreadyExistException;
import com.example.user_service.Exception.FavouriteNotFoundException;

import com.example.user_service.Exception.UserAlreadyExistException;
import com.example.user_service.Exception.UserNotFoundException;
import com.example.user_service.domain.Favourite;
import com.example.user_service.domain.User;
import com.example.user_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class UserServiceImp implements IUserService {
    private UserRepository userRepository;
    @Autowired
    public UserServiceImp(UserRepository userRepository)
    {
        this.userRepository=userRepository;
    }


    @Override
    public User register(String userName, String email, String password, MultipartFile image) throws UserAlreadyExistException {
        return null;
    }

    @Override
    public User addFavourites(Favourite favourite, int userId) throws FavouriteAlreadyExistException,UserNotFoundException{
        if(userRepository.findById(userId).isEmpty())
        {
            throw new UserNotFoundException();
        }
        User user = userRepository.findById(userId).get();
        List<Favourite>favouriteList=user.getFavouriteList();
        if(favouriteList==null)
        {
            System.out.println("Add to list");
            favouriteList= Arrays.asList(favourite);
            user.setFavouriteList(favouriteList);
        }
        else
        {
            for(Favourite f:favouriteList)
            {
                if(f.getId()==favourite.getId())
                {
                    throw new FavouriteAlreadyExistException();
                }
            }
            favouriteList.add(favourite);
            user.setFavouriteList(favouriteList);
        }

        return userRepository.save(user);
    }

    @Override
    public User deleteFavourite(int id, int userId) throws UserNotFoundException, FavouriteNotFoundException {
        if(userRepository.findById(userId).isEmpty())
        {
            throw new UserNotFoundException();
        }
        User foundUser = userRepository.findById(userId).get();
        List<Favourite>favouriteList=foundUser.getFavouriteList();
        boolean favouritePresent = favouriteList.removeIf(x->x.getId()==id);
        if(!favouritePresent)
        {
            throw new FavouriteNotFoundException();
        }
        foundUser.setFavouriteList(favouriteList);
        return userRepository.save(foundUser);
    }

    @Override
    public List<Favourite> getAllFavourite(int userId) throws UserNotFoundException,FavouriteNotFoundException {
        if(userRepository.findById(userId).isEmpty())
        {
            throw new UserNotFoundException();
        }
        User foundUser = userRepository.findById(userId).get();
        List<Favourite>favouriteList=foundUser.getFavouriteList();
        if(favouriteList.isEmpty())
        {
            throw new FavouriteNotFoundException();
        }
        return favouriteList;
    }
}
