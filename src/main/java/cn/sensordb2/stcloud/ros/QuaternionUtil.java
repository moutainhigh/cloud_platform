package cn.sensordb2.stcloud.ros;


public class QuaternionUtil {

    public static Quaternion Euler2Quaternion(Double yaw, Double pitch, Double roll) {
        double cy = Math.cos(yaw * 0.5);
        double sy = Math.sin(yaw * 0.5);
        double cp = Math.cos(pitch * 0.5);
        double sp = Math.sin(pitch * 0.5);
        double cr = Math.cos(roll * 0.5);
        double sr = Math.sin(roll * 0.5);
        Quaternion quaternion = new Quaternion();
        quaternion.setW(cy * cp * cr + sy * sp * sr);
        quaternion.setX(cy * cp * sr - sy * sp * cr);
        quaternion.setY(sy * cp * sr + cy * sp * cr);
        quaternion.setZ(sy * cp * cr - cy * sp * sr);
        return quaternion;
    }

    //quaternion    EulerAngles ToEulerAngles(Quaternion q) {
//   ?EulerAngles angles;
//
//   ?// roll (x-axis rotation)
//   ?double sinr_cosp = 2 * (q.w * q.x + q.y * q.z);
//   ?double cosr_cosp = 1 - 2 * (q.x * q.x + q.y * q.y);
//   ?angles.roll = std::atan2(sinr_cosp, cosr_cosp);
//
//   ?// pitch (y-axis rotation)
//   ?double sinp = 2 * (q.w * q.y - q.z * q.x);
//   ?if (std::abs(sinp) >= 1)
//       ?angles.pitch = std::copysign(M_PI / 2, sinp); // use 90 degrees if out of range
//   ?else
//       ?angles.pitch = std::asin(sinp);
//
//   ?// yaw (z-axis rotation)
//   ?double siny_cosp = 2 * (q.w * q.z + q.x * q.y);
//   ?double cosy_cosp = 1 - 2 * (q.y * q.y + q.z * q.z);
//   ?angles.yaw = std::atan2(siny_cosp, cosy_cosp);
//
//   ?return angles;
//    }
    public static Euler Quaternion2EulerQuaternion2Euler(Double w, Double x, Double y, Double z) {
        Euler euler = new Euler();
// roll (x-axis rotation)
        Double sinr_cosp = 2 * (w * x + y * z);
        Double cosr_cosp = 1 - 2 * (x * x + y * y);
        euler.setRoll(Math.atan2(sinr_cosp, cosr_cosp));
// pitch (y-axis rotation)
        Double sinp = 2 * (w * y - z * x);
        if (Math.abs(sinp) >= 1) {
             euler.setPitch(Math.copySign(Math.PI / 2, sinp));// use 90 degrees if out of range
        } else {
            euler.setPitch(Math.asin(sinp));
        }
//yaw (z-axis rotation)
        Double siny_cosp = 2 * (w * z + x * y);
        Double cosy_cosp = 1 - 2 * (y * y + z * z);
        euler.setYaw(Math.atan2(siny_cosp, cosy_cosp));
        return euler;
    }
}

