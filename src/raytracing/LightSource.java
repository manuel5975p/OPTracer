package raytracing;

import java.awt.Color;

public class LightSource extends Sphere{
	public ColorMask color;
	public LightSource(Vector3D pos, double rad,ColorMask col) {
		super(pos, rad);
		color = col;
		diffuse = false;
	}
	/*public Ray reflectedRay(Ray i){
		return i;
	}*/
}
