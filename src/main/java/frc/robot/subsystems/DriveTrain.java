package frc.robot.subsystems;

import java.util.HashMap;

import frc.robot.RobotMap;
import frc.robot.commands.joystick.DriveWithJoystick;
import net.bancino.robotics.swerveio.SwerveDrive;
import net.bancino.robotics.swerveio.SwerveModule;
import net.bancino.robotics.swerveio.module.AbstractSwerveModule;
import net.bancino.robotics.swerveio.module.MK2SwerveModule;
import net.bancino.robotics.swerveio.module.MultiEncoderModule;
import net.bancino.robotics.swerveio.module.MultiEncoderModule.EncoderSetting;
import net.bancino.robotics.swerveio.exception.SwerveException;
import net.bancino.robotics.swerveio.encoder.MK2Encoder;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Our implementation of the Swerve Drive subsystem.
 * 
 * @author Jordan Bancino
 */
public class DriveTrain extends SwerveDrive {
  /* These are in centimeters, but unit really doesn't matter. */
  public static final double BASE_WIDTH = 51;
  public static final double BASE_LENGTH = 56;
  /* Our 1:1 Encoder. */
  public static final double COUNTS_PER_PIVOT_REVOLUTION = 360;

  /* The encoder offsets tell us where zero is for each motor. */
  public static final double FRONT_RIGHT_ENCODER_OFFSET = 91.054678;
  public static final double FRONT_LEFT_ENCODER_OFFSET  = 326.337857;
  public static final double REAR_LEFT_ENCODER_OFFSET   = 179.121075;
  public static final double REAR_RIGHT_ENCODER_OFFSET  = 234.404273;

  private static MK2Encoder frontRightEncoder = new MK2Encoder(RobotMap.FRONT_RIGHT_ANALOG_ENCODER,
      FRONT_RIGHT_ENCODER_OFFSET);
  private static MK2Encoder frontLeftEncoder = new MK2Encoder(RobotMap.FRONT_LEFT_ANALOG_ENCODER,
      FRONT_LEFT_ENCODER_OFFSET);
  private static MK2Encoder rearLeftEncoder = new MK2Encoder(RobotMap.REAR_LEFT_ANALOG_ENCODER,
      REAR_LEFT_ENCODER_OFFSET);
  private static MK2Encoder rearRightEncoder = new MK2Encoder(RobotMap.REAR_RIGHT_ANALOG_ENCODER,
      REAR_RIGHT_ENCODER_OFFSET);

  /**
   * Create the SwerveDrive with the default settings and the robot map.
   */
  public DriveTrain() throws SwerveException {
    super(BASE_WIDTH, BASE_LENGTH, COUNTS_PER_PIVOT_REVOLUTION, () -> {
      var modules = new HashMap<SwerveModule, AbstractSwerveModule>();
      modules.put(SwerveModule.FRONT_RIGHT,
          new MK2SwerveModule(RobotMap.FRONT_RIGHT_DRIVE_MOTOR, RobotMap.FRONT_RIGHT_PIVOT_MOTOR, frontRightEncoder));
      modules.put(SwerveModule.FRONT_LEFT,
          new MK2SwerveModule(RobotMap.FRONT_LEFT_DRIVE_MOTOR, RobotMap.FRONT_LEFT_PIVOT_MOTOR, frontLeftEncoder));
      modules.put(SwerveModule.REAR_LEFT,
          new MK2SwerveModule(RobotMap.REAR_LEFT_DRIVE_MOTOR, RobotMap.REAR_LEFT_PIVOT_MOTOR, rearLeftEncoder));
      modules.put(SwerveModule.REAR_RIGHT,
          new MK2SwerveModule(RobotMap.REAR_RIGHT_DRIVE_MOTOR, RobotMap.REAR_RIGHT_PIVOT_MOTOR, rearRightEncoder));
      return modules; /* Return the module map for the constructor's use. */
    }, (module) -> {
      module.setPivotClosedLoopRampRate(0);
    });
  }

  @Override
  protected void initDefaultCommand() {
    setDefaultCommand(new DriveWithJoystick());
  }
}
