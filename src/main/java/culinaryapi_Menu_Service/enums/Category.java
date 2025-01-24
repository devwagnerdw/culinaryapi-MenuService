package culinaryapi_Menu_Service.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Category {
        FOOD,
        DRINK,
        DESSERT,
        OTHER;

        @JsonCreator
        public static Category fromValue(String value) {
                for (Category category : values()) {
                        if (category.name().equalsIgnoreCase(value)) {
                                return category;
                        }
                }
                throw new IllegalArgumentException("Invalid value for category: " + value);
        }
}
