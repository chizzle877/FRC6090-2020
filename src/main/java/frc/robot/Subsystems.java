package frc.robot;

import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.Gyro;

import net.bancino.robotics.swerveio.exception.SwerveException;

/**
 * Instantiate all the subsystems here for static reference, since this is how
 * WPI models it. These are declared final when possible to prevent subsystems
 * from being arbitrarily modified by other code.
 * 
 * @author Jordan Bancino
 */
public class Subsystems {
    public static DriveTrain driveTrain;

    static {
        try {
            driveTrain = new DriveTrain();
        } catch (SwerveException e) {
            e.printStackTrace();
        }
    }

    public static final Gyro gyro = new Gyro();
}