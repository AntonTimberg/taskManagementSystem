package com.example.taskmanagementsystem.task;

public enum TaskStatus {
    PENDING("В ожидании"),
    IN_PROGRESS("В процессе"),
    COMPLETED("Завершено");

    private final String displayValue;

    TaskStatus(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}
