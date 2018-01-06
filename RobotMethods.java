import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by fsbrobots on 11/10/2017.
 */
@Override
public void init() {
        BackRight = hardwareMap.dcMotor.get("BackRight");
        BackLeft = hardwareMap.dcMotor.get("BackLeft");
        FrontRight = hardwareMap.dcMotor.get("FrontRight");
        FrontLeft = hardwareMap.dcMotor.get("FrontLeft");
        FrontRight.setDirection(DcMotor.Direction.REVERSE);
        BackRight.setDirection(DcMotor.Direction.REVERSE);
        }

    /*
     * This method will be called repeatedly in a loop
    */


public class RobotMethods extends OpMode {


    double FRval = 0;
    double FLval = 0;
    double BRval = 0;
    double BLval = 0;

    DcMotor FrontRight;
    DcMotor FrontLeft;
    DcMotor BackRight;
    DcMotor BackLeft;

    final int MAX = 1;
    final double CLIP_NUM = 0.9;
    final double FORWARD_POWER = 1;

    //int spikeTime = 0;

    public void forward(double speed, long time) {
        FrontRight.setPower(speed);
        FrontLeft.setPower(speed);
        BackRight.setPower(speed);
        BackLeft.setPower(speed);
        wait(time);
        FrontRight.setPower(0);
        FrontLeft.setPower(0);
        BackRight.setPower(0);
        BackLeft.setPower(0);

    }

}