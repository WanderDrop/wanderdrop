package com.wanderdrop.wserver.repository;

import com.wanderdrop.wserver.model.Attraction;
import com.wanderdrop.wserver.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttractionRepository extends JpaRepository<Attraction, Long> {
    List<Attraction> findByCreatedBy(User user);
}
