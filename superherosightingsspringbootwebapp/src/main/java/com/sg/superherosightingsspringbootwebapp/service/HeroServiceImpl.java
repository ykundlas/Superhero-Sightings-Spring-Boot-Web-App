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
public class HeroServiceImpl implements HeroService {

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
    public Hero getHeroById(int heroId) {
        return heroDao.getHeroById(heroId);
    }

    @Override
    public List<Hero> getAllHeroes() {
        return heroDao.getAllHeroes();
    }

    @Override
    public Hero addHero(Hero hero) {

        HashSet<String> set = new HashSet<String>();
        List<Hero> list = heroDao.getAllHeroes();
        for (Hero fromList : list) {
            set.add(fromList.getName());
        }
        if (set.contains(hero.getName())) {
            return null;
        }

        return heroDao.addHero(hero);

    }

    @Override
    public void updateHero(Hero hero) {
        heroDao.updateHero(hero);
    }

    @Override
    public void deleteHeroById(int heroId) {
        heroDao.deleteHeroById(heroId);
    }

    @Override
    public List<Hero> getHeroesatLocation(Location location) {
        return heroDao.getHeroesatLocation(location);
    }

    @Override
    public List<Hero> getMembersOfOrganization(Organization organization) {
        return heroDao.getMembersOfOrganization(organization);
    }

    @Override
    public List<Hero> getHeroesForSuperpower(Superpower superpower) {
        return heroDao.getHeroesForSuperpower(superpower);
    }

    @Override
    public void validateHero(Hero hero) throws DuplicateNameException {
        List<Hero> heroes = heroDao.getAllHeroes();
        boolean isDuplicate = false;

        for (Hero fromList : heroes) {
            if (fromList.getName().toLowerCase().equals(hero.getName().toLowerCase())) {
                isDuplicate = true;
            }
            if (isDuplicate) {
                throw new DuplicateNameException("Hero Name Already Exists");
            }
        }
    }

}
