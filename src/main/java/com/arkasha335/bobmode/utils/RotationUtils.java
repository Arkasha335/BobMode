package com.arkasha335.bobmode.utils;

public class RotationUtils {

    /**
     * Интеллектуально привязывает Yaw игрока к ближайшему диагональному (45°) или кардинальному (90°) углу.
     * @param yaw Текущий yaw игрока.
     * @return Выровненный yaw.
     */
    public static float snapTo45DegreeYaw(float yaw) {
        // Нормализуем угол в диапазон [0, 360)
        float normalizedYaw = (yaw % 360 + 360) % 360;
        // Делим круг на 8 секторов (360 / 8 = 45)
        int sector = Math.round(normalizedYaw / 45.0f);
        // Возвращаем угол в центре сектора
        return sector * 45.0f;
    }
}