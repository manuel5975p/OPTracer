package raytracing;

public class ObstacleAndRay {
	public final Ray ray;
	public final Obstacle obstacle;
	public ObstacleAndRay(Obstacle obs, Ray ray) {
		this.ray = ray;
		this.obstacle = obs;
	}

	public ObstacleAndRay(Ray ray, Obstacle obs) {
		this.ray = ray;
		this.obstacle = obs;
	}
}
