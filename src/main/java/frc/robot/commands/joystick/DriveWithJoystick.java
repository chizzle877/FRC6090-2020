package frc.robot.commands.joystick;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.Subsystems;

/**
 * Drive the drivetrain with the joystick. This year is
 * a swerve drive train.
 * @author Jordan Bancino
 */
public class DriveWithJoystick extends Command {
  /**
   * Create the command, requiring the drivetrain to drive
   * and the gyro to provide field-centric navigation.
   */
  public DriveWithJoystick() {
    requires(Subsystems.driveTrain);
    requires(Subsystems.gyro);
  }

  @Override
  protected void initialize() {
    Robot.oi.setDeadband(0.15);
  }

  @Override
  protected void execute() {
    double throttle = Robot.oi.getThrottle();
    double fwd = Robot.oi.deadbandMod(Robot.oi.xBoxLeftJoystickVertical()* throttle);
    double str = -Robot.oi.deadbandMod(Robot.oi.xBoxLeftJoystickHorizontal()* throttle);
    double rcw = -Robot.oi.deadbandMod(Robot.oi.xBoxRightJoystickHorizontal()* throttle);
    /**
     * Drive the drivetrain using the axes from the joystick and the gyro
     * angle.
     */
    Subsystems.driveTrain.drive(fwd, str, rcw, Subsystems.gyro.getYaw());
  }

  /**
   * Don't end on our own, require an interruption
   */
  @Override
  protected boolean isFinished() {
    return false;
  }

  /**
   * Stop moving when the joystick is no longer in control.
   * Generally, autonomous commands will take over from here.
   */
  @Override
  protected void end() {
    Subsystems.driveTrain.stop();
  }

  /**
   * End this command if it is interrupted by another one.
   * This will allow autonomous to take over.
   */
  @Override
  protected void interrupted() {
    end();
  }
}
