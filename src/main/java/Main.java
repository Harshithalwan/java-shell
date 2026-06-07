import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
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
                 String trimmedInput = input.substring(5);
                 if(COMMANDS.containsValue(trimmedInput)) {
                     System.out.println(trimmedInput + " is a shell builtin");
                 } else {
                     String dirList = System.getenv(PATH);
                     boolean pathFound = false;
                     Path path = null;
                     for(String dir : dirList.split(File.pathSeparator)){
                         path = Path.of(dir, trimmedInput);
                         if(Files.isExecutable(path)){
                             pathFound = true;
                             break;
                         }
                     }
                     if(pathFound) System.out.println(trimmedInput + " is " + path);
                     else System.out.println(trimmedInput + ": not found");
                 }
             }
             else{
                 System.out.println(input + ": command not found");
             }
         }
    }
}
