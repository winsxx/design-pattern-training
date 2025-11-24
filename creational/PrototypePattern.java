package creational;

public class PrototypePattern {

  // Mutable sub-object that must be copied for a deep clone
  public static class Address implements Cloneable {
    private String city;

    public Address(String city) {
      this.city = city;
    }

    @Override
    protected Address clone() throws CloneNotSupportedException {
      return (Address) super.clone();
    }

    @Override
    public String toString() { return "Address{" + city + "}"; }
  }

  // Prototype class
  public static class Person implements Cloneable {
    private String name;
    private int age;
    private Address address; // mutable field — must be cloned for deep copy

    Person(String name, int age, Address address) {
      this.name = name;
      this.age = age;
      this.address = address;
    }

    // Deep clone
    @Override
    public Person clone() throws CloneNotSupportedException {
        Person p = (Person) super.clone();
        p.address = this.address.clone(); // deep copy mutable field
        return p;
    }

    @Override
    public String toString() { 
      return "Person{name=" + name + ", age=" + age + ", " + address + "}"; 
    }
  }

  // Demo
  public static void main(String[] args) throws CloneNotSupportedException {
    Person prototype = new Person(
      "Alice", 30, new Address("Jakarta"));

    // create new objects by cloning prototype and tweaking fields
    Person p1 = prototype.clone();
    p1.name = "Bob";
    p1.address.city = "Bandung"; // deep-copied address, prototype stays same

    Person p2 = prototype.clone();
    p2.name = "Cara";
    p2.age = 28;

    System.out.println("prototype: " + prototype);
    System.out.println("p1: " + p1);
    System.out.println("p2: " + p2);
  }

}

