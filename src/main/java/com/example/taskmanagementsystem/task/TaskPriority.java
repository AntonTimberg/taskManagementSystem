package com.example.taskmanagementsystem.task;

public enum TaskPriority {
    LOW("Низкий"),
    MEDIUM("Средний"),
    HIGH("Высокий");

    private final String displayValue;

    TaskPriority(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}
