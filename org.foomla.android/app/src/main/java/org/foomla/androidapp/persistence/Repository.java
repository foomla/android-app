package org.foomla.androidapp.persistence;

import java.io.Serializable;
import java.util.List;

public interface Repository<T extends Serializable> {

    List<T> getAll();

    T getById(int id);

    T save(T entity);

    void delete(int id);
}
