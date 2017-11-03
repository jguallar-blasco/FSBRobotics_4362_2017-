
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

//Basic TeleOp for robot

//Two motors for moving treadmill sides
//One/Two motor to raise/lower treadmill
    //1. Button/Stick to lift, automatically lowers upon letting go.
    //2. Locks in place, can only raise/lower on command
    //3. 1., but with a button for locking

//Current inputs
/* Controller 1:
    - Left joystick used for movement
    - Right joystick used for turning
*/
/* Controller 2:
    - Left joystick raises/lowers arm
    - Right joystick moves treads
    - Left bumper freezes arm in current position while held/clicked on
 */

public class TeleOp_One extends OpMode {

    //Motor speed values
    double FRval = 0;
    double FLval = 0;
    double BRval = 0;
    double BLval = 0;

    //Motor variables
    DcMotor FrontRight; //Left and right are decided when facing same direction as robot
    DcMotor FrontLeft;
    DcMotor BackRight;
    DcMotor BackLeft;

    //Ditto for tread
    double TLval = 0;
    double TRval = 0;

    DcMotor TreadLeft;
    DcMotor TreadRight;

    //Again for the motor(s?) controlling arm that raises the treads

    double armVal = 0;

    DcMotor ArmMotor;

    final int MAX = 1;
    final double CLIP_NUM = 0.9;
    final double FORWARD_POWER = 1;

    //int spikeTime = 0;


    /*
     * Code to run when the op mode is initialized goes here
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#init()
     */

    @Override
    public void init() {
        BackRight = hardwareMap.dcMotor.get("BackRight");
        BackLeft = hardwareMap.dcMotor.get("BackLeft");
        FrontRight = hardwareMap.dcMotor.get("FrontRight");
        FrontLeft = hardwareMap.dcMotor.get("FrontLeft");
        FrontRight.setDirection(DcMotor.Direction.REVERSE);
        BackRight.setDirection(DcMotor.Direction.REVERSE);

        TreadLeft = hardwareMap.dcMotor.get("treadLeft");
        TreadRight = hardwareMap.dcMotor.get("treadRight");

        ArmMotor = hardwareMap.dcMotor.get("armMotor");
    }

    /*
     * This method will be called repeatedly in a loop
    */

    //SUNLIGHTU YELLOW
    @Override
    public void loop() {

        // throttle: left_stick_y ranges from -1 to 1, where -1 is full up, and
        // 1 is full down
        // direction: left_stick_x ranges from -1 to 1, where -1 is full left
        // and 1 is full right

        double y1 = -gamepad1.left_stick_y;
        double x1 = gamepad1.left_stick_x;
        double y2 = -gamepad1.right_stick_y;
        double x2 = gamepad1.right_stick_x; //This one is for turning

        //double leftTrigger = gamepad1.left_trigger; //Left is to accelerate, right is to brake
        //double rightTrigger = gamepad1.right_trigger;

        boolean leftBumper = gamepad2.left_bumper;
        boolean rightBumper = gamepad2.right_bumper;

        /**
        if (gamepad2.right_bumper) rightBumper = true;
        else {
            rightBumper = false;
        }

        if (gamepad2.left_bumper) leftBumper = true;
        else {
            leftBumper = false;
        }
        */

        double armStickValue = -gamepad2.right_stick_y; //input for raising/lowering arm


        double treadStickValueR = -gamepad2.left_trigger; //input for changing tread speed
        double treadStickValueL = -gamepad2.left_trigger; //input for changing tread speed

        /**
        boolean dpadUP = gamepad1.dpad_up;
        boolean dpadDOWN = gamepad1.dpad_down;
        boolean dpadLEFT = gamepad1.dpad_left;
        boolean dpadRIGHT = gamepad1.dpad_right;
        */

        //On a scale of 1, -1, if it's less than 0.05, then it may be 0 in reality. 12.75 in 255 land
        if (Math.abs(x1) <= 0.05*MAX)
            x1 = 0;
        if (Math.abs(y1) <= 0.05*MAX)
            y1 = 0;
        if (Math.abs(x2) <= 0.05*MAX)
            x2 = 0;
        if (Math.abs(y2) <= 0.05*MAX)
            y2 = 0;

        if (Math.abs(armStickValue) <= 0.05*MAX)
            armStickValue = 0;

        if (Math.abs(treadStickValueR) <= 0.05*MAX)
            treadStickValueR = 0;
        if (Math.abs(treadStickValueL) <= 0.05*MAX)
            treadStickValueL = 0;

        boolean LeftBumpVal = leftBumper;
        boolean RightBumpVal = rightBumper;
        //Decides direction by splitting circle into four quadrants, and assumes that the stick is pushed to the edge of the circle for precision between quadrants
        //See unit circle to explain why x must be less than or greater than Rt(2)/2

        if (y1 > 0) //Joystick forwards
            if (x1 < -(Math.sqrt(2)/2)) //Moving straight left
            {
                FLval = -Math.round(Math.abs(x1*10))/10.0;
                FRval = Math.round(Math.abs(x1*10))/10.0;
                BLval = Math.round(Math.abs(x1*10))/10.0;
                BRval = -Math.round(Math.abs(x1*10))/10.0;
            }
            else if (x1 > (Math.sqrt(2)/2)) //Moving right
            {
                FLval = Math.round(Math.abs(x1*10))/10.0;
                FRval = -Math.round(Math.abs(x1*10))/10.0;
                BLval = -Math.round(Math.abs(x1*10))/10.0;
                BRval = Math.round(Math.abs(x1*10))/10.0;
            }
            else //Forwards
            {
                FLval = Math.round(Math.abs(y1*10))/10.0;
                FRval = Math.round(Math.abs(y1*10))/10.0;
                BLval = Math.round(Math.abs(y1*10))/10.0;
                BRval = Math.round(Math.abs(y1*10))/10.0;
            }
        else if (y1 < 0) //Stick backwards
            if (x1 < -(Math.sqrt(2)/2)) //Straight left
            {
                FLval = -Math.round(Math.abs(x1*10))/10.0;
                FRval = Math.round(Math.abs(x1*10))/10.0;
                BLval = Math.round(Math.abs(x1*10))/10.0;
                BRval = -Math.round(Math.abs(x1*10))/10.0;
            }
            else if (x1 > (Math.sqrt(2)/2)) //Right
            {
                FLval = Math.round(Math.abs(x1*10))/10.0;
                FRval = -Math.round(Math.abs(x1*10))/10.0;
                BLval = -Math.round(Math.abs(x1*10))/10.0;
                BRval = Math.round(Math.abs(x1*10))/10.0;
            }
            else //Backwards
            {
                FLval = -Math.round(Math.abs(y1*10))/10.0;
                FRval = -Math.round(Math.abs(y1*10))/10.0;
                BLval = -Math.round(Math.abs(y1*10))/10.0;
                BRval = -Math.round(Math.abs(y1*10))/10.0;
            }
        else //stick not moved vertically
        if (x1 > 0) { //Right

            FLval = Math.round(Math.abs(x1*10))/10.0;
            FRval = -Math.round(Math.abs(x1*10))/10.0;
            BLval = -Math.round(Math.abs(x1*10))/10.0;
            BRval = Math.round(Math.abs(x1*10))/10.0;
        }
        else if (x1 < 0) { //Left
            FLval = -Math.round(Math.abs(x1*10))/10.0;
            FRval = Math.round(Math.abs(x1*10))/10.0;
            BLval = Math.round(Math.abs(x1*10))/10.0;
            BRval = -Math.round(Math.abs(x1*10))/10.0;
        }
        else { //Stick at origin
            FLval = 0;
            FRval = 0;
            BLval = 0;
            BRval = 0;
        }

        //Right now turning overrides other movement

        if (x2 != 0) //Turning
        {
            FRval = FORWARD_POWER*-(x2/(Math.abs(x2)));
            FLval = FORWARD_POWER*(x2/(Math.abs(x2)));
            BRval = FORWARD_POWER*-(x2/(Math.abs(x2)));
            BLval = FORWARD_POWER*(x2/(Math.abs(x2)));
        }

        //For changing tread speed
        TLval = treadStickValueL;
        TRval = treadStickValueR;


        //For changing arm value
        armVal = armStickValue;

        Range.clip(FLval, -CLIP_NUM, CLIP_NUM); //This is to make sure that no STRANGE values somehow get in
        Range.clip(BLval, -CLIP_NUM, CLIP_NUM);
        Range.clip(BRval, -CLIP_NUM, CLIP_NUM);
        Range.clip(FRval, -CLIP_NUM, CLIP_NUM);

        /*
        boolean imaniDoesNotCareForHerOwnSafety = gamepad2.a;
        if ((lastWasForSlow && !imaniDoesNotCareForHerOwnSafety) || (lastWasBackSlow && !gamepad2.y))
            spikeTime = 0;
        */

        FrontRight.setPower(FRval);
        FrontLeft.setPower(FLval);
        BackRight.setPower(BRval);
        BackLeft.setPower(BLval);

        TreadLeft.setPower(TLval);
        TreadRight.setPower(TRval);

        ArmMotor.setPower(armVal);

        telemetry.addData("Front Left: ", FLval);
        telemetry.addData("Front Right: ", FRval);
        telemetry.addData("Back Left: ", BLval);
        telemetry.addData("Back Right: ", BRval);
        telemetry.addData("Tread Left: ", TLval);
        telemetry.addData("Tread Right: ", TRval);
        telemetry.addData("Arm: ", armVal);
        telemetry.addData("xLeft: ", x1);
        telemetry.addData("yLeft: ", y1);
        telemetry.addData("xRight: ", x2);
    }

    /*
     * Code to run when the op mode is first disabled goes here
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#stop()
     */
    @Override
    public void stop() {

    }

}
