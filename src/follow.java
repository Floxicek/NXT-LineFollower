import lejos.nxt.Button;
import lejos.nxt.Motor;
import lejos.nxt.MotorPort;
import lejos.nxt.NXTMotor;
import lejos.nxt.SensorPort;
import lejos.util.Delay;
import lejos.nxt.LightSensor;

public class follow {
	static int white;
	static int black;
	static LightSensor ls = new LightSensor(SensorPort.S4);
	static NXTMotor motA = new NXTMotor(MotorPort.A); 
	static NXTMotor motC = new NXTMotor(MotorPort.C);

	static float baseSpeed = 50f;
	static float kp = 0.67f; // reg constant
	static float ki = 0f; // reg constant
	static float antiWindup = 5/100f;
	static float kd = 0.1f; // reg constant

	static float colorMid;
	static int colorDiff;

	public static void getInitialValues() {
		System.out.println("Place on black");
		Button.waitForAnyPress();
		black = ls.getNormalizedLightValue();

		Motor.C.setSpeed(160);

		Motor.C.rotate(-200);
//		Motor.B.rotate(-150);

		white = ls.getNormalizedLightValue();

		System.out.println(black);
		System.out.println(white);

		Motor.C.rotate(200); // 90 deg robot rotation
//		Motor.B.rotate(150);
		
		Motor.C.suspendRegulation();
		colorMid = (float) (white + black) / 2.0f;
		colorDiff = Math.abs(white - black);
//		 Button.waitForAnyPress();
	}

	public static float getError() {
//		-1 to 1
		int lightValue = ls.getNormalizedLightValue();
		if (lightValue > white) {
			return 1;
		} else if (lightValue < black) {
			return -1;
		}
//		white > black

		float err = (float) (lightValue - colorMid) / (float) (colorDiff / 2.0f);
		return err;
	}

	public static void print(String str) {
		System.out.println(str);
	}

	static float i;
	static float previousErr;
	static float lastTime;
	
	public static float getDeltaTime() {
		float delta = (System.currentTimeMillis()-lastTime)/1000f;
		lastTime = System.currentTimeMillis();
		return delta;
	}

	public static void regulateSpeed() {
		float delta = getDeltaTime();
		
		float error = getError();

		float p = error;
		i += error * delta;
		i = Math.min(Math.max(i, -antiWindup), antiWindup);
		
		float d = (float)(error - previousErr)/delta;
		previousErr = error;

		float correction = p * kp + i * ki + d * kd;
		setSpeed(baseSpeed + baseSpeed * correction, baseSpeed - baseSpeed * correction);
//		System.out.println(error);
		//System.out.printf("E %.2f I %.2f\n", error, i);
		System.out.println(d);
		
	}
	static boolean aForward = true;
	static boolean bForward = true;

	public static void setSpeed(float left, float right) {
//		Motor.A.setSpeed(Math.max(right, 1));
		motA.setPower((int) right);
		motC.setPower((int) left);
//		Motor.B.setSpeed(Math.max(left, 1));
	}

	public static void main(String[] args) {
		getInitialValues();
//		Motor.A.backward();
		motA.backward();
		motC.backward();
//		Motor.B.backward();

		while (!Button.ENTER.isDown()) {
//		 	MAIN LOOP
			regulateSpeed();
			Delay.msDelay(10);
		}
		Motor.A.stop();
		Motor.B.stop();

//		for (int i = 0; i < 10; i++) {
//			System.out.println(" ");
//		}
//		 Button.waitForAnyPress();
	}
}
