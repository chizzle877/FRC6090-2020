package frc.robot.subsystems;

import java.util.LinkedHashMap;

import frc.robot.RobotMap;
import frc.robot.commands.joystick.DriveWithJoystick;
import net.bancino.robotics.swerveio.SwerveDrive;
import net.bancino.robotics.swerveio.SwerveModule;
import net.bancino.robotics.swerveio.module.AbstractSwerveModule;
import net.bancino.robotics.swerveio.module.MK2SwerveModule;
import net.bancino.robotics.swerveio.module.MultiEncoderModule;
import net.bancino.robotics.swerveio.module.MultiEncoderModule.EncoderSetting;
import net.bancino.robotics.swerveio.exception.SwerveException;

/**
 * Our implementation of the Swerve Drive subsystem.
 * 
 * @author Jordan Bancino
 */
public class DriveTrain extends SwerveDrive {
  public static final double BASE_WIDTH = 20;
  public static final double BASE_LENGTH = 22;
  public static final double COUNTS_PER_PIVOT_REVOLUTION = 17.90471839904785;

  /**
   * Create the SwerveDrive with the default settings and the robot map.
   */
  public DriveTrain() throws SwerveException {
    super(BASE_WIDTH, BASE_LENGTH, COUNTS_PER_PIVOT_REVOLUTION,
        () -> {
          var m = new LinkedHashMap<SwerveModule, AbstractSwerveModule>();
          m.put(SwerveModule.FRONT_RIGHT, new MK2SwerveModule(RobotMap.FRONT_RIGHT_DRIVE_MOTOR,
              RobotMap.FRONT_RIGHT_PIVOT_MOTOR, RobotMap.FRONT_RIGHT_ANALOG_ENCODER));
          m.put(SwerveModule.FRONT_LEFT, new MK2SwerveModule(RobotMap.FRONT_LEFT_DRIVE_MOTOR,
              RobotMap.FRONT_LEFT_PIVOT_MOTOR, RobotMap.FRONT_LEFT_ANALOG_ENCODER));
          m.put(SwerveModule.REAR_LEFT, new MK2SwerveModule(RobotMap.REAR_LEFT_DRIVE_MOTOR,
              RobotMap.REAR_LEFT_PIVOT_MOTOR, RobotMap.REAR_LEFT_ANALOG_ENCODER));
          m.put(SwerveModule.REAR_RIGHT, new MK2SwerveModule(RobotMap.REAR_RIGHT_DRIVE_MOTOR,
              RobotMap.REAR_RIGHT_PIVOT_MOTOR, RobotMap.REAR_RIGHT_ANALOG_ENCODER));

          return m; /* Return the module map for the constructor's use. */
        }, (module) -> {
          MultiEncoderModule memodule = (MultiEncoderModule) module;
          /* TODO: Use the analog encoder! */
          memodule.setEncoder(EncoderSetting.INTERNAL);
          memodule.zeroDriveEncoder();

          module.setPivotClosedLoopRampRate(0);
          module.setPivotPidP(0.1);
          module.setPivotPidI(1e-4);
          module.setPivotPidD(1);
          module.setPivotPidIZone(0);
          module.setPivotPidFF(0);
        });

  }

  @Override
  protected void initDefaultCommand() {
    setDefaultCommand(new DriveWithJoystick());
  }
}
