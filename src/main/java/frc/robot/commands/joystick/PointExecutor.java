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
public class PointExecutor extends Command {

    private boolean done = false;

  /**
   * Create the command, requiring the drivetrain to drive
   * and the gyro to provide field-centric navigation.
   */
  public PointExecutor() {
    requires(Subsystems.driveTrain);
    requires(Subsystems.gyro);
  }

  @Override
  protected void initialize() {
    PointRecorder.pc.rewind();
  }

  @Override
  protected void execute() {
    PointCollector.Point p;
    
    try {
        p = PointRecorder.pc.seek();
    /**
     * Drive the drivetrain using the axes from the joystick and the gyro
     * angle.
     */
    Subsystems.driveTrain.drive(p.getFwd(), p.getStr(), p.getRcw(), Subsystems.gyro.getYaw());
    } catch (IndexOutOfBoundsException e) {

    done = true;
    }
    

    
  }

  /**
   * Don't end on our own, require an interruption
   */
  @Override
  protected boolean isFinished() {
    return done;
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
