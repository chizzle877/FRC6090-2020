package frc.robot.subsystems;

import frc.robot.RobotMap;
import frc.robot.commands.joystick.DriveWithJoystick;
import net.bancino.robotics.swerveio.SwerveDrive;
import net.bancino.robotics.swerveio.SwerveModule;
import net.bancino.robotics.swerveio.module.AbstractSwerveModule;
import net.bancino.robotics.swerveio.module.MK2SwerveModule;
import net.bancino.robotics.swerveio.module.MultiEncoderModule;
import net.bancino.robotics.swerveio.module.MultiEncoderModule.EncoderSetting;

/**
 * Our implementation of the Swerve Drive subsystem.
 * @author Jordan Bancino
 */
public class DriveTrain extends SwerveDrive {
  public static final double BASE_WIDTH = 20;
  public static final double BASE_LENGTH = 22;

  /**
   * Create the SwerveDrive with the default settings and the
   * robot map.
   */
  public DriveTrain() {
    super(BASE_WIDTH, BASE_LENGTH,
      new MK2SwerveModule(RobotMap.FRONT_LEFT_DRIVE_MOTOR, RobotMap.FRONT_LEFT_PIVOT_MOTOR, RobotMap.FRONT_LEFT_ANALOG_ENCODER),
      new MK2SwerveModule(RobotMap.FRONT_RIGHT_DRIVE_MOTOR, RobotMap.FRONT_RIGHT_PIVOT_MOTOR, RobotMap.FRONT_RIGHT_ANALOG_ENCODER),
      new MK2SwerveModule(RobotMap.REAR_LEFT_DRIVE_MOTOR, RobotMap.REAR_LEFT_PIVOT_MOTOR, RobotMap.REAR_LEFT_ANALOG_ENCODER),
      new MK2SwerveModule(RobotMap.REAR_RIGHT_DRIVE_MOTOR, RobotMap.REAR_RIGHT_PIVOT_MOTOR, RobotMap.REAR_RIGHT_ANALOG_ENCODER)
    );
    countsPerPivotRevolution = 17.90471839904785;
    /**
     * Here we set the module's settings. Set them for each module.
     */
    for (var modKey : moduleMap.keySet()) {
      MultiEncoderModule module = (MultiEncoderModule) moduleMap.get(modKey);
      /* TODO: Use the analog encoder! */
      module.setEncoder(EncoderSetting.INTERNAL);
      module.zeroDriveEncoder();

      module.setPivotClosedLoopRampRate(0);
      module.setPivotPidP(0.1);
      module.setPivotPidI(1e-4);
      module.setPivotPidD(1);
      module.setPivotPidIZone(0);
      module.setPivotPidFF(0);
    }
  }

  @Override
  protected void initDefaultCommand() {
    setDefaultCommand(new DriveWithJoystick());
  }
}
