/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.SPI;

/**
 * The Gyro allows field centric navigation.
 * 
 * @author Jordan Bancino
 */
public class Gyro extends Subsystem {

  /* Create the AHRS NavX Gyro */
  private final AHRS navxGyro = new AHRS(SPI.Port.kMXP);

  /**
   * Zero the gyro.
   */
  public Gyro() {
    zero();
  }

  /**
   * Get the continuous angle of the gyro, accumulative.
   * 
   * @return The continuous angle straight from the gyro.
   */
  public double getAngle() {
    return navxGyro.getAngle();
  }

  /**
   * Get the actual yaw value, this is not continuous, it reports an actual angle.
   * 
   * @return The direction in degrees that the gyro is facing.
   */
  public double getYaw() {
    return navxGyro.getYaw();
  }

  public void zero() {
    System.out.println("NavX Zero-ed!");
    navxGyro.zeroYaw();
  }

  public boolean isConnected() {
    return navxGyro.isConnected();
  }

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }
}
