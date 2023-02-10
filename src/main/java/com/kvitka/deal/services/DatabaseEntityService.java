package com.kvitka.deal.services;

public interface DatabaseEntityService<T> {
    T save(T entity);
    T findById(Long id);
}
