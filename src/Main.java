import java.io.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
       System.out.println("What do you want to do?");
       System.out.println("1. Encrypt");
       System.out.println("2. Decrypt");
       System.out.println("3. Frequency Cryptanalysis");

       int num = -1;
       Scanner scanner = new Scanner(System.in);
       while (true) {
           System.out.println("Enter a number");
           String input = scanner.nextLine();

           try {
               num = Integer.parseInt(input);
           } catch (NumberFormatException e) {
               System.out.println("You should enter an integer");
               continue;
           }
           if (num < 1 || num > 3) {
               System.out.println("You should enter a number from 1 to 3");
               continue;
           }
           break;
       }

       VizhenerTable vizhenerTable = new VizhenerTable();
       FrequencyCryptanalysis fc = new FrequencyCryptanalysis();
       File inputFile = new File("input.txt");
       File outputFile = new File("output.txt");
       String finalStr = "";

        try {
            Scanner fileScanner = new Scanner(inputFile);
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile, false));
            String str = fileScanner.nextLine();
            if (num == 1) {
                String codeWord = fileScanner.nextLine();
                System.out.println("\nString to encrypt:\n" + str);
                System.out.println("Key Word: " + codeWord);
                System.out.println("Encrypted string:");
                finalStr = vizhenerTable.encryptString(str, codeWord);
                System.out.println(finalStr);
                writer.write(finalStr);


            } else if (num == 2) {
                String codeWord = fileScanner.nextLine();
                System.out.println("\nString to decrypt:\n" + str);
                System.out.println("Key Word: " + codeWord);
                System.out.println("Decrypted string:");
                finalStr = vizhenerTable.decryptString(str, codeWord);
                System.out.println(finalStr);
                writer.write(finalStr);

            } else if (num == 3){
                System.out.println("\nString for Frequency Cryptanalysis:\n" + str);
                int keyLength = fileScanner.nextInt();
                System.out.println("Estimated keyword length = " + keyLength);
                fc.decrypt(str, keyLength);
            }

            writer.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }



    }
}