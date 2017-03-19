package raytracing;

public class Sphere extends Obstacle {
	public Vector3D center;
	double radius;
	public boolean diffuse = true;
	public ColorMask color;

	public Sphere(Vector3D pos, double rad) {
		radius = rad;
		center = pos;
		color = new ColorMask(1, 1, 1);
	}

	public Sphere(Vector3D pos, double rad, ColorMask c) {
		radius = rad;
		center = pos;
		color = c;
	}

	public Sphere(Vector3D pos, double rad, ColorMask c, boolean d) {
		radius = rad;
		center = pos;
		color = c;
		diffuse = d;
	}

	public Vector3D reflectionPoint(Ray ray) {
		Vector3D v = ray.r.subtract(center);
		double b = 2 * ray.t.dot(v);
		double c = v.dot(v) - radius * radius;
		double d = b * b - 4 * c;
		double refl_t = 0;
		if (d > 0) {
			double x1 = (-b - StrictMath.sqrt(d)) / 2;
			double x2 = (-b + StrictMath.sqrt(d)) / 2;
			if (x1 >= Ray.small_value && x2 >= Ray.small_value)
				refl_t = x1;
			if (x1 < Ray.small_value && x2 >= Ray.small_value)
				refl_t = x2;
		}
		if (refl_t > Ray.small_value) {
			return ray.r.add(ray.t.multiply(refl_t));
		}
		return null;
	}

	public double reflectingT(Ray ray) {
		Vector3D v = ray.r.subtract(center);
		double b = 2 * ray.t.dot(v);
		double c = v.dot(v) - radius * radius;
		double d = b * b - 4 * c;
		double refl_t = -1;
		if (d > 0) {
			double x1 = (-b - StrictMath.sqrt(d)) / 2;
			double x2 = (-b + StrictMath.sqrt(d)) / 2;
			if (x1 >= Ray.small_value && x2 >= Ray.small_value)
				refl_t = x1;
			if (x1 < Ray.small_value && x2 >= Ray.small_value)
				refl_t = x2;
		}
		return refl_t;
	}

	public Ray reflectedRay(Ray i) {
		Vector3D refl = reflectionPoint(i);
		if (refl == null) {
			return null;
		}
		Vector3D sphereVect = refl.subtract(center).normalized();
		if (!diffuse) {
			Vector3D op = i.t.subtract(sphereVect.multiply(sphereVect.dot(i.t) * 2));
			op.normalize();
			// System.out.println(i.t.multiply(-1).dot(sphereVect) -
			// op.dot(sphereVect));
			return new Ray(refl, op, i.getColorStack().bangMult(color),false);
		}
		Vector3D refv = Vector3D.random();
		double a;
		while (true) {
			a = refv.dot(sphereVect);
			if (a > 0.05d) {
				break;
			}
			refv = Vector3D.random();
		}

		if (a < 0) {
			refv = refv.add(sphereVect.multiply(2 * a));
		}
		refv.normalize();
		if (color.equals(new ColorMask(1, 1, 1))) {
			return new Ray(refl, refv, i.getColorStack().bangMult(color.mult(Math.abs(a))).bangAdd(new ColorMask(2, 2, 2)),false);
		}
		return new Ray(refl, refv, i.getColorStack().bangMult(color.mult(Math.abs(a))),false);
	}
}
