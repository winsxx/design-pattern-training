package creational;

public class BuilderPattern {

  public static class Person {
    private final String firstName; // required
    private final String lastName; // required
    private final String email; // optional

    private Person(Builder b) {
      this.firstName = b.firstName;
      this.lastName = b.lastName;
      this.email = b.email;
    }

    // getters...
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }

    // The Builder
    public static class Builder {
      // required
      private String firstName;
      private String lastName;
      // optional
      private String email;

      public Builder firstName(String firstName) {
        this.firstName = firstName;
        return this;
      }

      public Builder lastName(String lastName) {
        this.lastName = lastName;
        return this;
      }

      public Builder email(String email) {
        this.email = email;
        return this;
      }

      public Person build() {
        // validate required or invariant if needed
        if (firstName == null || lastName == null) {
          throw new IllegalStateException("name required");
        }
        return new Person(this);
      }
    }
  }

  public static void main(String[] args) {
    Person person1 = new Person.Builder()
        .firstName("Ada")
        .lastName("Lovelace")
        .build();
    System.out.println("Succesfully created person 1");

    Person person2 = new Person.Builder()
        .firstName("Ada")
        .email("ada@example.com")
        .build();
    System.out.println("Succesfully created person 2");
  }

}


