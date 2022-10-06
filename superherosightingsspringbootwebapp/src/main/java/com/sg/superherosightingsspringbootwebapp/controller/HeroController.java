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
import com.sg.superherosightingsspringbootwebapp.entities.Location;
import com.sg.superherosightingsspringbootwebapp.entities.Organization;
import com.sg.superherosightingsspringbootwebapp.entities.Superpower;
import com.sg.superherosightingsspringbootwebapp.service.DuplicateNameException;
import com.sg.superherosightingsspringbootwebapp.service.HeroService;
import com.sg.superherosightingsspringbootwebapp.service.LocationService;
import com.sg.superherosightingsspringbootwebapp.service.OrganizationService;
import com.sg.superherosightingsspringbootwebapp.service.SightingService;
import com.sg.superherosightingsspringbootwebapp.service.SuperpowerService;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Yash
 */
@Controller
public class HeroController {

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

    Set<ConstraintViolation<Hero>> violations = new HashSet<>();

    @GetMapping("heroes")
    public String displayHeroes(Model model) {
        List<Superpower> superpowers = superpowerService.getAllSuperpowers();
        List<Organization> organizations = organizationService.getAllOrganizations();
        List<Hero> heroes = heroService.getAllHeroes();

        Hero hero = new Hero();
        model.addAttribute("hero", hero);
        model.addAttribute("heroes", heroes);
        model.addAttribute("superpowers", superpowers);
        model.addAttribute("organizations", organizations);

        return "heroes";
    }

    @PostMapping("addHero")
    public String addHero(@Valid @ModelAttribute("hero") Hero hero, BindingResult result, HttpServletRequest request, Model model, @RequestParam("heroSaveImage") MultipartFile file) throws IOException {

        String isHero = request.getParameter("isHero");
        if (isHero != null) {

            hero.setIsHero(Boolean.parseBoolean(isHero));
        } else {
            FieldError error = new FieldError("hero", "isHero", "Must include one type");
            result.addError(error);

        }
        

        String superpowerId = request.getParameter("superpowerId");
        if (superpowerId != null) {
            Superpower superpower = superpowerService.getSuperpowerById(Integer.parseInt(superpowerId));
            hero.setSuperpower(superpower);

        } else {
            FieldError error = new FieldError("hero", "superpower", "Must include a power");
            result.addError(error);
        }

        String[] organizationsIds = request.getParameterValues("organizationId");

        List<Organization> organizations = new ArrayList<>();
        if (organizationsIds != null) {
            for (String organizationId : organizationsIds) {
                organizations.add(organizationService.getOrganizationById(Integer.parseInt(organizationId)));
            }
        } else {
            FieldError error = new FieldError("hero", "organizations", "Must include atlease one organization");
            result.addError(error);
        }

        try {
            heroService.validateHero(hero);
        } catch (DuplicateNameException ex) {
            FieldError error = new FieldError("hero", "name", ex.getMessage());
            result.addError(error);
        }

        hero.setOrganizations(organizations);
        
        try {
            hero.setSuperImage(file.getBytes());
        } catch (IOException ex) {
            FieldError error = new FieldError("hero", "superImage", ex.getMessage());
            result.addError(error);
        }
         
        if (result.hasErrors()) {
            List<Hero> heroes = heroService.getAllHeroes();
            model.addAttribute("heroes", heroes);
            model.addAttribute("hero", hero);
            List<Superpower> superpowers = superpowerService.getAllSuperpowers();
            List<Organization> organizationList = organizationService.getAllOrganizations();
            model.addAttribute("superpowers", superpowers);
            model.addAttribute("organizations", organizationList);
            return "heroes";
        }
        heroService.addHero(hero);
        return "redirect:/heroes";
    }

    @GetMapping("deleteHero")
    public String deleteHero(HttpServletRequest request, Model model) {
        int heroId = Integer.parseInt(request.getParameter("id"));
        Hero hero = heroService.getHeroById(heroId);

        model.addAttribute("hero", hero);
        Superpower superpower = superpowerService.getSuperpowerForHero(hero);
        List<Organization> organizations = organizationService.getOrganizationsForHero(hero);
        model.addAttribute("superpower", superpower);
        model.addAttribute("organizations", organizations);

        return "deleteHero";
    }

    @PostMapping("deleteHero")
    public String performDeleteHero(HttpServletRequest request) {
        int heroId = Integer.parseInt(request.getParameter("id"));
        heroService.deleteHeroById(heroId);
        return "redirect:/heroes";

    }

    @GetMapping("editHero")
    public String editHero(HttpServletRequest request, Model model) {
        int heroId = Integer.parseInt(request.getParameter("id"));
        Hero hero = heroService.getHeroById(heroId);

        model.addAttribute("hero", hero);
        List<Superpower> superpowers = superpowerService.getAllSuperpowers();
        List<Organization> organizations = organizationService.getAllOrganizations();
        model.addAttribute("superpowers", superpowers);
        model.addAttribute("organizations", organizations);
        return "editHero";
    }
    
     
   
    @PostMapping("editHero")
    public String performEditHero(@Valid Hero hero, BindingResult result, HttpServletRequest request, Model model, @RequestParam("heroSaveImage") MultipartFile file) { 

        String isHero = request.getParameter("isHero");

        hero.setIsHero(Boolean.parseBoolean(isHero));

        String superpowerId = request.getParameter("superpowerId");

        Superpower superpower = superpowerService.getSuperpowerById(Integer.parseInt(superpowerId));
        hero.setSuperpower(superpower);

        List<Organization> organizations = new ArrayList<>();
        String[] organizationsIds = request.getParameterValues("organizationId");
        if (organizationsIds != null) {
            for (String organizationId : organizationsIds) {
                organizations.add(organizationService.getOrganizationById(Integer.parseInt(organizationId)));
            }
        } else {
            FieldError error = new FieldError("hero", "organizations", "Must include one organization");
            result.addError(error);
        }

        hero.setOrganizations(organizations);
   
        String heroId = request.getParameter("heroId");
     
        hero.setSuperImage(heroService.getHeroById(Integer.parseInt(heroId)).getSuperImage());
        if(!file.isEmpty()){
       try {
           hero.setSuperImage(file.getBytes());
       } catch (IOException ex) {
          FieldError error = new FieldError("hero", "superImage", ex.getMessage());
          result.addError(error);
        }
        }
        if (result.hasErrors()) {
            model.addAttribute("superpowers", superpowerService.getAllSuperpowers());
            model.addAttribute("organizations", organizationService.getAllOrganizations());
            model.addAttribute("hero", hero);
            return "editHero";
        }

        heroService.updateHero(hero);
        Superpower superpowera = superpowerService.getSuperpowerById(Integer.parseInt(superpowerId));
        model.addAttribute("superpower", superpowera);
        model.addAttribute("organizations", organizations);

        List<Location> locations = locationService.getLocationsForHero(hero);

        model.addAttribute("locations", locations);
        model.addAttribute("hero", hero);
        return "detailsHero";
    }
    
    
    @GetMapping("detailsHero")
    public String detailsHero(HttpServletRequest request, Model model) {
        int heroId = Integer.parseInt(request.getParameter("id"));
        Hero hero = heroService.getHeroById(heroId);

        model.addAttribute("hero", hero);

        List<Organization> organizations = organizationService.getOrganizationsForHero(hero);
        Superpower superpower = superpowerService.getSuperpowerForHero(hero);
        model.addAttribute("superpower", superpower);
        model.addAttribute("organizations", organizations);

        List<Location> locations = locationService.getLocationsForHero(hero);

        model.addAttribute("locations", locations);
        return "detailsHero";
    }

    @GetMapping("heroes/{id}/image")
    public void renderSuperImage(@PathVariable String id, HttpServletResponse response) throws IOException {
        Hero hero = heroService.getHeroById(Integer.parseInt(id));

        response.setContentType("image/jpeg");
        InputStream is = new ByteArrayInputStream(hero.getSuperImage());
        IOUtils.copy(is, response.getOutputStream());
    }

}
