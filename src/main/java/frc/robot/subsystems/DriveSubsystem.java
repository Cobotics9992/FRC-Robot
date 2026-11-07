package frc.robot.subsystems;

import java.util.function.DoubleSupplier;

import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.util.sendable.SendableRegistry;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class DriveSubsystem extends SubsystemBase {
    private final WPI_VictorSPX leftMaster = new WPI_VictorSPX(11);
    private final WPI_VictorSPX rightMaster = new WPI_VictorSPX(12);
    private final WPI_VictorSPX leftFollower = new WPI_VictorSPX(13);
    private final WPI_VictorSPX rightFollower = new WPI_VictorSPX(14);

    private final DifferentialDrive m_drive;

    public DriveSubsystem() {
        leftMaster.configFactoryDefault();
        rightMaster.configFactoryDefault();
        leftFollower.configFactoryDefault();
        rightFollower.configFactoryDefault();

        leftFollower.follow(leftMaster);
        rightFollower.follow(rightMaster);
        leftFollower.setInverted(InvertType.FollowMaster);
        rightFollower.setInverted(InvertType.FollowMaster);

        leftMaster.setInverted(false);
        rightMaster.setInverted(false);

        m_drive = new DifferentialDrive(leftMaster, rightMaster);
        SendableRegistry.addChild(m_drive, leftMaster);
        SendableRegistry.addChild(m_drive, rightMaster);
    }

    public Command drive(DoubleSupplier speed, DoubleSupplier rotation) {
        return this.run(() -> {
            SmartDashboard.putNumber("DriveSubsystem/TargetSpeed", speed.getAsDouble());
            SmartDashboard.putNumber("DriveSubsystem/TargetRotation", rotation.getAsDouble());

            m_drive.arcadeDrive(speed.getAsDouble(), rotation.getAsDouble());
        });
    }
}
