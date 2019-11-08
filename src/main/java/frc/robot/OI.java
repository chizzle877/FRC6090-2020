package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 * 
 * This exact class defines an operator interface that consists of one 3 axis
 * joystick, and an X-Box controller. The throttle, X, Y, and Z axes come from
 * the joystick, everything else comes from the X-Box controller. Buttons can be
 * mapped to certain functions as well, since there are a few spare buttons on
 * the Joystick.
 * 
 * @author Jordan Bancino
 * @version 1.0
 * @since 1.0
 */
public class OI {
  /* Joysticks Ports */
  public static final int JOYSTICK_PORT = 0;
  public static final int XBOX_PORT = 1;

  /* Joystick Buttons */
  public static final int JOYSTICK_BUTTON_COUNT = 12;

  /*
   * Throttle configurations.
   * 
   * The throttle lower bound is the minimum speed that the motors will travel.
   */
  public static final double THROTTLE_LOWER_BOUND = 0.3d;

  /* Joysticks */
  private Joystick joystick = new Joystick(JOYSTICK_PORT);
  private Joystick xBoxJoystick = new Joystick(XBOX_PORT);

  /* Joystick Buttons */
  private JoystickButton[] joystickButton = new JoystickButton[JOYSTICK_BUTTON_COUNT + 1];

  private double deadband = 0.0;

  public OI() {
    /* Instantiate all the buttons for easy use, and less code. */
    joystickButton[0] = null; /* There is no button on 0. */
    for (int i = 1; i <= JOYSTICK_BUTTON_COUNT; i++) {
      joystickButton[i] = new JoystickButton(joystick, i);
    }
    /*
     * Assign button actions here. UPDATE: This way is DEPRECATED, only put CRUCIAL,
     * INTERNAL actions that will never change across robots here. For external,
     * robot-specific actions, use the registerCommand() method.
     */
    /* joystickButton[2].whileHeld(new SomeCommand()); */
    /* joystickButton[9].whenPressed(new AnotherCommand()); */
  }

  /**
   * The different action types that a command is invoked via. PRESS signifies
   * that when a button is pressed, a command will run until it is complete or
   * interrupted. HOLD is useful to run a command only when a button is held,
   * interrupting the command if it has not completed by the time the button is
   * released.
   */
  public static enum ActionType {
    PRESS, HOLD
  }

  /**
   * Set a deadband for use with the deadband mod function.
   * 
   * @param deadband The deadband to set. The absolute value of this must be
   *                 between zero and one.
   */
  public void setDeadband(double deadband) {
    deadband = Math.abs(deadband);
    if (deadband > 1.0 || deadband < 0) {
      throw new IllegalArgumentException("The deadband must be between 0 and 1.");
    } else {
      this.deadband = deadband;
    }
  }

  /**
   * Get the deadband being used by this OI's deadband mod function.
   * 
   * @return The deadband set with the deadband setter function.
   */
  public double getDeadband() {
    return deadband;
  }

  /**
   * Register a command with the OI, mapping it to a joystick button.
   * 
   * @param button     The button to register the command to.
   * @param actionType The action type that the command should be invoked on.
   * @param command    The command to register with the OI.
   */
  public void registerCommand(int button, ActionType actionType, Command command) {
    switch (actionType) {
    case PRESS:
      joystickButton[button].whenPressed(command);
      break;
    case HOLD:
      joystickButton[button].whileHeld(command);
      break;
    }
    System.out.println("Command registered for button: " + button + " (Type: " + actionType + ")");
  }

  /**
   * Registers a runnable interface as a command. This is extremely useful when
   * subsystem API methods need to be called directly from a joystick button, or
   * some other random, very simplistic code needs to be run that doesn't
   * necessarily require a full command. This is most practical when the runnable
   * inteface is implemented as an annonomous class, allowing very minimal code to
   * be written. Runnable's run() function is executed. This method is even more
   * efficent with the use of Lambda expressions, too! Since Runnable is a
   * single-method interface, a lambda expresson can be passed directly to this
   * function, so not even an annonomous inner type is needed!
   * 
   * @param button             The button to map the runnable too.
   * @param actionType         The button action type.
   * @param runnable           The instantiation of Runnable to execute. This is
   *                           actually wrapped into a custom command that is then
   *                           passed to the other form of registerCommand(), but
   *                           this method is specifically designed to allow very
   *                           small segments of code to run.
   * @param requiredSubsystems If any, the subsystems required to run this
   *                           runnable. Keep in mind that the code inside the
   *                           runnable is not looped, it is executed once via
   *                           Command.execute().
   */
  public void registerCommand(int button, ActionType actionType, Runnable runnable, Subsystem... requiredSubsystems) {
    /**
     * This is honestly quite a hack. It's a nested class that extends the command,
     * implementing the only two required methods, isFinished() and execute(). In
     * execute, Runnable's run function is called, the the command is signalled to
     * terminate. This is designed to be a quick and efficient way of running very
     * minimal code.
     */
    class RunnableCommand extends Command {

      private boolean isFinished = false;
      private Runnable runnable;

      public RunnableCommand(Runnable runnable, Subsystem[] requiredSubsystems) {
        for (Subsystem subsystem : requiredSubsystems) {
          requires(subsystem);
        }
        this.runnable = runnable;
      }

      @Override
      protected boolean isFinished() {
        return isFinished;
      }

      @Override
      protected void execute() {
        runnable.run();
        isFinished = true;
        System.out.println("Executed Runnable Command.");
      }
    }
    registerCommand(button, actionType, new RunnableCommand(runnable, requiredSubsystems));
  }

  /**
   * Get the raw value of the slider axis, and call it throttle.
   * 
   * @return The raw axis value of the slider.
   */
  public double getRawThrottle() {
    return this.joystick.getRawAxis(3);
  }

  /**
   * Get the adjusted throttle scale from the raw throttle. This translation
   * currently takes the scale: -1 -> 0 -> 1 <br>
   * And converts it to the scale: 0 -> 0.5 -> 1 <br>
   * Then it applies the scale to go from the lower bound -> 1
   * 
   * @return The throttle percent, set by the slider.
   */
  public double getThrottle() {
    /* Set the scale to go 0 -> 1 */
    double throttle = (0.5d * getRawThrottle()) + 0.5d;
    /* Calculate the scale to go from THROTTLE_LOWER_BOUND -> 1 */
    return ((1 - THROTTLE_LOWER_BOUND) * throttle) + THROTTLE_LOWER_BOUND;
  }

  /**
   * Apply the slider throttle to the X axis.
   * 
   * @return The throttle, multiplied by the raw X axis.
   */
  public double getThrottledX() {
    return getThrottle() * joystick.getX();
  }

  /**
   * Apply the slider throttle to the Y axis.
   * 
   * @return The throttle, multiplied by the raw Y axis.
   */
  public double getThrottledY() {
    return getThrottle() * joystick.getY();
  }

  /**
   * Apply the slider throttle to the Z axis.
   * 
   * @return The throttle, multiplied by the raw Z axis.
   */
  public double getThrottledZ() {
    return getThrottle() * joystick.getZ();
  }

  /**
   * Get the state of the specifed button on the regular joystick.
   * 
   * @param button The button to retrieve. They are labled on our joystick.
   * @return Whether or not the specified button is pressed.
   */
  public boolean getJoystickButton(int button) {
    return this.joystick.getRawButton(button);
  }

  /**
   * All possible states of the Joystick POV.
   */
  public static enum JoystickPovPosition {
    UP, DOWN, LEFT, RIGHT, DIAG_UP_LEFT, DIAG_UP_RIGHT, DIAG_DOWN_LEFT, DIAG_DOWN_RIGHT, NEUTRAL, UNKNOWN
  }

  /**
   * Get the state of the Joystick POV.
   * 
   * @return A JoystickPovPosition that represents the current POV position.
   */
  public JoystickPovPosition getJoystickPOV() {
    switch (this.joystick.getPOV()) {
    case -1:
      return JoystickPovPosition.NEUTRAL;
    case 0:
      return JoystickPovPosition.UP;
    case 45:
      return JoystickPovPosition.DIAG_UP_RIGHT;
    case 90:
      return JoystickPovPosition.RIGHT;
    case 135:
      return JoystickPovPosition.DIAG_DOWN_RIGHT;
    case 180:
      return JoystickPovPosition.DOWN;
    case 225:
      return JoystickPovPosition.DIAG_DOWN_LEFT;
    case 270:
      return JoystickPovPosition.LEFT;
    case 315:
      return JoystickPovPosition.DIAG_UP_LEFT;
    default:
      return JoystickPovPosition.UNKNOWN;

    }
  }

  /**
   * Get the state of the A button on the XBox controller.
   * 
   * @return Whether or not the A button is pressed.
   */
  public boolean xBoxA() {
    return this.xBoxJoystick.getRawButton(1);
  }

  /**
   * Get the state of the B button on the XBox controller.
   * 
   * @return Whether or not the B button is pressed.
   */
  public boolean xBoxB() {
    return this.xBoxJoystick.getRawButton(2);
  }

  /**
   * Get the state of the X button on the XBox controller.
   * 
   * @return Whether or not the X button is pressed.
   */
  public boolean xBoxX() {
    return this.xBoxJoystick.getRawButton(3);
  }

  /**
   * Get the state of the Y button on the XBox controller.
   * 
   * @return Whether or not the Y button is pressed.
   */
  public boolean xBoxY() {
    return this.xBoxJoystick.getRawButton(4);
  }

  /**
   * Get the state of the left bumper on the XBox controller.
   * 
   * @return Whether or not the left bumper is pressed.
   */
  public boolean xBoxLeftBumper() {
    return this.xBoxJoystick.getRawButton(5);
  }

  /**
   * Get the state of the right bumper on the XBox controller.
   * 
   * @return Whether or not the right bumper is pressed.
   */
  public boolean xBoxRightBumper() {
    return this.xBoxJoystick.getRawButton(6);
  }

  /**
   * Get the left trigger raw value on the XBox controller.
   * 
   * @return The current position of the left trigger.
   */
  public double xBoxLeftTrigger() {
    return this.xBoxJoystick.getRawAxis(2);
  }

  /**
   * Get the right trigger raw value on the XBox controller.
   * 
   * @return The current position of the right trigger.
   */
  public double xBoxRightTrigger() {
    return this.xBoxJoystick.getRawAxis(3);
  }

  /**
   * Get the horizontal raw value of the left joystick on the XBox controller.
   * 
   * @return The current position of the left joystick in the horizontal
   *         direction.
   */
  public double xBoxLeftJoystickHorizontal() {
    return this.xBoxJoystick.getRawAxis(0);
  }

  /**
   * Get the vertical raw value of the left joystick on the XBox controller.
   * 
   * @return The current position of the left joystick in the vertical direction.
   */
  public double xBoxLeftJoystickVertical() {
    return this.xBoxJoystick.getRawAxis(1);
  }

  /**
   * Get the horizontal raw value of the right joystick on the XBox controller.
   * 
   * @return The current position of the right joystick in the horizontal
   *         direction.
   */
  public double xBoxRightJoystickHorizontal() {
    return this.xBoxJoystick.getRawAxis(4);
  }

  /**
   * Get the vertical raw value of the right joystick on the XBox controller.
   * 
   * @return The current position of the right joystick in the vertical direction.
   */
  public double xBoxRightJoystickVertical() {
    return this.xBoxJoystick.getRawAxis(5);
  }

  /**
   * All possible states of the XBox POV.
   */
  public static enum XBoxPovPosition {
    UP, DOWN, LEFT, RIGHT, NEUTRAL, UNKNOWN
  }

  /**
   * The position of the XBox POV. <br>
   * The {@code getPOV()} function returns -1 when neutral, 0 if up, 90 if right,
   * 180 if bottom, 270 if left.
   * 
   * @return An XBoxPovPosition that reflects the position of the XBoxPov.
   */
  public XBoxPovPosition xBoxPOV() {
    // return this.xBoxJoystick.getPOV();
    switch (this.xBoxJoystick.getPOV()) {
    case -1:
      return XBoxPovPosition.NEUTRAL;
    case 0:
      return XBoxPovPosition.UP;
    case 90:
      return XBoxPovPosition.RIGHT;
    case 180:
      return XBoxPovPosition.DOWN;
    case 270:
      return XBoxPovPosition.LEFT;
    default:
      return XBoxPovPosition.UNKNOWN;

    }
  }

  /**
   * Calculate a deadband mod.
   * 
   * @param joystickInput The input on the joystick to mod
   * @return The result of the mod.
   */
  public double deadbandMod(double joystickInput) {
    /* This will be our result */
    double mod;
    /* Compute the deadband mod */
    if (joystickInput < 0.0d) {
      if (joystickInput <= -deadband) {
        mod = joystickInput + deadband;
      } else {
        mod = 0.0d;
      }
    } else {
      if (joystickInput >= deadband) {
        mod = joystickInput - deadband;
      } else {
        mod = 0.0d;
      }
    }
    /* Return the result. */
    return mod;
  }
}
