package com.github.kegszool.database.entity.service;

import com.github.kegszool.database.entity.dto.BaseEntityDto;
import com.github.kegszool.database.entity.mapper.EntityMapper;
import com.github.kegszool.database.repository.EntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EntityService<E, Dto extends BaseEntityDto> {

    private final EntityRepository<E, Integer> entityRepository;
    private final EntityMapper<E, Dto> entityMapper;

    @Autowired
    public EntityService(EntityRepository<E, Integer> entityRepository, EntityMapper<E, Dto> entityMapper) {
        this.entityRepository = entityRepository;
        this.entityMapper = entityMapper;
    }

    public void saveEntity(Dto entityDto) {
        E entity = entityMapper.toEntity(entityDto);
        E savedEntity = entityRepository.save(entity);
    }

    public Dto findEntityById(Integer id) {
        Optional<E> foundedEntity = entityRepository.findById(id);
        if(foundedEntity.isPresent()) {
            return entityMapper.toDto(foundedEntity.get());
        }
        throw new RuntimeException();
    }
}