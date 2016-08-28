package segway;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.port.TachoMotorPort;
import lejos.robotics.RegulatedMotor;

/**
 * La clase EV3Motor está diseñada para trabajar con los motores. Los métodos
 * desarrollados en ella permiten establecer la velocidad y ángulo de los motores. En base al
 * codigo realizado por Steven Jan Witzand.
 * 
 * @author Erevart -- José Emilio Traver
 * @version Agosto 2016
 */

class EV3Motor
{

   private EV3LargeRegulatedMotor leftMotor;
   private EV3LargeRegulatedMotor rightMotor;
   
   // Sinusoidal parameters used to smooth motors
   private double sin_x = 0.0;
   private final double sin_speed = 0.1;
   private final double sin_amp = 20.0;

   /**
    * MotorController constructor.
    * 
    * @param leftMotor
    *           The GELways left motor.
    * @param rightMotor
    *           The GELways right motor.
    */
   public EV3Motor(Port portleftMotor,Port portrightMotor)
   {
      leftMotor = new EV3LargeRegulatedMotor(portleftMotor);
      leftMotor.resetTachoCount();

      rightMotor = new EV3LargeRegulatedMotor(portrightMotor);
      rightMotor.resetTachoCount();
   }

   /**
    * Method is used to set the power level to the motors required to keep it upright. A
    * dampened sinusoidal curve is applied to the motors to reduce the rotation of the
    * motors over time from moving forwards and backwards constantly.
    * 
    * @param leftPower
    *           A double used to set the power of the left motor. Maximum value depends on
    *           battery level but is approximately 815. A negative value results in motors
    *           reversing.
    * @param rightPower
    *           A double used to set the power of the right motor. Maximum value depends on
    *           battery level but is approximately 815. A negative value results in motors
    *           reversing.
    */
   public void setPower(double leftPower, double rightPower)
   {
      sin_x += sin_speed;
      int pwl = (int) (leftPower + Math.sin(sin_x) * sin_amp);
      int pwr = (int) (rightPower - Math.sin(sin_x) * sin_amp);

      leftMotor.setSpeed(pwl);
      if (pwl < 0) {
         leftMotor.backward();
      } else if (pwl > 0) {
         leftMotor.forward();
      } else {
         leftMotor.stop();
      }

      rightMotor.setSpeed(pwr);
      if (pwr < 0) {
         rightMotor.backward();
      } else if (pwr > 0) {
         rightMotor.forward();
      } else {
         rightMotor.stop();
      }
   }

   /**
    * getAngle returns the average motor angle of the left and right motors
    * 
    * @return A double of the average motor angle of the left and right motors in degrees.
    */
   public double getAngle()
   {
      return ((double) leftMotor.getTachoCount() + 
            (double) rightMotor.getTachoCount()) / 2.0;
   }

   /**
    * getAngle returns the average motor velocity of the left and right motors
    * 
    * @return a double of the average motor velocity of the left and right motors in
    *         degrees.
    */
   public double getAngularVelocity()
   {
      return ((double) leftMotor.getSpeed() + 
            (double) rightMotor.getSpeed()) / 2.0;
   }

   /**
    * reset the motors tacho count
    */
   public void resetMotors()
   {
      leftMotor.resetTachoCount();
      rightMotor.resetTachoCount();
   }

   /**
    * stop both motors from rotating
    */
   public void stop()
   {
      leftMotor.stop();
      rightMotor.stop();
   }
   
   /**
    * float stop both motors from rotating
    */
   public void flt()
   {
      leftMotor.flt();
      rightMotor.flt();
   }
   
}