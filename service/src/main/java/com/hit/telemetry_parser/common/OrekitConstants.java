package com.hit.telemetry_parser.common;

import org.orekit.bodies.CelestialBody;
import org.orekit.bodies.CelestialBodyFactory;
import org.orekit.bodies.OneAxisEllipsoid;
import org.orekit.frames.Frame;
import org.orekit.frames.FramesFactory;
import org.orekit.time.AbsoluteDate;
import org.orekit.utils.Constants;
import org.orekit.utils.IERSConventions;

/**
 * orekit计算常量
 *
 * @author Yang_Haiyue
 * @author 卫星技术研究所_哈尔滨工业大学
 * @since 2023/4/12 16:40
 */
public interface OrekitConstants {
	// 积分器相关参数
	double MAX_CHECK 		= 60.0;
	double THRESHOLD 		= 0.001;
	double SAMPLING_STEP 	= 1000.;

	// 天体参数
	CelestialBody SUN 	= CelestialBodyFactory.getSun();
	CelestialBody MOON 	= CelestialBodyFactory.getMoon();
	CelestialBody EARTH = CelestialBodyFactory.getEarth();

	// 地球参数
	OneAxisEllipsoid EARTH_SHAPE = new OneAxisEllipsoid(Constants.WGS84_EARTH_EQUATORIAL_RADIUS,
			Constants.WGS84_EARTH_FLATTENING, FramesFactory.getTIRF(IERSConventions.IERS_2010, true));

	// 常用坐标系
	Frame FRAME_J2000 	= FramesFactory.getEME2000();
	Frame FRAME_ECF 	= FramesFactory.getTIRF(IERSConventions.IERS_2010, true);
	Frame FRAME_TEME 	= FramesFactory.getTEME();

	// 空白日期
	AbsoluteDate NAN_DATE = AbsoluteDate.JAVA_EPOCH;
}
