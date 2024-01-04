// Note: Do not write @Enumerated annotation above CountryName in this model.
package com.driver.model;

import javax.persistence.*;

@Entity
@Table(name = "country")

public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer countryId;
    private CountryName countryName;
    private String code;
    @JoinColumn
    @OneToOne
    private User user;
    @JoinColumn
    @ManyToOne
    private ServiceProvider serviceProvider;

    public Country() {
    }

    public Country(Integer countryId, CountryName countryName, String code, User user, ServiceProvider serviceProvider) {
        this.countryId = countryId;
        this.countryName = countryName;
        this.code = code;
        this.user = user;
        this.serviceProvider = serviceProvider;
    }

    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    public CountryName getCountryName() {
        return countryName;
    }

    public void setCountryName(CountryName countryName) {
        this.countryName = countryName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ServiceProvider getServiceProvider() {
        return serviceProvider;
    }

    public void setServiceProvider(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }
}
