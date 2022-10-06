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
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Yash
 */
@Repository
public class SightingDaoDB implements SightingDao{
    
    @Autowired
    JdbcTemplate jdbc;

    @Override
    public Sighting getSightingById(int sightingId) {
        try {
            final String GET_SIGHTING_BY_ID = "SELECT * FROM Sighting WHERE sightingId = ?";
            Sighting sighting = jdbc.queryForObject(GET_SIGHTING_BY_ID, new SightingMapper(), sightingId);
            sighting.setLocation(getLocationForSighting(sightingId));
            sighting.setHero(getHeroForSighting(sightingId));
            return sighting;

        } catch (DataAccessException ex) {
            return null;
        }

    }

    @Override
    public List<Sighting> getAllSightings() {
        final String GET_ALL_SIGHTINGS = "SELECT * FROM Sighting";
        List<Sighting> sightings = jdbc.query(GET_ALL_SIGHTINGS, new SightingMapper());
        for (Sighting sighting : sightings) {
            sighting.setLocation(getLocationForSighting(sighting.getSightingId()));
            sighting.setHero(getHeroForSighting(sighting.getSightingId()));
        }
        return sightings;
    }

    @Override
    @Transactional
    public Sighting addSighting(Sighting sighting) {
        final String INSERT_SIGHTING = "INSERT INTO sighting(heroId,locationId,date) "
                + "VALUES(?,?,?)";
        jdbc.update(INSERT_SIGHTING,
                sighting.getHero().getHeroId(),
                sighting.getLocation().getLocationId(),
                Date.valueOf(sighting.getDate())
        );

        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        sighting.setSightingId(newId);
        return sighting;
    }

    @Override
    public void updateSighting(Sighting sighting) {
        final String UPDATE_SIGHTING = "UPDATE Sighting SET heroId= ?,locationId = ?, "
                + "date = ? WHERE sightingId = ?";
        jdbc.update(UPDATE_SIGHTING,
                sighting.getHero().getHeroId(),
                sighting.getLocation().getLocationId(),
                Date.valueOf(sighting.getDate()),
                sighting.getSightingId());
    }

    @Override
    public void deleteSightingById(int sightingId) {
        final String DELETE_SIGHTING = "DELETE FROM Sighting WHERE sightingId = ?";
        jdbc.update(DELETE_SIGHTING, sightingId);
    }

    @Override
    public List<Sighting> getSightingsByDate(LocalDate date) {
        final String GET_SIGHTINGS_BY_DATE = "select * FROM sighting where date = ?";
        List<Sighting> sightings = jdbc.query(GET_SIGHTINGS_BY_DATE, new SightingMapper(), Date.valueOf(date));
        for (Sighting sighting : sightings) {
            sighting.setLocation(getLocationForSighting(sighting.getSightingId()));
            sighting.setHero(getHeroForSighting(sighting.getSightingId()));
        }

        return sightings;
    }

    private Location getLocationForSighting(int sightingId) {
        final String SELECT_LOCATION_FOR_SIGHTING = "SELECT l.* FROM Location l "
                + "JOIN Sighting s ON s.locationId = l.locationId "
                + "WHERE s.sightingId = ?";
        return jdbc.queryForObject(SELECT_LOCATION_FOR_SIGHTING, new LocationDaoDB.LocationMapper(), sightingId);

    }

    private Hero getHeroForSighting(int sightingId) {
        final String SELECT_HERO_FOR_SIGHTING = "SELECT h.* FROM Hero h  "
                + "JOIN Sighting s ON s.heroId = h.heroId "
                + "WHERE s.sightingId = ?";
        Hero hero = jdbc.queryForObject(SELECT_HERO_FOR_SIGHTING, new HeroDaoDB.HeroMapper(), sightingId);
        hero.setSuperpower(getSuperpowerForHero(hero.getHeroId()));
        hero.setOrganizations(getOrganizationsForHero(hero));
        return hero;

    }

    private Superpower getSuperpowerForHero(int heroId) {
        final String SELECT_SUPERPOWER_FOR_HERO = "SELECT s.* FROM Superpower s  "
                + "JOIN Hero h ON h.superpowerId = s.superpowerId "
                + "WHERE h.heroId = ?";
        return jdbc.queryForObject(SELECT_SUPERPOWER_FOR_HERO, new SuperpowerDaoDB.SuperpowerMapper(), heroId);
    }

    private List<Organization> getOrganizationsForHero(Hero hero) {
        final String GET_ORGANIZATIONS = "SELECT * FROM ORGANIZATION o JOIN HeroOrganization ho ON ho.organizationId =o.organizationId Where heroId= ?";
        List<Organization> list = jdbc.query(GET_ORGANIZATIONS, new OrganizationDaoDB.OrganizationMapper(), hero.getHeroId());

        return list;

    }

    public static final class SightingMapper implements RowMapper<Sighting> {

        @Override
        public Sighting mapRow(ResultSet rs, int index) throws SQLException {
            Sighting sighting = new Sighting();
            sighting.setSightingId(rs.getInt("sightingId"));
            sighting.setDate(rs.getDate("date").toLocalDate());
            return sighting;

        }

    }

    
}
