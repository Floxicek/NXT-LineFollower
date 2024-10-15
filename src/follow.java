import lejos.nxt.Button;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.LightSensor;

public class follow {
	static int white;
	static int black;
	static LightSensor ls = new LightSensor(SensorPort.S4);
	
	static int baseSpeed = 250;
	static int kp = 180; // reg constant
	
	static float colorMid;
	static int colorDiff;
	
	static float max = 0;
	
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
		 
//		 colorMid = (white - black )/2;
		 colorMid = (float)(white + black)/2.0f;
		 colorDiff = white - black;
		 
//		 Button.waitForAnyPress();
	}
	
	public static float getColorError() {
//		-1 to 1
		int lightValue = ls.getNormalizedLightValue();
//		white > black
		
//		lightValue -= colorMid;
		float err = (float)(lightValue - colorMid) / (float)(colorDiff/2.0f);
		System.out.println("err " + String.valueOf(err));
		if (err > max) {
			max = err;
		}

		return err;
		
//		float height = (float)black * 2 / (float) colorDiff;
//		float offset = - height - 1;
//		float err = (lightValue / colorMid) + offset;
//		System.out.println("height" + String.valueOf(height));
//		System.out.println("offset" + String.valueOf(offset));
//		System.out.println("err" + String.valueOf(err));
//	
//		return err;
	}
	
	public static void print(String str) {
		System.out.println(str);
	}
	
	public static void regulateSpeed() {
		float turn = kp * getColorError();
//		System.out.println(getColorError());
		 
		 
		 Motor.A.setSpeed(baseSpeed + turn);
		 Motor.B.setSpeed(baseSpeed - turn);
	}
	
	 public static void main (String[] args) 
	 { 
		 getInitialValues();
		 Motor.A.backward();
		 Motor.B.backward();
		 
		 while(true){			 
			 regulateSpeed();
			System.out.println("err" + String.valueOf(max));
		 }
	 } 
}
