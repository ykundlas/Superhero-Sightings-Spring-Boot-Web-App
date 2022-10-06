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
import com.sg.superherosightingsspringbootwebapp.entities.Organization;
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
public class OrganizationController {

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

    //Set<ConstraintViolation<Organization>> violations = new HashSet<>();
    @GetMapping("organizations")
    public String displayOrganizations(Model model) {
        List<Organization> organizations = organizationService.getAllOrganizations();

        model.addAttribute("organizations", organizations);
        Organization organization = new Organization();
        model.addAttribute("organization", organization);

        return "organizations";
    }

    @PostMapping("addOrganization")
    public String addOrganization(@Valid @ModelAttribute("organization") Organization organization, BindingResult result, Model model, HttpServletRequest request) {// (HttpServletRequest request){
        String isHero = request.getParameter("isHero");
        if (isHero != null) {

            organization.setIsHero(Boolean.parseBoolean(isHero));
        } else {
            FieldError error = new FieldError("organization", "isHero", "Must include one type");
            result.addError(error);

        }

        try {
            organizationService.validateOrganization(organization);
        } catch (DuplicateNameException ex) {
            FieldError error = new FieldError("organization", "name", ex.getMessage());
            result.addError(error);
        }
        String contact = request.getParameter("contact");

        try {
            organizationService.validateContact(contact);
        } catch (InvalidDataException ex) {
            FieldError error = new FieldError("location", "contact", ex.getMessage());
            result.addError(error);
        }

        if (result.hasErrors()) {
            List<Organization> organizations = organizationService.getAllOrganizations();
            model.addAttribute("organizations", organizations);
            model.addAttribute("organization", organization);
            return "organizations";
        }
        organizationService.addOrganization(organization);
        return "redirect:/organizations";
    }

    @GetMapping("deleteOrganization")
    public String deleteOrganization(HttpServletRequest request, Model model) {
        int organizationId = Integer.parseInt(request.getParameter("id"));
        Organization organization = organizationService.getOrganizationById(organizationId);

        model.addAttribute("organization", organization);
        return "deleteOrganization";
    }

    @PostMapping("deleteOrganization")
    public String performDeleteOrganization(HttpServletRequest request) {
        int superpowerId = Integer.parseInt(request.getParameter("id"));
        organizationService.deleteOrganizationById(superpowerId);
        return "redirect:/organizations";

    }

    @GetMapping("editOrganization")
    public String editOrganization(HttpServletRequest request, Model model) {
        int organizationId = Integer.parseInt(request.getParameter("id"));
        Organization organization = organizationService.getOrganizationById(organizationId);

        model.addAttribute("organization", organization);
        return "editOrganization";
    }

    @PostMapping("editOrganization")
    public String performEditOrganization(@Valid Organization organization, BindingResult result, Model model, HttpServletRequest request) { //(HttpServletRequest request, Model model)

        String isHero = request.getParameter("isHero");

        organization.setIsHero(Boolean.parseBoolean(isHero));

        String contact = request.getParameter("contact");
        try {
            organizationService.validateContact(contact);
        } catch (InvalidDataException ex) {
            FieldError error = new FieldError("location", "contact", ex.getMessage());
            result.addError(error);
        }

        if (result.hasErrors()) {
            return "editOrganization";
        }
        organizationService.updateOrganization(organization);
        model.addAttribute("organization", organization);

        List<Hero> heroes = heroService.getMembersOfOrganization(organization);
        model.addAttribute("heroes", heroes);

        return "detailsOrganization";
    }

    @GetMapping("detailsOrganization")
    public String detailsOrganization(HttpServletRequest request, Model model) {
        int organizationId = Integer.parseInt(request.getParameter("id"));
        Organization organization = organizationService.getOrganizationById(organizationId);

        model.addAttribute("organization", organization);

        List<Hero> heroes = heroService.getMembersOfOrganization(organization);
        model.addAttribute("heroes", heroes);
        return "detailsOrganization";
    }

}
