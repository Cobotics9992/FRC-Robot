package frc.robot.subsystems;

import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

class SwerveModuleSubsystem extends SubsystemBase {
    private final TalonFX m_driveMotor;
    private final TalonFX m_rotationMotor;

    SwerveModuleSubsystem(int driveMotorId, int rotationMotorId) {
        m_driveMotor = new TalonFX(driveMotorId);
        m_rotationMotor = new TalonFX(rotationMotorId);
    }
}
