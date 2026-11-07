package org.firstinspires.ftc.teamcode.hors.shooter;

import com.qualcomm.robotcore.hardware.DcMotorSimple;

/**
 * BadWolf's pollen and nectar shooters: hardware names, directions, ticks per rev, target RPM and
 * tuning, for HORS Clutch Teleop and the two FtcAutoTune OpModes.
 *
 * Nothing here changes TeleOpBadWolf, FlywheelAdvanced/Flywheel or any other existing code: they
 * keep their own values. Run "AutoTune Pollen Shooter" and "AutoTune Nectar Shooter" on the robot
 * and paste each one's printed lines over the tuned values below.
 *
 * Provenance labels:
 *   BADWOLF CODE   read from BadWolf's own code (file and branch named)
 *   SPEC           from the manufacturer's spec sheet
 *   CARRIED OVER   copied from BioHORS (team 33950); true for that robot, not measured on BadWolf
 *   PLACEHOLDER    made up so the code runs
 */
public final class ShooterConstants {

    private ShooterConstants() {
    }

    // ===== Hardware names: BADWOLF CODE (teleop/TeleOpBadWolf.java and FlywheelAdvanced/Flywheel.java) =====
    public static final String POLLEN_SHOOTER_NAME = "flywheel_motor_pollen";
    public static final String NECTAR_SHOOTER_NAME = "flywheel_motor_nectar";

    // ===== Directions: BADWOLF CODE, development branch (TeleOpBadWolf, 2026-11-07 "hill" commit) =====
    // master's TeleOpBadWolf sets no direction (FORWARD); development sets both to REVERSE. The newer
    // development value is used. Flip it here if a wheel spins backwards.
    public static DcMotorSimple.Direction POLLEN_SHOOTER_DIRECTION = DcMotorSimple.Direction.REVERSE;
    public static DcMotorSimple.Direction NECTAR_SHOOTER_DIRECTION = DcMotorSimple.Direction.REVERSE;

    // ===== Encoder =====
    // 28 counts per motor-shaft rev: BADWOLF CODE (FlywheelAdvanced/Flywheel.encoderCPM = 28, marked
    // "change depending on motor later"), matches the SPEC for a goBILDA / REV 6000 RPM bare motor.
    // RPM here is MOTOR-SHAFT rpm. Flywheel.gearRatio = 5 is marked "change depending on actual
    // gearratio for badwolf" and is NOT applied: TeleOpBadWolf commands motor ticks/s directly.
    private static final double MOTOR_TICKS_PER_REV = 28.0;

    // Target: BADWOLF CODE. TeleOpBadWolf runs both wheels at setVelocity(2000) ticks/s
    // = 2000 / 28 * 60 = 4285.7 motor rpm.
    private static final double BADWOLF_TARGET_RPM = 2000.0 / MOTOR_TICKS_PER_REV * 60.0;

    public static final ShooterController.Config POLLEN_SHOOTER = new ShooterController.Config();
    public static final ShooterController.Config NECTAR_SHOOTER = new ShooterController.Config();

    static {
        POLLEN_SHOOTER.label = "Pollen";
        POLLEN_SHOOTER.ticksPerRev = MOTOR_TICKS_PER_REV;
        POLLEN_SHOOTER.targetRpm = BADWOLF_TARGET_RPM;
        // Gains: CARRIED OVER from BioHORS until "AutoTune Pollen Shooter" is run on BadWolf.
        POLLEN_SHOOTER.ffReferenceVoltage = 12.0;
        POLLEN_SHOOTER.ffReferenceMaxTicksPerSec = 2250.8;
        POLLEN_SHOOTER.kF = 1.0;
        POLLEN_SHOOTER.kS = 0.0;
        POLLEN_SHOOTER.kP = 0.02;
        POLLEN_SHOOTER.kI = 0.000000;
        POLLEN_SHOOTER.kD = 0.000296;
        POLLEN_SHOOTER.integralLimit = 50.0;        // CARRIED OVER
        POLLEN_SHOOTER.derivativeAlpha = 0.9;       // CARRIED OVER
        POLLEN_SHOOTER.rpmFilterAlpha = 0.72;       // CARRIED OVER
        POLLEN_SHOOTER.powerSmoothingAlpha = 0.5;   // CARRIED OVER
        POLLEN_SHOOTER.rpmTolerance = 75.0;         // CARRIED OVER

        NECTAR_SHOOTER.label = "Nectar";
        NECTAR_SHOOTER.ticksPerRev = MOTOR_TICKS_PER_REV;
        NECTAR_SHOOTER.targetRpm = BADWOLF_TARGET_RPM;
        // Gains: CARRIED OVER from BioHORS (its pollen values) until "AutoTune Nectar Shooter" is run on BadWolf.
        NECTAR_SHOOTER.ffReferenceVoltage = 12.0;
        NECTAR_SHOOTER.ffReferenceMaxTicksPerSec = 2250.8;
        NECTAR_SHOOTER.kF = 1.0;
        NECTAR_SHOOTER.kS = 0.0;
        NECTAR_SHOOTER.kP = 0.02;
        NECTAR_SHOOTER.kI = 0.000000;
        NECTAR_SHOOTER.kD = 0.000296;
        NECTAR_SHOOTER.integralLimit = 50.0;        // CARRIED OVER
        NECTAR_SHOOTER.derivativeAlpha = 0.9;       // CARRIED OVER
        NECTAR_SHOOTER.rpmFilterAlpha = 0.72;       // CARRIED OVER
        NECTAR_SHOOTER.powerSmoothingAlpha = 0.5;   // CARRIED OVER
        NECTAR_SHOOTER.rpmTolerance = 75.0;         // CARRIED OVER
    }

    // ===== Shooter encoder check (ShooterController), CARRIED OVER =====
    /** Only judged while the motor is given at least this power. */
    public static double SHOOTER_ENCODER_CHECK_POWER = 0.3;
    /** Below this reading, rpm, the encoder counts as silent. */
    public static double SHOOTER_ENCODER_DEAD_RPM = 50.0;
    /** Silent (or backwards) this long while pushed, seconds, means dead. */
    public static double SHOOTER_ENCODER_DEAD_S = 0.5;
}
