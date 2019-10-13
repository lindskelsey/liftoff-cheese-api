package org.launchcode.liftoffapi.service;
import org.launchcode.liftoffapi.model.Cheese;
import org.launchcode.liftoffapi.repository.CheeseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CheeseService {

    private CheeseRepository cheeseRepository;


    public CheeseService(CheeseRepository cheeseRepository)
    {
        this.cheeseRepository = cheeseRepository;
    }

    public List<Cheese> getAll()
    {
        return cheeseRepository.findAll();
    }

    public Cheese addCheese(Cheese cheese)
    {
        return cheeseRepository.save(cheese);
    }
}