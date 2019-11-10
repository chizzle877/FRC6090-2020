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
    /*
     * The joystick I was given was quite crappy, so a small deadband
     * was added to prevent random values being assigned when the joystick
     * wasn't being touched.
     */
    Robot.oi.setDeadband(0.1);
  }

  @Override
  protected void execute() {
    double y = Robot.oi.deadbandMod(Robot.oi.getThrottledY());
    double x = Robot.oi.deadbandMod(Robot.oi.getThrottledX());
    double z = Robot.oi.deadbandMod(Robot.oi.getThrottledZ());
    /**
     * Drive the drivetrain using the axes from the joystick and the gyro
     * angle.
     */
    Subsystems.driveTrain.drive(y, -x, -z, Subsystems.gyro.getYaw());
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
