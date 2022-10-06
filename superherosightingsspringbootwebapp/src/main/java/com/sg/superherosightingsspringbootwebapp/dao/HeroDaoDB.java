/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.superherosightingsspringbootwebapp.dao;

import com.sg.superherosightingsspringbootwebapp.dao.OrganizationDaoDB.OrganizationMapper;
import com.sg.superherosightingsspringbootwebapp.dao.SuperpowerDaoDB.SuperpowerMapper;
import com.sg.superherosightingsspringbootwebapp.entities.Hero;
import com.sg.superherosightingsspringbootwebapp.entities.Location;
import com.sg.superherosightingsspringbootwebapp.entities.Organization;
import com.sg.superherosightingsspringbootwebapp.entities.Superpower;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Yash
 */
@Repository
public class HeroDaoDB implements HeroDao{
    
    @Autowired
    JdbcTemplate jdbc;
    
    private final String IMAGE_DIRECTORY = "src/main/resources/static/images";
    private final String IMAGE_EXTENSION = ".jpg";

    @Override
    public Hero getHeroById(int heroId) {
        try {
            final String SELECT_HERO_BY_ID = "SELECT * FROM Hero WHERE heroId = ?";
            Hero hero = jdbc.queryForObject(SELECT_HERO_BY_ID, new HeroMapper(), heroId);
            hero.setSuperpower(getSuperpowerForHero(heroId));
            hero.setOrganizations(getOrganizationsForHero(heroId));
            return hero;
        } catch (DataAccessException ex) {
            return null;
        }
    }

    private List<Organization> getOrganizationsForHero(int heroId) {
        final String SELECT_ORGANIZATION_FOR_HERO = "SELECT o.* FROM Organization o JOIN HeroOrganization ho "
                + "ON o.organizationId = ho.organizationId "
                + "WHERE ho.heroId = ?";
        return jdbc.query(SELECT_ORGANIZATION_FOR_HERO, new OrganizationMapper(), heroId);
    }

    private Superpower getSuperpowerForHero(int heroId) {
        final String SELECT_SUPERPOWER_FOR_HERO = "SELECT s.* FROM Superpower s JOIN Hero h ON "
                + "h.superpowerId = s.superpowerId WHERE h.heroId = ?";
        return jdbc.queryForObject(SELECT_SUPERPOWER_FOR_HERO, new SuperpowerMapper(), heroId);
    }

    @Override
    public List<Hero> getAllHeroes() {
        final String SELECT_ALL_HERO = "SELECT * FROM Hero";

        List<Hero> heroes = jdbc.query(SELECT_ALL_HERO, new HeroMapper());
        for (Hero hero : heroes) {
            hero.setSuperpower(getSuperpowerForHero(hero.getHeroId()));
            List<Organization> organizations = getOrganizationsForHero(hero.getHeroId());
            hero.setOrganizations(organizations);
        }
        return heroes;

    }
    

    @Override
    @Transactional
    public Hero addHero(Hero hero1) {
        final String INSERT_HERO = "INSERT INTO Hero(isHero,name, description, superpowerId, superImage) VALUES(?,?,?,?,?)";
        jdbc.update(INSERT_HERO,
                hero1.isIsHero(),
                hero1.getName(),
                hero1.getDescription(),
                hero1.getSuperpower().getSuperpowerId(),
                hero1.getSuperImage());

        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        hero1.setHeroId(newId);

        insertHeroOrganization(hero1);

        return hero1;
    }

    private void insertHeroOrganization(Hero hero) {
        final String INSERT_HERO_ORGANIZATION = "INSERT into HeroOrganization(heroId, organizationId) VALUES(?, ?)";
        for (Organization organization : hero.getOrganizations()) {
            jdbc.update(INSERT_HERO_ORGANIZATION, hero.getHeroId(), organization.getOrganizationId());
        }
    }

    @Override
    @Transactional
    public void updateHero(Hero hero) {
        final String UPDATE_HERO = "UPDATE Hero SET isHero = ?, name = ?, description = ?, superpowerId= ?,"
                + "superImage=? WHERE heroId = ?";
        jdbc.update(UPDATE_HERO,
                hero.isIsHero(),
                hero.getName(),
                hero.getDescription(),
                hero.getSuperpower().getSuperpowerId(),
                hero.getSuperImage(),
                hero.getHeroId());

        final String DELETE_HERO_ORGANIZATION = "DELETE FROM HeroOrganization WHERE heroId = ?";
        jdbc.update(DELETE_HERO_ORGANIZATION, hero.getHeroId());
        insertHeroOrganization(hero);

    }

    @Override
    @Transactional
    public void deleteHeroById(int heroId) {

        final String DELETE_HERO_ORGANIZATION = "DELETE FROM HeroOrganization WHERE HeroId = ?";
        jdbc.update(DELETE_HERO_ORGANIZATION, heroId);

        final String DELETE_SIGHTING = "DELETE FROM Sighting WHERE HeroId = ?";
        jdbc.update(DELETE_SIGHTING, heroId);

        final String DELETE_HERO = "DELETE FROM Hero WHERE HeroId = ?";
        jdbc.update(DELETE_HERO, heroId);
    }

    @Override
    public List<Hero> getHeroesatLocation(Location location) {
        final String SELECT_HEROS_FOR_LOCATION = "SELECT DISTINCT h.* FROM Hero h "
                + "JOIN Sighting s ON s.HeroId = h.HeroId "
                + "WHERE s.LocationId = ?";
        List<Hero> heroes = jdbc.query(SELECT_HEROS_FOR_LOCATION,
                new HeroMapper(), location.getLocationId());

        for (Hero hero : heroes) {
            hero.setSuperpower(getSuperpowerForHero(hero.getHeroId()));
            hero.setOrganizations(getOrganizationsForHero(hero.getHeroId()));
        }
        return heroes;
    }

    @Override
    public List<Hero> getMembersOfOrganization(Organization organization) {
        final String SELECT_ALL_MEMBERS = "SELECT DISTINCT h.* from hero h " + "JOIN HeroOrganization ho on h.heroId = ho.heroId where ho.organizationId = ?";
        List<Hero> list = jdbc.query(SELECT_ALL_MEMBERS, new HeroMapper(), organization.getOrganizationId());
        for (Hero hero : list) {
            hero.setSuperpower(getSuperpowerForHero(hero.getHeroId()));
            hero.setOrganizations(getOrganizationsForHero(hero.getHeroId()));
        }
        return list;
    }
    
    @Override
    public List<Hero>  getHeroesForSuperpower(Superpower superpower) {
         final String SELECT_HEROS_FOR_SUPERPOWER = "SELECT h.* FROM Hero h JOIN "
                + "Superpower s ON s.SuperpowerId = h.SuperpowerId WHERE s.SuperpowerId = ?";
        List<Hero> heroes = jdbc.query(SELECT_HEROS_FOR_SUPERPOWER, 
                new HeroMapper(), superpower.getSuperpowerId());
        for (Hero hero : heroes) {
            hero.setSuperpower(getSuperpowerForHero(hero.getHeroId()));
            hero.setOrganizations(getOrganizationsForHero(hero.getHeroId()));
        }
        return heroes;
        
    }
    
    public void uploadFile(String fileName, MultipartFile multipartFile) throws IOException{
        Path uploadPath = Paths.get(IMAGE_DIRECTORY);
         
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
         
        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName+IMAGE_EXTENSION);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {        
            throw new IOException("Could not save image file: " + fileName, ioe);
        } 
    }
    
 public boolean isImageSet(String fileName){
     
     try{
         File f = new File(IMAGE_DIRECTORY+"/" + fileName+IMAGE_EXTENSION);
         if(f.exists() && (f.isDirectory())){
             return true;
         }else{
             return false;
         }
     }catch (Exception e) {        
            return false;
        } 
     
 }
 

    public static final class HeroMapper implements RowMapper<Hero> {

        @Override
        public Hero mapRow(ResultSet rs, int index) throws SQLException {
            Hero hero = new Hero();
            hero.setHeroId(rs.getInt("heroId"));
            hero.setIsHero(rs.getBoolean("isHero"));
            hero.setName(rs.getString("name"));
            hero.setDescription(rs.getString("description"));
            hero.setSuperImage(rs.getBytes("superImage"));
            return hero;
        }

    }
    
}
