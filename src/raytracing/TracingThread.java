package raytracing;

import java.awt.Color;
import java.util.ArrayList;
import static raytracing.SceneDisplay.*;
public class TracingThread extends Thread {
	int height;
	//private int end;
	int width;
	int start;
	private int i = 0;
	Color[][] pixels;
	ArrayList<Obstacle> sceneObjects;
	public TracingThread(int width,int height,int start,ArrayList<Obstacle> scene){
		this.height = height;
		this.start = start;
		//this.end = end;
		this.width = width;
		sceneObjects = scene;
	}
	public double progress(){
		return (double)i / width; 
	}
	public void run(){
		pixels = new Color[width / 4][height];
		for (i = start; i < width; i += 4) {
			kl: for (int ih = 0; ih < height; ih++) {
				// Ray r = new Ray(cameraPos, new Vector3D(-0.5d + (double) i /
				// 500, -0.5d + (double) ih / 500, 0).subtract(cameraPos));
				/*
				 * if(hitsSphere(r, sfir.center, 0.5)){ r =
				 * sfir.reflectedRay(r); } if(hitsSphere(r, new Vector3D(1, 0,
				 * 11), 0.2)){ pixels[i][ih] = new Color(255, 255, 255); } else{
				 * pixels[i][ih] = new Color(0,0,0); }
				 */
				ColorMask pixelMask = new ColorMask(0,0,0);
				double red = 0, g = 0, b = 0;
				os: for (int p = 0; p < SceneDisplay.count; p++) {//
					Ray r = new Ray(SceneDisplay.cameraPos, new Vector3D(-0.5d + (double) i / width, -0.5d + (double) ih / height, 0).subtract(cameraPos));
					boolean lsFound = false;
					in: for (int f = 0; f < 5; f++) {// Bounces
						ObstacleAndRay oar = r.reflectFromScene(sceneObjects);
						if (oar == null) {
							if (f == 0) {
								pixelMask = SceneDisplay.ambient.mult(count);
								break os;
							}
							r.colorRay(ambient);
							// pixels[i][ih] = new Color(0,0,0);
							break in;
						}
						else {
							// System.out.println(oar.ray);
							r = oar.ray.clone();
							if (oar.obstacle instanceof LightSource) {
								// System.out.println(p);
								pixelMask.bangAdd(r.getColor()/*.mult(((LightSource) oar.obstacle).color)*/);
								/*red += ((LightSource) oar.obstacle).color.getRed() * r.color.r;
								g += ((LightSource) oar.obstacle).color.getGreen() * r.color.g;
								b += ((LightSource) oar.obstacle).color.getBlue() * r.color.b;*/
								// pixels[i][ih] = ((LightSource)
								// oar.obstacle).color;
								lsFound = true;
								break in;
							}
						}
					}
					pixelMask.bangAdd(r.getColor());
					if (!lsFound && false) {
						Obstacle a = r.nextIntersection(sceneObjects);
						if (a != null) {
							if (a instanceof LightSource) {
								pixelMask.bangAdd(r.getColor().mult(((LightSource) a).color));
								/*red += ((LightSource) a).color.getRed() * r.color.r;
								g += ((LightSource) a).color.getGreen() * r.color.g;
								b += ((LightSource) a).color.getBlue() * r.color.b;*/
							}
							else {

							}
						}
						else {

						}
					}
				}
				pixelMask.bangDivide(count);
				// if(ih < 300 && red != 255){
				// System.out.println(red);
				// }
				pixels[(i - start) / 4][ih] = pixelMask.mult(lightbright).asColor();
				//pixels[i][ih] = new Color(Math.min((int) (red * bright), 255), Math.min((int) (g * bright), 255), Math.min((int) (b * bright), 255));
			}
		}
	}
}
