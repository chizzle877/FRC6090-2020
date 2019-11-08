package frc.robot;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.Gyro;

import net.bancino.robotics.swerveio.exception.SwerveException;
import net.bancino.robotics.swerveio.exception.SwerveRuntimeException;

/**
 * An interface that can be used to create subystems.
 */
@FunctionalInterface
interface SubsystemCreator {

    /**
     * When a subsystem's constructor throws an exception, attempt to create the
     * object and return it. If it fails, convert the exception into a runtime
     * exception.
     * @return A subsystem that can be cast to the specific type.
     * @throws RuntimeException if an error creating the subsystem occurs.
     */
    public Subsystem create() throws RuntimeException;
}

/**
 * Instantiate all the subsystems here for static reference, since this is how
 * WPI models it. These are declared final when possible to prevent subsystems
 * from being arbitrarily modified by other code.
 * 
 * @author Jordan Bancino
 */
public class Subsystems {
    /** Our drivetrain, which is a SwerveIO swerve drive. */
    public static final DriveTrain driveTrain = (DriveTrain) new SubsystemCreator (){
        @Override
        public Subsystem create() throws RuntimeException {
            try {
                return new DriveTrain();
            } catch (SwerveException e) {
                throw new SwerveRuntimeException(e);
            }
        }
    }.create();

    /** The gyro. */
    public static final Gyro gyro = new Gyro();
}