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
import com.sg.superherosightingsspringbootwebapp.entities.Sighting;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Yash
 */
@Service
public class SightingServiceImpl implements SightingService {

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
    public Sighting getSightingById(int sightingId) {
        return sightingDao.getSightingById(sightingId);
    }

    @Override
    public List<Sighting> getAllSightings() {
        return sightingDao.getAllSightings();
    }

    @Override
    public Sighting addSighting(Sighting sightng) {
        return sightingDao.addSighting(sightng);
    }

    @Override
    public void updateSighting(Sighting sighting) {
        sightingDao.updateSighting(sighting);
    }

    @Override
    public void deleteSightingById(int sightingId) {
        sightingDao.deleteSightingById(sightingId);
    }

    @Override
    public List<Sighting> getSightingsByDate(LocalDate date) {
        return sightingDao.getSightingsByDate(date);
    }

    @Override
    public void validateDate(LocalDate ld) throws InvalidDataException {
        LocalDate today = LocalDate.now();

        if (today.isBefore(ld)) {
            throw new InvalidDataException("Date can't be in future");
        }

    }

}
