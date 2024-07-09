package com.example.user_service.services;

import com.example.user_service.Exception.*;

import com.example.user_service.Proxy.UserProxy;

import com.example.user_service.domain.Product;
import com.example.user_service.domain.Restaurant;
import com.example.user_service.domain.User;
import com.example.user_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;



import java.util.Arrays;
import java.util.List;


@Service
public class UserServiceImp implements IUserService {
    private UserRepository userRepository;
    private UserProxy userProxy;

    @Autowired
    public UserServiceImp(UserRepository userRepository,UserProxy userProxy) {
        this.userRepository = userRepository;
        this.userProxy=userProxy;
    }



    @Override
    public User register(String userName,String email, String password) throws UserAlreadyExistException {
        System.out.println("Registering user with email: " + email);
        if(userRepository.findByEmail(email).isPresent())
        {
            throw new UserAlreadyExistException();
        }
        User user = new User();
        user.setUserName(userName);
        user.setEmail(email);
        user.setPassword(password);

        User savedUser = userRepository.save(user);
        System.out.println(savedUser);
        if(!savedUser.getUserId().isEmpty())
        {
            ResponseEntity<?> r = userProxy.saveUser(user);
            System.out.println("Saved User: "+r.getBody());

        }

        return savedUser;
    }





    @Override
    public User addFavourites(Restaurant restaurant, String userId) throws FavouriteAlreadyExistException, UserNotFoundException {
        if (userRepository.findById(userId).isEmpty()) {
            throw new UserNotFoundException();
        }
        User user = userRepository.findById(userId).get();
        List<Restaurant> favouriteList = user.getFavouriteList();
        if (favouriteList == null) {
            System.out.println("Add to list");
            favouriteList = Arrays.asList(restaurant);
            user.setFavouriteList(favouriteList);
        } else {
            for (Restaurant f : favouriteList) {
                if (f.getId() == restaurant.getId()) {
                    throw new FavouriteAlreadyExistException();
                }
            }
            favouriteList.add(restaurant);
            user.setFavouriteList(favouriteList);
        }

        return userRepository.save(user);
    }

    @Override
    public User deleteFavourite(int id, String userId) throws UserNotFoundException, FavouriteNotFoundException {
        if (userRepository.findById(userId).isEmpty()) {
            throw new UserNotFoundException();
        }
        User foundUser = userRepository.findById(userId).get();
        List<Restaurant> favouriteList = foundUser.getFavouriteList();
        boolean favouritePresent = favouriteList.removeIf(x -> x.getId() == id);
        if (!favouritePresent) {
            throw new FavouriteNotFoundException();
        }
        foundUser.setFavouriteList(favouriteList);
        return userRepository.save(foundUser);
    }

    @Override
    public List<Restaurant> getAllFavourite(String userId) throws UserNotFoundException, FavouriteNotFoundException {
        if (userRepository.findById(userId).isEmpty()) {
            throw new UserNotFoundException();
        }
        User foundUser = userRepository.findById(userId).get();
        List<Restaurant> favouriteList = foundUser.getFavouriteList();
        if (favouriteList.isEmpty()) {
            throw new FavouriteNotFoundException();
        }
        return favouriteList;
    }

    @Override
    public User addCartItems(Product product, String userId) throws ProductAlreadyExistException, UserNotFoundException {
        if(userRepository.findById(userId).isEmpty())
        {
            throw new UserNotFoundException();
        }
        User user=userRepository.findById(userId).get();
        List<Product> cartList=user.getCartItems();
        if(cartList.isEmpty())
        {
            System.out.println("add to cart");
            cartList=Arrays.asList(product);
            user.setCartItems(cartList);
        }
        else {
            for(Product p:cartList)
            {
                if(p.getId()== product.getId())
                {
                    throw new ProductAlreadyExistException();
                }

            }
            cartList.add(product);
            user.setCartItems(cartList);
        }
        return userRepository.save(user);
    }

    @Override
    public User removeCartItems(int id, String userId) throws ProductNotFoundException, UserNotFoundException {
        if(userRepository.findById(userId).isEmpty())
        {
            throw new UserNotFoundException();
        }

        User user= userRepository.findById(userId).get();
        List<Product>cartList=user.getCartItems();
        boolean cartPresent = cartList.removeIf(x -> x.getId() == id);
        if(!cartPresent)
        {
            throw new ProductNotFoundException();
        }
        user.setCartItems(cartList);
        return userRepository.save(user);
    }

    @Override
    public List<Product> getCartItems(String userId) throws ProductNotFoundException, UserNotFoundException {
        if(userRepository.findById(userId).isEmpty())
        {
            throw new UserNotFoundException();
        }
        User user=userRepository.findById(userId).get();
        List<Product>cartList=user.getCartItems();
        if(cartList.isEmpty())
        {
            throw new ProductNotFoundException();
        }
        return cartList;
    }
}

