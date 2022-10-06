/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.superherosightingsspringbootwebapp.dao;

import com.sg.superherosightingsspringbootwebapp.entities.Hero;
import com.sg.superherosightingsspringbootwebapp.entities.Organization;
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
public class OrganizationDaoDB implements OrganizationDao{
    
    @Autowired
    JdbcTemplate jdbc;

    @Override
    public Organization getOrganizationById(int organizationId) {
        try {
            final String SELECT_ORGANIZATION_BY_ID = "SELECT * FROM Organization WHERE organizationId = ?";
            Organization organization = jdbc.queryForObject(SELECT_ORGANIZATION_BY_ID, new OrganizationMapper(), organizationId);
            return organization;
            
        } catch (DataAccessException ex) {
            return null;
        }
    }

    @Override
    public List<Organization> getAllOrganizations() {
        
        final String SELECT_ALL_ORGANIZATIONS = "SELECT * FROM Organization";
        List<Organization> list = jdbc.query(SELECT_ALL_ORGANIZATIONS, new OrganizationMapper());
        return list;

    }

    @Override
    @Transactional
    public Organization addOrganization(Organization organization) {
        final String INSERT_ORGANIZATION = "INSERT INTO Organization(isHero,name, description, address, contact) "
                + "VALUES(?, ?, ?, ?, ?)";
        jdbc.update(INSERT_ORGANIZATION,
                organization.isIsHero(),
                organization.getName(),
                organization.getDescription(),
                organization.getAddress(),
                organization.getContact()
        );

        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        organization.setOrganizationId(newId);

        return organization;

    }

    @Override
    public void updateOrganization(Organization organization) {
        final String UPDATE_ORGANIZATION = "UPDATE organization SET ishero= ?,name = ?, description= ?, address= ?, contact= ?"
                + "WHERE organizationId = ?";
        jdbc.update(UPDATE_ORGANIZATION,
                organization.isIsHero(),
                organization.getName(),
                organization.getDescription(),
                organization.getAddress(),
                organization.getContact(),
                organization.getOrganizationId());

    }

    @Override
    @Transactional
    public void deleteOrganizationById(int organizationId) {
        final String DELETE_HERO_ORGANIZATION = "DELETE FROM HeroOrganization WHERE organizationId = ?";
        jdbc.update(DELETE_HERO_ORGANIZATION, organizationId);

        final String DELETE_ORGANIZATION = "DELETE FROM Organization WHERE  organizationId= ?";
        jdbc.update(DELETE_ORGANIZATION, organizationId);

    }

    @Override
    public List<Organization> getOrganizationsForHero(Hero hero) {
        final String GET_ORGANIZATIONS = "SELECT o.* FROM ORGANIZATION o " + "JOIN HeroOrganization ho ON ho.organizationId =o.organizationId Where ho.heroId= ?";
        List<Organization> list = jdbc.query(GET_ORGANIZATIONS, new OrganizationMapper(), hero.getHeroId());

        return list;

    }

    public static final class OrganizationMapper implements RowMapper<Organization> {

        @Override
        public Organization mapRow(ResultSet rs, int index) throws SQLException {
            Organization organization = new Organization();
            organization.setOrganizationId(rs.getInt("organizationId"));
            organization.setIsHero(rs.getBoolean("isHero"));
            organization.setName(rs.getString("name"));
            organization.setDescription(rs.getString("description"));
            organization.setAddress(rs.getString("address"));
            organization.setContact(rs.getString("contact"));
            return organization;

        }

    }

    
}
