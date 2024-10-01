import lejos.nxt.Button;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.LightSensor;

public class follow {
	static int white;
	static int black;
	static LightSensor ls = new LightSensor(SensorPort.S4);
	
	static int baseSpeed = 50;
	static int kp = 10; // reg constant
	
	static int colorMid;
	
	
	public static void getInitialValues() {
		 
		 System.out.println("Place on black"); 
		 Button.waitForAnyPress();
		 black = ls.getNormalizedLightValue();
		 
		 Motor.A.setSpeed(90);
		 Motor.B.setSpeed(90);
		 Motor.A.rotate(150, true);
		 Motor.B.rotate(-150);
		 
		 white = ls.getNormalizedLightValue();
		 
		 System.out.println(black);
		 System.out.println(white);
		 
		 Motor.A.rotate(-150, true); // 90 deg robot rotation
		 Motor.B.rotate(150);
		 
		 colorMid = (black - white)/2;
		 
		 Button.waitForAnyPress();
	}
	
	public static void regulateSpeed() {
		 int lightValue = ls.getNormalizedLightValue();
		 int error = lightValue - colorMid;
//		 int idk = lightValue - black;
		 int turn = kp * error;
		 
		 
		 
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
		 }
	 } 
}
