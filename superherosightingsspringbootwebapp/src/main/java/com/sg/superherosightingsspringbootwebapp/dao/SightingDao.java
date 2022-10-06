/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.superherosightingsspringbootwebapp.dao;

import com.sg.superherosightingsspringbootwebapp.entities.Sighting;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author Yash
 */
public interface SightingDao {
    
    //This method returns a sighting associated with a sightingId.
    Sighting getSightingById(int sightingId);
    
    //This method returns a list of all sightings.
    List<Sighting> getAllSightings();
    
    //This method adds a sighting to the database.
    Sighting addSighting(Sighting sightng);
    
    //This method updates a sighting in the database.
    void updateSighting(Sighting sighting);
    
    //This method deletes a sighting associated with a sightingnId.
    void deleteSightingById(int sightingId);
    
    //This method returns a list of sightings on a date.
    List<Sighting> getSightingsByDate(LocalDate date);
    
}
