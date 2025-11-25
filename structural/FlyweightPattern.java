import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class FlyweightPattern {

  // Flyweight (intrinsic, shared)
  public static final class ParticleType {
    final String name;
    final String texture;
    final double drag;

    ParticleType(String name, String texture, double drag) {
      this.name = name;
      this.texture = texture;
      this.drag = drag;
    }

    void render(double x, double y, double life) {
      System.out.printf("%s[%s] at (%.1f,%.1f) life=%.2f%n", 
          name, texture, x, y, life);
    }
  }

  // Factory that returns shared ParticleType instances
  public static class ParticleFactory {
    private final Map<String, ParticleType> types = new ConcurrentHashMap<>();

    ParticleType get(String name, String texture, double drag) {
      return types.computeIfAbsent(name, k -> new ParticleType(name, texture, drag));
    }
    
    int distinct() {
      return types.size();
    }
  }

  // Particle holds only extrinsic state
  public static class Particle {
    final ParticleType type;
    double x, y, vx, vy, life;

    Particle(ParticleType t, double x, double y, double vx, double vy, double life) {
      this.type = t;
      this.x = x;
      this.y = y;
      this.vx = vx;
      this.vy = vy;
      this.life = life;
    }

    void update(double dt) {
      x += vx * dt;
      y += vy * dt;
      vx *= (1 - type.drag * dt);
      vy *= (1 - type.drag * dt);
      life -= dt;
    }

    boolean alive() {
      return life > 0;
    }

    void render() {
      type.render(x, y, life);
    }
  }

  // Tiny demo
  public static void main(String[] args) {
    ParticleFactory f = new ParticleFactory();
    ParticleType spark = f.get("spark", "spark.png", 0.02);
    ParticleType smoke = f.get("smoke", "smoke.png", 0.05);

    List<Particle> list = new ArrayList<>();
    Random r = new Random(0);
    for (int i = 0; i < 6; i++) {
      list.add(new Particle(spark, 0, 0, 
        r.nextDouble() * 6 - 3, r.nextDouble() * 6 - 3, 1.0 + r.nextDouble()));
      list.add(new Particle(smoke, 0, 0, 
        r.nextDouble() * 2 - 1, r.nextDouble() * 2 - 1, 2.0 + r.nextDouble()));
    }

    for (int step = 0; step < 5; step++) {
      System.out.println("Step " + step);
      Iterator<Particle> it = list.iterator();
      while (it.hasNext()) {
        Particle p = it.next();
        p.update(0.2);
        if (!p.alive())
          it.remove();
        else
          p.render();
      }
      System.out.println("Alive: " + list.size() + "  |  Distinct types: " + f.distinct());
      System.out.println();
    }
  }

}

