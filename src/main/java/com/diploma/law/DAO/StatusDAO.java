package com.diploma.law.DAO;
import com.diploma.law.models.StatusEntity;

import java.util.List;

public interface StatusDAO {
    StatusEntity findById(int id);
    StatusEntity FindByLogin(String title);
    List<StatusEntity> findAllStatuses();
}
