package com.hit.telemetry_parser.utils;

import java.util.Comparator;

/**
 * 对区域顶点进行逆时针排序
 * @author Yang_Haiyue
 */
public class VertexComparator implements Comparator<Double[]> {
    private final double centerX;
    private final double centerY;

    public VertexComparator(Double[][] polygon) {
        // 计算中心点的经纬度坐标
        double centerX = 0.0;
        double centerY = 0.0;
        for (Double[] vertex : polygon) {
            centerX += vertex[0];
            centerY += vertex[1];
        }
        centerX /= polygon.length;
        centerY /= polygon.length;

        this.centerX = centerX;
        this.centerY = centerY;
    }

    @Override
    public int compare(Double[] vertex1, Double[] vertex2) {
        double angle1 = Math.atan2(vertex1[1] - centerY, vertex1[0] - centerX);
        double angle2 = Math.atan2(vertex2[1] - centerY, vertex2[0] - centerX);
        return Double.compare(angle2, angle1);
    }
}
