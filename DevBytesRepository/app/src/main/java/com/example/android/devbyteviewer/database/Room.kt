/*
 * Copyright (C) 2019 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.devbyteviewer.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface VideoDao {
    @Transaction
    @Query("select * from database_with_updated")
    fun getVideos(): LiveData<List<DatabaseVideo>>


    @Transaction
    @Query("select * from database_with_updated INNER JOIN `Update` ON updatedOwner = databaseVideoId WHERE updatedId = :search")
    fun findByUpdated(search: Int): LiveData<List<DatabaseVideo>>


    @Insert(onConflict = OnConflictStrategy.REPLACE )
    fun insertDatabaseWithUpdated( videos: List<DatabaseWithUpdated>)

    @Insert(onConflict = OnConflictStrategy.REPLACE )
    fun insertUpdated( updated: List<Update>)
}



@Database(entities = [DatabaseWithUpdated::class, Update::class], version = 1)
abstract class VideosDatabase: RoomDatabase() {
    abstract val videoDao: VideoDao
}

private lateinit var INSTANCE: VideosDatabase

fun getDatabase(context: Context): VideosDatabase {
    synchronized(VideosDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                    VideosDatabase::class.java,
                    "videosV2").build()
        }
    }
    return INSTANCE
}
