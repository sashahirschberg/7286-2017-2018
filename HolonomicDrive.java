/**
 *
 * Created by , FTC Team 11779, Chrononauts
 * version 1.0 Apr 13, 2017
 *
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;


/*
   Robot wheel mapping:
   
     FL   XXXX        XXXX   FR
        X    X        X    X  
      X      X        X      X
      X      X        X      X
      XXXXXXXXXXXXXXXXXXXXXXXX
      X                      X
      X                      X
        X                  X
     BL   XXXXXXXXXXXXXXXX   BR
*/
@TeleOp(name = "HolonomicDrive", group = "firstTest")
//@Disabled
public class HolonomicDrive extends OpMode {

	DcMotor motorFrontRight;
	DcMotor motorFrontLeft;
	DcMotor motorBackRight;
	DcMotor motorBackLeft;
	public DcMotor  frontintake;
	/**
	 * Constructor
	 */
	public HolonomicDrive() {

	}

	@Override
	public void init() {


		/*
		 * Use the hardwareMap to get the dc motors and servos by name. Note
		 * that the names of the devices must match the names used when you
		 * configured your robot and created the configuration file.
		 */


		motorFrontRight = hardwareMap.dcMotor.get("MotorFR");
		motorFrontLeft = hardwareMap.dcMotor.get("MotorFL");
		motorBackLeft = hardwareMap.dcMotor.get("MotorBL");
		motorBackRight = hardwareMap.dcMotor.get("MotorBR");
		//These work without reversing (Tetrix motors).
		//AndyMark motors may be opposite, in which case uncomment these lines:
		//motorFrontLeft.setDirection(DcMotor.Direction.REVERSE);
		//motorBackLeft.setDirection(DcMotor.Direction.REVERSE);
		motorFrontRight.setDirection(DcMotor.Direction.REVERSE);
		motorBackRight.setDirection(DcMotor.Direction.REVERSE);
		frontintake = hardwareMap.dcMotor.get("intakefront");

		frontintake.setPower(0);

	}

	@Override
	public void loop() {


		// left stick controls direction
		// right stick X controls rotation

		float gamepad1LeftYTurn = -gamepad1.left_stick_y;
		float gamepad1LeftXBackforward = gamepad1.right_stick_y;
		float gamepad1RightXLeftRight = gamepad1.right_stick_x;

		// holonomic formulas

		float FrontLeft = -gamepad1LeftYTurn - gamepad1LeftXBackforward - gamepad1RightXLeftRight;
		float FrontRight = gamepad1LeftYTurn - gamepad1LeftXBackforward - gamepad1RightXLeftRight;
		float BackRight = gamepad1LeftYTurn + gamepad1LeftXBackforward - gamepad1RightXLeftRight;
		float BackLeft = -gamepad1LeftYTurn + gamepad1LeftXBackforward - gamepad1RightXLeftRight;

		// clip the right/left values so that the values never exceed +/- 1
		FrontRight = Range.clip(FrontRight, -1, 1);
		FrontLeft = Range.clip(FrontLeft, -1, 1);
		BackLeft = Range.clip(BackLeft, -1, 1);
		BackRight = Range.clip(BackRight, -1, 1);

		// write the values to the motors
		motorFrontRight.setPower(FrontRight);
		motorFrontLeft.setPower(FrontLeft);
		motorBackLeft.setPower(BackLeft);
		motorBackRight.setPower(BackRight);






		/*
		 * Telemetry for debugging
		 */
        telemetry.addData("Text", "*** Robot Data***");
		telemetry.addData("Joy XL YL XR",  String.format("%.2f", gamepad1LeftXBackforward) + " " +
				String.format("%.2f", gamepad1LeftYTurn) + " " +  String.format("%.2f", gamepad1RightXLeftRight));
        telemetry.addData("f left pwr",  "front left  pwr: " + String.format("%.2f", FrontLeft));
        telemetry.addData("f right pwr", "front right pwr: " + String.format("%.2f", FrontRight));
		telemetry.addData("b right pwr", "back right pwr: " + String.format("%.2f", BackRight));
		telemetry.addData("b left pwr", "back left pwr: " + String.format("%.2f", BackLeft));

	}

	@Override
	public void stop() {

	}

	/*
	 * This method scales the joystick input so for low joystick values, the
	 * scaled value is less than linear.  This is to make it easier to drive
	 * the robot more precisely at slower speeds.
	 */
	double scaleInput(double dVal)  {
		double[] scaleArray = { 0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24,
				0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00 };

		// get the corresponding index for the scaleInput array.
		int index = (int) (dVal * 16.0);

		// index should be positive.
		if (index < 0) {
			index = -index;
		}

		// index cannot exceed size of array minus 1.
		if (index > 16) {
			index = 16;
		}

		// get value from the array.
		double dScale = 0.0;
		if (dVal < 0) {
			dScale = -scaleArray[index];
		} else {
			dScale = scaleArray[index];
		}

		// return scaled value.
		return dScale;
	}

}
