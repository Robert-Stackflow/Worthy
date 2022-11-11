package com.cloudchewie.client.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.cloudchewie.client.domin.User;

import java.util.List;

@Dao
public interface UserDao {
    @Query("select * from user")
    List<User> getAll();

    @Query("select * from user where user_id= :user_id")
    User findById(int user_id);

    @Update
    void update(User user);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User user);

    @Delete
    void delete(User user);

    @Query("select token from user")
    List<String> getToken();

    @Query("delete from user where 1=1")
    void clear();
}
