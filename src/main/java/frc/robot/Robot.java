// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software released under the WPILib BSD licence.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

public class Robot extends TimedRobot {
    private RobotContainer m_robotContainer;
    private Command m_autonomousCommand;

    @Override
    public void robotInit() {
        m_robotContainer = new RobotContainer();
    }

    @Override
    public void robotPeriodic() {
        // This runs the CommandScheduler, ensuring that all commands
        // and subsystems are called on schedule.
        CommandScheduler.getInstance().run();
    }

    @Override
    public void teleopInit() {
        // Cancels the autonomous command when teleop starts.
        if (m_autonomousCommand != null) {
            m_autonomousCommand.cancel();
        }
    }

    // All periodic teleoperated function is instantied
    // inside RobotContainer.java's configureBindings method.
    // Subsequently, this function is empty.
    @Override
    public void teleopPeriodic() {}
    
    
    @Override
    public void autonomousInit() {
        m_autonomousCommand = m_robotContainer.getAutonomousCommand();
        CommandScheduler.getInstance().schedule(m_autonomousCommand);
    }
 
    @Override
    public void autonomousPeriodic() {}


    @Override
    public void disabledInit() {}

    @Override
    public void disabledPeriodic() {}
}