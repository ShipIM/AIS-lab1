package org.example;

import org.jpl7.Atom;
import org.jpl7.Query;
import org.jpl7.Term;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Scanner;

public class AppTest {

    @BeforeAll
    static void prepareProlog() {
        var query = new Query("consult", new Term[]{new Atom("src/test/resources/satisfactory.pl")});
        query.hasSolution();
    }

    @Test
    void incorrectSentence() {
        String input = "Incorrect sentence pattern!";
        Scanner scanner = new Scanner(input);

        Assertions.assertEquals(App.process(scanner), input);
    }

    @Test
    void incorrectFirstPart() {
        String input = "Incorrect sentence pattern, what can I construct?";
        Scanner scanner = new Scanner(input);

        Assertions.assertEquals(App.process(scanner), "Incorrect sentence pattern!");
    }

    @Test
    void incorrectSecondPart() {
        String input = "I have an iron ore, incorrect sentence pattern?";
        Scanner scanner = new Scanner(input);

        Assertions.assertEquals(App.process(scanner), "Incorrect sentence pattern!");
    }

    @Test
    void missingEntity() {
        String input = "I have an, incorrect sentence pattern?";
        Scanner scanner = new Scanner(input);

        Assertions.assertEquals(App.process(scanner), "Incorrect sentence pattern!");
    }

    @Test
    void missingTarget() {
        String input = "I have an iron ore, what can I?";
        Scanner scanner = new Scanner(input);

        Assertions.assertEquals(App.process(scanner), "Incorrect sentence pattern!");
    }

    @Test
    void correctSentence() {
        String input = "I have an iron ore, what can I melt?";
        Scanner scanner = new Scanner(input);

        Assertions.assertEquals(App.process(scanner), "iron ore is a resource, you can melt: iron ingot, etc.");
    }

    @Test
    void unknownEntity() {
        String input = "I have an unknown entity, what can I construct?";
        Scanner scanner = new Scanner(input);

        Assertions.assertEquals(App.process(scanner),
                "unknown entity is a unknown entity, you can construct nothing with it.");
    }

    @Test
    void unknownTarget() {
        String input = "I have an iron ore, what can I do?";
        Scanner scanner = new Scanner(input);

        Assertions.assertEquals(App.process(scanner),
                "iron ore is a resource, you can do nothing with it.");
    }

    @Test
    void unsupportedTarget() {
        String input = "I have an iron ore, what can I construct?";
        Scanner scanner = new Scanner(input);

        Assertions.assertEquals(App.process(scanner),
                "iron ore is a resource, you can construct nothing with it.");
    }

    @Test
    void unknownEntityAndUnknownTarget() {
        String input = "I have an unknown entity, what can I do?";
        Scanner scanner = new Scanner(input);

        Assertions.assertEquals(App.process(scanner),
                "unknown entity is a unknown entity, you can do nothing with it.");
    }
}
