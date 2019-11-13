package frc.robot.commands.joystick;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.Subsystems;

import net.bancino.robotics.swerveio.PointCollector;

/**
 * Drive the drivetrain with the joystick. This year is
 * a swerve drive train.
 * @author Jordan Bancino
 */
public class PointRecorder extends Command {

  public static PointCollector pc = new PointCollector();

  private long t = 0L;

  /**
   * Create the command, requiring the drivetrain to drive
   * and the gyro to provide field-centric navigation.
   */
  public PointRecorder() {

  }

  @Override
  protected void initialize() {
      t = System.currentTimeMillis();
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
    pc.collect(y, -x, -z);
  }

  /**
   * Don't end on our own, require an interruption
   */
  @Override
  protected boolean isFinished() {
    return System.currentTimeMillis() - t >= 10000;
  }

  /**
   * Stop moving when the joystick is no longer in control.
   * Generally, autonomous commands will take over from here.
   */
  @Override
  protected void end() {
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
