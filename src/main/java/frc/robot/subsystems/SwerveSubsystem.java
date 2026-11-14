package frc.robot.subsystems;

import java.util.function.DoubleSupplier;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.StructArrayPublisher;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class SwerveSubsystem extends SubsystemBase {
    PigeonSubsystem m_pigeonSubsystem;
    StructArrayPublisher<SwerveModuleState> m_statePublisher = NetworkTableInstance.getDefault()
        .getStructArrayTopic("SwerveSubsystem/SwerveModuleStates", SwerveModuleState.struct).publish();
    SwerveModuleSubsystem m_flSwerveSubsystem = new SwerveModuleSubsystem(0, 0);
    SwerveModuleSubsystem m_frSwerveSubsystem = new SwerveModuleSubsystem(0, 0);
    SwerveModuleSubsystem m_blSwerveSubsystem = new SwerveModuleSubsystem(0, 0);
    SwerveModuleSubsystem m_brSwerveSubsystem = new SwerveModuleSubsystem(0, 0);

    // change to reflect actual locations
    Translation2d m_frontLeftLocation = new Translation2d(0.381, 0.381);
    Translation2d m_frontRightLocation = new Translation2d(0.381, -0.381);
    Translation2d m_backLeftLocation = new Translation2d(-0.381, 0.381);
    Translation2d m_backRightLocation = new Translation2d(-0.381, -0.381);

    SwerveDriveKinematics m_kinematics = new SwerveDriveKinematics(
        m_frontLeftLocation, m_frontRightLocation, m_backLeftLocation, m_backRightLocation
    );

    public SwerveSubsystem(PigeonSubsystem pigeonSubsystem) {
        m_pigeonSubsystem = pigeonSubsystem;
    }

    public void drive(ChassisSpeeds speeds) {
        SwerveModuleState[] states = m_kinematics.toSwerveModuleStates(speeds);
        m_statePublisher.set(states);

        SwerveModuleState frontLeft = states[0];
        SwerveModuleState frontRight = states[1];
        SwerveModuleState backLeft = states[2];
        SwerveModuleState backRight = states[3];

        m_flSwerveSubsystem.drive(frontLeft);
        m_frSwerveSubsystem.drive(frontRight);
        m_blSwerveSubsystem.drive(backLeft);
        m_brSwerveSubsystem.drive(backRight);
    }

    public Command driveFromJoysticks(DoubleSupplier vx, DoubleSupplier vy, DoubleSupplier angleRad) {
        return this.run(() -> {
            ChassisSpeeds targetSpeed = ChassisSpeeds.fromFieldRelativeSpeeds(
                vx.getAsDouble(), 
                vy.getAsDouble(), 
                angleRad.getAsDouble(),
                new Rotation2d(m_pigeonSubsystem.getYaw())
            );
            drive(targetSpeed);
        });
    }
}
