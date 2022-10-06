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
import com.sg.superherosightingsspringbootwebapp.entities.Superpower;
import java.util.HashSet;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Yash
 */
@Service
public class SuperpowerServiceImpl implements SuperpowerService {

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
    public Superpower getSuperpowerById(int superpowerId) {
        return superpowerDao.getSuperpowerById(superpowerId);

    }

    @Override
    public List<Superpower> getAllSuperpowers() {
        return superpowerDao.getAllSuperpowers();
    }

    @Override
    public void validateSuperPower(Superpower superpower) throws DuplicateNameException {
        List<Superpower> superpowers = superpowerDao.getAllSuperpowers();
        boolean isDuplicate = false;

        for (Superpower fromList : superpowers) {
            if (fromList.getName().toLowerCase().equals(superpower.getName().toLowerCase())) {
                isDuplicate = true;
            }
            if (isDuplicate) {
                throw new DuplicateNameException("Power Name Already Exists");
            }
        }

    }

    @Override
    public Superpower addSuperpower(Superpower superpower) {

        return superpowerDao.addSuperpower(superpower);
    }

    @Override
    public void updateSuperpower(Superpower superpower) {

        superpowerDao.updateSuperpower(superpower);

    }

    @Override
    public void deleteSuperpowerById(int superpowerId) {
        superpowerDao.deleteSuperpowerById(superpowerId);
    }

    @Override
    public Superpower getSuperpowerForHero(Hero hero) {
        return superpowerDao.getSuperpowerForHero(hero);
    }

}
