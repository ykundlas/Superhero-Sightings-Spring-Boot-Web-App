/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.superherosightingsspringbootwebapp.dao;

import com.sg.superherosightingsspringbootwebapp.entities.Hero;
import com.sg.superherosightingsspringbootwebapp.entities.Superpower;
import java.sql.ResultSet;
import java.sql.SQLException;
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
public class SuperpowerDaoDB implements SuperpowerDao{
    
    @Autowired
    JdbcTemplate jdbc;

    @Override
    public Superpower getSuperpowerById(int superpowerId) {
        try {
            final String GET_SUPERPOWER_BY_ID = "SELECT * FROM Superpower WHERE superpowerId = ?";
            return jdbc.queryForObject(GET_SUPERPOWER_BY_ID, new SuperpowerMapper(), superpowerId);
        } catch (DataAccessException ex) {
            return null;
        }
    }

    @Override
    public List<Superpower> getAllSuperpowers() {
        final String GET_ALL_SUPERPOWERS = "SELECT * FROM Superpower";
        return jdbc.query(GET_ALL_SUPERPOWERS, new SuperpowerMapper());
    }

    @Override
    @Transactional
    public Superpower addSuperpower(Superpower superpower) {
        final String INSERT_SUPERPOWER = "INSERT INTO Superpower(name, description) "
                + "VALUES(?,?)";
        jdbc.update(INSERT_SUPERPOWER,
                superpower.getName(),
                superpower.getDescription());

        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        superpower.setSuperpowerId(newId);
        return superpower;
    }

    @Override
    public void updateSuperpower(Superpower superpower) {
        final String UPDATE_SUPERPOWER = "UPDATE Superpower SET name = ?, description = ? "
                + "WHERE superpowerId = ?";
        jdbc.update(UPDATE_SUPERPOWER,
                superpower.getName(),
                superpower.getDescription(),
                superpower.getSuperpowerId());
    }

    @Override
    @Transactional
    public void deleteSuperpowerById(int superpowerId) {
        // delete hero in HeroOrganization with superpowerId
        final String DELETE_HERO_ORG = "DELETE ho.* FROM HeroOrganization ho " + "JOIN hero h on ho.heroId = h.heroId "
                + "JOIN superpower s on h.superpowerId = s.superpowerId WHERE s.superpowerId = ?";
        jdbc.update(DELETE_HERO_ORG, superpowerId);

        // delete hero in sighting with superpowerId
        final String DELETE_SIGHTING = "DELETE sg.* FROM Sighting sg " + "JOIN hero h ON sg.heroId = h.heroId "
                + "JOIN superpower s on h.superpowerId = s.superpowerId WHERE s.superpowerId = ?";
        jdbc.update(DELETE_SIGHTING, superpowerId);

        // delete hero in sighting with superpower = id
        final String DELETE_HERO = "DELETE FROM Hero WHERE superpowerId = ?";
        jdbc.update(DELETE_HERO, superpowerId);

        // delete superpower
        final String DELETE_SUPERPOWER = "DELETE FROM Superpower WHERE superpowerId = ?";
        jdbc.update(DELETE_SUPERPOWER, superpowerId);

    }
    
    @Override
    public Superpower getSuperpowerForHero(Hero hero){
         final String SELECT_SUPERPOWER_FOR_HERO = "SELECT s.* FROM Superpower s "
                + "JOIN Hero h ON h.superpowerId = s.superpowerId "
                + "WHERE h.heroId = ?";
        return jdbc.queryForObject(SELECT_SUPERPOWER_FOR_HERO, new SuperpowerDaoDB.SuperpowerMapper(), hero.getHeroId());
    }

    public static final class SuperpowerMapper implements RowMapper<Superpower> {

        @Override
        public Superpower mapRow(ResultSet rs, int index) throws SQLException {
            Superpower superpower = new Superpower();
            superpower.setSuperpowerId(rs.getInt("superpowerId"));
            superpower.setName(rs.getString("name"));
            superpower.setDescription(rs.getString("description"));
            return superpower;
        }

    }
    
}
