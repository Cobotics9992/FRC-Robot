package frc.robot.subsystems;

import com.ctre.phoenix6.hardware.Pigeon2;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class PigeonSubsystem extends SubsystemBase {
    private final Pigeon2 m_pigeon = new Pigeon2(6);

    public PigeonSubsystem() {
        m_pigeon.setYaw(0.0);
    }

    public double getYaw() {
        return m_pigeon.getYaw().getValueAsDouble();
    }

    public Command resetYaw() {
        return this.run(() -> {
            m_pigeon.setYaw(0.0);
            System.out.println("Resetting yaw.");
        });
    }

    @Override
    public void periodic() {
        final double yaw = m_pigeon.getYaw().getValueAsDouble();
        final double pitch = m_pigeon.getYaw().getValueAsDouble();
        final double roll = m_pigeon.getYaw().getValueAsDouble();

        SmartDashboard.putNumber("PigeonSubSystem/Yaw", yaw);
        SmartDashboard.putNumber("PigeonSubSystem/Pitch", pitch);
        SmartDashboard.putNumber("PigeonSubSystem/Roll", roll);
    }
}
