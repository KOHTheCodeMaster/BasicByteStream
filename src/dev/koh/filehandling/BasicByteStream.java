package dev.koh.filehandling;

import java.io.*;
import java.util.Scanner;

public class BasicByteStream {

    Scanner scanner = new Scanner(System.in);
    private static final String ABSOLUTE_FILE_PATH = "E:\\HEADQUARTERS..!!\\CodeBase\\Java\\CoreJava\\" +
            "FileHandling\\src\\dev\\koh\\filehandling\\res\\txtfiles\\";

    private String filePath = null;
    private String fileName = null;
    private String fileExt = null;
    private String fileData = null;
    private boolean appendFlag;


    public static void main(String[] args) throws FileNotFoundException {

//        Time Stamp: 22nd October 2K18, 3:52 PM!
        System.out.println("Begin.");

        BasicByteStream obj = new BasicByteStream();
        obj.menu();

        System.out.println("End.");
//  Time Stamp: 30th October 2K18, 01:53 PM!

    }

    private void menu() throws FileNotFoundException {
//        Time Stamp: 28th October 2K18, 04:37 PM!
        int ch;
        do {
            System.out.println("1. Create New File.");
//            System.out.println("2. Read Existing File.");
            System.out.println("0. Exit!");
            System.out.print("Enter Choice: ?: ");

            ch = scanner.nextInt();
            scanner.nextLine(); //  Empty Buffer.
            switch (ch) {

                //  Exit the Program!
                case 0:
                    System.out.println("Shutting Down the program.");
                    if(scanner != null){
                        scanner.close();
                    }
                    break;

                //  Create New File!
                case 1:
                    /*
                     * Obtain File Details from the User including: New File Name, Extension, Path,
                     * if file already Exists then Prompt User for
                     * Write Mode i.e. Append, Overwrite, Keep Both Files by renaming the new file accordingly.
                     */
                    obtainFileDetails();

                    //  Using the file details obtained, create new file accordingly.
                    createNewFile();
                    break;

                default:
                    System.out.println("Please Enter Valid Choice!");
            }
        } while (ch != 0);
//        Time Stamp: 28th October 2K18, 05:01 PM!

    }

    private void obtainFileDetails() {

        fileName = obtainFileName();
        fileExt = obtainFileExt();
        filePath = obtainFilePath();

        //  Check if file already exists & Prompt User for append or overwrite!
        appendFlag = wannaAppend();

        fileData = obtainFileData();

        //  Validations required!

    }

    private String obtainFileName() {
        System.out.print("Enter File Name: ");
        return scanner.nextLine();
    }

    private String obtainFileExt() {
        //  Time Stamp: 30th October 2K18, 11:04 AM!
        String fExt = "";
        char[] temp = null;
        boolean invalidFileExtension = false;
        boolean unnecessaryWhiteSpace = false;

        outerLoop: //  label : "do while" loop!
        do {
            invalidFileExtension = false;
            System.out.println("---------------------");
            System.out.print("Enter File Extention (Only ALPHA-Numeric Charset. without any period) : ");
            fExt = scanner.nextLine();

            //  Check if fExt is Empty.
            if(fExt.isEmpty()){
                System.out.println("File Extension can't be EMPTY!\nPlease try again...");
                invalidFileExtension = true;
                continue;
            }

            //  Check if fExt is only White Space.
            if(fExt.isBlank()){
                System.out.println("File Extension Contains only White Spaces!\nPlease try again...");
                invalidFileExtension = true;
                continue;
            }

            if(fExt.length() > 250){
                System.out.println("File Extension Length Limit Exceeded (250 Characters)\nPlease try again...");
                invalidFileExtension = true;
                continue;
            }

            //  Validate for any special characters.
            if( consistsSpecialCharacter(fExt) ){
                invalidFileExtension = true;
                System.out.println("Please Try Again...");
                continue;
            }

            //  Check if there's any white space character in between of the file extension.
            temp = fExt.trim().toCharArray();
            for (int i = 0; i < temp.length; i++) {
                char c = temp[i];
                if (Character.isWhitespace(c)) {
                    System.out.println("Please do not enter any white space character in the file extension." +
                            " (Unicode: " + Character.codePointAt(temp, i) + " | Character: '" + temp[i] + "')");

                    //  needs this method for the above statement!
                    //  String appendZeroes(int codePoint, int numLength);

                    invalidFileExtension = true;
                    continue outerLoop;
                }
            }

            //  Raise Warning to user if there's any unnecessary white space characters at the
            //  beginning or at the end of the File Extension.
            if(fExt.charAt(0) == ' ' || fExt.charAt(fExt.length() -1) == ' '){
                System.out.println("Warning:\nAdditional Spaces found at: ");
                unnecessaryWhiteSpace = true;
                invalidFileExtension = false;
            }
            if(fExt.charAt(0) == ' ') {
                System.out.print("Beginning, ");
            }
            if (fExt.charAt(fExt.length() -1) == ' ') {
                System.out.println("End, ");
            }

            //  Trim any unnecessary white space characters from the beginning and the end of the File Extension.
            if(unnecessaryWhiteSpace){
                System.out.println("Removing Unnecessary White Spaces from the File Extension: " +
                        "\"" + fExt + "\"" );
                System.out.println( "File Extension Trimmed to: \"" + fExt.trim() + "\"" );
            }

        } while (invalidFileExtension);

        return fExt.trim(); //  return the valid file extension.
        //  Time Stamp: 30th October 2K18, 01:53 PM!
    }

    private boolean consistsSpecialCharacter(String fExt) {
        //  Time Stamp: 30th October 2K18, 11:04 AM!

        boolean temp = false;
        int countSpecialCharacters;
        countSpecialCharacters = 1;

        System.out.println("Do not enter any of the following special characters: ");
        //  Hebrew Punctuation Paseq (U+05C0) Big Vertical Line used as separator between Unicode & Character!

        //  Required!
        //  Display in the following format, but remove this line if no special char. found!
        //  Sr. No.  |  Unicode  |  Character Symbol  |

        if(fExt.contains(".")){
            System.out.print("[" + countSpecialCharacters + "] " );
            System.out.println("Periods or Dots! (.)");
            countSpecialCharacters++;
            temp = true;
        }
        if(fExt.contains("/")){
            System.out.print("[" + countSpecialCharacters + "] ");
            System.out.println(String.format("%-20s" + "%-4s", "Front Slash! ", "(/)") );
            countSpecialCharacters++;
            temp = true;
        }
        if(fExt.contains("\\")){
            System.out.println("[" + countSpecialCharacters + "] " );
            System.out.println("Back Slash! (\\)");
            temp = true;
        }
        if(fExt.contains("?")){
            System.out.println("[" + countSpecialCharacters + "] " );
            System.out.println("Question Mark! (?)");
            temp = true;
        }
        if(fExt.contains("\"")){
            System.out.println("[" + countSpecialCharacters + "] " );
            System.out.println("Double Quotes! (\")");
            temp = true;
        }
        if(fExt.contains(":")){
            System.out.println("Colon (:)");
            temp = true;
        }
        if(fExt.contains("|")){
            System.out.println("Unicode: U+01C0 \u05C0 Character: Vertical Line! (\u01C0)");
            temp = true;
        }
        if(fExt.contains("<")){
            System.out.println("Unicode: U+01C0 \u05C0 Character: Less Than! (\u003C)");
            temp = true;
        }
        if(fExt.contains(">")){
            System.out.println("Unicode: U+01C0 \u05C0 Character: Greater Than! (\u003E)");
            temp = true;
        }
        if(fExt.contains("*")){
            System.out.println("Unicode: U+01C0 \u05C0 Character: Asterisk! (\u002A)");
            temp = true;
        }

        return temp;
        //  Time Stamp: 30th October 2K18, 01:53 PM!
    }

    private String obtainFilePath() {

        String fPath = null;
        int ch = 0;
        do {
            System.out.println("Choose File Path:\n1. Default: " + ABSOLUTE_FILE_PATH);
            System.out.println("2. Enter Path Manually. ");

            //  ch -> user's input.
            ch = scanner.nextInt();

            //  clear the return or space character from the buffer.
            scanner.nextLine();

            switch (ch){

                //  Default Path.
                case 1:
                    fPath = ABSOLUTE_FILE_PATH;
                    break;

                //  Manually Input Complete Path.
                case 2:
                    System.out.println("Enter New File Path: ");
                    fPath = scanner.nextLine(); //  Accept entire path for the new file.
                    //  Validation required.
                    break;

                default:
                    System.out.println("Invalid Choice! Please Try Again...");
            }

        } while (ch != 1 && ch != 2);   //  Prompt user for input until user opts for a valid choice i.e. 1 or 2.
        return fPath;   //  return the selected new file path.
    }

    private boolean wannaAppend() {
        boolean temp = true;
        int rnCount;
        int ch = 0;
        //  Check if the file already exists at the path specified.
        boolean fExists = (new File(filePath + fileName + fileExt).exists());
        if (fExists)
            System.out.println("There is already a file with the same name in this location!");

        // increment the counter of new file name according to the last version of the existing file name.
        //  Example: a.txt | a (2).txt | a (3).txt |
        //  there exists these 3 files already, if one decides to keep both the files i.e. new
        //  and the old then findout how many files with the count exists & store the value of the
        //  next consecutive value for the new file name in rnCount.
        rnCount = gatherRenameCounter();

        while ( fExists && (ch < 1 || ch > 3) ) {
            //  Prompt User with choice to Append the file contents to the existing file.
            //  y => appendFlag = true | n =< overwrite i.e. appendFlag = false.
            System.out.println("1. Append the content in the file with an additional new line.");
            System.out.println("2. Overwrite the file with the new content.");
            System.out.println("3. Keep Both Files, Renaming the new file as " + fileName + " (" +
                    rnCount + ")" + fileExt);
//            System.out.println("4. Go Back!");
            ch = scanner.nextInt();
            scanner.nextLine();

            switch (ch) {
                case 1:
                    //  For Appending, set temp = true.
                    temp = true;
                    break;
                case 2:
                    //  For Overwriting, set temp = false.
                    temp = false;
                    break;
                case 3:
                    //  To Keep both the files, rename the new file with count value of 1 more than
                    //  the last version of the existing file.
                    fileName += " (" + rnCount + ")";
                    temp = false;
                    break;
                default:
                    System.out.println("Invalid Choice!\nPlease Try Again...\n");
            }
        }
        return temp;
    }

    private int gatherRenameCounter() {
        //  Initially, the file already exists, so the rename count has to start with 1.
        int tempCount = 1;

        // Requirement:
        // check if file name already consists the (1), then increment the count within the name itself.

        for(int i = 1; i < 10; i++){

            //  check if the file with next count (within the parenthesis) also already exists.
            if( new File(filePath + fileName + " (" + i + ")" + fileExt).exists() ) {

                //  increase the counter if the file with next count value already exists.
                tempCount++;

                //  if the value of i goes above 8, either there are too many copies of the file,
                //  or there is a bug in code. Hence, requires manual check for confirmation!
                if (i > 8) {
                    System.out.println("Too Many Duplicate Files!!!");
                    System.out.println("Check it quick!");
                }
            }
        }
        return tempCount;
    }

    private String obtainFileData() {
        //  local variable for storing the file content i.e. String.
        String fData = "";

        //  temporary variable for storing user's choice for entering more data i.e.
        //  either 'y' or 'n'.
        int ch = 0;
        do{
            System.out.println("Enter File Content:");

            //  Append the entire Line of content entered by user to fData.
            fData += scanner.nextLine();

            //  Prompt user for choice of entering more content in form of: (y/n)
            System.out.print("-----------------\nWant to Enter more content? (y/n) : ");

            //  Consider only the first character of the user's input word for the choice.
            ch = scanner.next().charAt(0);
            scanner.nextLine();

            switch (ch){
                case 'y':
                case 'Y':
                    //  Continue the do while loop & Prompt the user for entering another line content.
                    continue;
                case 'n':
                case 'N':
                    System.out.println("----------------------");
                    System.out.println("Content to be written in File:");
                    System.out.println(fData);
                    break;
                default:
                    System.out.println("Invalid Choice!\nPlease Try Again...");
            }

        } while (ch != 'n');    //  exit the loop only when user opts to stop entering the content for file.
        return fData;   //  return the local variable which contains the entire content of the file.
    }

    private void createNewFile() throws FileNotFoundException {
//        Time Stamp: 22nd October 2K18, 4:17 PM!
//        Time Stamp: 28th October 2K18, 08:16 PM!

        //  fPath must always consist of '/' directory delimiter
        //  & character at its last index must be '/'.
        OutputStream fos = new FileOutputStream(filePath + fileName + "." + fileExt, appendFlag);

        try {

            fos.write(fileData.getBytes());

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
                try {
                    if(fos != null) {
                        fos.close();
                        displaySuccessMsg();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }

//        Time Stamp: 22nd October 2K18, 4:53 PM!
    }

    private void displaySuccessMsg() {
        System.out.println("File |" + fileName + "." + fileExt + "| successfully created in the following directory,");
        System.out.println(filePath);
    }

    private String replaceBackSlash(String tempFilePath){

        if(tempFilePath.charAt(tempFilePath.length()-1) == '/');
        else if(tempFilePath.charAt(tempFilePath.length()-1) == '\\'){

        }

        else
            tempFilePath += "/";

        String updatedPath = "";

        for(int i = 0; i < tempFilePath.length(); i++){
            if(tempFilePath.charAt(i) == '\\')
                updatedPath += "/";
            else
                updatedPath += tempFilePath.substring(i, i+1);
        }
        return updatedPath;
    }

}

/*
 * Date Created: 22nd October 2K18, 03:52 PM!
 * Date Modified: 30th October 2K18, 01:53 PM!
 *
 * Code Developed By,
 * K.O.H..!! ^__^
 */
























