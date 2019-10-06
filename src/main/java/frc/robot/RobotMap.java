package frc.robot;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
  /**
   * Encoders - Analog input channels
   */
  public static final int FRONT_LEFT_ANALOG_ENCODER_A  = 0;
  public static final int FRONT_RIGHT_ANALOG_ENCODER_A = 1;
  public static final int REAR_LEFT_ANALOG_ENCODER_A   = 2;
  public static final int REAR_RIGHT_ANALOG_ENCODER_A  = 3;

  public static final int FRONT_LEFT_ANALOG_ENCODER_B  = 4;
  public static final int FRONT_RIGHT_ANALOG_ENCODER_B = 5;
  public static final int REAR_LEFT_ANALOG_ENCODER_B   = 6;
  public static final int REAR_RIGHT_ANALOG_ENCODER_B  = 7;

  /**
   * Motors - CAN IDs
   */
  public static final int FRONT_RIGHT_DRIVE_MOTOR = 1; /* Module 1 */
  public static final int FRONT_LEFT_DRIVE_MOTOR  = 2; /* Module 2 */
  public static final int REAR_LEFT_DRIVE_MOTOR   = 3; /* Module 3 */
  public static final int REAR_RIGHT_DRIVE_MOTOR  = 4; /* Module 4 */
  
  public static final int FRONT_RIGHT_PIVOT_MOTOR = 5; /* Module 1 */
  public static final int FRONT_LEFT_PIVOT_MOTOR  = 6; /* Module 2 */
  public static final int REAR_LEFT_PIVOT_MOTOR   = 7; /* Module 3 */
  public static final int REAR_RIGHT_PIVOT_MOTOR  = 8; /* Module 4 */
}
