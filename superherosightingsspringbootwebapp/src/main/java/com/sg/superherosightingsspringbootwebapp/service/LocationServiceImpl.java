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
import java.util.HashSet;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Yash
 */
@Service
public class LocationServiceImpl implements LocationService {

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
    public Location getLocationById(int locationId) {
        return locationDao.getLocationById(locationId);
    }

    @Override
    public List<Location> getAllLocations() {
        return locationDao.getAllLocations();
    }

    @Override
    public Location addLocation(Location location) {

        return locationDao.addLocation(location);
    }

    @Override
    public void updateLocation(Location location) {
        locationDao.updateLocation(location);
    }

    @Override
    public void deleteLocationById(int locationId) {
        locationDao.deleteLocationById(locationId);
    }

    @Override
    public List<Location> getLocationsForHero(Hero hero) {
        return locationDao.getLocationsForHero(hero);
    }

    @Override
    public void validateLocation(Location location) throws DuplicateNameException {
        List<Location> locations = locationDao.getAllLocations();
        boolean isDuplicate = false;

        for (Location fromList : locations) {
            if (fromList.getName().toLowerCase().equals(location.getName().toLowerCase())) {
                isDuplicate = true;
            }
        }
        if (isDuplicate) {
            throw new DuplicateNameException("Location Name Already Exists");
        }
    }

    @Override
    public void validateLatitudeLongitude(Location location) throws InvalidDataException {
        String latitude = "^-?([1-8]?[1-9]|[1-9]0)\\.{1}\\d{1,6}";

        String longitude = "^-?([1]?[1-7][1-9]|[1]?[1-8][0][1-9]?[0-9])\\.{1}\\d{1,6}";

        if (!location.getLatitude().matches(latitude)) {
            throw new InvalidDataException("Please Enter Valid Latitude");
        }

        if (!location.getLongitude().matches(longitude)) {
            throw new InvalidDataException("Please Enter Valid Latitude");
        }
    }

}
