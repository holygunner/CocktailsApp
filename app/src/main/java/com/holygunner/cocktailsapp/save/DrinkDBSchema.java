package com.holygunner.cocktailsapp.save;

// not used yet
public class DrinkDBSchema {
    public static final class DrinkTable{
        public static final String NAME = "drinks";

        public static final class COLS{
            public static final String ID = "id";
            public static final String NAME = "name";
            public static final String CATEGORY = "category";
            public static final String GLASS = "glass";
            public static final String INSTRUCTION = "instruction";
            public static final String URL_IMAGE = "url_image";
            public static final String ALCOHOLIC = "alcoholic";
        }
    }
}
