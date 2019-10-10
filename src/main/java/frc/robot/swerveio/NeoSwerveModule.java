package frc.robot.swerveio;

import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.CANSparkMax.IdleMode;

/**
 * A swerve module implementation that uses RevRobotics Neo
 * motors and Spark Max motor controllers.
 * @author Jordan Bancino
 */
public class NeoSwerveModule extends AbstractSwerveModule {
    private CANSparkMax driveMotor, pivotMotor;

    private CANPIDController pivotPid;

    /**
     * The swerve module is constructed to allow the pivot motor
     * to coast, this allows for adjustments, but as soon as the
     * module is driven, it switches to brake mode to prevent
     * outside modifications.
     */
    private boolean setPivotIdleMode = false;

    /**
     * Create a new swerve module composed of Neo brushless
     * motors, this uses spark max motor controllers.
     */
    public NeoSwerveModule (int driveCanId, int pivotCanId) {
        driveMotor = new CANSparkMax(driveCanId, MotorType.kBrushless);
        pivotMotor = new CANSparkMax(pivotCanId, MotorType.kBrushless);
        pivotMotor.setClosedLoopRampRate(0.4);
        pivotMotor.setIdleMode(IdleMode.kCoast);

        pivotPid = pivotMotor.getPIDController();
        pivotPid.setP(0.1);
        pivotPid.setI(1e-4);
        pivotPid.setD(1);
        pivotPid.setIZone(0);
        pivotPid.setFF(0);
        pivotPid.setOutputRange(-1, 1);
    }

    @Override
    public void setPivotMotorSpeed(double speed) {
        if (!setPivotIdleMode) {
            zeroPivotEncoder();
            pivotMotor.setIdleMode(IdleMode.kBrake);
            setPivotIdleMode = true;
        }
        pivotMotor.set(speed);
    }

    @Override
    public void setDriveMotorSpeed(double speed) {
        driveMotor.set(speed);
    }

    @Override
    public double getPivotMotorSpeed() {
        return pivotMotor.get();
    }

    @Override
    public double getDriveMotorSpeed() {
        return driveMotor.get();
    }

    @Override
    public double getPivotMotorEncoder() {
        return pivotMotor.getEncoder().getPosition();
    }

    @Override
    public double getDriveMotorEncoder() {
        return driveMotor.getEncoder().getPosition();
    }

    @Override
    public void zeroPivotEncoder() {
        pivotMotor.getEncoder().setPosition(0);
    }

    @Override
    public void zeroDriveEncoder() {
        driveMotor.getEncoder().setPosition(0);

    }

    @Override
    public void stopPivotMotor() {
        pivotMotor.stopMotor();
    }

    @Override
    public void stopDriveMotor() {
        driveMotor.stopMotor();
    }

    @Override
    public void setPivotReference(double ref) {
        pivotPid.setReference(ref, ControlType.kPosition);
    }

    @Override
    public void setDriveReference(double ref) {
        throw new SwerveImplementationException("setDriveReference() is not implemented for NeoSwerveModule!");
    }

    @Override
    public void setPivotClosedLoopRampRate(double rate) {
        pivotMotor.setClosedLoopRampRate(rate);
    }

    @Override
    public void setDriveClosedLoopRampRate(double rate) {
        driveMotor.setClosedLoopRampRate(rate);
    }

    @Override
    public void setPidControllerP(double gain) {
        pivotPid.setP(gain);
    }

    @Override
    public void setPidControllerI(double gain) {
        pivotPid.setI(gain);
    }

    @Override
    public void setPidControllerD(double gain) {
        pivotPid.setD(gain);
    }

    @Override
    public void setPidControllerIZone(double IZone) {
        pivotPid.setIZone(IZone);
    }

    @Override
    public void setPidControllerFF(double gain) {
        pivotPid.setFF(gain);
    }
}