/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.superherosightingsspringbootwebapp.service;

import com.sg.superherosightingsspringbootwebapp.dao.HeroDao;
import com.sg.superherosightingsspringbootwebapp.dao.LocationDao;
import com.sg.superherosightingsspringbootwebapp.dao.OrganizationDao;
import com.sg.superherosightingsspringbootwebapp.dao.SightingDao;
import com.sg.superherosightingsspringbootwebapp.dao.SuperpowerDao;
import com.sg.superherosightingsspringbootwebapp.entities.Hero;
import com.sg.superherosightingsspringbootwebapp.entities.Location;
import com.sg.superherosightingsspringbootwebapp.entities.Organization;
import java.util.HashSet;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Yash
 */
@Service
public class OrganizationServiceImpl implements OrganizationService {

    @Autowired
    HeroDao heroDao;

    @Autowired
    LocationDao locationDao;

    @Autowired
    OrganizationDao organizationDao;

    @Autowired
    SightingDao sightingDao;

    @Autowired
    SuperpowerDao superpowerDao;

    @Override
    public Organization getOrganizationById(int organizationId) {
        return organizationDao.getOrganizationById(organizationId);
    }

    @Override
    public List<Organization> getAllOrganizations() {
        return organizationDao.getAllOrganizations();
    }

    @Override
    public Organization addOrganization(Organization organization) {
        HashSet<String> organizations = new HashSet<String>();
        List<Organization> list = organizationDao.getAllOrganizations();
        for (Organization fromList : list) {
            organizations.add(fromList.getName());
        }
        if (organizations.contains(organization.getName())) {
            return null;
        }
        return organizationDao.addOrganization(organization);
    }

    @Override
    public void updateOrganization(Organization organization) {
        organizationDao.updateOrganization(organization);
    }

    @Override
    public void deleteOrganizationById(int organizationId) {
        organizationDao.deleteOrganizationById(organizationId);
    }

    @Override
    public List<Organization> getOrganizationsForHero(Hero hero) {
        return organizationDao.getOrganizationsForHero(hero);
    }

    @Override
    public void validateContact(String contact) throws InvalidDataException {

        if (!contact.matches("[0-9]{3}[-]{1}[0-9]{3}[-]{1}[0-9]{4}")) {
            throw new InvalidDataException("Please enter valid phone number.");//return true;
        }
    }

    @Override
    public void validateOrganization(Organization organization) throws DuplicateNameException {
        List<Organization> organizations = organizationDao.getAllOrganizations();
        boolean isDuplicate = false;

        for (Organization fromList : organizations) {
            if (fromList.getName().toLowerCase().equals(organization.getName().toLowerCase())) {
                isDuplicate = true;
            }
            if (isDuplicate) {
                throw new DuplicateNameException("Organization Name Already Exists");
            }
        }
    }

}
