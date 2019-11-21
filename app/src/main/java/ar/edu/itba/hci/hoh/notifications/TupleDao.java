package ar.edu.itba.hci.hoh.notifications;

import androidx.room.*;

import java.util.List;

@Dao
public interface TupleDao {
    @Query("SELECT * FROM tuple WHERE id IS :id")
    Tuple getById(String id);

    @Query("SELECT * FROM tuple WHERE id NOT IN (:ids) ")
    List<Tuple> getMissing(String[] ids);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Tuple tuple);

    @Delete
    void delete(Tuple tuple);

    @Query("DELETE FROM tuple")
    void deleteAll();
}