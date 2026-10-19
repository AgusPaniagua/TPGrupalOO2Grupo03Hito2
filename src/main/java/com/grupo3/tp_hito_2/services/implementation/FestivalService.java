package com.grupo3.tp_hito_2.services.implementation;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import com.grupo3.tp_hito_2.entities.Festival;
import com.grupo3.tp_hito_2.repositories.IFestivalRepository;
import com.grupo3.tp_hito_2.services.IFestivalService;

@Service("festivalService")
public class FestivalService implements IFestivalService {

    private static final Logger logger =
            LoggerFactory.getLogger(FestivalService.class);

    private final IFestivalRepository festivalRepository;

    public FestivalService(IFestivalRepository festivalRepository) {
        this.festivalRepository = festivalRepository;
    }

    @Override
    public List<Festival> getAll() {
        return festivalRepository.findAll();
    }

    @Override
    public Optional<Festival> findById(Long id) {
        return festivalRepository.findById(id);
    }

    @Override
    public Festival insertOrUpdate(Festival festival) {
        return festivalRepository.save(festival);
    }

    @Override
    public boolean remove(Long id) {
        try {
            festivalRepository.deleteById(id);
            return true;
        } catch (EmptyResultDataAccessException |
                 DataIntegrityViolationException e) {
            logger.warn(
                    "No se pudo eliminar el festival con id {}", id, e);
            return false;
        }
    }
}
