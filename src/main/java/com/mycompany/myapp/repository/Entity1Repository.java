package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Entity1;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Entity1 entity.
 */
@SuppressWarnings("unused")
@Repository
public interface Entity1Repository extends JpaRepository<Entity1, Long> {

}
