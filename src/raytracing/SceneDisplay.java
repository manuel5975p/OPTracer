package raytracing;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class SceneDisplay extends JPanel {
	private static final long serialVersionUID = 85762398746459807L;
	static final double lightbright = 3;
	static Vector3D cameraPos = new Vector3D(0, 0, -6);
	static Vector3D spherePos = new Vector3D(1, 0, 12);
	static double sphereRad = 1;
	static Sphere sfir = new Sphere(new Vector3D(-6, -3, 10), 4);
	Color[][] pixels;
	public static final int count = 60;
	public static final ColorMask ambient = new ColorMask(0.4, 0.4, 0.4);
	static JFrame j;
	private int iProgress = 0;
	int width;
	private int height;
	boolean saved = false;
	boolean finished = false;
	ArrayList<Obstacle> sceneObjects = new ArrayList<Obstacle>();

	public void init(int width, int height) {
		this.width = width;
		this.height = height;
		//sceneObjects.add(sfir);
		sceneObjects.add(new Sphere(new Vector3D(0, 1, 11), 0.5));
		sceneObjects.add(new GlassSphere(new Vector3D(0, 0, 12), 0.5, new ColorMask(0.98, 0.98, 0.6)));
		sceneObjects.add(new Sphere(new Vector3D(0, 0, 22), 9.5,new ColorMask(.5,0.5,0.5),false));
		/*sceneObjects.add(new Sphere(new Vector3D(0, -0.4, 12), 0.5, new ColorMask(1, 1, 0.999), true));
		sceneObjects.add(new Sphere(new Vector3D(0, 0.6, 12), 0.5, new ColorMask(1, 1, 0), true));
		sceneObjects.add(new Sphere(new Vector3D(-1, 0.6, 12), 0.5, new ColorMask(0, 1, 1), true));
		sceneObjects.add(new GlassSphere(new Vector3D(0.7, -0.2, 10.5), 0.5, new ColorMask(0.98, 0.98, 0.98)));*/
		//sceneObjects.add(new LightSource(new Vector3D(-0.5, -3.3, 9), 2, new ColorMask(1, 1, 1)));
		pixels = new Color[width][height];
		TracingThread t1 = new TracingThread(width, height, 0, sceneObjects);
		TracingThread t2 = new TracingThread(width, height, 1, sceneObjects);
		TracingThread t3 = new TracingThread(width, height, 2, sceneObjects);
		TracingThread t4 = new TracingThread(width, height, 3, sceneObjects);
		t1.start();
		t2.start();
		t3.start();
		t4.start();
		Thread prog = new Thread() {
			public void run() {
				try {
					while (true) {
						Thread.sleep(1000);
						System.out.println("Fortschritt: " + (int)((t1.progress() + t2.progress() + t3.progress() + t4.progress()) * 25) + "%");
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		prog.start();
		try {
			t1.join();
			t2.join();
			t3.join();
			t4.join();
			prog.stop();
		}
		
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		for (int i = 0; i < width; i++) {
			int a = i % 4;
			for (int ih = 0; ih < height; ih++) {
				if(a == 0){
					pixels[i][ih] = t1.pixels[i / 4][ih];
				}
				if(a == 1){
					pixels[i][ih] = t2.pixels[i / 4][ih];
				}
				if(a == 2){
					pixels[i][ih] = t3.pixels[i / 4][ih];
				}
				if(a == 3){
					pixels[i][ih] = t4.pixels[i / 4][ih];
				}
			}
		}
		// pixels = t1.pixels;
		finished = true;
		repaint();
		SceneDisplay.j.revalidate();
		// System.out.println("F");
	}

	public SceneDisplay(int width, int height) {
		super();
		// init(width,height);
	}

	public static void main(String[] args) {
		ambient.bangDivide(lightbright);
		j = new JFrame("Raytracing");
		int width = 300;
		int height = 300;
		j.setSize(width, height);
		j.setResizable(false);
		j.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		j.setVisible(true);
		j.setContentPane(new SceneDisplay(width, height));
		// .init(sd.width, sd.height);
		j.revalidate();
		((SceneDisplay) j.getContentPane()).init(width, height);
		// j.getContentPane().repaint();
		// j.repaint();
		// j.getContentPane().getGraphics().setColor(new Color(1, 1, 1));

	}

	public void paintComponent(Graphics g) {

		BufferedImage bi = null;
		if (!saved && false)
			bi = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
		g.drawRect(0, 0, width, height);
		o: for (int i = 0; i < pixels.length; i++) {
			if (i > iProgress) {
				iProgress = i;
			}
			for (int ih = 0; ih < pixels[0].length; ih++) {
				if (pixels[i][ih] != null) {
					g.setColor(pixels[i][ih]);
					g.drawRect(i, ih, 1, 1);
				}
				else {
					break o;
				}
				if (!saved && false)
					bi.setRGB(i, ih, pixels[i][ih].getRGB());

			}
		}
		if (!saved && iProgress == width - 1) {
			bi = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
			for (int i = 0; i < pixels.length; i++) {
				for (int ih = 0; ih < pixels[0].length; ih++) {
					bi.setRGB(i, ih, pixels[i][ih].getRGB());
				}
			}
			try {
				ImageIO.write(bi, "png", new File("OPBild.png"));
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static boolean hitsSphere(Ray ray, Vector3D spherePos, double rad) {
		Vector3D v = ray.r.subtract(spherePos);
		double b = 2 * ray.t.dot(v);
		double c = dot(v, v) - rad * rad;
		double d = b * b - 4 * c;
		if (d > 0) {
			double x1 = (-b - Math.sqrt(d)) / 2;
			double x2 = (-b + Math.sqrt(d)) / 2;
			if (x1 >= 0 && x2 >= 0)
				return true;// x1
			if (x1 < 0 && x2 >= 0)
				return true;// x2
		}
		return false;
	}

	/*
	 * public static boolean hitsSphere(Ray r, Vector3D spherePos,double rad){
	 * double a = Math.pow(r.t.dot(r.r.subtract(spherePos)),2) -
	 * Math.pow(r.t.length, 2) * (Math.pow(r.r.subtract(spherePos).length, 2) -
	 * rad * rad); System.out.println(a); return a > 0; }
	 */
	private static double dot(Vector3D v, Vector3D v2) {
		return v.dot(v2);
	}
}
