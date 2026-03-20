package com.nutritrack.app.data.db

import com.nutritrack.app.data.model.FoodCategory
import com.nutritrack.app.data.model.FoodItem

object FoodDatabaseSeeder {

    fun getFoods(): List<FoodItem> = listOf(

        // ── Meat ─────────────────────────────────────────────────
        FoodItem(name = "Chicken breast (cooked)",  category = FoodCategory.MEAT,
            caloriesPer100g = 165f, proteinPer100g = 31f, carbsPer100g = 0f,  fatPer100g = 3.6f),
        FoodItem(name = "Ground beef 90% lean",     category = FoodCategory.MEAT,
            caloriesPer100g = 176f, proteinPer100g = 26f, carbsPer100g = 0f,  fatPer100g = 8f),
        FoodItem(name = "Turkey breast (cooked)",   category = FoodCategory.MEAT,
            caloriesPer100g = 135f, proteinPer100g = 30f, carbsPer100g = 0f,  fatPer100g = 1f),
        FoodItem(name = "Pork tenderloin",          category = FoodCategory.MEAT,
            caloriesPer100g = 143f, proteinPer100g = 26f, carbsPer100g = 0f,  fatPer100g = 3.5f),
        FoodItem(name = "Beef steak (sirloin)",     category = FoodCategory.MEAT,
            caloriesPer100g = 207f, proteinPer100g = 26f, carbsPer100g = 0f,  fatPer100g = 11f),

        // ── Fish ─────────────────────────────────────────────────
        FoodItem(name = "Salmon (cooked)",          category = FoodCategory.FISH,
            caloriesPer100g = 208f, proteinPer100g = 20f, carbsPer100g = 0f,  fatPer100g = 13f),
        FoodItem(name = "Tuna (canned in water)",   category = FoodCategory.FISH,
            caloriesPer100g = 116f, proteinPer100g = 26f, carbsPer100g = 0f,  fatPer100g = 1f),
        FoodItem(name = "Tilapia (cooked)",         category = FoodCategory.FISH,
            caloriesPer100g = 128f, proteinPer100g = 26f, carbsPer100g = 0f,  fatPer100g = 2.7f),
        FoodItem(name = "Shrimp (cooked)",          category = FoodCategory.FISH,
            caloriesPer100g = 99f,  proteinPer100g = 24f, carbsPer100g = 0f,  fatPer100g = 0.3f),
        FoodItem(name = "Cod (cooked)",             category = FoodCategory.FISH,
            caloriesPer100g = 105f, proteinPer100g = 23f, carbsPer100g = 0f,  fatPer100g = 0.9f),

        // ── Eggs & Dairy ──────────────────────────────────────────
        FoodItem(name = "Whole egg",                category = FoodCategory.EGGS,
            caloriesPer100g = 155f, proteinPer100g = 13f, carbsPer100g = 1.1f, fatPer100g = 11f),
        FoodItem(name = "Egg white",                category = FoodCategory.EGGS,
            caloriesPer100g = 52f,  proteinPer100g = 11f, carbsPer100g = 0.7f, fatPer100g = 0.2f),
        FoodItem(name = "Greek yogurt (plain)",     category = FoodCategory.DAIRY,
            caloriesPer100g = 59f,  proteinPer100g = 10f, carbsPer100g = 3.6f, fatPer100g = 0.4f),
        FoodItem(name = "Cottage cheese (low fat)", category = FoodCategory.DAIRY,
            caloriesPer100g = 72f,  proteinPer100g = 12f, carbsPer100g = 2.7f, fatPer100g = 1f),
        FoodItem(name = "Milk (whole)",             category = FoodCategory.DAIRY,
            caloriesPer100g = 61f,  proteinPer100g = 3.2f, carbsPer100g = 4.8f, fatPer100g = 3.3f),
        FoodItem(name = "Cheddar cheese",           category = FoodCategory.DAIRY,
            caloriesPer100g = 402f, proteinPer100g = 25f, carbsPer100g = 1.3f, fatPer100g = 33f),
        FoodItem(name = "Whey protein powder",      category = FoodCategory.DAIRY,
            caloriesPer100g = 370f, proteinPer100g = 80f, carbsPer100g = 6f,   fatPer100g = 4f),

        // ── Vegetables ────────────────────────────────────────────
        FoodItem(name = "Broccoli",                 category = FoodCategory.VEGETABLE,
            caloriesPer100g = 34f,  proteinPer100g = 2.8f, carbsPer100g = 7f,  fatPer100g = 0.4f),
        FoodItem(name = "Spinach",                  category = FoodCategory.VEGETABLE,
            caloriesPer100g = 23f,  proteinPer100g = 2.9f, carbsPer100g = 3.6f, fatPer100g = 0.4f),
        FoodItem(name = "Sweet potato",             category = FoodCategory.VEGETABLE,
            caloriesPer100g = 86f,  proteinPer100g = 1.6f, carbsPer100g = 20f,  fatPer100g = 0.1f),
        FoodItem(name = "Kale",                     category = FoodCategory.VEGETABLE,
            caloriesPer100g = 49f,  proteinPer100g = 4.3f, carbsPer100g = 9f,   fatPer100g = 0.9f),
        FoodItem(name = "Asparagus",                category = FoodCategory.VEGETABLE,
            caloriesPer100g = 20f,  proteinPer100g = 2.2f, carbsPer100g = 3.9f, fatPer100g = 0.1f),
        FoodItem(name = "Bell pepper",              category = FoodCategory.VEGETABLE,
            caloriesPer100g = 31f,  proteinPer100g = 1f,   carbsPer100g = 6f,   fatPer100g = 0.3f),
        FoodItem(name = "Cucumber",                 category = FoodCategory.VEGETABLE,
            caloriesPer100g = 15f,  proteinPer100g = 0.7f, carbsPer100g = 3.6f, fatPer100g = 0.1f),
        FoodItem(name = "Zucchini",                 category = FoodCategory.VEGETABLE,
            caloriesPer100g = 17f,  proteinPer100g = 1.2f, carbsPer100g = 3.1f, fatPer100g = 0.3f),
        FoodItem(name = "Tomato",                   category = FoodCategory.VEGETABLE,
            caloriesPer100g = 18f,  proteinPer100g = 0.9f, carbsPer100g = 3.9f, fatPer100g = 0.2f),

        // ── Fruits ────────────────────────────────────────────────
        FoodItem(name = "Banana",                   category = FoodCategory.FRUIT,
            caloriesPer100g = 89f,  proteinPer100g = 1.1f, carbsPer100g = 23f,  fatPer100g = 0.3f),
        FoodItem(name = "Apple",                    category = FoodCategory.FRUIT,
            caloriesPer100g = 52f,  proteinPer100g = 0.3f, carbsPer100g = 14f,  fatPer100g = 0.2f),
        FoodItem(name = "Blueberries",              category = FoodCategory.FRUIT,
            caloriesPer100g = 57f,  proteinPer100g = 0.7f, carbsPer100g = 14f,  fatPer100g = 0.3f),
        FoodItem(name = "Strawberries",             category = FoodCategory.FRUIT,
            caloriesPer100g = 32f,  proteinPer100g = 0.7f, carbsPer100g = 7.7f, fatPer100g = 0.3f),
        FoodItem(name = "Mango",                    category = FoodCategory.FRUIT,
            caloriesPer100g = 60f,  proteinPer100g = 0.8f, carbsPer100g = 15f,  fatPer100g = 0.4f),
        FoodItem(name = "Orange",                   category = FoodCategory.FRUIT,
            caloriesPer100g = 47f,  proteinPer100g = 0.9f, carbsPer100g = 12f,  fatPer100g = 0.1f),
        FoodItem(name = "Avocado",                  category = FoodCategory.FRUIT,
            caloriesPer100g = 160f, proteinPer100g = 2f,   carbsPer100g = 9f,   fatPer100g = 15f),

        // ── Cereals & Grains ──────────────────────────────────────
        FoodItem(name = "Oats (dry)",               category = FoodCategory.CEREAL,
            caloriesPer100g = 389f, proteinPer100g = 17f, carbsPer100g = 66f,  fatPer100g = 7f),
        FoodItem(name = "White rice (cooked)",      category = FoodCategory.CEREAL,
            caloriesPer100g = 130f, proteinPer100g = 2.7f, carbsPer100g = 28f,  fatPer100g = 0.3f),
        FoodItem(name = "Brown rice (cooked)",      category = FoodCategory.CEREAL,
            caloriesPer100g = 112f, proteinPer100g = 2.6f, carbsPer100g = 24f,  fatPer100g = 0.9f),
        FoodItem(name = "Quinoa (cooked)",          category = FoodCategory.CEREAL,
            caloriesPer100g = 120f, proteinPer100g = 4.4f, carbsPer100g = 22f,  fatPer100g = 1.9f),
        FoodItem(name = "Whole wheat bread",        category = FoodCategory.CEREAL,
            caloriesPer100g = 247f, proteinPer100g = 13f, carbsPer100g = 41f,  fatPer100g = 4.2f),
        FoodItem(name = "Pasta (cooked)",           category = FoodCategory.CEREAL,
            caloriesPer100g = 131f, proteinPer100g = 5f,  carbsPer100g = 25f,  fatPer100g = 1.1f),

        // ── Legumes ───────────────────────────────────────────────
        FoodItem(name = "Black beans (cooked)",     category = FoodCategory.LEGUME,
            caloriesPer100g = 132f, proteinPer100g = 8.9f, carbsPer100g = 24f,  fatPer100g = 0.5f),
        FoodItem(name = "Lentils (cooked)",         category = FoodCategory.LEGUME,
            caloriesPer100g = 116f, proteinPer100g = 9f,   carbsPer100g = 20f,  fatPer100g = 0.4f),
        FoodItem(name = "Chickpeas (cooked)",       category = FoodCategory.LEGUME,
            caloriesPer100g = 164f, proteinPer100g = 8.9f, carbsPer100g = 27f,  fatPer100g = 2.6f),
        FoodItem(name = "Edamame",                  category = FoodCategory.LEGUME,
            caloriesPer100g = 122f, proteinPer100g = 11f,  carbsPer100g = 10f,  fatPer100g = 5.2f),

        // ── Nuts & Seeds ──────────────────────────────────────────
        FoodItem(name = "Almonds",                  category = FoodCategory.NUT,
            caloriesPer100g = 579f, proteinPer100g = 21f, carbsPer100g = 22f,  fatPer100g = 50f),
        FoodItem(name = "Peanut butter",            category = FoodCategory.NUT,
            caloriesPer100g = 588f, proteinPer100g = 25f, carbsPer100g = 20f,  fatPer100g = 50f),
        FoodItem(name = "Chia seeds",               category = FoodCategory.NUT,
            caloriesPer100g = 486f, proteinPer100g = 17f, carbsPer100g = 42f,  fatPer100g = 31f),
        FoodItem(name = "Walnuts",                  category = FoodCategory.NUT,
            caloriesPer100g = 654f, proteinPer100g = 15f, carbsPer100g = 14f,  fatPer100g = 65f),

        // ── Supplements ───────────────────────────────────────────
        FoodItem(name = "Creatine monohydrate",     category = FoodCategory.SUPPLEMENT,
            caloriesPer100g = 0f,   proteinPer100g = 0f,  carbsPer100g = 0f,   fatPer100g = 0f),
        FoodItem(name = "Casein protein powder",    category = FoodCategory.SUPPLEMENT,
            caloriesPer100g = 360f, proteinPer100g = 78f, carbsPer100g = 5f,   fatPer100g = 2f),
        FoodItem(name = "BCAA powder",              category = FoodCategory.SUPPLEMENT,
            caloriesPer100g = 200f, proteinPer100g = 50f, carbsPer100g = 0f,   fatPer100g = 0f)
    )
}
