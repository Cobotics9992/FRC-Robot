package frc.robot.subsystems;

import com.andymark.jni.AM_CAN_HexBoreEncoder;
import com.andymark.jni.AM_CAN_HexBoreEncoder.AM_Encoder_Telemetry;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class PickupEncoderSubsystem extends SubsystemBase {
    private final AM_CAN_HexBoreEncoder encoder = new AM_CAN_HexBoreEncoder(0);
    private AM_Encoder_Telemetry telemetry = new AM_Encoder_Telemetry();

    private static final double COUNTS_PER_REV = 16384.0;
    private static final double DEG_PER_COUNT = 360.0 / COUNTS_PER_REV;

    public double getVelocityDegPerSec() {
        return telemetry.velocityCountsPerSec * DEG_PER_COUNT;
    }

    public double getAngleDegrees() {
        return (telemetry.angleZeroedCounts & 0x3FFF) * DEG_PER_COUNT;
    }

    public PickupEncoderSubsystem() {
        encoder.setReportPeriod(20);
        encoder.setZeroHere();
    }

    @Override
    public void periodic() {
        telemetry = encoder.getTelemetry();
        SmartDashboard.putNumber("PickupEncoderSubsystem/Velocity", getVelocityDegPerSec());
        SmartDashboard.putNumber("PickupEncoderSubsystem/Angle", getAngleDegrees());
    }
}