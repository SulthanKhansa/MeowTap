package dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import java.util.ArrayList;
import java.util.List;
import org.bson.conversions.Bson;
import util.MongoManager;

public class GenericDAO<T> implements BaseDAO<T> {

    private final MongoCollection<T> collection;

    public GenericDAO(String collectionName, Class<T> clazz) {
        this.collection = MongoManager.getDatabase().getCollection(collectionName, clazz);
    }

    @Override
    public void save(T data) {
        collection.insertOne(data);
    }

    @Override
    public void update(Bson filter, T data) {
        collection.replaceOne(filter, data);
    }

    @Override
    public void delete(Bson filter) {
        collection.deleteOne(filter);
    }

    @Override
    public List<T> findAll() {
        List<T> results = new ArrayList<>();
        collection.find().into(results);
        return results;
    }

    @Override
    public T findOne(Bson filter) {
        return collection.find(filter).first();
    }

    @Override
    public List<T> findMany(Bson filter) {
        List<T> results = new ArrayList<>();
        collection.find(filter).into(results);
        return results;
    }
}
