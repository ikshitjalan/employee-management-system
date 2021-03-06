package com.team.ghana.businessUnit;

import com.team.ghana.company.Company;

import java.util.Objects;

public class BusinessUnitResponse {

    private Long id;
    private String name;
    private int floor;
    private Company company;

    public BusinessUnitResponse(Long id, String name, int floor, Company company) {
        this.id = id;
        this.name = name;
        this.floor = floor;
        this.company = company;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BusinessUnitResponse that = (BusinessUnitResponse) o;
        return floor == that.floor &&
                Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(company, that.company);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, floor, company);
    }
}
