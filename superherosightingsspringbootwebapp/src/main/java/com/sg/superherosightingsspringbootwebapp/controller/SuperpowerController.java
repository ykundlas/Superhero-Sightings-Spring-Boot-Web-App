/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.superherosightingsspringbootwebapp.controller;

import com.sg.superherosightingsspringbootwebapp.dao.HeroDao;
import com.sg.superherosightingsspringbootwebapp.dao.LocationDao;
import com.sg.superherosightingsspringbootwebapp.dao.OrganizationDao;
import com.sg.superherosightingsspringbootwebapp.dao.SightingDao;
import com.sg.superherosightingsspringbootwebapp.dao.SuperpowerDao;
import com.sg.superherosightingsspringbootwebapp.entities.Hero;
import com.sg.superherosightingsspringbootwebapp.entities.Superpower;
import com.sg.superherosightingsspringbootwebapp.service.DuplicateNameException;

import com.sg.superherosightingsspringbootwebapp.service.HeroService;
import com.sg.superherosightingsspringbootwebapp.service.LocationService;
import com.sg.superherosightingsspringbootwebapp.service.OrganizationService;
import com.sg.superherosightingsspringbootwebapp.service.SightingService;
import com.sg.superherosightingsspringbootwebapp.service.SuperpowerService;
import java.util.Date;
//import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 *
 * @author Yash
 */
@Controller
public class SuperpowerController {

    @Autowired
    HeroService heroService;

    @Autowired
    LocationService locationService;

    @Autowired
    OrganizationService organizationService;

    @Autowired
    SightingService sightingService;

    @Autowired
    SuperpowerService superpowerService;

    @GetMapping("superpowers")
    public String displaySuperpowers(Model model) {
        List<Superpower> superpowers = superpowerService.getAllSuperpowers();
        Superpower superpower = new Superpower();
        model.addAttribute("superpower", superpower);
        model.addAttribute("superpowers", superpowers);

        return "superpowers";
    }

    @PostMapping("addSuperpower")
    public String addSuperpower(@Valid @ModelAttribute("superpower") Superpower superpower, BindingResult result, Model model) { 

        try {
            superpowerService.validateSuperPower(superpower);
        } catch (DuplicateNameException ex) {
            FieldError error = new FieldError("superpower", "name", ex.getMessage());
            result.addError(error);
        }
        
        if (result.hasErrors()) {
            List<Superpower> superpowers = superpowerService.getAllSuperpowers();
            model.addAttribute("superpowers", superpowers);
            return "superpowers";
        }

        superpowerService.addSuperpower(superpower);
        return "redirect:/superpowers";
    }

    @GetMapping("deleteSuperpower")
    public String deleteSuperpower(HttpServletRequest request, Model model) {
        int superpowerId = Integer.parseInt(request.getParameter("id"));
        Superpower superpower = superpowerService.getSuperpowerById(superpowerId);

        model.addAttribute("superpower", superpower);
        return "deleteSuperpower";
    }

    @PostMapping("deleteSuperpower")
    public String performDeleteSuperpower(HttpServletRequest request) {
        int superpowerId = Integer.parseInt(request.getParameter("id"));
        superpowerService.deleteSuperpowerById(superpowerId);
        return "redirect:/superpowers";

    }

    @GetMapping("editSuperpower")
    public String editSuperpower(HttpServletRequest request, Model model) {
        int superpowerId = Integer.parseInt(request.getParameter("id"));
        Superpower superpower = superpowerService.getSuperpowerById(superpowerId);

        model.addAttribute("superpower", superpower);
        return "editSuperpower";
    }

    @PostMapping("editSuperpower")
    public String performEditSuperpower(@Valid Superpower superpower, BindingResult result, Model model) {       //      (HttpServletRequest request, Model model)

        if (result.hasErrors()) {
            return "editSuperpower";
        }

        superpowerService.updateSuperpower(superpower);

        List<Hero> heroes = heroService.getHeroesForSuperpower(superpower);
        model.addAttribute("heroes", heroes);

        return "detailsSuperpower";

    }

    @GetMapping("detailsSuperpower")
    public String detailSuperpower(HttpServletRequest request, Model model) {
        int superpowerId = Integer.parseInt(request.getParameter("id"));
        Superpower superpower = superpowerService.getSuperpowerById(superpowerId);

        model.addAttribute("superpower", superpower);

        List<Hero> heroes = heroService.getHeroesForSuperpower(superpower);
        model.addAttribute("heroes", heroes);
        return "detailsSuperpower";
    }

}
