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
    private static final short FILE_NAME_MAX_LENGTH = 250;
    private static final byte FILE_EXTENSION_MAX_LENGTH = 50;

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

        boolean invalidFlag = true;

        while (invalidFlag) {
            //  Prompt User for File Name!
            fileName = obtainFileName();

            //  Validate the file name input by user!
            invalidFlag = validation(fileName, "File Name");
            if(invalidFlag)
            System.out.println("Please Try Again...");
        }

        invalidFlag = true;

        while (invalidFlag) {
            //  Prompt User for File Extension!
            fileExt = obtainFileExt();

            //  Validate the file name input by user!
            invalidFlag = validation(fileExt, "File Extension");
            if(invalidFlag)
                System.out.println("Please Try Again...");
        }

        //  Prompt User for the File Path!
        filePath = obtainFilePath();

        //  Check if file already exists & Prompt User for append or overwrite!
        appendFlag = wannaAppend();

        //  Prompt User for content to be written in file.
        fileData = obtainFileData();

        //  Validations required! (wanna Append)

    }

    private String obtainFileExt() {
        System.out.print("Enter File Extention (Only ALPHA-Numeric Charset. without any period) : ");
        return scanner.nextLine();
    }

    private String obtainFileName() {
        System.out.print("Enter File Name: ");
        return scanner.nextLine();
    }

    private boolean validation(String fPart, String printTag) {

        if( isItEmpty(fPart, printTag) )
            return true;    //  given fPart i.e. fileName or fileExtension is Empty!
        else if( isItBlank(fPart, printTag) )
            return true;    //  given fPart i.e. fileName or fileExtension is Blank!
        else if( doesMaxLimitExceed(fPart, printTag, FILE_NAME_MAX_LENGTH) )
            return true;
        else if( consistsSpecialCharacter(fPart, printTag) )
            return true;   //  given fPart consists Special Characters!
        else if( containsWhiteSpace(fPart, printTag) )
            return true;   //  given fPart contains white spaces!
        else
            return false;   //  given fPart i.e. fileName or fileExtension is valid!
    }

    private boolean containsWhiteSpace(String fPart, String printTag) {
        //  Check if there's any white space character in between of the file extension.

        boolean whiteSpaceFlag = false;

        char [] temp = fPart.trim().toCharArray();
        for (int i = 0; i < temp.length; i++) {
            char c = temp[i];
            if (Character.isWhitespace(c)) {
                System.out.println("Please do not enter any white space character in the file extension." +
                        "\n (Unicode: " + Character.codePointAt(temp, i) + " | Character: '" + temp[i] + "')");

                //  needs this method for the above statement!
                //  String appendZeroes(int codePoint, int numLength);
                whiteSpaceFlag = true;
            }
        }

        //  Raise Warning to user if there's any unnecessary white space characters at the
        //  beginning or at the end of the File Extension.
        if(fPart.charAt(0) == ' ' || fPart.charAt(fPart.length() -1) == ' '){
            System.out.print("Warning:\nAdditional Spaces found at: ");
        }
        if(fPart.charAt(0) == ' ') {
            System.out.print("Beginning, ");
        }
        if (fPart.charAt(fPart.length() -1) == ' ') {
            System.out.println("End, ");
        }

        return whiteSpaceFlag;
    }

    private boolean doesMaxLimitExceed(String fPart, String printTag, short maxLength) {

        if(fPart.length() > FILE_EXTENSION_MAX_LENGTH){
            System.out.println(printTag + " Length Limit Exceeded (" + maxLength +
                    " Characters)\nPlease try again...");
            return true;
        }
        return false;

    }

    private String obtainFileExtOld() {
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
            if ( isItEmpty(fExt, "File Extension") ) {
                invalidFileExtension = true;
                continue;
            }
            //  Check if fExt is only White Space.
            if ( isItEmpty(fExt, "File Extension") ){
                invalidFileExtension = true;
                continue;
            }

            if(fExt.length() > FILE_EXTENSION_MAX_LENGTH){
                System.out.println("File Extension Length Limit Exceeded (250 Characters)\nPlease try again...");
                invalidFileExtension = true;
                continue;
            }

            //  Validate for any special characters.
            if( consistsSpecialCharacter(fExt, "File Extension") ){
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

    private boolean isItEmpty(String fPart, String printTag) {

        //  fPart   => fileName     | fileExtension
        //  str => "File Name" or "File Extension"
        boolean temp = false;
        if(fPart.isEmpty()){
            System.out.println(printTag + " can't be EMPTY!\nPlease try again...");
            temp = true;
        }
        return temp;
    }

    private boolean isItBlank(String fPart, String printTag) {

        //  fPart   => fileName     | fileExtension
        //  printTag     => "File Name"  | "File Extension"
        boolean temp = false;
        if(fPart.isBlank()){
            System.out.println(printTag + " contains only White Spaces!\nPlease try again...");
            temp = true;
        }
        return temp;
    }


        private boolean consistsSpecialCharacter(String fPart, String printTag) {
            //  Time Stamp: 30th October 2K18, 11:04 AM!

            boolean temp = false;
            int countSpecialCharacters;
            countSpecialCharacters = 1;

            //  Hebrew Punctuation Paseq (U+05C0) Big Vertical Line used as separator between Unicode & Character!

            //  Required!
            //  Display in the following format, but remove this line if no special char. found!
            //  Sr. No.  |  Unicode  |  Character Symbol  |
            if (fPart.contains("/") || fPart.contains("\\") || fPart.contains(":") || fPart.contains("|") ||
                    fPart.contains("<") || fPart.contains(">") || fPart.contains("?") || fPart.contains("\"") ||
                    fPart.contains("*") || (!printTag.equals("File Name") && fPart.contains("."))) {

                System.out.println("Do not enter any of the following special characters: ");
                System.out.println("Sr. No.  |  Unicode  |  Symbol  |  Character  ");
//            System.out.println(String.format("%4d     |  %5s    |  %5s   |  %s", 1, "010A", "(\u002E)", "Period/Dot") );
            }

            if (fPart.contains("*")) {
//            System.out.println("Unicode: U+01C0 \u05C0 Character: Asterisk! (\u002A)");
                System.out.println(String.format("%4d     |  %5s    |  %5s   |  %s", countSpecialCharacters, "002A", "(\u002A)", "Asterisk!"));
                countSpecialCharacters++;
                temp = true;
            }

            if (fPart.contains(":")) {
                System.out.println(String.format("%4d     |  %5s    |  %5s   |  %s", countSpecialCharacters, "003A", "(\u003A)", "Colon!"));
                countSpecialCharacters++;
                temp = true;
            }

            if (fPart.contains(">")) {
//            System.out.println("Unicode: U+01C0 \u05C0 Character: Greater Than! (\u003E)");
                System.out.println(String.format("%4d     |  %5s    |  %5s   |  %s", countSpecialCharacters, "003E", "(\u003E)", "Greater Than!"));
                countSpecialCharacters++;
                temp = true;
            }
            if (fPart.contains("<")) {
//            System.out.println("Unicode: U+01C0 \u05C0 Character: Less Than! (\u003C)");
                System.out.println(String.format("%4d     |  %5s    |  %5s   |  %s", countSpecialCharacters, "003C", "(\u003C)", "Less Than!"));
                countSpecialCharacters++;
                temp = true;
            }
            if (fPart.contains("?")) {
                System.out.println(String.format("%4d     |  %5s    |  %5s   |  %s", countSpecialCharacters, "004F", "(\u003F)", "Question Mark!"));
                countSpecialCharacters++;
                temp = true;
            }

            if (fPart.contains("\"")) {
                System.out.println(String.format("%4d     |  %5s    |  %5s   |  %s", countSpecialCharacters, "0022", "(\")", "Quotation Mark!"));
                countSpecialCharacters++;
                temp = true;
            }
            if (fPart.contains("\\")) {
                System.out.println(String.format("%4d     |  %5s    |  %5s   |  %s", countSpecialCharacters, "005C", "(\\)", "Reverse Solidus!"));
                countSpecialCharacters++;
                temp = true;
            }
            if (fPart.contains("/")) {
                System.out.println(String.format("%4d     |  %5s    |  %5s   |  %s", countSpecialCharacters, "002F", "(\u002F)", "Solidus!"));
                countSpecialCharacters++;
                temp = true;
            }

            if (fPart.contains("|")) {
//            System.out.println("Unicode: U+01C0 \u05C0 Character: Vertical Line! (\u01C0)");
                System.out.println(String.format("%4d     |  %5s    |  %5s   |  %s", countSpecialCharacters, "01C0", "(\u01C0)", "Vertical Line!"));
                countSpecialCharacters++;
                temp = true;
            }
            if (fPart.contains(".")) {
                if (printTag.equals("File Name")) {
                    if (fPart.indexOf('.') == fPart.length() - 1) {
                        System.out.println("File Name can't end with a Period or Dot (.)");
                        System.out.println();
                        temp = true;
                    }
                } else {
                    System.out.println(String.format("%4d     |  %5s    |  %5s   |  %s", countSpecialCharacters, "002E", "(\u002E)", "Period / Dot!"));
                    countSpecialCharacters++;
                    temp = true;
                }
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
 * Date Modified: 30th October 2K18, 09:48 PM!
 *
 * Code Developed By,
 * K.O.H..!! ^__^
 */
























