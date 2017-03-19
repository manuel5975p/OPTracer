package raytracing;

import java.util.ArrayList;

public class Ray {
	public static final double small_value = 0.00000001;
	public Vector3D r;
	public Vector3D t;
	public int level = 1;
	ColorStack colorStack;
	private boolean colorUpToDate = false;
	public boolean inMaterial = false;

	private Ray() {
		colorStack = ColorStack.defauLt();
	}

	public Ray(Vector3D start, Vector3D adder) {
		this();
		r = start;
		t = adder;
		t.normalize();
	}

	public Ray colorRay(ColorMask c) {
		colorStack.bangMult(c);
		return this;
	}

	public Ray brightenRay(ColorMask c) {
		colorStack.bangAdd(c);
		return this;
	}

	public ColorMask getColor() {
		ColorMask ret = colorStack.calcColor();
		
		if(Math.random() > 0.99999d){
			//System.out.println(Thread.currentThread().getStackTrace()[2].getMethodName());
			//System.out.println(ret);
		}
		return ret;
	}

	/*public Ray(Vector3D start, Vector3D adder, boolean inM) {
		this();
		r = start;
		t = adder;
		t.normalize();
		color = new ColorMask(1, 1, 1);
		inMaterial = inM;
	}*/
	public Ray(Vector3D start, Vector3D adder, ColorStack i, boolean inM) {
		r = start;
		t = adder;
		t.normalize();
		colorStack = i;
		inMaterial = inM;
	}
	/*public Ray(Vector3D start, Vector3D adder, ColorMask i, boolean inM) {
		this();
		r = start;
		t = adder;
		t.normalize();
		color = i;
		inMaterial = inM;
	}

	public Ray(Vector3D start, Vector3D adder, ColorMask i) {
		this();
		r = start;
		t = adder;
		t.normalize();
		color = i;
	}*/

	public Ray setLevel(int level) {
		this.level = level;
		return this;
	}
	public ColorStack getColorStack(){
		return colorStack;
	}
	public String toString() {
		return r.toString() + " + t" + t.toString();
	}

	public Ray clone() {
		return new Ray(r.clone(), t.clone(), getColorStack(), inMaterial).setLevel(level);
	}

	public ObstacleAndRay reflectFromScene(ArrayList<Obstacle> spheres) {
		Obstacle reflectingSphere = null;
		double t_lowscore = 10000000;
		for (int i = 0; i < spheres.size(); i++) {
			double a = spheres.get(i).reflectingT(this);
			if (a < t_lowscore && a > small_value) {
				t_lowscore = a;
				reflectingSphere = spheres.get(i);
			}
		}
		if (reflectingSphere == null) {
			return null;
		}
		else {
			Ray ret = reflectingSphere.reflectedRay(clone());
			ret.level = level + 1;
			return new ObstacleAndRay(ret, reflectingSphere);
		}
	}

	public Obstacle nextIntersection(ArrayList<Obstacle> obstacles) {
		double low = 10000000;
		Obstacle lowo = null;
		for (int i = 0; i < obstacles.size(); i++) {
			Obstacle obs = obstacles.get(i);
			double c = obs.reflectingT(this);
			if (c > 0.0000000001 && c < low) {
				low = c;
				lowo = obs;
			}
		}
		return lowo;
	}
}
