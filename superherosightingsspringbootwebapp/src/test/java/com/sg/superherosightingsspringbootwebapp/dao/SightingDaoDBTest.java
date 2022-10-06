/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.superherosightingsspringbootwebapp.dao;

import com.sg.superherosightingsspringbootwebapp.entities.Hero;
import com.sg.superherosightingsspringbootwebapp.entities.Location;
import com.sg.superherosightingsspringbootwebapp.entities.Organization;
import com.sg.superherosightingsspringbootwebapp.entities.Sighting;
import com.sg.superherosightingsspringbootwebapp.entities.Superpower;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 *
 * @author Yash
 */
@SpringBootTest
public class SightingDaoDBTest {
    
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
    
    public SightingDaoDBTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
        
        List<Hero> heroes = heroDao.getAllHeroes();
        for (Hero hero : heroes) {
            heroDao.deleteHeroById(hero.getHeroId());
        }

        List<Location> locations = locationDao.getAllLocations();
        for (Location location : locations) {
            locationDao.deleteLocationById(location.getLocationId());
        }

        List<Organization> organizations = organizationDao.getAllOrganizations();
        for (Organization organization : organizations) {
            organizationDao.deleteOrganizationById(organization.getOrganizationId());
        }

        List<Sighting> sightings = sightingDao.getAllSightings();
        for (Sighting sighting : sightings) {
            sightingDao.deleteSightingById(sighting.getSightingId());
        }

        List<Superpower> superpowers = superpowerDao.getAllSuperpowers();
        for (Superpower superpower : superpowers) {
            superpowerDao.deleteSuperpowerById(superpower.getSuperpowerId());
        }
    }
    
    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of getSightingById method, of class SightingDaoDB.
     */
    @Test
    public void testAddGetSightingById() {
        //Creating test input
        Superpower superpower = new Superpower();
        superpower.setName("Magic");
        superpower.setDescription("Weird magic");
        superpower = superpowerDao.addSuperpower(superpower);

        Organization organization = new Organization();
        organization.setIsHero(true);
        organization.setName("Superhero Organization");
        organization.setDescription("Big");
        organization.setAddress("420 Sage Bluff, Calgary");
        organization.setContact("4034674567");
        organization = organizationDao.addOrganization(organization);

        List<Organization> organizationList = new ArrayList<>();
        organizationList.add(organization);

        Hero hero = new Hero();
        hero.setIsHero(true);
        hero.setName("Widerman");
        hero.setDescription("Tall");
        hero.setSuperpower(superpower);
        hero.setOrganizations(organizationList);
        hero = heroDao.addHero(hero);

        Location location = new Location();
        location.setName("Calgary");
        location.setDescription("Mountains");
        location.setAddress("Canada");
        location.setLatitude("51.049999");
        location.setLongitude("114.066666");
        location = locationDao.addLocation(location);

        //sighting
        Sighting sighting = new Sighting();
        sighting.setHero(hero);
        sighting.setLocation(location);
        LocalDate date = LocalDate.parse("2022-02-09");
        sighting.setDate(date);
        sighting = sightingDao.addSighting(sighting);

        //retrieve it from dao
        Sighting fromDao = sightingDao.getSightingById(sighting.getSightingId());

        //Assert
        assertEquals(sighting, fromDao);

    }

    /**
     * Test of getAllSightings method, of class SightingDaoDB.
     */
    @Test
    public void testGetAllSightings() {
        //Creating test inputs
        Superpower superpower1 = new Superpower();
        superpower1.setName("Magic");
        superpower1.setDescription("Weird magic");
        superpower1 = superpowerDao.addSuperpower(superpower1);

        Superpower superpower2 = new Superpower();
        superpower2.setName("Fly");
        superpower2.setDescription("High speed");
        superpower2 = superpowerDao.addSuperpower(superpower2);

        Organization organization = new Organization();
        organization.setIsHero(true);
        organization.setName("Superhero Organization");
        organization.setDescription("Big");
        organization.setAddress("420 Sage Bluff, Calgary");
        organization.setContact("4034674567");
        organization = organizationDao.addOrganization(organization);

        List<Organization> organizationList = new ArrayList<>();
        organizationList.add(organization);

        Hero hero1 = new Hero();
        hero1.setIsHero(true);
        hero1.setName("Widerman");
        hero1.setDescription("Tall");
        hero1.setSuperpower(superpower1);
        hero1.setOrganizations(organizationList);
        hero1 = heroDao.addHero(hero1);

        Hero hero2 = new Hero();
        hero2.setIsHero(true);
        hero2.setName("Widerman");
        hero2.setDescription("Tall");
        hero2.setSuperpower(superpower2);
        hero2.setOrganizations(organizationList);
        hero2 = heroDao.addHero(hero2);

        Location location1 = new Location();
        location1.setName("Calgary");
        location1.setDescription("Mountains");
        location1.setAddress("Canada");
        location1.setLatitude("51.049999");
        location1.setLongitude("114.066666");
        location1 = locationDao.addLocation(location1);

        Location location2 = new Location();
        location2.setName("Niagra Falls");
        location2.setDescription("falls");
        location2.setAddress("Canada");
        location2.setLatitude("91.049999");
        location2.setLongitude("104.066666");
        location2 = locationDao.addLocation(location2);
        
        //sighting1
        Sighting sighting1 = new Sighting();
        sighting1.setHero(hero1);
        sighting1.setLocation(location1);
        LocalDate date1 = LocalDate.parse("2022-02-09");
        sighting1.setDate(date1);
        sighting1 = sightingDao.addSighting(sighting1);
        
        
        //sighting2
        Sighting sighting2 = new Sighting();
        sighting2.setHero(hero2);
        sighting2.setLocation(location2);
        LocalDate date2 = LocalDate.parse("2022-03-09");
        sighting2.setDate(date2);
        sighting2 = sightingDao.addSighting(sighting2);
        
        //retrieve a list of sightings
        List<Sighting> sightings = sightingDao.getAllSightings();
        
        //Assert
        assertEquals(2, sightings.size());
        assertTrue(sightings.contains(sighting1));
        assertTrue(sightings.contains(sighting2));

    }

    /**
     * Test of updateSighting method, of class SightingDaoDB.
     */
    @Test
    public void testUpdateSighting() {
        //creating test inputs
        Superpower superpower = new Superpower();
        superpower.setName("Magic");
        superpower.setDescription("Weird magic");
        superpower = superpowerDao.addSuperpower(superpower);

        Organization organization = new Organization();
        organization.setIsHero(true);
        organization.setName("Superhero Organization");
        organization.setDescription("Big");
        organization.setAddress("420 Sage Bluff, Calgary");
        organization.setContact("4034674567");
        organization = organizationDao.addOrganization(organization);

        List<Organization> organizationList = new ArrayList<>();
        organizationList.add(organization);

        Hero hero = new Hero();
        hero.setIsHero(true);
        hero.setName("Widerman");
        hero.setDescription("Tall");
        hero.setSuperpower(superpower);
        hero.setOrganizations(organizationList);
        hero = heroDao.addHero(hero);

        Location location1 = new Location();
        location1.setName("Calgary");
        location1.setDescription("Mountains");
        location1.setAddress("Canada");
        location1.setLatitude("51.049999");
        location1.setLongitude("114.066666");
        location1 = locationDao.addLocation(location1);

        Location location2 = new Location();
        location2.setName("Niagra Falls");
        location2.setDescription("falls");
        location2.setAddress("Canada");
        location2.setLatitude("91.049999");
        location2.setLongitude("104.066666");
        location2 = locationDao.addLocation(location2);
        
        //sighting
        Sighting sighting = new Sighting();
        sighting.setHero(hero);
        sighting.setLocation(location1);
        LocalDate date = LocalDate.parse("2022-02-09");
        sighting.setDate(date);
        sighting = sightingDao.addSighting(sighting);

        Sighting fromDao = sightingDao.getSightingById(sighting.getSightingId());
        
        //Assert
        assertEquals(sighting, fromDao);
       
        sighting.setLocation(location2);
        
        //update sighting
        sightingDao.updateSighting(sighting);
        
        //assert
        assertNotEquals(sighting, fromDao);

        fromDao = sightingDao.getSightingById(sighting.getSightingId());
        
        //assert
        assertEquals(sighting, fromDao);
    }

    /**
     * Test of deleteSightingById method, of class SightingDaoDB.
     */
    @Test
    public void testDeleteSightingById() {
        //creating test inputs
        Superpower superpower = new Superpower();
        superpower.setName("Magic");
        superpower.setDescription("Weird magic");
        superpower = superpowerDao.addSuperpower(superpower);

        Organization organization = new Organization();
        organization.setIsHero(true);
        organization.setName("Superhero Organization");
        organization.setDescription("Big");
        organization.setAddress("420 Sage Bluff, Calgary");
        organization.setContact("4034674567");
        organization = organizationDao.addOrganization(organization);

        List<Organization> organizationList = new ArrayList<>();
        organizationList.add(organization);

        Hero hero = new Hero();
        hero.setIsHero(true);
        hero.setName("Widerman");
        hero.setDescription("Tall");
        hero.setSuperpower(superpower);
        hero.setOrganizations(organizationList);
        hero = heroDao.addHero(hero);

        Location location = new Location();
        location.setName("Calgary");
        location.setDescription("Mountains");
        location.setAddress("Canada");
        location.setLatitude("51.049999");
        location.setLongitude("114.066666");
        location = locationDao.addLocation(location);

        //sighting
        Sighting sighting = new Sighting();
        sighting.setHero(hero);
        sighting.setLocation(location);
        LocalDate date = LocalDate.parse("2022-02-09");
        sighting.setDate(date);
        sighting = sightingDao.addSighting(sighting);

        //deleting sighting
        sightingDao.deleteSightingById(sighting.getSightingId());

        Sighting fromDao = sightingDao.getSightingById(sighting.getSightingId());

        //assert
        assertNull(fromDao);

    }

    /**
     * Test of getSightingsByDate method, of class SightingDaoDB.
     */
    @Test
    public void testGetSightingsByDate() {
        //creating test inputs
        Superpower superpower = new Superpower();
        superpower.setName("Magic");
        superpower.setDescription("Weird magic");
        superpower = superpowerDao.addSuperpower(superpower);

        Organization organization = new Organization();
        organization.setIsHero(true);
        organization.setName("Superhero Organization");
        organization.setDescription("Big");
        organization.setAddress("420 Sage Bluff, Calgary");
        organization.setContact("4034674567");
        organization = organizationDao.addOrganization(organization);

        List<Organization> organizationList = new ArrayList<>();
        organizationList.add(organization);

        Hero hero = new Hero();
        hero.setIsHero(true);
        hero.setName("Widerman");
        hero.setDescription("Tall");
        hero.setSuperpower(superpower);
        hero.setOrganizations(organizationList);
        hero = heroDao.addHero(hero);

        List<Hero> heroList = new ArrayList<>();
        heroList.add(hero);

        Location location = new Location();
        location.setName("Calgary");
        location.setDescription("Mountains");
        location.setAddress("Canada");
        location.setLatitude("51.049999");
        location.setLongitude("114.066666");
        location = locationDao.addLocation(location);
        
        //sighting
        Sighting sighting = new Sighting();
        sighting.setHero(hero);
        sighting.setLocation(location);
        LocalDate date = LocalDate.parse("2022-02-09");
        sighting.setDate(date);
        sighting = sightingDao.addSighting(sighting);
        
        //list of sightings by date
        List<Sighting> listFromDao = sightingDao.getSightingsByDate(LocalDate.parse("2022-02-09"));

        assertEquals(1, listFromDao.size());
        assertTrue(listFromDao.contains(sighting));

    }

    
}
