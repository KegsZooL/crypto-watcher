package com.github.kegszool.database.entity.service;

import com.github.kegszool.database.entity.mapper.EntityMapper;
import com.github.kegszool.database.repository.EntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Optional;

@Service
public abstract class EntityService<E, Dto, ID extends Serializable> {

    protected final EntityRepository<E, ID> entityRepository;
    protected final EntityMapper<E, Dto> entityMapper;

    @Autowired
    public EntityService(EntityRepository<E, ID> entityRepository, EntityMapper<E, Dto> entityMapper) {
        this.entityRepository = entityRepository;
        this.entityMapper = entityMapper;
    }

    public E saveEntity(Dto entityDto) {
        E entity = entityMapper.toEntity(entityDto);
        E savedEntity = entityRepository.save(entity);
        return savedEntity;
    }

    public Optional<E> findEntityById(ID id) {
        Optional<E> foundedEntity = entityRepository.findById(id);
        return foundedEntity;
    }
}