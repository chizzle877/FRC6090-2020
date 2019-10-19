package frc.robot.subsystems;

import frc.robot.RobotMap;
import frc.robot.commands.joystick.DriveWithJoystick;
import frc.robot.swerveio.AbstractSwerveModule;
import frc.robot.swerveio.MultiEncoderModule;
import frc.robot.swerveio.NeoSwerveModule;
import frc.robot.swerveio.SwerveDrive;
import frc.robot.swerveio.SwerveDriveCalculator;
import frc.robot.swerveio.SwerveImplementationException;
import frc.robot.swerveio.SwerveModule;
import frc.robot.swerveio.MultiEncoderModule.EncoderSetting;

import java.util.HashMap;

/**
 * The Swerve Drive subsystem.
 * 
 * The decision to have the maps be returned as unmodifiable
 * HashMap is so that members outside of this class cannot directly
 * add or remove items to the map, or change the values. That behavior
 * could lead to unpredictable results, so as a safety mechanism, while
 * the values of the map can be operated on, they cannot be modified.
 * @author Jordan Bancino
 */
public class DriveTrain extends SwerveDrive {
  public static final double BASE_WIDTH = 20;
  public static final double BASE_LENGTH = 22;

  /**
   * This map stores how many counts it takes to go one full revolution of the wheel
   * on the pivot motor. This is set up for MultiEncoderModules that may switch back
   * and forth between encoders, so each encoder's revolution constant can be set here.
   */
  public static final double PIVOT_REVOLUTION = 17.90471839904785;

  private static final HashMap<SwerveModule, AbstractSwerveModule> modules = createModuleMap();

  public static HashMap<SwerveModule, AbstractSwerveModule> createModuleMap() {
    HashMap<SwerveModule, MultiEncoderModule> moduleMap = new HashMap<>();
    moduleMap.put(SwerveModule.FRONT_LEFT, new NeoSwerveModule(RobotMap.FRONT_LEFT_DRIVE_MOTOR, RobotMap.FRONT_LEFT_PIVOT_MOTOR, RobotMap.FRONT_LEFT_ANALOG_ENCODER));
    moduleMap.put(SwerveModule.FRONT_RIGHT, new NeoSwerveModule(RobotMap.FRONT_RIGHT_DRIVE_MOTOR, RobotMap.FRONT_RIGHT_PIVOT_MOTOR, RobotMap.FRONT_RIGHT_ANALOG_ENCODER));
    moduleMap.put(SwerveModule.REAR_LEFT, new NeoSwerveModule(RobotMap.REAR_LEFT_DRIVE_MOTOR, RobotMap.REAR_LEFT_PIVOT_MOTOR, RobotMap.REAR_LEFT_ANALOG_ENCODER));
    moduleMap.put(SwerveModule.REAR_RIGHT, new NeoSwerveModule(RobotMap.REAR_RIGHT_DRIVE_MOTOR, RobotMap.REAR_RIGHT_PIVOT_MOTOR, RobotMap.REAR_RIGHT_ANALOG_ENCODER));
    HashMap<SwerveModule, AbstractSwerveModule> retMap = new HashMap<>();
    for (var modKey : moduleMap.keySet()) {
      MultiEncoderModule module = moduleMap.get(modKey);
      /* TODO: Use the analog encoder! */
      module.setEncoder(EncoderSetting.INTERNAL);
      module.zeroDriveEncoder();
      retMap.put(modKey, module);
    }
    return retMap;
  }

  /**
   * Create the SwerveDrive with the default settings and the
   * robot map.
   */
  public DriveTrain() {
    super(BASE_WIDTH, BASE_LENGTH, modules);
  }

  @Override
  public void drive(double fwd, double str, double rcw, double gyroAngle) throws SwerveImplementationException {
    for (SwerveModule module : modules.keySet()) {
      //SwerveModule module = SwerveModule.FRONT_LEFT;
        if (module != null) {
          double speed = calc.getWheelSpeed(module, fwd, str, rcw); /* Use the calculator to calculate wheel speed for this module. */
          double targetAngle = calc.getWheelAngle(module, fwd, str, rcw, gyroAngle); /* Calculate the pivot angle for this module. */
          AbstractSwerveModule swerveModule = modules.get(module); /* Fetch the module from the module map so we can manipulate it. */

          /**
           * Convert the target angle from a degree measure to an encoder reference using the
           * static method in the swerve drive calculator. This takes the angle measure in degrees
           * and how many encoder counts it takes to go one full revolution to convert the angle
           * into an encoder count.
           */
          double currentPos = swerveModule.getPivotMotorEncoder();
          double targetPos = SwerveDriveCalculator.convertFromDegrees(targetAngle, PIVOT_REVOLUTION);

          /* Find the distance between where we currently are and where we want to be. */
          double distance = (targetPos - (currentPos % PIVOT_REVOLUTION));
          /**
           * Find the shortest distance possible to get from the current position to
           * the target position.
           */
          if (distance > (PIVOT_REVOLUTION / 2.0) || distance < - (PIVOT_REVOLUTION / 2.0)) {
            distance = PIVOT_REVOLUTION - Math.abs(distance);
          }
          /* The final reference is just an offset of distance from the current position. */
          double pivotRef = currentPos + distance;

          /* Pass the pivot reference into the pivot motor of the swerve module. */
          swerveModule.setPivotReference(pivotRef);
          /* Set the drive motor speed to the calculated speed. */
          swerveModule.setDriveMotorSpeed(speed);
        }
    } 
  }

  @Override
  protected void initDefaultCommand() {
    setDefaultCommand(new DriveWithJoystick());
  }
}
