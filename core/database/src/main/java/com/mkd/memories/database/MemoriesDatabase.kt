/*
 * Copyright 2025 SilleBille
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 *
 */

package com.mkd.memories.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mkd.memories.database.dao.DayContentDao
import com.mkd.memories.database.dao.DaysDao
import com.mkd.memories.database.model.DayContentEntity
import com.mkd.memories.database.model.DaysEntity

@Database(
    entities = [DaysEntity::class, DayContentEntity::class],
    version = 1,
)
abstract class MemoriesDatabase : RoomDatabase() {
    abstract fun daysDao(): DaysDao
    abstract fun dayContentDao(): DayContentDao
}
