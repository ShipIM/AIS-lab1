package org.example;

import org.jpl7.*;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class App {
    public static void main(String[] args) {
        var query = new Query("consult", new Term[]{new Atom("src/main/resources/satisfactory.pl")});
        query.hasSolution();

        Scanner scanner = new Scanner(System.in);

        System.out.println(process(scanner));
    }

    public static String process(Scanner scanner) {
        // Чтение потока ввода и инициализация регулярных выражений.
        var types = List.of("resource", "object", "consumable", "material");
        var userInput = scanner.nextLine().toLowerCase();

        StringBuilder outputString = new StringBuilder();

        Pattern entityPattern = Pattern.compile("i have (a|an) (.+)(?=,)"),
                targetPattern = Pattern.compile("what can i (.+)(?=\\?)");
        Matcher entityMatcher = entityPattern.matcher(userInput),
                targetMatcher = targetPattern.matcher(userInput);

        // Сообщаем о несоответствии формата входной строки, если с регулярными выражениями не было совпадений.
        if (!entityMatcher.find() || !targetMatcher.find()) {
            return "Incorrect sentence pattern!";
        }

        // Извлечение фактов из найденных фрагментов.
        String entity = entityMatcher.group().replace("i have ", "").replace("a ", "")
                .replace("an ", ""), target = targetMatcher.group()
                .replace("what can i ", "");

        // Поиск информации в базе знаний о полученной из потока ввода сущности.
        var entityResults = types.stream().map(type -> {
            var entityQuery = new Query(new Compound(type, new Term[]{new Atom(entity)}));

            return new Pair<>(type, entityQuery.hasSolution());
        }).collect(Collectors.toList());
        var entityResult = entityResults.stream().filter(Pair::getValue).findAny()
                .orElse(new Pair<>("unknown entity", false));

        outputString.append(entity).append(" ").append(entityResult);

        // Если информация о сущности не найдена, сообщаем пользователю.
        if (!entityResult.getValue()) {
            outputString.append(", you can ").append(target).append(" nothing with it.");

            return outputString.toString();
        }

        /*
         * Поиск информации в базе знаний о том, что можно получить после
         * выполнения извлечённой цели над сущностью.
         */
        try {
            var targetQuery = new Query(new Compound(target, new Term[]{new Atom(entity), new Variable("X")}));
            var targetResults = targetQuery.allSolutions();

            System.out.println(targetResults.length);
            for (var targets : targetResults) {
                System.out.println(targets);
            }

            if (targetResults.length == 0) {
                outputString.append(", you can ").append(target).append(" nothing with it.");

                return outputString.toString();
            }

            // Перечисление всех полученных результатов.
            StringBuilder builder = new StringBuilder(", you can ").append(target).append(": ");
            Arrays.stream(targetResults).forEach(result -> result.forEach((key, value) -> builder
                    .append(value.toString().replace("'", "")).append(", ")));

            outputString.append(builder).append("etc.");

            // Если указанная цель не содержится в базе знаний.
        } catch (PrologException e) {
            outputString.append(", you can ").append(target).append(" nothing with it.");

            return outputString.toString();
        }

        return outputString.toString();
    }
}
