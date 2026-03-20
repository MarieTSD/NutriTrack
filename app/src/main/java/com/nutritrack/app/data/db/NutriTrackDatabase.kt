package com.nutritrack.app.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.nutritrack.app.data.model.*

class Converters {
    @TypeConverter fun fromActivityLevel(v: ActivityLevel) = v.name
    @TypeConverter fun toActivityLevel(v: String) = ActivityLevel.valueOf(v)

    @TypeConverter fun fromGoal(v: Goal) = v.name
    @TypeConverter fun toGoal(v: String) = Goal.valueOf(v)

    @TypeConverter fun fromSex(v: Sex) = v.name
    @TypeConverter fun toSex(v: String) = Sex.valueOf(v)

    @TypeConverter fun fromFoodCategory(v: FoodCategory) = v.name
    @TypeConverter fun toFoodCategory(v: String) = FoodCategory.valueOf(v)

    @TypeConverter fun fromMealType(v: MealType) = v.name
    @TypeConverter fun toMealType(v: String) = MealType.valueOf(v)
}

@Database(
    entities = [
        UserProfile::class,
        FoodItem::class,
        FoodLogEntry::class,
        WaterLog::class,
        SupplementLog::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class NutriTrackDatabase : RoomDatabase() {
    abstract fun userProfileDao(): UserProfileDao
    abstract fun foodItemDao(): FoodItemDao
    abstract fun foodLogDao(): FoodLogDao
    abstract fun waterLogDao(): WaterLogDao
    abstract fun supplementLogDao(): SupplementLogDao
}
