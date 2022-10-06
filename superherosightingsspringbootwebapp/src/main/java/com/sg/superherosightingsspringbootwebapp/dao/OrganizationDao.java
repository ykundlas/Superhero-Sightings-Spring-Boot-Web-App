/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.superherosightingsspringbootwebapp.dao;

import com.sg.superherosightingsspringbootwebapp.entities.Hero;
import com.sg.superherosightingsspringbootwebapp.entities.Organization;
import java.util.List;

/**
 *
 * @author Yash
 */
public interface OrganizationDao {
    
     //This method returns an organization associated with an organizationId.
    Organization getOrganizationById(int organizationId);

    //This method returns a list of organizations.
    List<Organization> getAllOrganizations();

    //This method adds an organization to the database.
    Organization addOrganization(Organization Organization);

    //This method updates an organization in the database.
    void updateOrganization(Organization organization);

    //This method deletes an organization associated with a organizationId.
    void deleteOrganizationById(int organizationId);

    //This method returns a list of organizations associated with a hero.
    List<Organization> getOrganizationsForHero(Hero hero);
    
}
