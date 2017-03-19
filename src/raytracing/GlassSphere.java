package raytracing;

public class GlassSphere extends Sphere {
	public static final double glassDensity = 1.0d;
	public double ior = 1.5;
	static int as = 0;

	public GlassSphere(Vector3D pos, double rad, ColorMask c) {
		super(pos, rad, c);
		diffuse = false;
	}

	public GlassSphere(Vector3D pos, double rad) {
		super(pos, rad);
		diffuse = false;
	}

	public Ray reflectedRay(Ray in) {
		if (in.inMaterial == false) {
			Vector3D refl = reflectionPoint(in);
			Vector3D sphereVect = refl.subtract(center).normalized();
			if (Math.random() < 0.02d) {
				Vector3D op = in.t.subtract(sphereVect.multiply(sphereVect.dot(in.t) * 2));
				op.normalize();
				// System.out.println(i.t.multiply(-1).dot(sphereVect) -
				// op.dot(sphereVect));
				return new Ray(refl, op, in.getColorStack().bangMult(color),false);
			}

			double inAngleCos = in.t.multiply(-1).dot(sphereVect);
			double inAngleSin = StrictMath.sqrt(1 - StrictMath.pow(inAngleCos, 2));
			// System.out.println(inAngleCos);
			double outAngleSin = inAngleSin / ior;
			Vector3D planevect = in.t.multiply(-1).cross(sphereVect);
			Vector3D onPlane = planevect.cross(sphereVect).normalized();
			double outAngleCos = StrictMath.sqrt(1 - StrictMath.pow(outAngleSin, 2));
			Vector3D rayVect = sphereVect.multiply(-1).multiply(outAngleCos);
			rayVect = rayVect.add(onPlane.multiply(outAngleSin));
			// System.out.println(rayVect.length);
			Ray s = new Ray(refl, rayVect,in.getColorStack(), true);
			if (in.r.equals(SceneDisplay.cameraPos)) {
				// System.out.println(in.t.multiply(-1).dot(sphereVect) + ", " +
				// rayVect.dot(sphereVect));
				if (!rayVect.equals(in.t)) {
					// System.out.println(onPlane.dot(in.t.multiply(mult)));
					// as++;
					// System.out.println(as);
				}
				else {

				}
			}
			return s;
		}
		else {
			// System.out.println("ja op");
			Vector3D refl = reflectionPoint(in);
			Vector3D sphereVect = refl.subtract(center).normalized();
			double inAngleCos = in.t.dot(sphereVect);
			double inAngleSin = StrictMath.sqrt(1 - StrictMath.pow(inAngleCos, 2));
			double outAngleSin = inAngleSin * ior;
			if (outAngleSin > 1) {
				Vector3D op = in.t.subtract(sphereVect.multiply(sphereVect.dot(in.t) * -2));
				op.normalize();
				Ray s = new Ray(refl,op,in.getColorStack(),true);
				s.colorRay(color.pow(glassDensity * refl.dist(in.r)));
				return s;
			}
			else {
				double outAngleCos = StrictMath.sqrt(1 - StrictMath.pow(outAngleSin, 2));
				Vector3D planevect = in.t.cross(sphereVect);
				Vector3D onPlane = planevect.cross(sphereVect).normalized().multiply(-1);
				Vector3D rayVect = sphereVect.multiply(outAngleCos);
				rayVect = rayVect.add(onPlane.multiply(outAngleSin));
				if (!rayVect.equals(in.t)) {
					// System.out.println(onPlane.dot(in.t.multiply(mult)));
					// as++;
					// System.out.println(as);
				}
				Ray s = new Ray(refl,rayVect,in.getColorStack(),false);
				s.colorRay(color.pow(glassDensity * refl.dist(in.r)));
				if(Math.random() > 0.9999)
				System.out.println(glassDensity * refl.dist(in.r));
				return s;
			}
		}
	}
}
