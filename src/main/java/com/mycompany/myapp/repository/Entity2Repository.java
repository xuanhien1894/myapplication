package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Entity2;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Entity2 entity.
 */
@SuppressWarnings("unused")
@Repository
public interface Entity2Repository extends JpaRepository<Entity2, Long> {

}
