package com.snapstudy.backend.model.DTO;

public class DegreeDTO {

    private String name;

    private String type;

    public DegreeDTO(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public DegreeDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
