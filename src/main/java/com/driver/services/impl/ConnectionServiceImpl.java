package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ConnectionRepository;
import com.driver.repository.ServiceProviderRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConnectionServiceImpl implements ConnectionService {
    @Autowired
    UserRepository userRepository2;
    @Autowired
    ServiceProviderRepository serviceProviderRepository2;
    @Autowired
    ConnectionRepository connectionRepository2;

    @Override
    public User connect(int userId, String countryName) throws Exception{
        User user=userRepository2.findById(userId).get();

        if(user.getMaskedIp()!=null){
            throw new Exception("Already connected");
        }
        if(countryName.equalsIgnoreCase(user.getOriginalCountry().getCountryName().toString())){
            return user;
        }
        if(user.getServiceProviderList()==null){
            throw new Exception("Unable to connect");
        }
        List<ServiceProvider> serviceProviderList=user.getServiceProviderList();
        int x=Integer.MAX_VALUE;
        ServiceProvider serviceProvider=null;
        Country country=null;

        for(ServiceProvider serviceProvider1:serviceProviderList){
            List<Country> countryList=serviceProvider1.getCountryList();
            for(Country country1:countryList){
                if(countryName.equalsIgnoreCase(country1.getCountryName().toString()) && x > serviceProvider1.getId()){
                    x=serviceProvider1.getId();
                    serviceProvider=serviceProvider1;
                    country=country1;
                }
            }
        }
        if(serviceProvider != null){
            Connection connection=new Connection();
            connection.setUser(user);
            connection.setServiceProvider(serviceProvider);
            user.setMaskedIp(country.getCode()+"."+serviceProvider.getId()+"."+user.getId());
            user.setConnected(true);
            user.getConnectionList().add(connection);
            serviceProvider.getConnectionList().add(connection);
            userRepository2.save(user);
            serviceProviderRepository2.save(serviceProvider);
        }
        else{
            throw new Exception("Unable to connect");
        }
        return user;
    }
    @Override
    public User disconnect(int userId) throws Exception {
        User user=userRepository2.findById(userId).get();

        if(!user.getConnected()){
            throw new Exception("Already disconnected");
        }
        user.setMaskedIp(null);
        user.setConnected(false);
        userRepository2.save(user);
        return user;
    }
    @Override
    public User communicate(int senderId, int receiverId) throws Exception {
        User sender=userRepository2.findById(senderId).get();
        User receiver=userRepository2.findById(receiverId).get();

        if(receiver.getMaskedIp()!=null){
            String receiverMaskedIP= receiver.getMaskedIp();
            String code=receiverMaskedIP.substring(0,3);
            code = code.toUpperCase();
            if(code.equals(sender.getOriginalCountry().getCode())){
                return sender;
            }
            String countryName="";
            CountryName [] countryNames=CountryName.values();

            for(CountryName countryName1:countryNames){
                if(countryName1.toCode().equals(code)){
                    countryName=countryName1.toString();
                }
            }
            try{
                sender = connect(senderId, countryName);

            }catch (Exception e){
                throw new Exception("Cannot establish communication");
            }

            if(!sender.getConnected()){
                throw new Exception("Cannot establish communication");
            }
            return sender;
        }
        if(sender.getOriginalCountry().equals(receiver.getOriginalCountry())){
            return sender;
        }
        String receiverCountryName=receiver.getOriginalCountry().getCountryName().toString();
        sender=connect(senderId,receiverCountryName);
        if(!sender.getConnected()){
            throw new Exception("Cannot establish communication");
        }
        return sender;
    }
}
