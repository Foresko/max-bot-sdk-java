package ru.max.bot;

import java.util.Set;

import org.jetbrains.annotations.Nullable;

public class MaxBotOptions {
    private final Set<String> updateTypes;

    public MaxBotOptions(@Nullable Set<String> updateTypes) {
        this.updateTypes = updateTypes;
    }

    public Set<String> getUpdateTypes() {
        return updateTypes;
    }
}
