package org.firstinspires.ftc.teamcode.hors.tuning.autotune;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.hors.shooter.ShooterConstants;
import org.firstinspires.ftc.teamcode.hors.shooter.ShooterController;

/** FtcAutoTune on the nectar shooter. See {@link ShooterAutoTuneOpMode}. */
@TeleOp(name = "AutoTune Nectar Shooter", group = "Tuning")
public class NectarShooterAutoTune extends ShooterAutoTuneOpMode {

    @Override
    String label() {
        return "Nectar";
    }

    @Override
    String field() {
        return "NECTAR_SHOOTER";
    }

    @Override
    String motorName() {
        return ShooterConstants.NECTAR_SHOOTER_NAME;
    }

    @Override
    DcMotorSimple.Direction direction() {
        return ShooterConstants.NECTAR_SHOOTER_DIRECTION;
    }

    @Override
    ShooterController.Config config() {
        return ShooterConstants.NECTAR_SHOOTER;
    }
}
