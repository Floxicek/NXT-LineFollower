import lejos.nxt.Button;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.LightSensor;

public class follow {
	static int white;
	static int black;
	static LightSensor ls = new LightSensor(SensorPort.S4);
	
	static int baseSpeed = 300;
	static int kp = 215; // reg constant
	static int ki = 0; // reg constant
	static int kd = 240; // reg constant
	
	static float colorMid;
	static int colorDiff;
	
	public static void getInitialValues() { 
		 System.out.println("Place on black"); 
		 Button.waitForAnyPress();
		 black = ls.getNormalizedLightValue();
		 
		 Motor.A.setSpeed(80);
		 Motor.B.setSpeed(80);
		 
		 Motor.A.rotate(150, true);
		 Motor.B.rotate(-150);
		 
		 white = ls.getNormalizedLightValue();
		 
		 System.out.println(black);
		 System.out.println(white);
		 
		 Motor.A.rotate(-150, true); // 90 deg robot rotation
		 Motor.B.rotate(150);
		 colorMid = (float)(white + black)/2.0f;
		 colorDiff = white - black;
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
		
		float err = (float)(lightValue - colorMid) / (float)(colorDiff/2.0f);
		return err;
	}
	
	public static void print(String str) {
		System.out.println(str);
	}
	
	static float i;
	static float previousErr;
	public static void regulateSpeed() {
		float error = getError();
		
		float p = error;
		i += error;
		float d = error - previousErr;
		previousErr = error;
		
		
		float correction = p * kp + i * ki + d*kd;
		
//		System.out.println(getColorError());
		 
		 
		 Motor.A.setSpeed(baseSpeed + correction);
		 Motor.B.setSpeed(baseSpeed - correction);
		 
		 
		 
		 
		 
	}
	
	 public static void main (String[] args) 
	 { 
		 getInitialValues();
		 Motor.A.backward();
		 Motor.B.backward();
		 
		 while(!Button.ENTER.isDown()){			 
//		 	MAIN LOOP
			 regulateSpeed();
		 }
		 Motor.A.stop();
		 Motor.B.stop();
		 
		 for (int i = 0; i < 10; i++) {
			System.out.println(" ");
		}
		 Button.waitForAnyPress();
		 } 
}
