package com.example.pidev.Repositories;

import com.example.pidev.Entities.SessionFormation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SessionFormationRepository extends JpaRepository<SessionFormation, Long> {

}
