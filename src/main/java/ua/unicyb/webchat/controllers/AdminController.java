package ua.unicyb.webchat.controllers;

import org.springframework.web.bind.annotation.*;
import ua.unicyb.webchat.dto.UserDto;
import ua.unicyb.webchat.services.UserService;

import java.util.List;

@RestController
public class AdminController {

    private UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping(value = "/users")
    public List<UserDto> findUsers(){
        return userService.findAll();
    }

    @PatchMapping(value = "/ban/{login}")
    public UserDto banUser(@PathVariable("login") String login){
        return userService.banUser(login);
    }

    @DeleteMapping(value = "/ban/{login}")
    public UserDto unbanUser(@PathVariable("login") String login){
        return userService.unbanUser(login);
    }

}
