package org.launchcode.liftoffapi.repository;
import org.launchcode.liftoffapi.model.Cheese;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CheeseRepository extends JpaRepository<Cheese, Long>
{
}