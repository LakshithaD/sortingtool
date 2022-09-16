package sorting;

import java.io.*;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Main {

    static Scanner scanner;
    static PrintWriter writer;
    public static void main(final String[] args) throws IOException {
        try {
            Arguments arguments = buildArguments(args);
            if (!Objects.isNull(arguments.getInputFile())) {
                scanner = new Scanner(new File(arguments.getInputFile()));
            } else {
                scanner = new Scanner(System.in);
            }
            if (!Objects.isNull(arguments.getOutputFile())) {
                writer = new PrintWriter(new FileWriter(arguments.getOutputFile()), true);
            }
            if (arguments.getDataType().equalsIgnoreCase("long")) {
                sortNumbers(arguments.getSortingType());
            } else if (arguments.getDataType().equalsIgnoreCase("line")) {
                sortLines(arguments.getSortingType());
            } else if (arguments.getDataType().equalsIgnoreCase("word")) {
                sortWords(arguments.getSortingType());
            }
        } catch (RuntimeException ex) {
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (writer != null) {
            writer.close();
        }
    }

    private static void sortIntegers() throws IOException {
        Scanner scanner = new Scanner(System.in);
        List<Long> numbers = new ArrayList<>();
        while (scanner.hasNextLong()) {
            long number = scanner.nextLong();
            numbers.add(number);
        }
        printLine(String.format("Total numbers: %d.", numbers.size()));
        String str = numbers.stream().sorted().map(aLong -> aLong.toString()).collect(Collectors.joining(" "));
        printLine(String.format("Sorted data: %s", str));
    }

    private static void sortNumbers(String sort) throws IOException {

        List<Long> numbers = new ArrayList<>();
        Pattern pattern = Pattern.compile("-?\\d+");
        scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String number = scanner.next();
            if (pattern.matcher(number).matches()) {
                numbers.add(Long.parseLong(number));
            } else {
                printLine(String.format("%s is not a valid parameter. It will be skipped.", number));
            }
        }
        if (sort == null || "natural".equalsIgnoreCase(sort)) {
           printLine(String.format("Total numbers: %d.", numbers.size()));
            String sorted = numbers.stream().sorted().map(Objects::toString).collect(Collectors.joining(" "));
            printLine(String.format("Sorted data: %s", sorted));
        } else {
            System.out.println(String.format("Total numbers: %d", numbers.size()));
            Map<Long, Integer> map = numbers.stream()
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.summingInt(value -> 1)));
            map.entrySet()
                    .stream()
                    .sorted(Map.Entry.<Long, Integer>comparingByValue().thenComparing(Map.Entry.comparingByKey()))
                    .forEach(entry -> {
                        int percentage = entry.getValue() * 100 / numbers.size();
                        try {
                            printLine(String.format("%d: %d time(s), %d%%", entry.getKey(), entry.getValue(), percentage));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
        }
    }

    private static void sortWords(String sort) throws IOException {
        Scanner scanner = new Scanner(System.in);
        List<String> words = new ArrayList<>();
        while (scanner.hasNext()) {
            String word = scanner.next();
            words.add(word);
        }
        if ("natural".equalsIgnoreCase(sort)) {
            printLine(String.format("Total numbers: %d.", words.size()));
            String str = words.stream().map(Long::parseLong).sorted().map(aLong -> aLong.toString()).collect(Collectors.joining(" "));
            printLine(String.format("Sorted data: %s", str));
        } else {
            System.out.println(String.format("Total words: %d", words.size()));
            words.stream()
                    .collect(Collectors.groupingBy(Function.identity(), LinkedHashMap::new, Collectors.summingInt(value -> 1)))
                    .entrySet()
                    .stream().sorted(Map.Entry.<String, Integer>comparingByValue().thenComparing(Map.Entry::getKey))
                    .forEach(entry -> {
                        int percentage = entry.getValue() * 100 / words.size();
                        try {
                            printLine(String.format("%s: %d time(s), %d%%", entry.getKey(), entry.getValue(), percentage));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
        }
    }

    private static void sortLines(String sort) throws IOException {
        Scanner scanner = new Scanner(System.in);
        List<String> lines = new ArrayList<>();
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            lines.add(line);
        }
        if ("natural".equalsIgnoreCase(sort)) {
            printLine(String.format("Total lines: %d.", lines.size()));
            printLine(String.format("Sorted data:"));
            lines.stream().sorted().forEach(System.out::println);

        } else {
            System.out.println(String.format("Total lines: %d%%.", lines.size()));
            lines.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.summingInt(value -> 1)))
                    .entrySet()
                    .stream()
                    .sorted(Map.Entry.<String, Integer>comparingByValue().thenComparing(Map.Entry.comparingByKey()))
                    .forEach(entry -> {
                        int percentage = entry.getValue() * 100 / lines.size();
                        try {
                            printLine(String.format("%s: %d time(s), %d%%", entry.getKey(), entry.getValue(), percentage));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
        }
    }

    private static Arguments buildArguments(String[] args) {

        Arguments arguments = new Arguments();
        for (int i = 0; i < args.length;) {
            if (args[i].equalsIgnoreCase("-dataType")) {
                if (args.length > i + 1) {
                    arguments.setDataType(args[i + 1]);
                } else {
                    System.out.println("No data type defined!");
                    throw new RuntimeException();
                }
            } else if (args[i].equalsIgnoreCase("-sortingType")) {
                if (args.length > i + 1) {
                    arguments.setSortingType(args[i + 1]);
                } else {
                    System.out.println("No sorting type defined!");
                    throw new RuntimeException();
                }

            } else if (args[i].equalsIgnoreCase("-inputFile")) {
                if (args.length > i + 1) {
                    arguments.setInputFile(args[i + 1]);
                } else {
                    System.out.println("No sorting type defined!");
                    throw new RuntimeException();
                }

            } else if (args[i].equalsIgnoreCase("-outputFile")) {
                if (args.length > i + 1) {
                    arguments.setOutputFile(args[i + 1]);
                } else {
                    System.out.println("No sorting type defined!");
                    throw new RuntimeException();
                }

            }
            i = i + 2;
        }
        return arguments;
    }

    private static void printLine(String line) throws IOException {
        if (writer == null) {
            System.out.println(line);
        } else {
            writer.println(line);
        }
    }
}

class Arguments {
    private String sortingType;
    private String dataType;
    private String inputFile;
    private String outputFile;

    public String getSortingType() {
        return sortingType;
    }

    public void setSortingType(String sortingType) {
        this.sortingType = sortingType;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getInputFile() {
        return inputFile;
    }

    public void setInputFile(String inputFile) {
        this.inputFile = inputFile;
    }

    public String getOutputFile() {
        return outputFile;
    }

    public void setOutputFile(String outputFile) {
        this.outputFile = outputFile;
    }
}