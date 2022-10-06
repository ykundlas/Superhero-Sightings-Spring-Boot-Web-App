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
import com.sg.superherosightingsspringbootwebapp.entities.Sighting;
import com.sg.superherosightingsspringbootwebapp.entities.Superpower;
import com.sg.superherosightingsspringbootwebapp.service.HeroService;
import com.sg.superherosightingsspringbootwebapp.service.InvalidDataException;
import com.sg.superherosightingsspringbootwebapp.service.LocationService;
import com.sg.superherosightingsspringbootwebapp.service.OrganizationService;
import com.sg.superherosightingsspringbootwebapp.service.SightingService;
import com.sg.superherosightingsspringbootwebapp.service.SuperpowerService;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
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
public class SightingContoller {

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

   //Set<ConstraintViolation<Sighting>> violations = new HashSet<>();

    @GetMapping("sightings")
    public String displaySightings(Model model) {
        List<Sighting> sightings = sightingService.getAllSightings();
        List<Hero> heroes = heroService.getAllHeroes();
        List<Location> locations = locationService.getAllLocations();
        Sighting sighting = new Sighting();
        model.addAttribute("sighting", sighting);
        model.addAttribute("sightings", sightings);
        model.addAttribute("heroes", heroes);
        model.addAttribute("locations", locations);
   
        return "sightings";
    }

    @PostMapping("addSighting")
    public String addSighting(@Valid @ModelAttribute("sighting") Sighting sighting, BindingResult result, Model model, HttpServletRequest request) { //(HttpServletRequest request)

        String heroId = request.getParameter("heroId");
        if (heroId != null) {
            Hero hero = heroService.getHeroById(Integer.parseInt(heroId));
            sighting.setHero(hero);
        } else {
            FieldError error = new FieldError("sighting", "hero", "Must include a super");
            result.addError(error);
        }
        

        String locationId = request.getParameter("locationId");
        if(locationId != null){
        Location location = locationService.getLocationById(Integer.parseInt(locationId));
        sighting.setLocation(location);
         }else{
          FieldError error = new FieldError("sighting", "location", "Must include a location");
        result.addError(error);
        }
        
        if (request.getParameter("date") != null && request.getParameter("date").length() != 0) {
            LocalDate ld = LocalDate.parse(request.getParameter("date"));
           try{
               sightingService.validateDate(ld);
           }catch(InvalidDataException ex){
                FieldError error = new FieldError("sighting","date", ex.getMessage());
            result.addError(error);
           }
            sighting.setDate(ld);
        }
        
          
        
        if (result.hasErrors()) {
            List<Sighting> sightings = sightingService.getAllSightings();
            model.addAttribute("sightings", sightings);
            List<Hero> heroes = heroService.getAllHeroes();
            List<Location> locations = locationService.getAllLocations();
            model.addAttribute("heroes", heroes);
            model.addAttribute("locations", locations);
            model.addAttribute("sighting",sighting);
            return "sightings";
        }
        sightingService.addSighting(sighting);
        return "redirect:/sightings";
    }

    @GetMapping("deleteSighting")
    public String deleteSighting(HttpServletRequest request, Model model) {
        int sightingId = Integer.parseInt(request.getParameter("id"));
        Sighting sighting = sightingService.getSightingById(sightingId);
        model.addAttribute("sighting", sighting);
        return "deleteSighting";
    }

    @PostMapping("deleteSighting")
    public String performDeleteSighting(HttpServletRequest request) {
        int sightingId = Integer.parseInt(request.getParameter("id"));
        sightingService.deleteSightingById(sightingId);
        return "redirect:/sightings";

    }

    @GetMapping("editSighting")
    public String editSighting(HttpServletRequest request, Model model) {
        int sightingId = Integer.parseInt(request.getParameter("id"));
        Sighting sighting = sightingService.getSightingById(sightingId);
        model.addAttribute("sighting", sighting);

        List<Hero> heroes = heroService.getAllHeroes();
        List<Location> locations = locationService.getAllLocations();
        model.addAttribute("heroes", heroes);
        model.addAttribute("locations", locations);
        return "editSighting";
    }

    @PostMapping("editSighting")
    public String performEditSighting(@Valid Sighting sighting, BindingResult result, Model model, HttpServletRequest request) { //(HttpServletRequest request, Model model)

        String heroId = request.getParameter("heroId");

        Hero hero = heroService.getHeroById(Integer.parseInt(heroId));
        sighting.setHero(hero);

        String locationId = request.getParameter("locationId");

        Location location = locationService.getLocationById(Integer.parseInt(locationId));
        sighting.setLocation(location);

        if (request.getParameter("date") != null && request.getParameter("date").length() != 0) {
            LocalDate ld = LocalDate.parse(request.getParameter("date"));
            try {
                sightingService.validateDate(ld);
            } catch (InvalidDataException ex) {
                FieldError error = new FieldError("sighting", "date", ex.getMessage());
                result.addError(error);
            }

        }

        if (result.hasErrors()) {
            model.addAttribute("heroes", heroService.getAllHeroes());
            model.addAttribute("locations", locationService.getAllLocations());
            model.addAttribute("sighting", sighting);
            return "editsighting";
        }

        List<Sighting> sightings = sightingService.getSightingsByDate(sighting.getDate());
        model.addAttribute("sightings", sightings);
        model.addAttribute("sighting", sighting);
        sightingService.updateSighting(sighting);
        return "detailsSighting";
    }

    @GetMapping("detailsSighting")
    public String detailSighting(HttpServletRequest request, Model model) {
        int sightingId = Integer.parseInt(request.getParameter("id"));

        Sighting sighting = sightingService.getSightingById(sightingId);
        model.addAttribute("sighting", sighting);

        List<Sighting> sightings = sightingService.getSightingsByDate(sighting.getDate());
        model.addAttribute("sightings", sightings);

        List<Hero> heroes = heroService.getAllHeroes();
        model.addAttribute("heroes", heroes);
        return "detailsSighting";
    }

    @GetMapping("index") //Go to index html page
    public String recentSightings(Model model) {
        List<Sighting> sightings = sightingService.getAllSightings();

        List<Sighting> recentSightings = sightings.stream()
                .sorted(Comparator.comparing(Sighting::getDate).reversed()) //order by date from most recent to oldest
                .limit(10) //get the 10 first sightings
                .collect(Collectors.toList());

        List<Hero> heroes = heroService.getAllHeroes();
        //List<Superpower> superpowers = superpowerService.getAllSuperpowers();
       // List<Organization> organizations = organizationService.getAllOrganizations();
        model.addAttribute("sightings", recentSightings);
        model.addAttribute("heroes", heroes);
       // model.addAttribute("superpowers", superpowers);
       // model.addAttribute("organizations", organizations);

        return "index";
    }

}
