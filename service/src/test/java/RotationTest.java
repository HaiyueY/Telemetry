import com.hit.telemetry_parser.common.OrekitConstants;
import com.hit.telemetry_parser.config.OrekitConfig;
import org.hipparchus.geometry.euclidean.threed.Rotation;
import org.hipparchus.geometry.euclidean.threed.RotationConvention;
import org.hipparchus.geometry.euclidean.threed.RotationOrder;
import org.hipparchus.geometry.euclidean.threed.Vector3D;
import org.hipparchus.util.FastMath;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.orekit.attitudes.Attitude;
import org.orekit.attitudes.TargetPointing;
import org.orekit.attitudes.YawCompensation;
import org.orekit.bodies.GeodeticPoint;
import org.orekit.frames.Frame;
import org.orekit.frames.LOFType;
import org.orekit.frames.LocalOrbitalFrame;
import org.orekit.frames.TopocentricFrame;
import org.orekit.orbits.Orbit;
import org.orekit.propagation.Propagator;
import org.orekit.propagation.SpacecraftState;
import org.orekit.propagation.analytical.tle.TLE;
import org.orekit.propagation.analytical.tle.TLEPropagator;
import org.orekit.time.AbsoluteDate;
import org.orekit.time.TimeScalesFactory;

public class RotationTest {

    @BeforeAll
    public static void init() {
        OrekitConfig.init();
    }

    @Test
    public void rotationTest() {
        GeodeticPoint start = new GeodeticPoint(FastMath.toRadians(5), FastMath.toRadians(-138.333), 0.);
        GeodeticPoint end = new GeodeticPoint(FastMath.toRadians(18.3333), FastMath.toRadians(-142.333), 0.);
        // 构建预报器
        TLE tle = new TLE("1 40961U 15057D   24260.96238518  .00002373  00000-0  34804-3 0  9991",
                "2 40961  97.6675 281.6690 0017695 173.8378 186.3054 14.76327341481377");
        Propagator propagator = TLEPropagator.selectExtrapolator(tle);
        Frame obsFrame = propagator.getFrame();
        TargetPointing startPointing = new TargetPointing(obsFrame, start, OrekitConstants.EARTH_SHAPE);
        TargetPointing endPointing = new TargetPointing(obsFrame, end, OrekitConstants.EARTH_SHAPE);
        YawCompensation yawCompensation = new YawCompensation(obsFrame, startPointing);
        AbsoluteDate observeTime = new AbsoluteDate(2024, 9, 21, 4, 0, 0., TimeScalesFactory.getUTC());
        // 建立观测点测站系
        final TopocentricFrame targetStartFrame = new TopocentricFrame(OrekitConstants.EARTH_SHAPE, start, "targetStartFrame");
        final TopocentricFrame targetEndFrame = new TopocentricFrame(OrekitConstants.EARTH_SHAPE, end, "targetEndFrame");

        // 开始
        propagator.setAttitudeProvider(yawCompensation);
        SpacecraftState startState = propagator.propagate(observeTime);
        Orbit startOrbit = startState.getOrbit();
        Frame orbitalFrame = new LocalOrbitalFrame(startOrbit.getFrame(), LOFType.VVLH, startOrbit, "bodyStartFrame");
        AbsoluteDate startDate = startState.getDate();
        Vector3D startInStartBody = targetStartFrame.getPVCoordinates(startDate, orbitalFrame).getPosition();
        Vector3D endInStartBody = targetEndFrame.getPVCoordinates(startDate, orbitalFrame).getPosition();
        Vector3D startDirection = endInStartBody.subtract(startInStartBody).normalize();
        Attitude startAttitude = startState.getAttitude().withReferenceFrame(orbitalFrame);
        // x轴调姿态后指向
        Vector3D startDirection1 = startAttitude.getRotation().applyTo(startDirection);
        // 偏流角补偿
        Rotation yawStartRotation = new Rotation(Vector3D.PLUS_I, new Vector3D(startDirection1.getX(), startDirection1.getY(), 0));
        double yawStart = yawStartRotation.getAngle();
        yawStart = startDirection1.getY() < 0 ? -yawStart : yawStart;
        double[] startAngles = startAttitude.withReferenceFrame(orbitalFrame).getRotation()
                .getAngles(RotationOrder.XYZ, RotationConvention.FRAME_TRANSFORM);

        double roll = FastMath.toDegrees(startAngles[0]);
        double pitch = FastMath.toDegrees(startAngles[1]);
        double heading = FastMath.toDegrees(startAngles[2]);
        double yaw = FastMath.toDegrees(yawStart);

        System.out.println(yawStart);
    }
}
