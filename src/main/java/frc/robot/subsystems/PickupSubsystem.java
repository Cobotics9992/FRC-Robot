package frc.robot.subsystems;

import java.util.function.DoubleSupplier;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class PickupSubsystem extends SubsystemBase {
    private final WPI_TalonSRX m_pickupMotor = new WPI_TalonSRX(22);

    public PickupSubsystem() {
        m_pickupMotor.configFactoryDefault();
    }

    public Command run(DoubleSupplier leftTrigger, DoubleSupplier rightTrigger) {
        return this.run(() -> {
            double left = leftTrigger.getAsDouble();
            double right = rightTrigger.getAsDouble();

            final double pickupSpeedMod = 0.1;
            double pickupSpeed = 0.0;

            SmartDashboard.putNumber("PickupSubsystem/LeftTriggerValue", left);
            SmartDashboard.putNumber("PickupSubsystem/RightTriggerValue", right);

            if (right > 0.3) {
                pickupSpeed = right * pickupSpeedMod;
            } else if (left > 0.3) {
                pickupSpeed = left * pickupSpeedMod * -1;
            }

            SmartDashboard.putNumber("PickupSubsystem/Speed", pickupSpeed);

            m_pickupMotor.set(pickupSpeed);
        });
    }

    public Command runExact(DoubleSupplier speed) {
        return this.run(() -> {
            SmartDashboard.putNumber("PickupSubsystem/Speed", speed.getAsDouble());
            m_pickupMotor.set(speed.getAsDouble());
        });
    }
}
