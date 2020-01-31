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

package com.example.android.devbyteviewer.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.android.devbyteviewer.database.*
import com.example.android.devbyteviewer.domain.DevByteVideo
import com.example.android.devbyteviewer.network.DevByteNetwork
import com.example.android.devbyteviewer.network.asDatabaseModel
import com.example.android.devbyteviewer.network.asUpdatedDatebaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

/**
 * Repository for fetching devbyte videos from the network and storing them on disk
 */
class VideosRepository(private val database: VideosDatabase) {

    val videos: LiveData<List<DevByteVideo>> = Transformations.map(database.videoDao.getVideos()) {
        it.asDomainModel()
    }


    /**
     * Refresh the videos stored in the offline cache.
     *
     * This function uses the IO dispatcher to ensure the database insert database operation
     * happens on the IO dispatcher. By switching to the IO dispatcher using `withContext` this
     * function is now safe to call from any thread including the Main thread.
     *
     */
    suspend fun refreshVideos() {
        withContext(Dispatchers.IO) {
            Timber.d("refresh videos is called");
            val playlist = DevByteNetwork.devbytes.getPlaylist().await()

            Log.i("test123", "on insert log ${playlist[0].videos}")

//            for (i in 0 until (playlist.size - 1)) {
//                database.videoDao.insertDatabaseWithUpdated(playlist[i].asDatabaseModel(i))
//                    database.videoDao.insertUpdated(playlist[0].videos[i].asUpdatedDatebaseModel(i))
//
//            }

            database.videoDao.insertDatabaseWithUpdated(listOf(DatabaseWithUpdated(1,
                    "https://www.youtube.com/watch?v=sYGKUtM2ga8",
                    "Android Jetpack",
                    "test 3456376dfgkjsasdser",
                    "https://i4.ytimg.com/vi/sYGKUtM2ga8/hqdefault.jpg")))

            database.videoDao.insertUpdated(listOf(Update(1,1,"2018-06-07T17:09:43+00:00",888),
                    Update(3,1,"2018-06-07T17:09:43+00:00",111)))


            database.videoDao.insertDatabaseWithUpdated(listOf(DatabaseWithUpdated(2,
                    "https://www.youtube.com/watch?v=sYGKUtM2ga8",
                    "Android Jetpack",
                    "test 3456376dfgkjsasdser",
                    "https://i4.ytimg.com/vi/sYGKUtM2ga8/hqdefault.jpg")))

            database.videoDao.insertUpdated(listOf(Update(2,1,"2018-06-07T17:09:43+00:00",999)))
        }
    }

}
