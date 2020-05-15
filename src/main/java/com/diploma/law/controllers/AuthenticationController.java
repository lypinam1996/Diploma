package com.diploma.law.controllers;

import com.diploma.law.models.UsersEntity;
import com.diploma.law.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class AuthenticationController
{

    @Autowired
    UserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView login()
    {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return modelAndView;
    }

    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public ModelAndView registration()
    {
        ModelAndView modelAndView = new ModelAndView();
        UsersEntity user = new UsersEntity();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("registration");
        return modelAndView;
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ModelAndView createNewUser(@ModelAttribute("user") @Valid UsersEntity user, BindingResult bindingResult)
    {
        ModelAndView modelAndView = new ModelAndView();
        UsersEntity userExists = userService.FindByLogin(user.getLogin());
        if (userExists != null)
        {
            bindingResult.rejectValue("login", "error.login", "*Данный логин занят");

        }
        if (bindingResult.hasErrors())
        {
            modelAndView.setViewName("registration");
        }
        else
        {
            userService.saveUser(user);
            modelAndView.addObject("successMessage", "Регистрация прошла успешно");
            modelAndView.addObject("login", user.getLogin());
            modelAndView.addObject("password", user.getPassword());
            modelAndView.setViewName("registration");
        }
        return modelAndView;
    }

    @RequestMapping("/")
    String index()
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.print(auth.getName());
        return "index";
    }
}
