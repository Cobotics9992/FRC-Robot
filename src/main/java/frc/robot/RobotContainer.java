package frc.robot;

import edu.wpi.first.units.measure.Time;
import static edu.wpi.first.units.Units.Milliseconds;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.PickupEncoderSubsystem;
import frc.robot.subsystems.PickupSubsystem;
import frc.robot.subsystems.PigeonSubsystem;

public class RobotContainer {
    private final CommandXboxController m_driverController = new CommandXboxController(0); 
    private final DriveSubsystem m_driveSubsystem = new DriveSubsystem();
    private final PickupSubsystem m_pickupSubsystem = new PickupSubsystem();
    private final PigeonSubsystem m_pigeonSubsystem = new PigeonSubsystem();
    private final PickupEncoderSubsystem m_pickupEncoderSubsystem = new PickupEncoderSubsystem();

    public RobotContainer() {
        configureBindings();
    }

    private void configureBindings() { 
        m_driveSubsystem.setDefaultCommand(
           m_driveSubsystem.drive(
            () -> -m_driverController.getLeftX(),
            () -> -m_driverController.getRightY()
          )
        ); 
        m_pickupSubsystem.setDefaultCommand(
            m_pickupSubsystem.run(
                () -> m_driverController.getLeftTriggerAxis(),
                () -> m_driverController.getRightTriggerAxis()
            )
        );
        m_driverController.start().onTrue(
            m_pigeonSubsystem.resetYaw()
        );
    }

    public Command getAutonomousCommand() {
        return new SequentialCommandGroup(
            m_driveSubsystem.drive(() -> 1.0, () -> 0.0).withTimeout(0.5),
            Commands.race(
                m_pickupSubsystem.runExact(() -> -0.4).repeatedly(),
                Commands.sequence(
                    m_driveSubsystem.drive(() -> -0.5, () -> 0.0)
                        .repeatedly().withTimeout(Milliseconds.of(400)),
                    m_driveSubsystem.drive(() -> 0.5, () -> 0.0)
                        .repeatedly().withTimeout(Milliseconds.of(400))
                )
            )
        ).repeatedly();
    }
}
