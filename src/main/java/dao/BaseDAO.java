package dao;

import java.util.List;
import org.bson.conversions.Bson;

public interface BaseDAO<T> {
    void save(T data);
    void update(Bson filter, T data);
    void delete(Bson filter);
    List<T> findAll();
    T findOne(Bson filter);
    List<T> findMany(Bson filter);
}
