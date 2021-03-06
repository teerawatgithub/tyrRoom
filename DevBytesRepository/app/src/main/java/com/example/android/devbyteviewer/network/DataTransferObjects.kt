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

package com.example.android.devbyteviewer.network

import android.net.Network
import androidx.room.Database
import androidx.room.PrimaryKey
import com.example.android.devbyteviewer.database.DatabaseVideo
import com.example.android.devbyteviewer.database.DatabaseWithUpdated
import com.example.android.devbyteviewer.database.Update
import com.example.android.devbyteviewer.domain.DevByteVideo
import com.squareup.moshi.JsonClass

/**
 * DataTransferObjects go in this file. These are responsible for parsing responses from the server
 * or formatting objects to send to the server. You should convert these to domain objects before
 * using them.
 *
 * @see domain package for
 */

/**
 * VideoHolder holds a list of Videos.
 *
 * This is to parse first level of our network result which looks like
 *
 * {
 *   "videos": []
 * }
 */
@JsonClass(generateAdapter = true)
data class NetworkVideoContainer(val videos: List<NetworkVideo>)

/**
 * Videos represent a devbyte that can be played.
 */
@JsonClass(generateAdapter = true)
data class NetworkVideo(
        val title: String,
        val description: String,
        val url: String,
        val updated: List<UpdatedNetWork>,
        val thumbnail: String)

/**
 * Convert Network results to database objects
 */
//fun NetworkVideo.asTest(): DatabaseWithUpdated {
//    return title.trim()
//}

@JsonClass(generateAdapter = true)
data class UpdatedNetWork(
        val updatedSmall: String,
        val number: Int)


/**
 * Convert Network results to database objects
 */
fun NetworkVideoContainer.asDatabaseModel(iId: Int): List<DatabaseWithUpdated> {
    return videos.map {
        DatabaseWithUpdated(
                databaseVideoId = iId,
                url = it.url,
                title = it.title,
                description = it.description,
                thumbnail = it.thumbnail
        )
    }
}


fun NetworkVideo.asUpdatedDatebaseModel(iId: Int): List<Update> {
    return  updated.map {
        Update(
                updatedId = iId,
                updatedOwner = iId,
                updatedSmall = it.updatedSmall,
                number = it.number
        )
    }
}

