package com.driver.services.impl;

import com.driver.model.Country;
import com.driver.model.CountryName;
import com.driver.model.ServiceProvider;
import com.driver.model.User;
import com.driver.repository.CountryRepository;
import com.driver.repository.ServiceProviderRepository;
import com.driver.repository.UserRepository;
import com.driver.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.driver.model.CountryName.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository3;
    @Autowired
    ServiceProviderRepository serviceProviderRepository3;
    @Autowired
    CountryRepository countryRepository3;

    @Override
    public User register(String username, String password, String countryName) throws Exception{
        //create a user of given country.
        // The originalIp of the user should be "countryCode.userId" and return the user.
        // Note that right now user is not connected and thus connected would be false and maskedIp would be null
        //Note that the userId is created automatically by the repository layer
        Country country = new Country();

        try {
            country.setCountryName(CountryName.valueOf(countryName.toUpperCase()));
            country.setCode(country.getCountryName().toCode());
        }
        catch (IllegalArgumentException ignored) {
            throw new Exception("Country not found");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setOriginalCountry(country);
        user.setOriginalIp(country.getCode() + "." + user.getId());

        userRepository3.save(user);

        return user;
    }

    @Override
    public User subscribe(Integer userId, Integer serviceProviderId) {
        //subscribe to the serviceProvider by adding it to the list of providers and return updated User
        ServiceProvider serviceProvider=serviceProviderRepository3.findById(serviceProviderId).get();
        User user=userRepository3.findById(userId).get();
        user.getServiceProviderList().add(serviceProvider);
        serviceProvider.getUsers().add(user);
        serviceProviderRepository3.save(serviceProvider);
        return user;
    }
}
