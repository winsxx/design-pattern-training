public class FacadePattern {
  // Subsystem classes (complex)
  static class Amplifier {
    void on() { System.out.println("Amp on"); }
    void setVolume(int v) { System.out.println("Amp volume " + v); }
    void off() { System.out.println("Amp off"); }
  }

  static class DvdPlayer {
    void on() { System.out.println("DVD on"); }
    void play(String movie) { System.out.println("DVD playing: " + movie); }
    void stop() { System.out.println("DVD stopped"); }
    void off() { System.out.println("DVD off"); }
  }

  static class Projector {
    void on() { System.out.println("Projector on"); }
    void wideScreenMode() { System.out.println("Projector in widescreen"); }
    void off() { System.out.println("Projector off"); }
  }

  // Facade that simplifies the subsystem
  static class HomeTheaterFacade {
    private final Amplifier amp;
    private final DvdPlayer dvd;
    private final Projector projector;

    public HomeTheaterFacade(Amplifier amp, DvdPlayer dvd, Projector projector) {
      this.amp = amp;
      this.dvd = dvd;
      this.projector = projector;
    }

    public void watchMovie(String movie) {
      System.out.println("Get ready to watch a movie...");
      projector.on();
      projector.wideScreenMode();
      amp.on();
      amp.setVolume(5);
      dvd.on();
      dvd.play(movie);
    }

    public void endMovie() {
      System.out.println("Shutting movie theater down...");
      dvd.stop();
      dvd.off();
      amp.off();
      projector.off();
    }
  }

  public static void main(String[] args) {
    Amplifier amp = new Amplifier();
    DvdPlayer dvd = new DvdPlayer();
    Projector projector = new Projector();

    HomeTheaterFacade theater = new HomeTheaterFacade(amp, dvd, projector);
    theater.watchMovie("Inception");
    System.out.println("--- later ---");
    theater.endMovie();
  }
    
}
