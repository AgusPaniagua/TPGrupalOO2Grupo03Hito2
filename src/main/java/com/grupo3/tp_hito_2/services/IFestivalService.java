package com.grupo3.tp_hito_2.services;
import com.grupo3.tp_hito_2.entities.Festival;

import java.util.List;
import java.util.Optional;

public interface IFestivalService {


        List<Festival> getAll();

        Optional<Festival> findById(Long id);

        Festival insertOrUpdate(Festival festival);

        boolean remove(Long id);

}
