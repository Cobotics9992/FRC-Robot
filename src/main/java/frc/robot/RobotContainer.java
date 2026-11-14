package frc.robot;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.subsystems.PickupEncoderSubsystem;
import frc.robot.subsystems.PickupSubsystem;
import frc.robot.subsystems.PigeonSubsystem;
import frc.robot.subsystems.SwerveSubsystem;

public class RobotContainer {
    private final CommandXboxController m_driverController = new CommandXboxController(0);
//     private final DriveSubsystem m_driveSubsystem = new DriveSubsystem();
    private final PickupSubsystem m_pickupSubsystem = new PickupSubsystem();
    private final PigeonSubsystem m_pigeonSubsystem = new PigeonSubsystem();
    private final PickupEncoderSubsystem m_pickupEncoderSubsystem = new PickupEncoderSubsystem();
    private final SwerveSubsystem m_swerveSubsystem = new SwerveSubsystem(m_pigeonSubsystem);

    public RobotContainer() {
        configureBindings();
    }

    private void configureBindings() {
        m_swerveSubsystem.setDefaultCommand(
                m_swerveSubsystem.driveFromJoysticks(
                        () -> -m_driverController.getLeftX(), 
                        () -> -m_driverController.getLeftY(), 
                        () -> -m_driverController.getRightX()
                )
        );
        m_pickupSubsystem.setDefaultCommand(
                m_pickupSubsystem.run(
                        () -> m_driverController.getLeftTriggerAxis(),
                        () -> m_driverController.getRightTriggerAxis()));
        m_driverController.start().onTrue(
                m_pigeonSubsystem.resetYaw());
    }

    public Command getAutonomousCommand() {
        return new SequentialCommandGroup();
        // return new SequentialCommandGroup(
        //         m_driveSubsystem.drive(() -> 1.0, () -> 0.0).withTimeout(0.5),
        //         Commands.race(
        //                 m_pickupSubsystem.runExact(() -> -0.4).repeatedly(),
        //                 Commands.sequence(
        //                         m_driveSubsystem.drive(() -> -0.5, () -> 0.0)
        //                                 .repeatedly().withTimeout(Milliseconds.of(400)),
        //                         m_driveSubsystem.drive(() -> 0.5, () -> 0.0)
        //                                 .repeatedly().withTimeout(Milliseconds.of(400)))))
        //         .repeatedly();
    }
}
