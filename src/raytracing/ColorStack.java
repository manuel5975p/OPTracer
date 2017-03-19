package raytracing;

import java.util.ArrayList;

public class ColorStack {
	public ArrayList<ColorMask> multers;
	public ArrayList<ColorMask> adders;
	private ColorMask color;
	private boolean colorUpToDate = false;

	public ColorStack(ArrayList<ColorMask> m, ArrayList<ColorMask> a) {
		multers = m;
		adders = a;
	}
	private ColorStack addFirstMulter(){
		if(multers.size() == 1)throw new Error();
		multers.add(new ColorMask(1,1,1));
		return this;
	}
	public ColorStack bangMult(ColorMask c) {
		multers.get(multers.size() - 1).bangMult(c);
		colorUpToDate = false;
		return this;
	}

	public ColorStack bangAdd(ColorMask c) {
		adders.add(c);
		multers.add(new ColorMask(1, 1, 1));
		colorUpToDate = false;
		return this;
	}

	public ColorMask calcColor() {
		//if(Math.random() < 0.00001)
		//System.out.println(multers);
		if (!colorUpToDate) {
			colorUpToDate = true;
			ColorMask start = new ColorMask(1, 1, 1);
			start.bangMult(multers.get(multers.size() - 1));
			for (int i = multers.size() - 2; i >= 0; i--) {
				start.bangAdd(adders.get(i));
				start.bangMult(multers.get(i));
			}
			color = start;
		}
		return color;
	}

	public static ColorStack defauLt() {
		return new ColorStack(new ArrayList<ColorMask>(), new ArrayList<ColorMask>()).addFirstMulter();
	}
}
