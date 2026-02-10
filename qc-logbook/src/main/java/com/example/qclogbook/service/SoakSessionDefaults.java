package com.example.qclogbook.service;

import com.example.qclogbook.domain.enums.EnvironmentType;

import java.util.List;

/**
 * SoakSessionの初期値定義。
 */
final class SoakSessionDefaults {

    record Default(EnvironmentType envType, int targetTempC) {
    }

    private SoakSessionDefaults() {
    }

    static List<Default> defaults() {
        return List.of(
            new Default(EnvironmentType.HIGH, 90),
            new Default(EnvironmentType.AMBIENT, 23),
            new Default(EnvironmentType.LOW, -10)
        );
    }
}