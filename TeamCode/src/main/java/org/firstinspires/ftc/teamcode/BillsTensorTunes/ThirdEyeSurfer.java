package org.firstinspires.ftc.teamcode.BillsTensorTunes;

import android.util.Log;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.BillsUnexpectedRoadtrip.Cadbot;
import org.firstinspires.ftc.teamcode.BillsUtilityGarage.Vector2D1;
import org.firstinspires.ftc.teamcode.BillsUtilityGarage.Vector3D;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.firstinspires.ftc.vision.tfod.TfodProcessor;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class ThirdEyeSurfer {
    private Cadbot cadbot;
    private Telemetry telemetry;

    // the camera in robot coordinates, relative to the center of the robot
    public final static double CAMERA_Z = 4.0+(3.0/8.0); // in
    public final static double CAMERA_Y = 0.0; // in
    public final static double CAMERA_X = 17.25/2.0; // in

    private static final String TFOD_MODEL_ASSET = "SmartStout.tflite";

    private static final boolean USE_WEBCAM = true;  // true for webcam, false for phone camera

    private WebcamName webcam1, webcam2;

    private AprilTagProcessor aprilTag;
    private TfodProcessor tfod;

    private VisionPortal visionPortal;

    public boolean webcam1Online = true;
    public boolean webcam2Online = true;

    // Returns a vector from the center of the robot to the camera in robot coordinates
    public static Vector3D vectorToFrontCamera(){
        return new Vector3D(CAMERA_X, CAMERA_Y, CAMERA_Z);
    }

    public static Vector3D vectorToBackCamera(){
        return new Vector3D(-CAMERA_X, CAMERA_Y, CAMERA_Z);
    }

    // Call this to initialize the vision system when the robot first starts
    public void initialize(Cadbot cadbot){
        this.cadbot = cadbot;
        this.telemetry = cadbot.telemetry;

        // define our image processors
        aprilTag = new AprilTagProcessor.Builder()
                .setLensIntrinsics(283.873438114,283.873438114, 322.786250709, 233.764652985) // my camera calibrations
                .build();

        // Adjust Image Decimation to trade-off detection-range for detection-rate.
        // eg: Some typical detection data using a Logitech C920 WebCam
        // Decimation = 1 ..  Detect 2" Tag from 10 feet away at 10 Frames per second
        // Decimation = 2 ..  Detect 2" Tag from 6  feet away at 22 Frames per second
        // Decimation = 3 ..  Detect 2" Tag from 4  feet away at 30 Frames Per Second
        // Decimation = 3 ..  Detect 5" Tag from 10 feet away at 30 Frames Per Second
        // Note: Decimation can be changed on-the-fly to adapt during a match.
        aprilTag.setDecimation(2);

        tfod = new TfodProcessor.Builder()
                // With the following lines commented out, the default TfodProcessor Builder
                // will load the default model for the season. To define a custom model to load,
                // choose one of the following:
                //   Use setModelAssetName() if the custom TF Model is built in as an asset (AS only).
                //   Use setModelFileName() if you have downloaded a custom team model to the Robot Controller.
                .setModelAssetName(TFOD_MODEL_ASSET)
                //.setModelFileName(TFOD_MODEL_FILE)

                // The following default settings are available to un-comment and edit as needed to
                // set parameters for custom models.
                //.setModelLabels(LABELS)
                //.setIsModelTensorFlow2(true)
                //.setIsModelQuantized(true)
                //.setModelInputSize(300)
                //.setModelAspectRatio(16.0 / 9.0)
                .build();

        // define our cameras
        try {
            webcam1 = cadbot.hardwareMap.get(WebcamName.class, "Webcam 1");
        }
        catch (Exception e){
            Log.e("ThirdEyeSurfer", "Webcam 1 Not found " + e.toString());
            webcam1Online = false;
        }
        try {
            webcam2 = cadbot.hardwareMap.get(WebcamName.class, "Webcam 2");
        }
        catch (Exception e){
            Log.e("ThirdEyeSurfer", "Webcam 2 Not found " + e.toString());
            webcam2Online = false;

        }
        // define a camera switcher (not mine!)
        CameraName switchableCamera = ClassFactory.getInstance()
                .getCameraManager().nameForSwitchableCamera(webcam1, webcam2);

        // Create the vision portal using a builder and build it
        visionPortal = new VisionPortal.Builder()
                .setCamera(switchableCamera)
                .addProcessors(aprilTag, tfod)
                .build();
    }

    public double orderUp(){
        List<Recognition> currentRecognitions = tfod.getRecognitions();
        telemetry.addData("# Objects Detected", currentRecognitions.size());
        double conf = 0;
        double maxConf = 0;

        // Step through the list of recognitions and display info for each one.
        for (Recognition recognition : currentRecognitions) {
            double x = (recognition.getLeft() + recognition.getRight()) / 2 ;
            double y = (recognition.getTop()  + recognition.getBottom()) / 2 ;
            conf = recognition.getConfidence();
            if(conf > maxConf)
                maxConf = conf;
            telemetry.addData(""," ");
            telemetry.addData("Image", "%s (%.0f %% Conf.)", recognition.getLabel(), recognition.getConfidence() * 100);
            telemetry.addData("- Position", "%.0f / %.0f", x, y);
            telemetry.addData("- Size", "%.0f x %.0f", recognition.getWidth(), recognition.getHeight());
        }   // end for() loop

        return maxConf;
    }

    public void aprilTagScanOn(){
        visionPortal.setProcessorEnabled(aprilTag, true);
    }

    public void tfodScanOn(){
        visionPortal.setProcessorEnabled(tfod, true);
    }

    public void aprilTagScanOff(){
        visionPortal.setProcessorEnabled(aprilTag, false);
    }

    public void tfodScanOff(){
        visionPortal.setProcessorEnabled(tfod, false);
    }

    // activates the forward camera, you must deactivate when done
    public void switchToForwardCamera(){
        visionPortal.resumeStreaming();
        visionPortal.setActiveCamera(webcam1);
    }

    // activates the back camera, you must deactivate when done
    public void switchToBackCamera(){
        visionPortal.resumeStreaming();
        visionPortal.setActiveCamera(webcam2);
    }

    // call this to save computing resources when the camera input is not needed while the robot is running
    public void ignoreCameras(){
        visionPortal.stopStreaming();
    }

    // call this when the op mode is ending
    public void close(){
        visionPortal.close();
    }

    /**
     * Add telemetry about AprilTag detections.
     */
    private void telemetryAprilTag() {

        List<AprilTagDetection> currentDetections = aprilTag.getDetections();
        telemetry.addData("# AprilTags Detected", currentDetections.size());

        // Step through the list of detections and display info for each one.
        for (AprilTagDetection detection : currentDetections) {
            if (detection.metadata != null) {
                telemetry.addLine(String.format("\n==== (ID %d) %s", detection.id, detection.metadata.name));
                telemetry.addLine(String.format("XYZ %6.1f %6.1f %6.1f  (inch)", detection.ftcPose.x, detection.ftcPose.y, detection.ftcPose.z));
                telemetry.addLine(String.format("PRY %6.1f %6.1f %6.1f  (deg)", detection.ftcPose.pitch, detection.ftcPose.roll, detection.ftcPose.yaw));
                telemetry.addLine(String.format("RBE %6.1f %6.1f %6.1f  (inch, deg, deg)", detection.ftcPose.range, detection.ftcPose.bearing, detection.ftcPose.elevation));
            } else {
                telemetry.addLine(String.format("\n==== (ID %d) Unknown", detection.id));
                telemetry.addLine(String.format("Center %6.0f %6.0f   (pixels)", detection.center.x, detection.center.y));
            }
        }   // end for() loop

        // Add "key" information to telemetry
        telemetry.addLine("\nkey:\nXYZ = X (Right), Y (Forward), Z (Up) dist.");
        telemetry.addLine("PRY = Pitch, Roll & Yaw (XYZ Rotation)");
        telemetry.addLine("RBE = Range, Bearing & Elevation");

    }   // end method telemetryAprilTag()

    /**
     * Add telemetry about TensorFlow Object Detection (TFOD) recognitions.
     */
    private void telemetryTfod() {
        List<Recognition> currentRecognitions = tfod.getRecognitions();
        telemetry.addData("# Objects Detected", currentRecognitions.size());

        // Step through the list of recognitions and display info for each one.
        for (Recognition recognition : currentRecognitions) {
            double x = (recognition.getLeft() + recognition.getRight()) / 2 ;
            double y = (recognition.getTop()  + recognition.getBottom()) / 2 ;

            telemetry.addData(""," ");
            telemetry.addData("Image", "%s (%.0f %% Conf.)", recognition.getLabel(), recognition.getConfidence() * 100);
            telemetry.addData("- Position", "%.0f / %.0f", x, y);
            telemetry.addData("- Size", "%.0f x %.0f", recognition.getWidth(), recognition.getHeight());
        }   // end for() loop

    }   // end method telemetryTfod()

    public double frontScan(){
        switchToForwardCamera();
        tfodScanOn();
        double conf = orderUp();
        ignoreCameras();
        tfodScanOff();
        return conf;
    }

    public double backScan(){
        switchToBackCamera();
        tfodScanOn();
        double conf = orderUp();
        ignoreCameras();
        tfodScanOff();
        return conf;
    }

    public Vector2D1 scanForwardForTagLocation(){
        switchToForwardCamera();
        aprilTagScanOn();
        // todo: scan
        Vector2D1 pose = scanForTagLocation();
        ignoreCameras();
        aprilTagScanOff();
        return pose;
    }

    public Vector2D1 scanBackwardForTagLocation(){
        switchToBackCamera();
        aprilTagScanOn();
        // todo: scan
        Vector2D1 pose = scanForTagLocation();
        ignoreCameras();
        aprilTagScanOff();
        return pose;
    }

    private Vector2D1 scanForTagLocation(){

        Vector2D1 pose = null;
        List<AprilTagDetection> currentDetections = aprilTag.getDetections();
        telemetry.addData("# AprilTags Detected", currentDetections.size());

        // Step through the list of detections and display info for each one.
        for (AprilTagDetection detection : currentDetections) {
            if (detection.metadata != null) {
                telemetry.addLine(String.format("\n==== (ID %d) %s", detection.id, detection.metadata.name));
                telemetry.addLine(String.format("XYZ %6.1f %6.1f %6.1f  (inch)", detection.ftcPose.x, detection.ftcPose.y, detection.ftcPose.z));
                telemetry.addLine(String.format("PRY %6.1f %6.1f %6.1f  (deg)", detection.ftcPose.pitch, detection.ftcPose.roll, detection.ftcPose.yaw));
                telemetry.addLine(String.format("RBE %6.1f %6.1f %6.1f  (inch, deg, deg)", detection.ftcPose.range, detection.ftcPose.bearing, detection.ftcPose.elevation));
            } else {
                telemetry.addLine(String.format("\n==== (ID %d) Unknown", detection.id));
                telemetry.addLine(String.format("Center %6.0f %6.0f   (pixels)", detection.center.x, detection.center.y));
            }
        }   // end for() loop

        // Add "key" information to telemetry
        telemetry.addLine("\nkey:\nXYZ = X (Right), Y (Forward), Z (Up) dist.");
        telemetry.addLine("PRY = Pitch, Roll & Yaw (XYZ Rotation)");
        telemetry.addLine("RBE = Range, Bearing & Elevation");

        // convert the pose from camera coordinates to world coordinates of the camera, using the tag's id
        // todo: cadbot.gameField.robotLocationOnFieldFromTag();
        return pose;
    }

}

