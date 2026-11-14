package frc.robot.subsystems;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.MotionMagicVelocityVoltage;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.MotorInfo;

class SwerveModuleSubsystem extends SubsystemBase {
    private final TalonFX m_driveMotor;
    private final TalonFX m_rotationMotor;

    private final MotionMagicVelocityVoltage m_driveVelocityRequest = new MotionMagicVelocityVoltage(0).withSlot(0);
    private final MotionMagicVoltage m_rotationVelocityRequest = new MotionMagicVoltage(0).withSlot(0);

    SwerveModuleSubsystem(int driveMotorId, int rotationMotorId) {
        TalonFXConfiguration rotationCfg = new TalonFXConfiguration();

        rotationCfg.Feedback.SensorToMechanismRatio = MotorInfo.gearRation;
        
        // todo should be tuned to our robot
        rotationCfg.Slot0.kS = 0.1;
        rotationCfg.Slot0.kV = 0.12;
        rotationCfg.Slot0.kP = 0.2;
        TalonFXConfiguration driveCfg = new TalonFXConfiguration();

        driveCfg.Feedback.SensorToMechanismRatio = MotorInfo.gearRation;
        
        // todo should be tuned to our robot
        driveCfg.Slot0.kS = 0.1;
        driveCfg.Slot0.kV = 0.12;
        driveCfg.Slot0.kP = 0.2;

        m_driveMotor = new TalonFX(driveMotorId);
        m_rotationMotor = new TalonFX(rotationMotorId);

        m_rotationMotor.getConfigurator().apply(rotationCfg);
        m_driveMotor.getConfigurator().apply(driveCfg);
    }

    void drive(SwerveModuleState state) {
        double driveWheelRotationsPerSecond = state.speedMetersPerSecond / MotorInfo.wheelDiameter * Math.PI;
        m_driveMotor.setControl(m_driveVelocityRequest.withVelocity(driveWheelRotationsPerSecond));

        double rotationWheelRotationsPerSecond = state.angle.getRotations() / MotorInfo.wheelDiameter * Math.PI;
        m_driveMotor.setControl(m_rotationVelocityRequest.withPosition(rotationWheelRotationsPerSecond));
    }
}
