/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.superherosightingsspringbootwebapp.dao;

import com.sg.superherosightingsspringbootwebapp.entities.Hero;
import com.sg.superherosightingsspringbootwebapp.entities.Location;
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
public class LocationDaoDB implements LocationDao{
    
    @Autowired
    JdbcTemplate jdbc;

    @Override
    public Location getLocationById(int locationId) {
        try {
            final String GET_LOCATION_BY_ID = "SELECT * FROM Location WHERE locationId = ?";
            return jdbc.queryForObject(GET_LOCATION_BY_ID, new LocationMapper(), locationId);
        } catch (DataAccessException ex) {
            return null;
        }
    }

    @Override
    public List<Location> getAllLocations() {
        final String GET_ALL_LOCATIONS = "SELECT * FROM Location";
        return jdbc.query(GET_ALL_LOCATIONS, new LocationMapper());
    }

    @Override
    @Transactional
    public Location addLocation(Location location) {
        final String INSERT_LOCATION = "INSERT INTO Location(name, description, address, latitude, longitude) "
                + "VALUES(?, ?, ?, ?, ?)";
        jdbc.update(INSERT_LOCATION,
                location.getName(),
                location.getDescription(),
                location.getAddress(),
                Double.parseDouble(location.getLatitude()),
                Double.parseDouble( location.getLongitude())
        );

        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        location.setLocationId(newId);
        return location;

    }

    @Override
    public void updateLocation(Location location) {
        final String UPDATE_LOCATION = "UPDATE location SET name = ?, description = ?, address = ?, "
                + "latitude = ?, longitude = ? "
                + "WHERE locationId = ?";
        jdbc.update(UPDATE_LOCATION,
                location.getName(),
                location.getDescription(),
                location.getAddress(),
                Double.parseDouble(location.getLatitude()),
                Double.parseDouble(location.getLongitude()),
                location.getLocationId());
    }

    @Override
    @Transactional
    public void deleteLocationById(int locationId) {
        final String DELETE_SIGHTING = "DELETE FROM Sighting WHERE locationId = ?";

        jdbc.update(DELETE_SIGHTING, locationId);

        final String DELETE_LOCATION = "DELETE from location where locationId = ?";
        jdbc.update(DELETE_LOCATION, locationId);
    }

    @Override
    public List<Location> getLocationsForHero(Hero hero) {
        final String GET_ALL_LOCATION_FOR_HERO = "select DISTINCT l.* from location l JOIN sighting st on st.locationId = l.locationId where st.heroId = ?";
        return jdbc.query(GET_ALL_LOCATION_FOR_HERO, new LocationMapper(), hero.getHeroId());
    }

    public static final class LocationMapper implements RowMapper<Location> {

        @Override
        public Location mapRow(ResultSet rs, int index) throws SQLException {
            Location location = new Location();
            location.setLocationId(rs.getInt("locationId"));
            location.setName(rs.getString("name"));
            location.setDescription(rs.getString("description"));
            location.setAddress(rs.getString("address"));
            location.setLatitude(String.valueOf(rs.getDouble("latitude")));
            location.setLongitude(String.valueOf(rs.getDouble("longitude")));
            return location;

        }

    }

    
}
