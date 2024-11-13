package com.orangeandbronze.enlistment.domain;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, String> {


}
