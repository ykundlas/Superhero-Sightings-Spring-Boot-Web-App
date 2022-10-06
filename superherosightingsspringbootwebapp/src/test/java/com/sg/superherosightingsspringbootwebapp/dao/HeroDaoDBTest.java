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
public class HeroDaoDBTest {
    
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
    
    public HeroDaoDBTest() {
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
     * Test of getHeroById method, of class HeroDaoDB.
     */
    @Test
    public void testAddGetHeroById() {
        // Create our method test inputs
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

        //hero object
        Hero hero = new Hero();
        hero.setIsHero(true);
        hero.setName("Widerman");
        hero.setDescription("Tall");
        hero.setSuperpower(superpower);
        hero.setOrganizations(organizationList);

        //add this hero object to the database
        hero = heroDao.addHero(hero);

        //get the hero object from the dao
        Hero fromDao = heroDao.getHeroById(hero.getHeroId());

        //Check the data is equal
        assertEquals(hero, fromDao);
    }

    /**
     * Test of getAllHeroes method, of class HeroDaoDB.
     */
    @Test
    public void testGetAllHeroes() {
        // Create our method test inputs
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

        //organization
        Organization organization = new Organization();
        organization.setIsHero(true);
        organization.setName("Superhero Organization");
        organization.setDescription("Big");
        organization.setAddress("420 Sage Bluff, Calgary");
        organization.setContact("4034674567");
        organization = organizationDao.addOrganization(organization);

        List<Organization> organizationList = new ArrayList<>();
        organizationList.add(organization);

        //hero1
        Hero hero1 = new Hero();
        hero1.setIsHero(true);
        hero1.setName("Wonderwoman");
        hero1.setDescription("Tall");
        hero1.setSuperpower(superpower1);
        hero1.setOrganizations(organizationList);
        //add hero1 to the dao
        hero1 = heroDao.addHero(hero1);

        //hero2
        Hero hero2 = new Hero();
        hero2.setIsHero(true);
        hero2.setName("Spiderman");
        hero2.setDescription("Tall");
        hero2.setSuperpower(superpower2);
        hero2.setOrganizations(organizationList);
        //add hero2 to the dao
        hero2 = heroDao.addHero(hero2);

        //get a list of heroes from the dao
        List<Hero> list = heroDao.getAllHeroes();

        // First check the general contents of the list
        assertEquals(2, list.size());

        // Then the specifics
        assertTrue(list.contains(hero1));
        assertTrue(list.contains(hero2));
    }

    /**
     * Test of updateHero method, of class HeroDaoDB.
     */
    @Test
    public void testUpdateHero() {
        // Create our method test inputs
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
        //add the hero object to the dao
        hero = heroDao.addHero(hero);

        //get the hero object from the dao
        Hero fromDao = heroDao.getHeroById(hero.getHeroId());

        //Check the data is equal
        assertEquals(hero, fromDao);

        //change the name of the hero
        hero.setName("Wonderwoman");
        //organization2
        Organization organization2 = new Organization();
        organization2.setIsHero(true);
        organization2.setName("Wonder Organization");
        organization2.setDescription("Good");
        organization2.setAddress("23 SE, Calgary");
        organization2.setContact("4034785623");
        organization2 = organizationDao.addOrganization(organization2);

        organizationList.add(organization2);
        hero.setOrganizations(organizationList);

        //update the hero object
        heroDao.updateHero(hero);

        //chack the data is not equal
        assertNotEquals(hero, fromDao);

        //retrieve the hero object from the dao
        fromDao = heroDao.getHeroById(hero.getHeroId());

        //Check the data is equal
        assertEquals(hero, fromDao);

    }

    /**
     * Test of deleteHeroById method, of class HeroDaoDB.
     */
    @Test
    public void testDeleteHeroById() {
        // Create our method test inputs
        //superpower
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
        organization = organizationDao.addOrganization(organization);

        List<Organization> organizationList = new ArrayList<>();
        organizationList.add(organization);

        //hero
        Hero hero = new Hero();
        hero.setIsHero(true);
        hero.setName("Widerman");
        hero.setDescription("Tall");
        hero.setSuperpower(superpower);
        hero.setOrganizations(organizationList);

        //add to the database
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

        //delete the hero object
        heroDao.deleteHeroById(hero.getHeroId());

        //retrieve the object 
        Hero fromDao = heroDao.getHeroById(hero.getHeroId());
        assertNull(fromDao);
    }

    /**
     * Test of getHeroesatLocation method, of class HeroDaoDB.
     */
    @Test
    public void testGetHeroesatLocation() {
        // Create our method test inputs
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

        Organization organization = new Organization();
        organization.setIsHero(true);
        organization.setName("Superhero Organization");
        organization.setDescription("Big");
        organization.setAddress("420 Sage Bluff, Calgary");
        organization.setContact("4034674567");
        organization = organizationDao.addOrganization(organization);

        List<Organization> organizationList = new ArrayList<>();
        organizationList.add(organization);

        //hero1
        Hero hero1 = new Hero();
        hero1.setIsHero(true);
        hero1.setName("Widerman");
        hero1.setDescription("Tall");
        hero1.setSuperpower(superpower1);
        hero1.setOrganizations(organizationList);
        //add the hero to the database
        hero1 = heroDao.addHero(hero1);

        Location location = new Location();
        location.setName("Calgary");
        location.setDescription("Mountains");
        location.setAddress("Canada");
        location.setLatitude("51.049999");
        location.setLongitude("114.066666");
        //add the location to the database
        location = locationDao.addLocation(location);

        //hero2
        Hero hero2 = new Hero();
        hero2.setIsHero(true);
        hero2.setName("Spiderman");
        hero2.setDescription("Tall");
        hero2.setSuperpower(superpower2);
        hero2.setOrganizations(organizationList);
        //add the hero2 to the database
        hero2 = heroDao.addHero(hero2);

        //sighting1
        Sighting sighting1 = new Sighting();
        sighting1.setHero(hero1);
        sighting1.setLocation(location);
        LocalDate date = LocalDate.of(2022, 2, 9);
        sighting1.setDate(date);
        //add sighting object to the database
        sighting1 = sightingDao.addSighting(sighting1);

        //sightings2
        Sighting sighting2 = new Sighting();
        sighting2.setHero(hero2);
        sighting2.setLocation(location);
        LocalDate date2 = LocalDate.of(2022, 3, 9);
        sighting2.setDate(date2);
        //add sighting object to the database
        sighting2 = sightingDao.addSighting(sighting2);

        //retrieve a list of heroes
        List<Hero> heroListAtLocation = heroDao.getHeroesatLocation(location);

        //asserts
        assertEquals(2, heroListAtLocation.size());
        assertTrue(heroListAtLocation.contains(hero1));
        assertTrue(heroListAtLocation.contains(hero2));
    }

    /**
     * Test of getMembersOfOrganization method, of class HeroDaoDB.
     */
    @Test
    public void testGetMembersOfOrganization() {
        // Create our method test inputs
        Superpower superpower1 = new Superpower();
        superpower1.setName("Magic");
        superpower1.setDescription("Weird magic");
        //add superpower1 to the database
        superpower1 = superpowerDao.addSuperpower(superpower1);

        Superpower superpower2 = new Superpower();
        superpower2.setName("Fly");
        superpower2.setDescription("High speed");
        //add superpower2 to the database
        superpower2 = superpowerDao.addSuperpower(superpower2);

        Organization organization = new Organization();
        organization.setIsHero(true);
        organization.setName("Superhero Organization");
        organization.setDescription("Big");
        organization.setAddress("420 Sage Bluff, Calgary");
        organization.setContact("4034674567");
        //add organization to the database
        organization = organizationDao.addOrganization(organization);

        List<Organization> organizationList = new ArrayList<>();
        organizationList.add(organization);

        //hero1 member of organization
        Hero hero1 = new Hero();
        hero1.setIsHero(true);
        hero1.setName("Wonderwoman");
        hero1.setDescription("Tall");
        hero1.setSuperpower(superpower1);
        hero1.setOrganizations(organizationList);
        //add hero1 object to the database
        hero1 = heroDao.addHero(hero1);

        //hero2 member of organization
        Hero hero2 = new Hero();
        hero2.setIsHero(true);
        hero2.setName("Spiderman");
        hero2.setDescription("Tall");
        hero2.setSuperpower(superpower2);
        hero2.setOrganizations(organizationList);
        //add hero2 object to the database
        hero2 = heroDao.addHero(hero2);

        //retrieve a list of heroes
        List<Hero> memberList = heroDao.getMembersOfOrganization(organization);
        //asserts
        assertEquals(2, memberList.size());
        assertTrue(memberList.contains(hero1));
        assertTrue(memberList.contains(hero2));

    }
    
     @Test
    public void testGetHeroesForSuperpower() {
        
        // Create our method test inputs
        //superpower
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
        organization = organizationDao.addOrganization(organization);

        List<Organization> organizationList = new ArrayList<>();
        organizationList.add(organization);

        //hero
        Hero hero = new Hero();
        hero.setIsHero(true);
        hero.setName("Widerman");
        hero.setDescription("Tall");
        hero.setSuperpower(superpower);
        hero.setOrganizations(organizationList);

        //add to the database
        hero = heroDao.addHero(hero);
        
        //retrieve hero for superpower"Magic" from the dao
        List<Hero> heroList= heroDao.getHeroesForSuperpower(superpower);
        
        
        assertEquals(1, heroList.size());
        assertTrue(heroList.contains(hero));
        
    }

}
