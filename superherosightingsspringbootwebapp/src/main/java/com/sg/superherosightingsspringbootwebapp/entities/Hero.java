/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.superherosightingsspringbootwebapp.entities;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 *
 * @author Yash
 */
public class Hero {

    private int heroId;
    private boolean isHero;
    
    @NotBlank(message = "Super name must not be empty.")
    @Size(max = 50, message="Super name must be less than 50 characters.")
    private String name;
    
    @Size(max = 255, message="Super description must be less than 255 characters.")
    private String description;
    
    private Superpower superpower;
    private List<Organization> organizations;
    private byte[] superImage;

    public int getHeroId() {
        return heroId;
    }

    public void setHeroId(int heroId) {
        this.heroId = heroId;
    }

    public boolean isIsHero() {
        return isHero;
    }

    public void setIsHero(boolean isHero) {
        this.isHero = isHero;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Superpower getSuperpower() {
        return superpower;
    }

    public void setSuperpower(Superpower superpower) {
        this.superpower = superpower;
    }

    public List<Organization> getOrganizations() {
        return organizations;
    }

    public void setOrganizations(List<Organization> organizations) {
        this.organizations = organizations;
    }

    public byte[] getSuperImage() {
        return superImage;
    }

    public void setSuperImage(byte[] superImage) {
        this.superImage = superImage;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + this.heroId;
        hash = 13 * hash + (this.isHero ? 1 : 0);
        hash = 13 * hash + Objects.hashCode(this.name);
        hash = 13 * hash + Objects.hashCode(this.description);
        hash = 13 * hash + Objects.hashCode(this.superpower);
        hash = 13 * hash + Objects.hashCode(this.organizations);
        hash = 13 * hash + Arrays.hashCode(this.superImage);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Hero other = (Hero) obj;
        if (this.heroId != other.heroId) {
            return false;
        }
        if (this.isHero != other.isHero) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.description, other.description)) {
            return false;
        }
        if (!Objects.equals(this.superpower, other.superpower)) {
            return false;
        }
        if (!Objects.equals(this.organizations, other.organizations)) {
            return false;
        }
        if (!Arrays.equals(this.superImage, other.superImage)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Hero{" + "heroId=" + heroId + ", isHero=" + isHero + ", name=" + name + ", description=" + description + ", superpower=" + superpower + ", organizations=" + organizations + ", superImage=" + superImage + '}';
    }
    
    

}