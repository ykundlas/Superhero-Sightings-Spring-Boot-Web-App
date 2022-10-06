/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.superherosightingsspringbootwebapp.service;

import com.sg.superherosightingsspringbootwebapp.entities.Hero;
import com.sg.superherosightingsspringbootwebapp.entities.Location;
import java.util.List;

/**
 *
 * @author Yash
 */
public interface LocationService {
    
    //This method returns a location associated with a locationId.
    Location getLocationById(int locationId);

    //This method returns a list of locations.
    List<Location> getAllLocations();

    //This method returns a location and returns it.
    Location addLocation(Location location);

    //This method updates a location in the database.
    void updateLocation(Location location);

    //This method deletes a location associated with a locationId.
    void deleteLocationById(int locationId);

    //This method returns a list of locations where a hero is sighted.
    List<Location> getLocationsForHero(Hero hero);

    void validateLocation(Location location) throws DuplicateNameException;

    void validateLatitudeLongitude(Location location) throws InvalidDataException;

}
