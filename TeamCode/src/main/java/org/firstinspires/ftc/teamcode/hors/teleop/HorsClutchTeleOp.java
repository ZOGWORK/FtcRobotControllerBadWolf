package org.firstinspires.ftc.teamcode.hors.teleop;

import static org.firstinspires.ftc.teamcode.MyTeamUtil.drive;
import static org.firstinspires.ftc.teamcode.MyTeamUtil.move_servos;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.firstinspires.ftc.teamcode.hors.shooter.ShooterConstants;
import org.firstinspires.ftc.teamcode.hors.shooter.ShooterController;

import java.util.ArrayList;
import java.util.List;

/**
 * A NEW teleop for BadWolf. TeleOpBadWolf is untouched and still on the menu.
 *
 * Everything except the flywheels is the same as TeleOpBadWolf on the development branch
 * (2026-11-07): drive, intake, transfer, flower servos, gates and hoods, same buttons, same values.
 * The one difference: the pollen and nectar flywheels run on ShooterController (RPM PIDF with the
 * gains in hors/shooter/ShooterConstants, which the AutoTune OpModes print) instead of
 * setVelocity(2000).
 *
 *   gamepad1 sticks         drive
 *   gamepad1 right trigger  intake + transfer
 *   gamepad1 A (release)    flower servos
 *   gamepad2 X (hold)       nectar flywheel on, nectar gate open
 *   gamepad2 Y (hold)       pollen flywheel on, pollen gate open
 *   gamepad2 right trigger  intake reverse
 *   gamepad2 sticks         hoods
 */
@TeleOp(name = "HORS Clutch Teleop", group = "HORS")
public class HorsClutchTeleOp extends LinearOpMode {

    private final List<DcMotor> wheelMotors = new ArrayList<>();
    private final List<Servo> flowerServos = new ArrayList<>();

    @Override
    public void runOpMode() {
        // ---- Hardware: same names as TeleOpBadWolf ----
        DcMotor frontLeftMotor = hardwareMap.get(DcMotor.class, "frontLeft");
        DcMotor backLeftMotor = hardwareMap.get(DcMotor.class, "backLeft");
        DcMotor frontRightMotor = hardwareMap.get(DcMotor.class, "frontRight");
        DcMotor backRightMotor = hardwareMap.get(DcMotor.class, "backRight");
        DcMotor intakeMotor = hardwareMap.get(DcMotor.class, "intake");
        DcMotor transferMotor = hardwareMap.get(DcMotor.class, "transferMotor");

        Servo flowerServo1 = hardwareMap.get(Servo.class, "flower_servo_1");
        Servo flowerServo2 = hardwareMap.get(Servo.class, "flower_servo_2");
        Servo nectarGate = hardwareMap.get(Servo.class, "gate_nectar");
        Servo pollenGate = hardwareMap.get(Servo.class, "gate_pollen");
        Servo nectarHood = hardwareMap.get(Servo.class, "nectarHood");
        // Same as TeleOpBadWolf: the pollen hood is also mapped to "nectarHood" there. Kept as is.
        Servo pollenHood = hardwareMap.get(Servo.class, "nectarHood");

        DcMotorEx nectarMotor = hardwareMap.get(DcMotorEx.class, ShooterConstants.NECTAR_SHOOTER_NAME);
        DcMotorEx pollenMotor = hardwareMap.get(DcMotorEx.class, ShooterConstants.POLLEN_SHOOTER_NAME);
        nectarMotor.setDirection(ShooterConstants.NECTAR_SHOOTER_DIRECTION);
        pollenMotor.setDirection(ShooterConstants.POLLEN_SHOOTER_DIRECTION);

        VoltageSensor battery = null;
        try {
            battery = hardwareMap.voltageSensor.iterator().next();
        } catch (Exception ignored) {
        }
        ShooterController nectar = new ShooterController(ShooterConstants.NECTAR_SHOOTER, nectarMotor, telemetry, battery);
        ShooterController pollen = new ShooterController(ShooterConstants.POLLEN_SHOOTER, pollenMotor, telemetry, battery);

        // Drive directions: same as TeleOpBadWolf (development branch).
        frontLeftMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        backLeftMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        backRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        frontRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        wheelMotors.add(frontLeftMotor);
        wheelMotors.add(backLeftMotor);
        wheelMotors.add(frontRightMotor);
        wheelMotors.add(backRightMotor);
        flowerServos.add(flowerServo1);
        flowerServos.add(flowerServo2);

        boolean currentAButton = false;
        boolean intakeYes = false;
        boolean reverseIntakeYes = false;

        telemetry.addData("Status", "HORS Clutch Teleop initialized");
        telemetry.update();

        waitForStart();
        if (isStopRequested()) return;

        while (opModeIsActive()) {
            drive(gamepad1.left_stick_x, -gamepad1.left_stick_y, gamepad1.right_stick_x, wheelMotors);

            boolean lastAButton = currentAButton;
            currentAButton = gamepad1.a;

            if (gamepad1.right_trigger_pressed) {
                intakeYes = true;
                intakeMotor.setPower(1);
                transferMotor.setPower(1);
            } else {
                if (!reverseIntakeYes) {
                    intakeMotor.setPower(0);
                }
                transferMotor.setPower(0);
            }
            if (!currentAButton && lastAButton) {
                move_servos(flowerServos);
            }

            // ---- Flywheels: ShooterController instead of setVelocity(2000) ----
            nectar.setShooterOn(gamepad2.x);
            nectarGate.setPosition(gamepad2.x ? 0.5 : 0);
            pollen.setShooterOn(gamepad2.y);
            pollenGate.setPosition(gamepad2.y ? 0.5 : 0);
            nectar.update();
            pollen.update();

            // Hoods: same conditions as TeleOpBadWolf.
            if (gamepad2.left_stick_y > 0 && gamepad2.left_stick_y < 1) {
                pollenHood.setPosition(pollenHood.getPosition() + 0.05);
            } else if (gamepad2.left_stick_y < 0 && gamepad2.left_stick_y > 0.5) {
                pollenHood.setPosition(pollenHood.getPosition() - 0.005);
            }
            if (gamepad2.right_stick_y > 0 && gamepad2.right_stick_y < 1) {
                nectarHood.setPosition(nectarHood.getPosition() + 0.05);
            } else if (gamepad2.left_stick_y < 0 && gamepad2.left_stick_y > 0.5) {
                nectarHood.setPosition(nectarHood.getPosition() - 0.005);
            }

            if (gamepad2.right_trigger_pressed) {
                intakeMotor.setPower(-1);
            } else if (!intakeYes) {
                intakeMotor.setPower(0);
            }

            telemetry.addData("Nectar", "%s  %.0f / %.0f rpm  power %.2f",
                    nectar.isShooterOn() ? "ON" : "off", nectar.getCurrentRpm(), nectar.getTargetRpm(), nectar.getLastAppliedPower());
            telemetry.addData("Pollen", "%s  %.0f / %.0f rpm  power %.2f",
                    pollen.isShooterOn() ? "ON" : "off", pollen.getCurrentRpm(), pollen.getTargetRpm(), pollen.getLastAppliedPower());
            if (nectar.encoderFault()) telemetry.addLine("NECTAR ENCODER FAULT: feedforward only");
            if (pollen.encoderFault()) telemetry.addLine("POLLEN ENCODER FAULT: feedforward only");
            telemetry.update();
        }
    }
}
