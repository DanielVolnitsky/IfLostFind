package com.daniel.iflostfind.controller;

import com.daniel.iflostfind.service.GoogleMapKeyService;
import com.daniel.iflostfind.service.UserService;
import com.daniel.iflostfind.service.dto.UserDto;
import com.daniel.iflostfind.service.exception.UserAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class RegistrationController {

    static final String USER_MODEL_NAME = "user";

    private final GoogleMapKeyService googleMapKeyService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationController(GoogleMapKeyService googleMapKeyService, UserService userService, PasswordEncoder passwordEncoder) {
        this.googleMapKeyService = googleMapKeyService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/register")
    public String getPage(Model m) {

        m.addAttribute("google_map_key", googleMapKeyService.getMapKey());
        m.addAttribute(USER_MODEL_NAME, new UserDto());
        return "registration";
    }

    @PostMapping("/register")
    public ModelAndView register(
            @ModelAttribute(USER_MODEL_NAME) @Valid UserDto userDto,
            BindingResult br) {

        ModelAndView mv = new ModelAndView("registration", USER_MODEL_NAME, userDto);
        mv.addObject("google_map_key", googleMapKeyService.getMapKey());

        if(br.hasErrors()){
            return mv;
        }

        try {
            userDto.setPassword(passwordEncoder.encode(userDto.getPassword())); //TODO move
            userService.registerNewUserAccount(userDto);
            return new ModelAndView("login", USER_MODEL_NAME, userDto);
        } catch (UserAlreadyExistsException e) {
            FieldError fe = new FieldError(
                    "email", "email", "User with this email already exists");
            br.addError(fe);
        }

        return mv;
    }
}
