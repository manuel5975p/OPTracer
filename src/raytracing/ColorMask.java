package raytracing;

import java.awt.Color;

public class ColorMask {
	public double r, g, b;

	public ColorMask(double R, double G, double B) {
		r = R;
		g = G;
		b = B;
		if (r < 0 || g < 0 || b < 0) {
			throw new IllegalArgumentException("No negative values allowed");
		}
	}

	public ColorMask mult(ColorMask cm) {
		return new ColorMask(r * cm.r, g * cm.g, b * cm.b);
	}

	public ColorMask bangMult(ColorMask cm) {
		r *= cm.r;
		g *= cm.g;
		b *= cm.b;
		return this;
	}

	public boolean equals(Object o) {
		if (o instanceof ColorMask) {
			ColorMask c = (ColorMask) o;
			double a = Math.abs(c.r - r);
			if (a > 0.0000001) {
				return false;
			}
			a = Math.abs(c.g - g);
			if (a > 0.0000001) {
				return false;
			}
			a = Math.abs(c.b - b);
			if (a > 0.0000001) {
				return false;
			}
			return true;
		}
		return false;
	}

	public ColorMask pow(double n) {
		return new ColorMask(StrictMath.pow(r, n), StrictMath.pow(g, n), StrictMath.pow(b, n));
	}

	public String toString() {
		return "Farbe(" + r + ", " + g + ", " + b + ")";
	}

	public ColorMask mult(double m) {
		return new ColorMask(r * m, g * m, b * m);
	}

	public ColorMask clone() {
		return new ColorMask(r, g, b);
	}

	public ColorMask bangAdd(ColorMask o) {
		r += o.r;
		g += o.g;
		b += o.b;
		return this;
	}

	public ColorMask bangDivide(double o) {
		r /= o;
		g /= o;
		b /= o;
		return this;
	}

	public Color asColor() {
		int R, G, B;
		R = (int) (r * 255);
		G = (int) (g * 255);
		B = (int) (b * 255);
		return new Color(Math.min(R, 255), Math.min(G, 255), Math.min(B, 255));
	}
}
