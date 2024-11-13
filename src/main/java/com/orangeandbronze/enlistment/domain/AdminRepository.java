package com.orangeandbronze.enlistment.domain;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {


}
