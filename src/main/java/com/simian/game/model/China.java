package com.simian.game.model;

import java.util.List;

public class China { 
    private String name;
    private List<Province>province;
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Province> getProvince() {
        return province;
    }

    public void setProvince(List<Province> province) {
        this.province = province;
    }

}