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
import com.sg.superherosightingsspringbootwebapp.service.DuplicateNameException;
import com.sg.superherosightingsspringbootwebapp.service.HeroService;
import com.sg.superherosightingsspringbootwebapp.service.InvalidDataException;
import com.sg.superherosightingsspringbootwebapp.service.LocationService;
import com.sg.superherosightingsspringbootwebapp.service.OrganizationService;
import com.sg.superherosightingsspringbootwebapp.service.SightingService;
import com.sg.superherosightingsspringbootwebapp.service.SuperpowerService;
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
public class LocationController {

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

    Set<ConstraintViolation<Location>> violations = new HashSet<>();
    String latitudeError = null;
    String longitudeError = null;

    @GetMapping("locations")
    public String displayLocations(Model model) {
        List<Location> locations = locationService.getAllLocations();
        model.addAttribute("locations", locations);
        Location location = new Location();
        model.addAttribute("location", location);
        model.addAttribute("latitudeError", latitudeError);
        model.addAttribute("longitudeError", longitudeError);
        return "locations";
    }

    @PostMapping("addLocation")
    public String addLocation(@Valid @ModelAttribute("location") Location location, BindingResult result, Model model, HttpServletRequest request) {   

        try {
            locationService.validateLocation(location);
        } catch (DuplicateNameException ex) {
            FieldError error = new FieldError("location", "name", ex.getMessage());
            result.addError(error);
        }

        try {
            locationService.validateLatitudeLongitude(location);
        } catch (InvalidDataException ex) {
            FieldError error1 = new FieldError("location", "latitude", ex.getMessage());
            FieldError error2 = new FieldError("location", "longitude", ex.getMessage());
            result.addError(error1);
            result.addError(error2);
        }

        if (result.hasErrors()) {
            List<Location> locations = locationService.getAllLocations();
            model.addAttribute("locations", locations);
            model.addAttribute("location", location);
            return "locations";
        }
        locationService.addLocation(location);
        return "redirect:/locations";
    }

    @GetMapping("deleteLocation")
    public String deleteLocation(HttpServletRequest request, Model model) {
        int locationId = Integer.parseInt(request.getParameter("id"));
        Location location = locationService.getLocationById(locationId);

        model.addAttribute("location", location);

        return "deleteLocation";
    }

    @PostMapping("deleteLocation")
    public String performDeleteLocation(HttpServletRequest request) {
        int locationId = Integer.parseInt(request.getParameter("id"));
        locationService.deleteLocationById(locationId);
        return "redirect:/locations";

    }

    @GetMapping("editLocation")
    public String editLocation(HttpServletRequest request, Model model) {
        int locationId = Integer.parseInt(request.getParameter("id"));
        Location location = locationService.getLocationById(locationId);

        model.addAttribute("location", location);
        return "editLocation";
    }

    @PostMapping("editLocation")
    public String performEditLocation(@Valid Location location, BindingResult result, Model model) {  //(HttpServletRequest request, Model model)
        try {
            locationService.validateLatitudeLongitude(location);
        } catch (InvalidDataException ex) {
            FieldError error1 = new FieldError("location", "latitude", ex.getMessage());
            FieldError error2 = new FieldError("location", "longitude", ex.getMessage());
            result.addError(error1);
            result.addError(error2);
        }
        if (result.hasErrors()) {
            model.addAttribute("location", location);
            return "editLocation";
        }

        locationService.updateLocation(location);
        model.addAttribute("location", location);

        List<Hero> heroes = heroService.getHeroesatLocation(location);
        model.addAttribute("heroes", heroes);
        return "detailsLocation";
    }

    @GetMapping("detailsLocation")
    public String detailsLocation(HttpServletRequest request, Model model) {
        int locationId = Integer.parseInt(request.getParameter("id"));
        Location location = locationService.getLocationById(locationId);

        model.addAttribute("location", location);
        List<Hero> heroes = heroService.getHeroesatLocation(location);
        model.addAttribute("heroes", heroes);
        return "detailsLocation";
    }

}
