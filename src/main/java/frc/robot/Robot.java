// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software released under the WPILib BSD licence.


package frc.robot;


import edu.wpi.first.util.sendable.SendableRegistry;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;


import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.ctre.phoenix6.hardware.Pigeon2;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

//hex bore encoder
import com.andymark.jni.AM_CAN_HexBoreEncoder;
import com.andymark.jni.AM_CAN_HexBoreEncoder.AM_EncoderStatus;
import com.andymark.jni.AM_CAN_HexBoreEncoder.AM_Encoder_Telemetry;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends TimedRobot {
    // CAN IDs
    private final WPI_VictorSPX leftMaster   = new WPI_VictorSPX(11);
    private final WPI_VictorSPX rightMaster  = new WPI_VictorSPX(12);
    private final WPI_VictorSPX leftFollower = new WPI_VictorSPX(13);
    private final WPI_VictorSPX rightFollower= new WPI_VictorSPX(14);
    private final WPI_TalonSRX pickupMotor    = new WPI_TalonSRX(22);
    private final Pigeon2 pidgey     = new   Pigeon2(6);
    AM_CAN_HexBoreEncoder /*hex bore*/encoder = new AM_CAN_HexBoreEncoder(0);
    private DifferentialDrive drive;
    private final XboxController driver = new XboxController(0);

    private AM_Encoder_Telemetry encoderTelemetry = new AM_Encoder_Telemetry();

    // for encoder telemetry
    private static final double COUNTS_PER_REV = 16384.0;
    private static final double DEG_PER_COUNT  = 360.0 / COUNTS_PER_REV;

    
   public double getVelocityDegPerSec() {
     return encoderTelemetry.velocityCountsPerSec * DEG_PER_COUNT;
   }

    public double getAngleDegrees() {
     return (encoderTelemetry.angleZeroedCounts & 0x3FFF) * DEG_PER_COUNT;
   }

    // ---------------------------------------------------------------------------
    // One-time setup
    // ---------------------------------------------------------------------------
    @Override
    public void robotInit() {
        // m_field.getObject("traj").setTrajectory(m_trajectory);
        System.out.println(pidgey.isConnected());
        pidgey.setYaw(0.0);

        //Initialize Device
        //The device's default CAN is 0. Change it using AndyMark IPK
        //SystemCore Bus 2 with device ID 0
 
        /* error for L.52
          required: int
          found:    int,int
          reason: actual and formal argument lists differ in length
          //5.10.26 changed init line to defining can id with others, L.33
          // also changed hexEncoder to encoder (var) to align w/ AM Example code for easier later
        */


        //Reset Report Period to the default of 10ms
        encoder.resetReportPeriod();

        //This will set the 0 of the encoder to the current position
        encoder.setZeroHere();

        long startTime = System.nanoTime();
        encoderTelemetry = encoder.getTelemetry();
        long endTime = System.nanoTime();
        long timeToGetTelementry = endTime - startTime;

    

        System.out.println("Took" + timeToGetTelementry / 1000000);

        // Reset all SPX settings
        leftMaster.configFactoryDefault();
        rightMaster.configFactoryDefault();
        leftFollower.configFactoryDefault();
        rightFollower.configFactoryDefault();
        pickupMotor.configFactoryDefault();


        // Followers
        leftFollower.follow(leftMaster);
        rightFollower.follow(rightMaster);
        leftFollower.setInverted(InvertType.FollowMaster);
        rightFollower.setInverted(InvertType.FollowMaster);


        // Drive-side inversion (adjust if your gearbox is mirrored)
        leftMaster.setInverted(false);//ft =left bw when drive fw
        rightMaster.setInverted(false);


        // Dif ferentialDrive wrapper
        drive = new DifferentialDrive(leftMaster, rightMaster);
        SendableRegistry.addChild(drive, leftMaster);
        SendableRegistry.addChild(drive, rightMaster);
    }


    // ---------------------------------------------------------------------------
    // Tele-op loop (called every 20 ms by default)
    // ---------------------------------------------------------------------------

    // ---------------------------------------------------------------------------
    // Current system
    // ---------------------------------------------------------------------------

    // vars
    static double mainspeed = 0.75;
    static double PickupSpeed;
    static double revertAngle = 0;
    static double PickupSpeedMod = 0.1;

    @Override
    public void teleopPeriodic() {
        if (driver.getRightBumperButton()) {
            mainspeed = 0.75;
            PickupSpeedMod = 0.80;
        }
        else if (driver.getLeftBumperButton()){
            mainspeed = 0.5;
            PickupSpeedMod = 0.25;
        }
        else {
            PickupSpeedMod = 0.80;
            mainspeed = 0.80;
        }
    
        // Split-arcade driving: left-stick Y for throttle, right-stick X for turn
        drive.arcadeDrive(
            (-driver.getLeftY() * mainspeed),   // invert so forward is positive
            (-driver.getRightX() * mainspeed)
        ); // invert so right is positive
        
        double LeftYJoy = driver.getLeftY();
        double RightXJoy = driver.getRightX();

    
        if (driver.getYButton()) {

        }
        
        if (driver.getAButton()) {
          
                
        }
    
    
        // Run console log of button pressed while x is held
        if (driver.getXButton()) {
            
        }
        
        if (driver.getBButton()) {
      
        }
    

        if (driver.getRightTriggerAxis() > 0.1 || driver.getLeftTriggerAxis() > 0.1) {
            //RT();
            //collection system
            if (driver.getRightTriggerAxis() > 0.3) {
                PickupSpeed = driver.getRightTriggerAxis() * PickupSpeedMod;
                // System.out.println("Aux speed: " + PickupSpeed);
            } else if (driver.getLeftTriggerAxis() > 0.3) {
              PickupSpeed = driver.getLeftTriggerAxis() * PickupSpeedMod * -1;
            }
        } else {
            PickupSpeed = 0.0;
        }

        if (driver.getStartButtonPressed()==true){
            revertAngle = pidgey.getYaw().getValueAsDouble();
            pidgey.setYaw(0.0);
            System.out.println("### Yaw Reset! ###");
        }

        // update telemetry
        encoderTelemetry = encoder.getTelemetry();

        pickupMotor.set(PickupSpeed);


        System.out.println(
            ""+
             
            "POV: " + driver.getPOV() + 
            " \n| AuxSpeed: " + PickupSpeed +
            " \n| LJoy: " + Math.round(LeftYJoy*1000)/1000 + 
            " \n| Mainspeed: " + Math.round(mainspeed*1000)/1000 + 
            " \n| RJoy: " + Math.round(RightXJoy*1000)/1000 + 
            " \n| Drive Powah: " + Math.round(mainspeed*1000)/1000 +
             
            " \n| Angle yaw : " + Math.round(pidgey.getYaw().getValueAsDouble()) +
            " \n| Angle pitch : " + Math.round(pidgey.getPitch().getValueAsDouble()) +
            " \n| Angle roll : " + Math.round(pidgey.getRoll().getValueAsDouble()) + 
           
            " \n| Hex Angle : " + getAngleDegrees() +
            " \n| Hex Speed : " + getVelocityDegPerSec()
        );
    }

    
    
    static double timer = 0;
    static Boolean time = false;
    static Boolean first = false;
    //autonomous
    


    @Override
    public void autonomousInit(){
      PickupSpeed = 0;
      timer = 0;
    }
 
 
    @Override
    public void autonomousPeriodic() {
        timer++;
        System.out.println("timer: "+timer);
        if (first == false) {
            drive.arcadeDrive(
                (1.0),  
                (0.0)
            );
            first = false;
        }

        if (timer >= 500.0) {   //time delay reached
            time = true;
            timer = 0;
            PickupSpeed = -0.4;
        }

        if (time == true){
            if (timer < 20) {   //backwards for 19 loops
                drive.arcadeDrive(
                    (-0.5),  
                    (0.0)
                );
            }
            if (timer >=20) {   //forwards for 31 loops
              drive.arcadeDrive(
                (0.5),  
                (0.0)
              );
              if (timer >= 50) {
                timer = 0;       //after cycle reset timer
              }
              pickupMotor.set(PickupSpeed);
            }
        } else {
            drive.arcadeDrive(
              (0.5),  
              (0.0)
            );
              
            System.out.println("else");
        }
    }
}
   
    //dpad here
    //if (driver.getdpad)
   
 
 
    /*
    dpad here
    if (driver.getdpad)
    */
  
  //-----------------------------------------------------------------------------------------------------------------
  // Potential new system of calling functions below.
  // Put inside @overide below driving systems and replace other if statements with the ones below,
  // make sure static void statements are outside of the @override statement.
  //-----------------------------------------------------------------------------------------------------------------
  // Run the aux motor while RB is held


