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
public class LocationDaoDBTest {
    
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
    
    public LocationDaoDBTest() {
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
     * Test of getLocationById method, of class LocationDaoDB.
     */
   @Test
    public void testAddGetLocationById() {

        Location location = new Location();
        location.setName("Calgary");
        location.setDescription("Mountains");
        location.setAddress("Canada");
        location.setLatitude("51.049999");
        location.setLongitude("114.066666");
        location = locationDao.addLocation(location);

        Location fromDao = locationDao.getLocationById(location.getLocationId());

        assertEquals(location, fromDao);
    }

    /**
     * Test of getAllLocations method, of class LocationDaoDB.
     */
    @Test
    public void testGetAllLocations() {
        //location1
        Location location1 = new Location();
        location1.setName("Calgary");
        location1.setDescription("Mountains");
        location1.setAddress("Canada");
        location1.setLatitude("51.049999");
        location1.setLongitude("114.066666");
        location1 = locationDao.addLocation(location1);

        //location2
        Location location2 = new Location();
        location2.setName("Niagra Falls");
        location2.setDescription("falls");
        location2.setAddress("Canada");
        location2.setLatitude("91.049999");
        location2.setLongitude("104.066666");
        location2 = locationDao.addLocation(location2);

        List<Location> locationList = locationDao.getAllLocations();

        assertEquals(2, locationList.size());
        assertTrue(locationList.contains(location1));
        assertTrue(locationList.contains(location2));
    }

    /**
     * Test of updateLocation method, of class LocationDaoDB.
     */
    @Test
    public void testUpdateLocation() {
        Location location = new Location();
        location.setName("Calgary");
        location.setDescription("Mountains");
        location.setAddress("Canada");
        location.setLatitude("51.049999");
        location.setLongitude("114.066666");
        location = locationDao.addLocation(location);

        Location fromDao = locationDao.getLocationById(location.getLocationId());

        assertEquals(location, fromDao);

        location.setName("MountainView");
        locationDao.updateLocation(location);

        assertNotEquals(location, fromDao);

        fromDao = locationDao.getLocationById(location.getLocationId());

        assertEquals(location, fromDao);
    }

    /**
     * Test of deleteLocationById method, of class LocationDaoDB.
     */
    @Test
    public void testDeleteLocationById() {

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

        Sighting sighting = new Sighting();
        sighting.setHero(hero);
        sighting.setLocation(location);
        LocalDate date = LocalDate.of(2022, 2, 9);
        sighting.setDate(date);
        sighting = sightingDao.addSighting(sighting);

        locationDao.deleteLocationById(location.getLocationId());

        Location fromDao = locationDao.getLocationById(location.getLocationId());

        assertNull(fromDao);
    }

    /**
     * Test of getLocationsForHero method, of class LocationDaoDB.
     */
    @Test
    public void testGetLocationsForHero() {

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

        //sighting1 at location1 for hero
        Sighting sighting1 = new Sighting();
        sighting1.setHero(hero);
        sighting1.setLocation(location1);
        LocalDate date1 = LocalDate.of(2022, 2, 9);
        sighting1.setDate(date1);
        sighting1 = sightingDao.addSighting(sighting1);

        //sighting2 at location2 for hero
        Sighting sighting2 = new Sighting();
        sighting2.setHero(hero);
        sighting2.setLocation(location2);
        LocalDate date2 = LocalDate.of(2022, 3, 9);
        sighting2.setDate(date2);
        sighting2 = sightingDao.addSighting(sighting2);

        List<Location> locationList = locationDao.getLocationsForHero(hero);

        assertEquals(2, locationList.size());
        assertTrue(locationList.contains(location1));
        assertTrue(locationList.contains(location2));
    }

}
