package raytracing;

import java.io.Serializable;

public class Vector3D implements Cloneable, Serializable, Comparable<Vector3D> {
	private static final long serialVersionUID = -8856917029303591750L;
	private double x;
	private double y;
	private double z;
	/**
	 * Do not modify!
	 */
	public double length;

	public Vector3D(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
		length = length();
	}
	public Vector3D multiply(double mult){
		return new Vector3D(x * mult,y * mult,z * mult);
	}
	public double length() {
		return StrictMath.sqrt(x * x + y * y + z * z);
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getZ() {
		return z;
	}

	public double dot(Vector3D other) {
		return x * other.x + y * other.y + z * other.z;
	}

	public Vector3D cross(Vector3D other) {
		return new Vector3D(y * other.z - z * other.y, z * other.x - x * other.z, x * other.y - y * other.x);
	}

	public void normalize() {
		x /= length;
		y /= length;
		z /= length;
		length = 1;
	}

	public Vector3D subtract(Vector3D other) {
		return new Vector3D(x - other.x, y - other.y, z - other.z);
	}
	public boolean equals(Object o){
		if(o instanceof Vector3D){
			Vector3D a = (Vector3D) o;
			double b;
			b = Math.abs(a.x - x);
			if(b > 0.000000001){
				return false;
			}
			b = Math.abs(a.y - y);
			if(b > 0.000000001){
				return false;
			}
			b = Math.abs(a.z - z);
			if(b > 0.000000001){
				return false;
			}
		}
		return true;
	}
	public Vector3D add(Vector3D other) {
		return new Vector3D(x + other.x, y + other.y, z + other.z);
	}

	public Vector3D clone() {
		return new Vector3D(x, y, z);
	}

	public int compareTo(Vector3D o) {
		if (length > o.length) {
			return 1;
		}
		if (length == o.length) {
			return 0;
		}
		return -1;
	}

	public String toString() {
		return "(" + x + ", " + y + ", " + z + ")";
	}
	public Vector3D normalized() {
		normalize();
		return this;
	}
	public static Vector3D random(){
		double a = Math.random() * Math.PI * 2;
		double x = StrictMath.cos(a);
		double y = StrictMath.sin(a);
		a = Math.random() * Math.PI * 2;
		double m = StrictMath.cos(a);
		x *= m;
		y *= m;
		double z = StrictMath.sin(a);
		return new Vector3D(x,y,z);
	}
	public double dist(Vector3D r) {
		double dx,dy,dz;
		dx = r.x - x;
		dx *= dx;
		dy = r.y - y;
		dy *= dy;
		dz = r.z - z;
		dz *= dz;
		return StrictMath.sqrt(dx + dy + dz);
	}
}
