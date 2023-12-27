package com.ftn.TravelOrganisation.repository;

import java.util.List;

import com.ftn.TravelOrganisation.model.Putovanje;


public interface PutovanjeRepository {
    public Putovanje findOne(Long id);
    public List<Putovanje> findAll();
    
}
