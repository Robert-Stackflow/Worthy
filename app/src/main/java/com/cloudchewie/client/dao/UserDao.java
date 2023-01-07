/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/18 13:13:37
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.client.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.cloudchewie.client.entity.User;

import java.util.List;

@Dao
public interface UserDao {
    @Query("select * from user")
    List<User> getAll();

    @Query("select * from user where userId= :user_id")
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
