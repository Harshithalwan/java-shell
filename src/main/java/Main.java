import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

public class Main {

    private static final String PATH = "PATH";

    enum COMMANDS {
        EXIT("exit"),
        ECHO("echo"),
        TYPE("type");

        private final String value;
        COMMANDS(String s) {
            this.value = s;
        }
        public String getValue(){
            return value;
        }

        public static boolean containsValue(String name) {
            return Arrays.stream(values()).anyMatch(enumKey -> enumKey.getValue().equals(name));
        }
    }
    public static void main(String[] args) throws Exception {
        // TODO: Uncomment the code below to pass the first stage
         Scanner scanner = new Scanner(System.in);
         while(true){
             System.out.print("$ ");
             String input = scanner.nextLine();
             if(input.equals(COMMANDS.EXIT.getValue())){
                 break;
             }
             else if(input.startsWith(COMMANDS.ECHO.getValue() + " ")){
                 System.out.println(input.substring(5));
             }
             else if(input.startsWith(COMMANDS.TYPE.getValue() + " ")){
                 handleType(input);
             }
             else{
                 String[] inputParts = input.split(" ");
                 String command = inputParts[0];
                 Path executablePath =  findExecutable(command);
                 if(Objects.nonNull(executablePath)) {
                     // create a new process and run the file at executablePath with parameters in inputParts
                     Process process = Runtime.getRuntime().exec(input.split(" "));
                     process.getInputStream().transferTo(System.out);
                 }
                 else System.out.println(input + ": command not found");
             }
         }
    }

    private static void handleType(String input) {
        String trimmedInput = input.substring(5);
        if(COMMANDS.containsValue(trimmedInput)) {
            System.out.println(trimmedInput + " is a shell builtin");
        } else {
            Path path = findExecutable(trimmedInput);
            if(Objects.nonNull(path)) System.out.println(trimmedInput + " is " + path);
            else System.out.println(trimmedInput + ": not found");
        }
    }

    private static Path findExecutable(String fileName) {
        String dirList = System.getenv(PATH);

        for(String dir : dirList.split(File.pathSeparator)){
            Path path = Path.of(dir, fileName);
            if(Files.isExecutable(path)){
                return path.toAbsolutePath();
            }
        }
        return null;
    }
}
