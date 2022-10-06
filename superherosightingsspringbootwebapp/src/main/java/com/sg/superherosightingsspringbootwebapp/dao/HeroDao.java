/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.superherosightingsspringbootwebapp.dao;

import com.sg.superherosightingsspringbootwebapp.entities.Hero;
import com.sg.superherosightingsspringbootwebapp.entities.Location;
import com.sg.superherosightingsspringbootwebapp.entities.Organization;
import com.sg.superherosightingsspringbootwebapp.entities.Superpower;
import java.io.IOException;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Yash
 */
public interface HeroDao {
    
     //This method returns hero associated with a heroId.
    Hero getHeroById(int heroId);

    //This method returns a list of all heroes.
    List<Hero> getAllHeroes();

    //This method returns and add a Hero to the database.
    Hero addHero(Hero hero);

    //This method update a hero associated with a heroId .
    void updateHero(Hero hero);

    //This method deletes a hero associated with a heroId.
    void deleteHeroById(int heroId);

    //This method returns a list of heroes at a sighted at a location.
    List<Hero> getHeroesatLocation(Location location);

    //This method returns a list of heroes associated with an organisation.
    List<Hero> getMembersOfOrganization(Organization organization);
    
    //This method returns a list of heroes associated with a Superpower.
    List<Hero> getHeroesForSuperpower(Superpower superpower);
    
    //void uploadFile(String fileName, MultipartFile multipartFile) throws IOException;
    
    //boolean isImageSet(String fileName);
    
}
