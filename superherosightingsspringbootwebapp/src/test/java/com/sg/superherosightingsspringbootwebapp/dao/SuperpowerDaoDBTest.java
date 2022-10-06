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
public class SuperpowerDaoDBTest {
    
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
    
    public SuperpowerDaoDBTest() {
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
        for ( Organization organization : organizations) {
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
     * Test of getSuperpowerById method, of class SuperpowerDaoDB.
     */
    @Test
    public void testAddGetSuperpowerById() {
        //creating test inputs
        Superpower superpower = new Superpower();
        superpower.setName("Magic");
        superpower.setDescription("Weird magic");
        superpower = superpowerDao.addSuperpower(superpower);
        
        //retreiving superpower from dao
        Superpower fromDao =  superpowerDao.getSuperpowerById(superpower.getSuperpowerId());
        
        //assert
        assertEquals(superpower, fromDao);
        
        
    }

    /**
     * Test of getAllSuperpowers method, of class SuperpowerDaoDB.
     */
    @Test
    public void testGetAllSuperpowers() {
         //creating test inputs
         
        //superpower1
        Superpower superpower1 = new Superpower();
        superpower1.setName("Magic");
        superpower1.setDescription("Weird magic");
        superpower1 = superpowerDao.addSuperpower(superpower1);
        
        //superpower2
        Superpower superpower2 = new Superpower();
        superpower2.setName("Fly");
        superpower2.setDescription("High speed");
        superpower2 = superpowerDao.addSuperpower(superpower2);
        
        //retreiving list of superpowers
        List<Superpower> superpowers = superpowerDao.getAllSuperpowers();
        
        //assert
        assertEquals(2, superpowers.size());
        assertTrue(superpowers.contains(superpower1));
        assertTrue(superpowers.contains(superpower2));
    }

    
    /**
     * Test of updateSuperpower method, of class SuperpowerDaoDB.
     */
    @Test
    public void testUpdateSuperpower() {
        //creating test inputs
        Superpower superpower = new Superpower();
        superpower.setName("Magic");
        superpower.setDescription("Weird magic");
        superpower = superpowerDao.addSuperpower(superpower);
        
        //retreiving superpower from dao
        Superpower fromDao =  superpowerDao.getSuperpowerById(superpower.getSuperpowerId());
        
        //assert
        assertEquals(superpower, fromDao);
        
        //change the name of superpower
        superpower.setName("Fly");
        superpower.setDescription("high");
        //updating the name
        superpowerDao.updateSuperpower(superpower);
        
        //assert
        assertNotEquals(superpower, fromDao);
        
        //retreive superpower
        fromDao =  superpowerDao.getSuperpowerById(superpower.getSuperpowerId());
        
        //assert
        assertEquals(superpower, fromDao);
    }

    /**
     * Test of deleteSuperpowerById method, of class SuperpowerDaoDB.
     */
    @Test
    public void testDeleteSuperpowerById() {
        //creating test inputs
        Superpower superpower = new Superpower();
        superpower.setName("Magic");
        superpower.setDescription("Weird magic");
        superpower = superpowerDao.addSuperpower(superpower);
        
        Superpower fromDao =  superpowerDao.getSuperpowerById(superpower.getSuperpowerId());
        
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
        
       
        
        Location location= new Location();
        location.setName("Calgary");
        location.setDescription("Mountains");
        location.setAddress("Canada");
        location.setLatitude("51.049999");
        location.setLongitude("114.066666");
        location= locationDao.addLocation(location);
        
        Sighting sighting = new Sighting();
        sighting.setHero(hero);
        sighting.setLocation(location);
        LocalDate date = LocalDate.of(2022,2,9);
        sighting.setDate(date);
        sighting= sightingDao.addSighting(sighting);
        
       
        //assert
        assertEquals(superpower, fromDao);
        
        //deleting superpower by Id
        superpowerDao.deleteSuperpowerById(superpower.getSuperpowerId());
        
        //retreive it again
        fromDao = superpowerDao.getSuperpowerById(superpower.getSuperpowerId());
        
        //assert
        assertNull(fromDao);
        
    }
    
    @Test
    public void testGetSuperpowerForHero() {

        //creating test inputs
        Superpower superpower = new Superpower();
        superpower.setName("Magic");
        superpower.setDescription("Weird magic");
        superpower = superpowerDao.addSuperpower(superpower);

        Superpower fromDao = superpowerDao.getSuperpowerById(superpower.getSuperpowerId());

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

        Superpower forHero = superpowerDao.getSuperpowerForHero(hero);

        //assert
        assertEquals(forHero, superpower);
    }
}
