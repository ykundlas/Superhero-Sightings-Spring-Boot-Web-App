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
public class OrganizationDaoDBTest {
    
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
    
    public OrganizationDaoDBTest() {
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
     * Test of getOrganizationById method, of class OrganizationDaoDB.
     */
    @Test
    public void testAddGetOrganizationById() {

        //creating test input
        Organization organization = new Organization();
        organization.setIsHero(true);
        organization.setName("Superhero Organization");
        organization.setDescription("Big");
        organization.setAddress("420 Sage Bluff, Calgary");
        organization.setContact("4034674567");
        organization = organizationDao.addOrganization(organization);

        //retrive organization from dao
        Organization fromDao = organizationDao.getOrganizationById(organization.getOrganizationId());

        //assert
        assertEquals(organization, fromDao);

    }

    /**
     * Test of getAllOrganizations method, of class OrganizationDaoDB.
     */
    @Test
    public void testGetAllOrganizations() {

        //creating test inputs
        //organization1
        Organization organization1 = new Organization();
        organization1.setIsHero(true);
        organization1.setName("Superhero Organization");
        organization1.setDescription("Big");
        organization1.setAddress("420 Sage Bluff, Calgary");
        organization1.setContact("4034674567");
        organization1 = organizationDao.addOrganization(organization1);

        //organization2
        Organization organization2 = new Organization();
        organization2.setIsHero(true);
        organization2.setName("Wonder Organization");
        organization2.setDescription("Good");
        organization2.setAddress("23 SE, Calgary");
        organization2.setContact("4034785623");
        organization2 = organizationDao.addOrganization(organization2);

        //retrieve a list of organizations
        List<Organization> list = organizationDao.getAllOrganizations();

        //assert
        assertEquals(2, list.size());
        assertTrue(list.contains(organization1));
        assertTrue(list.contains(organization2));

    }

    /**
     * Test of updateOrganization method, of class OrganizationDaoDB.
     */
    @Test
    public void testUpdateOrganization() {

        //creating test input
        Organization organization = new Organization();
        organization.setIsHero(true);
        organization.setName("Superhero Organization");
        organization.setDescription("Big");
        organization.setAddress("420 Sage Bluff, Calgary");
        organization.setContact("4034674567");
        organization = organizationDao.addOrganization(organization);

        //retrieve from dao
        Organization fromDao = organizationDao.getOrganizationById(organization.getOrganizationId());

        //assert
        assertEquals(organization, fromDao);

        organization.setAddress("Big Mall");
        //update organization
        organizationDao.updateOrganization(organization);

        //assert
        assertNotEquals(organization, fromDao);

        //retrieve from dao again
        fromDao = organizationDao.getOrganizationById(organization.getOrganizationId());

        //assert
        assertEquals(organization, fromDao);
    }

    /**
     * Test of deleteOrganizationById method, of class OrganizationDaoDB.
     */
    @Test
    public void testDeleteOrganizationById() {
        //creating test inputs
        Superpower superpower = new Superpower();
        superpower.setName("Magic");
        superpower.setDescription("Weird magic");
        superpower = superpowerDao.addSuperpower(superpower);

        //organization
        Organization organization = new Organization();
        organization.setIsHero(true);
        organization.setName("Superhero Organization");
        organization.setDescription("Big");
        organization.setAddress("420 Sage Bluff, Calgary");
        organization.setContact("4034674567");
        //add to the dao
        organization = organizationDao.addOrganization(organization);

        List<Organization> organizationList = new ArrayList<>();
        organizationList.add(organization);

        //hero
        Hero hero = new Hero();
        hero.setIsHero(true);
        hero.setName("Wonderwoman");
        hero.setDescription("Tall");
        hero.setSuperpower(superpower);
        hero.setOrganizations(organizationList);
        hero = heroDao.addHero(hero);

        //location
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

        //delete
        organizationDao.deleteOrganizationById(organization.getOrganizationId());

        //retreive from the dao
        Organization fromDao = organizationDao.getOrganizationById(organization.getOrganizationId());

        //assert
        assertNull(fromDao);

    }

    /**
     * Test of getOrganizationsForHero method, of class OrganizationDaoDB.
     */
    @Test
    public void testGetOrganizationsForHero() {
        //creating test inputs
        Superpower superpower = new Superpower();
        superpower.setName("Magic");
        superpower.setDescription("Weird magic");
        superpower = superpowerDao.addSuperpower(superpower);

        //organization1 for hero
        Organization organization1 = new Organization();
        organization1.setIsHero(true);
        organization1.setName("Superhero Organization");
        organization1.setDescription("Big");
        organization1.setAddress("420 Sage Bluff, Calgary");
        organization1.setContact("4034674567");
        organization1 = organizationDao.addOrganization(organization1);

        //organization2
        Organization organization2 = new Organization();
        organization2.setIsHero(true);
        organization2.setName("Wonder Organization");
        organization2.setDescription("Good");
        organization2.setAddress("23 SE, Calgary");
        organization2.setContact("4034785623");
        organization2 = organizationDao.addOrganization(organization2);

        //add both organizations to the list
        List<Organization> organizationList = new ArrayList<>();
        organizationList.add(organization1);
        organizationList.add(organization2);

        //hero affliated to two organizations
        Hero hero = new Hero();
        hero.setIsHero(true);
        hero.setName("Widerman");
        hero.setDescription("Tall");
        hero.setSuperpower(superpower);
        hero.setOrganizations(organizationList);
        hero = heroDao.addHero(hero);

        //retrieve a list of organizations for the hero
        List<Organization> listForHero = organizationDao.getOrganizationsForHero(hero);

        //assert
        assertEquals(2, listForHero.size());
        assertTrue(listForHero.contains(organization1));
        assertTrue(listForHero.contains(organization2));

    }

}
