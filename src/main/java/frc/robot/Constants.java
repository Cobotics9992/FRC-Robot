package frc.robot;

public class Constants {
    public static final class CanIDs {
        public static final int leftMasterMotor = 11;
        public static final int rightMasterMotor = 12;
        public static final int leftFollowerMotor = 13;
        public static final int rightFollowerMotor = 14;

        public static final int pigeon2 = 6;

        public static final int pickupEncoder = 0;
        public static final int pickupMotor = 22;

        public static final class SwerveDrive {
            public static final int flDrive = 30;
            public static final int flRotation = 30;
            public static final int frDrive = 30;
            public static final int frRotation = 30;
            public static final int blDrive = 30;
            public static final int blRotation = 30;
            public static final int brDrive = 30;
            public static final int brRotation = 30;
        }
    }

    public static final class MotorInfo {
        // todo: change these to reflect actual values
        public static final double motorRpm = 6000;
        public static final double gearRation = 6.75;
        public static final double wheelDiameter = 0.1016;
    }
}
