package com.hit.telemetry_parser.domain.bo;

import com.hit.telemetry_parser.common.OrekitConstants;
import com.hit.telemetry_parser.entity.OrbitKeplerEntity;
import com.hit.telemetry_parser.entity.OrbitTleEntity;
import com.hit.telemetry_parser.utils.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.hipparchus.util.FastMath;
import org.orekit.orbits.KeplerianOrbit;
import org.orekit.orbits.Orbit;
import org.orekit.orbits.PositionAngleType;
import org.orekit.time.AbsoluteDate;
import org.orekit.utils.Constants;

import java.util.Objects;

/**
 * 轨道业务对象
 * Orbit business object
 * @author Yang Haiyue
 * @author 卫星技术研究所
 * @since 2024-08-16
 */
@Slf4j
public class OrbitBO {

    /** 偏心率定值 */
    public static final double ECCENTRICITY_FIXED = 0.00000000000000081917168644135216;

    /** 六要素实体 */
    private final OrbitKeplerEntity kepler;

    /** 两行根数实体 */
    private final OrbitTleEntity tle;

    /** 轨道 */
    private final Orbit orbit;

    /**
     * Creates a new instance by Keplerian orbital elements
     * @param kepler orbital elements
     */
    public OrbitBO(OrbitKeplerEntity kepler) {
        if (Objects.nonNull(kepler)) {
            this.kepler = kepler;
            this.tle = null;
            // 轨道时刻
            AbsoluteDate epoch = TimeUtils.calendarToAbsDate(TimeUtils.dateToCalendar(kepler.getEpoch()));
            // 半长轴(m)
            double a = kepler.getSemiMajorAxis() * 1000;
            // 偏心率
            Double e = kepler.getEccentricity();
            if (e <= 0) {
//                log.warn("输入偏序率e={}有误, 使用圆轨默认值{}", e, ECCENTRICITY_FIXED);
                e = ECCENTRICITY_FIXED;
            }
            // 轨道倾角, 近地点幅角, 升交点赤经, 近点角(rad)
            double i        = FastMath.toRadians(kepler.getInclination());
            double o        = FastMath.toRadians(kepler.getPeriapsis());
            double raan     = FastMath.toRadians(kepler.getRaan());
            double u        = FastMath.toRadians(kepler.getAnomaly());
            // 近点角类型(真/平)
            PositionAngleType type;
            if (kepler.getAnomalyType()) {
                type = PositionAngleType.TRUE;
            } else {
                type = PositionAngleType.MEAN;
            }
            this.orbit = new KeplerianOrbit(a, e, i, o, raan, u, type, OrekitConstants.FRAME_J2000, epoch,
                    Constants.EIGEN5C_EARTH_MU);
        } else {
            log.error("输入轨道六要素为空");
            throw new IllegalArgumentException("输入轨道六要素为空");
        }
    }

    /**
     * Creates a new instance by two-line elements
     * @param tle TLE
     */
    public OrbitBO(OrbitTleEntity tle) {
        if (Objects.nonNull(tle)) {
            this.tle    = tle;
            this.kepler = null;
            this.orbit  = null;
        } else {
            log.error("输入两行根数为空");
            throw new IllegalArgumentException("输入两行根数为空");
        }
    }

    public OrbitTleEntity getTle() {
        if (Objects.nonNull(tle)) {
            return tle;
        } else {
            log.warn("未配置两行根数");
            return null;
        }
    }

    public OrbitKeplerEntity getKepler() {
        if (Objects.nonNull(kepler)) {
            return kepler;
        } else {
            log.warn("未配置轨道六根数");
            return null;
        }
    }

    public Orbit getOrbit() {
        if (Objects.nonNull(orbit)) {
            return orbit;
        } else {
            return null;
        }
    }

    /**
     * 获取轨道周期
     * @return Orbit period in seconds.
     */
    public Double getOrbitPeriod() {
        // 判断轨道加载模式
        if (Objects.nonNull(kepler)) {
            // 六根数计算轨道周期
            return orbit.getKeplerianPeriod();
        } else {
            // 两行根数直接读取轨道周期
            return 86400.0 / Double.parseDouble(this.tle.getLine2().substring(52, 63));
        }
    }
}
