/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.superherosightingsspringbootwebapp.service;

import com.sg.superherosightingsspringbootwebapp.entities.Hero;
import com.sg.superherosightingsspringbootwebapp.entities.Superpower;
import java.util.List;

/**
 *
 * @author Yash
 */
public interface SuperpowerService {

    //This method returns a superpower associated with a superpowerId.
    Superpower getSuperpowerById(int superpowerId);

    //This method returns a list of all superpowers.
    List<Superpower> getAllSuperpowers();

    //This method adds a superpower to the database.
    Superpower addSuperpower(Superpower superpower);

    //This method updates a superpower in the database.
    void updateSuperpower(Superpower superpower);

    //This method deletes a superpower associated with a superpowerId.
    void deleteSuperpowerById(int superpowerId);

    Superpower getSuperpowerForHero(Hero hero);
    
    void validateSuperPower(Superpower superpower) throws DuplicateNameException;

}
