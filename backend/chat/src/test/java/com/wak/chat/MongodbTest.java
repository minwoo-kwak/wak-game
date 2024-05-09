package com.wak.chat;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.context.ActiveProfiles;

import java.util.Objects;

import static org.springframework.data.mongodb.core.query.Criteria.*;

@SpringBootTest
@ActiveProfiles("test")
public class MongodbTest {

    @Autowired
    MongoOperations template;

    @Test
    void test() {
        Person person = new Person("kim");
        template.dropCollection(Person.class);
        template.insert(person);
        Person foundPerson = template.query(Person.class).matching(where("name").is("kim")).firstValue();
        System.out.println(foundPerson);
        Assertions.assertThat(person).isEqualTo(foundPerson);
    }


    public static class Person {
        String name;

        public Person(String name) {
            this.name = name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Person person = (Person) o;
            return Objects.equals(name, person.name);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(name);
        }
    }

}
