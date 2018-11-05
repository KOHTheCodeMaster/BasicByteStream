package dev.koh.filehandling;

import java.io.*;
import java.util.Scanner;

public class BasicByteStream {

    private Scanner scanner = new Scanner(System.in);
    private static final String ABSOLUTE_FILE_PATH = "E:\\HEADQUARTERS..!!\\CodeBase\\Java\\CoreJava\\" +
            "FileHandling\\src\\dev\\koh\\filehandling\\res\\txtfiles\\";
    private static final String DEFAULT_DIRECTORY_FILE_PATH = ".\\";

    private String filePath = null;
    private String fileName = null;
    private String fileExt = null;
    private String fileData = null;

    private boolean possiblyDuplicate = false;      //  when fileName itself contains "(count)" at the end.
    private String localFileName = null;    //  Temporary file name required for possibly duplicate case.

    private boolean appendFlag;     //  It Affects Write Mode | appendFlag: true -> Append, false -> Overwrite!
    private boolean warningRaised = false;          //  For displaying "Warning:" msg. only once.
    private static final int FILE_NAME_MAX_LENGTH = 250;    //  Maximum File Name Limit.
    private static final int FILE_EXTENSION_MAX_LENGTH = 50;    //  Maximum File Extension Limit.
    private boolean errorOccured = false;

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
        int choice;
        do {
            System.out.println("1. Create New File.");
//            System.out.println("2. Read Existing File.");
            System.out.println("0. Exit.");
            System.out.print("Enter Choice: ?: ");

            //  Prompt User for input (input: string, parse it in: int).
            choice = userInputInt();   //  take input from user as String but then parse it in int.

            //  Validate the user input.
            switch (choice) {

                //  In case of user input: "0", |   Exit the Program!
                case 0:
                    System.out.println("Shutting Down the program.");
                    if (scanner != null) {
                        scanner.close();
                    }
                    break;

                //  In case of user input: "0", |   Create New File!
                case 1:
                    /*
                     *  Obtain File Details from the User including: New File Name, Extension, Path,
                     *  if file already exists then Prompt the User for Write Mode i.e.
                     *  Append, Overwrite, Keep Both Files by renaming the new file accordingly.
                     */
                    obtainFileDetails();

                    //  Using the file details obtained, create new file accordingly.
                    createNewFile();
                    break;

                /*
                 *  Time Stamp: 5th November 2K18, 12:06 PM..!!
                 *  In case of User Input other than the desired ones i.e. [0, 1]
                 *  for suitable errors, if else statements are used inside the default case so
                 *  that, first of all, the error msg. could be printed for every invalid choice,
                 *  then listing the particular reason stating what's wrong in user input.
                 */
                default:
                    displayError();

                    if (choice == -2)
                        System.out.println("Choice can't be Empty!");
                    else if (choice == -3)
                        System.out.println("Choice can't be Blank!");
                    else if (choice == -4)
                        System.out.println("Choice must be a Single Number!");
                    else if (choice == -5)
                        System.out.println("Choice must be a Single Numeric Digit only!");

                    //  In case of user input is logically wrong but single digit character. (" 7 ")
                    //  handle it by resetting the choice value to null character i.e. -1.
                    else {
                        System.out.println("Select the Choice between: 0 and 1 only!");
                        choice = -1;
                    }
                System.out.println("Please enter valid choice!\n");
            }
            //  Keep prompting user for input until & unless its either
            //  0 for exit (get out of while loop) or 1 for proceeding further into program.
        } while (choice != 0);
//        Time Stamp: 28th October 2K18, 05:01 PM!

    }

    /*
     *  Time Stamp: 5th November 2K18, 12:18 PM..!!
     *  Prompt User for Input, consider it as String, but then parse it into a single digit int.
     *  Return suitable int values for various error msgs. i.e.
     *  Return Value    :       Error
     *  ---------------------------------
     *      -2          :       Empty
     *      -3          :       Blank
     *      -4          :   Multiple Char
     *      -5          :     Other Char
     *      0-9         :       Valid
     */
    private int userInputInt() {
        //  Time Stamp: 4th November 2K18, 10:22 PM..!!

        //  temporary variable for storing user's choice as String then parsing it
        //  to single digit & return it back as int.
        String str = scanner.nextLine();

        //  In case of Empty User Input, return -2.
        if ( str.isEmpty() )
            return -2;
        //  In case of User Input containing only white space characters, return -3.
        if ( str.isBlank() )
            return -3;

        //  Trim the User Input off of its unnecessary white space characters from the beginning & end.
        str = str.trim();

        //  In case of multiple characters input by user, return -4.
        if(str.length() > 1) {
            return -4;
        }

        //  if the user input is a single digit excluding white spaces,
        //  return that digit character parsed as int.
        if ( hasDigit(str) )
            return Integer.parseInt(str.charAt(0) + "");
        //  For rest of the characters, prompt user again for the correct input
        //  by returning -5.
        else
            return -5;
    }

    /*
     *  Time Stamp: 5th November 2K18, 12:22 PM..!!
     *  Prompt User for Input, consider it as String, but then return by parsing it into a single alphabet character.
     *  Return suitable char values for various error msgs. i.e.
     *  Return Value    :       Error
     *  ---------------------------------
     *      '2'         :       Empty
     *      '3'         :       Blank
     *      '4'         :   Multiple Char
     *      '5'         :     Other Char
     *     'a-zA-z'     :       Valid
     */
    private char userInputChar() {
        //  Time Stamp: 4th November 2K18, 10:22 PM..!!

        //  temporary variable for storing user's choice as String then return it back as char.
        String str = scanner.nextLine();

        //  In case of Empty User Input, return '2'.
        if ( str.isEmpty() )
            return '2';

        //  In case of User Input containing only white space characters, return '3'.
        if ( str.isBlank() )
            return '3';

        //  Trim the User Input off of its unnecessary white space characters from the beginning & end.
        str = str.trim();

        //  In case of multiple characters input by user, return '4'.
        if ( str.length() > 1 )
            return '4';

        //  if user input is a single alphabet (lower/upper) character excluding white spaces, then return it as a char.
        if ( hasLowerCase(str) || hasUpperCase(str) )
            return str.charAt(0);

        //  For rest of the characters, prompt user again for the correct input
        //  by returning '5'.
        else
            return '5';
    }

    private String userInputString() {
        //  Time Stamp: 5th November 2K18, 01:48 PM..!!

        //  temporary variable for storing user's choice as String then return it back as char.
        String str = scanner.nextLine();

        //  In case of Empty User Input, return '2'.
        if ( str.isEmpty() )
            return "2";

        //  In case of User Input containing only white space characters, return '3'.
        if ( str.isBlank() )
            return "3";

        //  Trim the User Input off of its unnecessary white space characters from the beginning & end.
        str = str.trim();

        //  In case of multiple characters input by user, return '4'.
        if ( str.length() > 1 )
            return "4";

        //  if user input is a single alphabet (lower/upper) character excluding white spaces, then return it as a char.
        if ( hasLowerCase(str) || hasUpperCase(str) )
            return str.charAt(0) + "";

            //  For rest of the characters, prompt user again for the correct input
            //  by returning '5'.
        else
            return "5";
    }


    /*
     *  Time Stamp: 5th November 2K18, 12:24 PM..!!
     *  Prompt User for Input for each line new line to be written into the file.
     *  Return suitable char values for various error msgs. i.e.
     *  Return Value    :       Error
     *  ---------------------------------
     *      '2'         :       Empty
     *      '3'         :       Blank
     *      '4'         :   Multiple Char
     *      '5'         :     Other Char
     *     'a-zA-z'     :       Valid
     */
    private void obtainFileDetails() {
        //  Time Stamp: 1st November 2K18, 06:10 PM..!!

        //  invalidFlag ->  true: invalid   |   false:  valid
        //  Initialize invalidFlag with true for the sake of 1st iteration of while loop.
        //  (do while loop could also be used as an alternative.)
        boolean invalidFlag = true;

        //  Prompt User to input: file name.
        while (invalidFlag) {

            //  Reset Warning & Error Flags to false.
            resetFlags();

            //  Prompt User for File Name!
            fileName = obtainFileName();

            //  Validate the file name input by user!
            invalidFlag = validation(fileName, "File Name");
            if (invalidFlag)
                System.out.println("Please Try Again...\n");
        }

        //  Reset invalidFlag to true for next user input for file extension.
        invalidFlag = true;

        //  Prompt User to input: file extension.
        while (invalidFlag) {

            //  Reset Warning & Error Flags to false.
            resetFlags();

            //  Prompt User for File Extension!
            fileExt = obtainFileExt();

            //  Validate the file extension input by user!
            invalidFlag = validation(fileExt, "File Extension");

            //  In case of fileExt is valid, check for upper case characters in fileExt.
            //  display Warning Message stating the fileExt contains upper case character.
            //  & update fileExt to lower case characters.
            converToLowerCaseChar(invalidFlag);

            if (invalidFlag)
                System.out.println("Please Try Again...\n");
        }

        //  Prompt User for the File Path!
        filePath = obtainFilePath();
//        filePath = DEFAULT_DIRECTORY_FILE_PATH;
        //  Requirement: (Pending)
        //  Validate filePath!

        //  Check if file already exists & Prompt User for append or overwrite!
        appendFlag = wannaAppend();

        //  Prompt User for content to be written in file.
        fileData = obtainFileData();

        //  Validations required! (wanna Append)

    }

    private void converToLowerCaseChar(boolean invalidFlag) {
        //  Time Stamp: 4th November 2K18, 10:22 PM..!!
        if(!invalidFlag)
            if( hasUpperCase(fileExt) ) {
                //  Display Warning Message.
                displayWarning();
                System.out.println("Upper Case Characters found in file extension!");
                System.out.println("Converting 'em all to lower case!\n");

                //  Update fileExt to all lower case characters.
                fileExt = fileExt.toLowerCase();
            }
    }

    //  Reset Warning & Error Flags to false.
    private void resetFlags() {
        //  Time Stamp: 4th November 2K18, 10:22 PM..!!
        setWarningRaised(false);
        setErrorOccured(false);
    }

    private boolean hasUpperCase(String str) {
        //  Time Stamp: 1st November 2K18, 06:10 PM..!!

        //  return true if any character of str is Upper Case.
        for(char c : str.toCharArray()){
            if(c >= 'A' && c <= 'Z')
                return true;
        }
        return false;
    }

    private boolean hasLowerCase(String str) {
        //  Time Stamp: 1st November 2K18, 06:10 PM..!!

        //  return true if any character of str is Lower Case.
        for(char c : str.toCharArray()){
            if(c >= 'a' && c <= 'z')
                return true;
        }
        return false;

    }

    private boolean hasDigit(String str) {
        //  Time Stamp: 1st November 2K18, 06:10 PM!

        //  return true if any character of str is a Digit.
        for(char c : str.toCharArray()){
            if(c >= '0' && c <= '9')
                return true;
        }
        return false;
    }

    private String obtainFileExt() {
        System.out.print("Enter File Extension (Only ALPHA-Numeric Charset. without any period) : ");
        return scanner.nextLine();
    }

    private String obtainFileName() {
        System.out.print("Enter File Name: ");
        return scanner.nextLine();
    }

    /*
     *  Time Stamp: 5th November 2K18, 12:52 PM..!!
     *  Validate fileName & fileExt according to their set of rules.
     *
     */
    private boolean validation(String fPart, String printTag) {

        //  fPart:  fileName    |   fileExt
        //  printTag:   "File Name" |   "File Extension"

        //  set maxLength according to the printTag which indicates whether validation method is
        //  invoked for fileName or fileExt.
        int maxLength = (printTag.equals("File Name") ? FILE_NAME_MAX_LENGTH : FILE_EXTENSION_MAX_LENGTH);

        if (isItEmpty(fPart, printTag))
            return true;    //  fPart is Empty!
        else if (isItBlank(fPart, printTag))    //  fPart is Blank i.e. contains only white space characters!
            return true;
        else if (doesMaxLimitExceed(fPart, printTag, maxLength))//  fPart exceeds Max. Char. Limit.
            return true;
        else if (consistsSpecialCharacter(fPart, printTag)) //  fPart consists illegal characters!
            return true;
        else if (containsWhiteSpace(fPart, printTag))   //  fPart contains unnecessary white spaces.
            return true;
        else                //  fPart i.e. fileName or fileExtension is valid!
            return false;
    }

    /*
     *  Time Stamp: 5th November 2K18, 12:43 PM..!!
     *  Check for empty string & state warning
     */
    private boolean isItEmpty(String fPart, String printTag) {

        //  fPart    => fileName     | fileExtension
        //  printTag => "File Name" or "File Extension"
        //  return true if fPart is an empty string.
        if (fPart.isEmpty()) {
            displayError();
            System.out.println(printTag + " can't be EMPTY!");
            return true;
        }
        //  fPart ain't empty, so return false.
        return false;
    }

    private boolean isItBlank(String fPart, String printTag) {

        //  fPart    => fileName     | fileExtension
        //  printTag => "File Name"  | "File Extension"  |  "Choice"
        //  return true if fPart contains only white spaces.
        if (fPart.isBlank()) {
            displayError();
            System.out.println(printTag + " contains only White Spaces!");
            return true;
        }
        //  fPart ain't Blank, so return false.
        return false;
    }

    private boolean doesMaxLimitExceed(String fPart, String printTag, int maxLength) {

        //  return true if length of fPart exceeds max limit.
        if (fPart.length() > maxLength) {
            displayError();
            System.out.println(printTag + " Max. Length Limit Exceeded (" + maxLength +
                    " Characters)");
            return true;
        }
        //  fPart length is within the max range, it doesn't exceeds max. limit. Hence, return false.
        return false;
    }

    /*
     *  Time Stamp: 1st November 2K18, 03:56 PM!
     *  Validation Rules:
     *  Case I:
     *      Rules for Valid File Name:
     *      1. It can contain any unicode characters except for the following:
     *          < > " | \ / ? * :
     *      2. It can contain Periods/Dots (.) but it'll be invalid if the dot occurs
     *         at the end of the file name.
     *  Case II:
     *      Rules for Valid File Name:
     *      1. It can contain any unicode characters except for the following:
     *          < > " | \ / ? * : .
     */

    private boolean consistsSpecialCharacter(String fPart, String printTag) {
        //  Time Stamp: 30th October 2K18, 11:04 AM!

        //  printTag is simply used to distinguish whether the fPart is fileName or fileExt
        //  could use ENUMs rather than hardcoding the printTag as String values.

        //  There could be more than 1 special characters involved in the fileName,
        //  So, we need to save the result into temporary variable "temp" initialized with false.
        boolean temp = false;

        //  countSpecialCharacters is used to keep the count of Special Characters encountered
        //  & show in the tabular form with proper index value.
        int countSpecialCharacters;

        //  Its initialized with 1 as the value of the count is first printed & then incremented,
        //  So, to avoid printing 0 for the first occurence, we initialize it with 1.
        countSpecialCharacters = 1;

        //  Loop Hole in File System Naming:
        //  File System allows any unicode character to be used as fileName or fileExt
        //  except for the following 9 characters:      < > \ / " : | * ?
        //  For Example, Following Symbol similar to the Vertical Line | could be used to trick the file sys.
        //  Hebrew Punctuation Paseq (U+05C0) Big Vertical Line could be used in fileName & fileExt via clipboard!

        //  Required! Done!
        //  Display in the following format, but remove this line if no special char. found!
        //  Following statement is the Header which occurs only when there is any special characters.
        //  Sr. No.  |  Unicode  |  Character Symbol  |

        //  First of all, Check once if fPart violates the naming rule i.e. contains any of the illegal Characters
        //  then only print the Header followed by the occurences of special characters in the tabular form
        //  in ascending order based on their Unicode Values.
        //  Otherwise do not print the Header & the table representing the illegal characters.
        if (fPart.contains("/") || fPart.contains("\\") || fPart.contains(":") || fPart.contains("|") ||
                fPart.contains("<") || fPart.contains(">") || fPart.contains("?") || fPart.contains("\"") ||
                fPart.contains("*") || (!printTag.equals("File Name") && fPart.contains("."))) {
            displayError();
            System.out.println("Do not enter any of the following special characters: ");
            System.out.println("Sr. No.  |  Unicode  |  Symbol  |  Character  ");
        }
        //  Check for individual constraints of illegal character occurrence.

        if (fPart.contains("\"")) {
            System.out.println(String.format("%4d     |  %5s    |  %5s   |  %s", countSpecialCharacters, "0022", "(\")", "Quotation Mark!"));
            countSpecialCharacters++;
            temp = true;
        }
        if (fPart.contains("*")) {
            System.out.println(String.format("%4d     |  %5s    |  %5s   |  %s", countSpecialCharacters, "002A", "(\u002A)", "Asterisk!"));
            countSpecialCharacters++;
            temp = true;
        }
        if (fPart.contains(".")) {
            if (printTag.equals("File Extension")) {
                //  File Extension can't contain any Periods/Dots as we've hardcoded
                //  the single dot '.' explicitly as a connector between file name & file extension.
                //  which is required as a separator between the file name & extension.
                System.out.println(String.format("%4d     |  %5s    |  %5s   |  %s",
                        countSpecialCharacters, "002E", "(\u002E)", "Period / Dot!"));
                temp = true;
            }
        }

        if (fPart.contains("/")) {
            System.out.println(String.format("%4d     |  %5s    |  %5s   |  %s",
                    countSpecialCharacters, "002F", "(\u002F)", "Solidus!"));
            countSpecialCharacters++;
            temp = true;
        }
        if (fPart.contains(":")) {
            System.out.println(String.format("%4d     |  %5s    |  %5s   |  %s",
                    countSpecialCharacters, "003A", "(\u003A)", "Colon!"));
            countSpecialCharacters++;
            temp = true;
        }
        if (fPart.contains("<")) {
            System.out.println(String.format("%4d     |  %5s    |  %5s   |  %s",
                    countSpecialCharacters, "003C", "(\u003C)", "Less Than!"));
            countSpecialCharacters++;
            temp = true;
        }
        if (fPart.contains(">")) {
            System.out.println(String.format("%4d     |  %5s    |  %5s   |  %s",
                    countSpecialCharacters, "003E", "(\u003E)", "Greater Than!"));
            countSpecialCharacters++;
            temp = true;
        }
        if (fPart.contains("?")) {
            System.out.println(String.format("%4d     |  %5s    |  %5s   |  %s",
                    countSpecialCharacters, "004F", "(\u003F)", "Question Mark!"));
            countSpecialCharacters++;
            temp = true;
        }
        if (fPart.contains("\\")) {
            System.out.println(String.format("%4d     |  %5s    |  %5s   |  %s",
                    countSpecialCharacters, "005C", "(\\)", "Reverse Solidus!"));
            countSpecialCharacters++;
            temp = true;
        }
        if (fPart.contains("|")) {
            System.out.println(String.format("%4d     |  %5s    |  %5s   |  %s",
                    countSpecialCharacters, "01C0", "(\u01C0)", "Vertical Line!"));
            countSpecialCharacters++;
            temp = true;
        }

        //  Period/Dots are allowed in File Names unless its the last character,
        //  Thus, additional checking is required particularly in the case of file name.
        if (fPart.contains(".")) {
            if (printTag.equals("File Name")) {

                //  Check if the Period/Dot occurs at the end of the file name.
                if (fPart.indexOf('.') == fPart.length() - 1) {
                    displayError();
                    System.out.println("File Name can't end with a Period or Dot (.)");
                    temp = true;
                }
            }
        }

        //  If fPart contains any illegal character until now, temp would be false, otherwise true.
        return temp;
        //  Time Stamp: 30th October 2K18, 01:53 PM!
    }

    private boolean containsWhiteSpace(String fPart, String printTag) {
        //  Check if there's any white space character in between of the file extension.

        boolean whiteSpaceFlag = false;

        //  Remove any additional white spaces from the beginning & end if any.
        removeAdditionalWhiteSpace(fPart, printTag);

        //  fileName can contain whiteSpaces, so no additional validations required,
        if (printTag.equals("File Name")){
            return false;
        }

        //  Remove any unnecessary white spaces from the fPart using trim() method & store its each character
        //  in the temp array.
        char[] temp = fPart.trim().toCharArray();
        for (int i = 0; i < temp.length; i++) {
            char c = temp[i];
            if (Character.isWhitespace(c)) {
                displayError();
                System.out.println("Please do not enter any white space character in the file extension." +
                        "\n (Unicode: " + Character.codePointAt(temp, i) + " | Character: '" + temp[i] + "')\n");

                //  Requirement! (Pending)
                //  needs this method for the above statement!
                //  String appendZeroes(int codePoint, int numLength);
                whiteSpaceFlag = true;
            }
        }

        return whiteSpaceFlag;
    }

    private void removeAdditionalWhiteSpace(String fPart, String printTag) {
        //  Time Stamp: 1st November 2K18, 04:55 PM!

        //  Raise Warning to user if there's any unnecessary white space characters at the
        //  beginning or at the end.

        //  Remove if any additional white space present at beginning or at the end.
        if (hasWhiteSpace(fPart)) {
            displayWarning();
            System.out.print("Removing Additional Spaces found at: ");

            //  Update fileName or fileExtension accordingly. (trimming all unnecessary white spaces)
            if(printTag.equals("File Name"))
                fileName = fileName.trim();
            else
                fileExt = fileExt.trim();
        }
        //  Display additional white space character occurrence.
        displayAdditionalWhiteSpaceFoundAt(fPart);
    }

    /*
     *  Time Stamp: 5th November 2K18, 01:14 PM..!!
     *  Display whether the additional white spaces found at the beginning
     *  or at the end or at both the sides.
     */
    private void displayAdditionalWhiteSpaceFoundAt(String fPart) {

        if ((fPart.charAt(0) == ' ') && !(fPart.charAt(fPart.length() - 1) == ' ')) {
            System.out.println("Beginning!\n");
        } else if (!(fPart.charAt(0) == ' ') && (fPart.charAt(fPart.length() - 1) == ' ')) {
            System.out.println("End!\n");
        } else if ((fPart.charAt(0) == ' ') && (fPart.charAt(fPart.length() - 1) == ' ')) {
            System.out.println("Beginning & End!\n");
        }
    }

    //  Returns True if the str contains additional white space at beginning or at the end.
    private boolean hasWhiteSpace(String str) {
        //  Time Stamp: 4th November 2K18, 06:23 PM!
        return ( str.charAt(0) == ' ' || str.charAt(str.length() - 1) == ' ' );
    }

    /*
     *  Time Stamp: 5th November 2K18, 01:28 PM..!!
     *  Obtain File Path from User,
     *  either Default Directory or Explicitly Absolute Path Input by User.
     */
    private String obtainFilePath() {

        String fPath = null;
        int ch = 0;

        do {
            System.out.println("Choose File Path:\n1. Default: " + new File(DEFAULT_DIRECTORY_FILE_PATH).getAbsolutePath());
            System.out.println("2. Enter Path Manually. ");

            //  ch -> user's input.
            ch = userInputInt();

            switch (ch) {

                //  Default Path.
                case 1:
                    fPath = DEFAULT_DIRECTORY_FILE_PATH;
                    break;

                //  Manually Input Complete Path.
                case 2:
                    //  Prompt user for file path & validate it.
//                    fPath = gatherFilePath();
                    //  VALIDATION PENDING!

                    break;

                default:
                    displayError();
                    System.out.println("Invalid Choice! Please Try Again...\n");
            }

        } while (ch != 1 && ch != 2);   //  Prompt user for input until user opts for a valid choice i.e. 1 or 2.
        return fPath;   //  return the selected new file path.
    }

    private String gatherFilePath() {

        boolean invalidFlag = true;
        String fPath = "";

        while (invalidFlag) {
            System.out.println("Enter New File Path: ");

            //  Reset Warning & Error Flags to false.
            resetFlags();

            //  Prompt User for File Name!
            fPath = scanner.nextLine(); //  Accept entire path for the new file.

            //  Validate the file name input by user!
            invalidFlag = validation(fileName, "File Path");
            if (invalidFlag)
                System.out.println("Please Try Again...\n");
        }

        return fPath;
    }

    private boolean wannaAppend() {

        //  temp is used to represent whether user wants to append the content to the existing file or not.
        boolean result = false;

        int ch = 0;
        //  Check if the file already exists at the path specified.
        boolean fExists = (new File(filePath + fileName + "." + fileExt).exists());

//        if (fExists)
//            System.out.println("There is already a file with the same name in this location!");

        /*
         * Increment the counter of new file name according to the last version of the existing file name.
         * Example: a.txt | a (2).txt | a (3).txt |
         * there exists these 3 files already, if one decides to keep both the files i.e. new
         * and the old then find out how many files with the count already exists & store the value of the
         * next consecutive count value for the new file name in rnCount.
         */
        int rnCount = gatherRenameCounter();

        //  Keep Prompting User for input until its single digit 1, 2 or 3.
        while (fExists && (ch < 1 || ch > 3)) {

            if (new File(filePath + fileName + "." + fileExt).exists())
                System.out.println("There is already a file with the same name in this location!");

            //  Prompt User with choice to Append the file contents to the existing file.
            //  y => appendFlag = true | n =< overwrite i.e. appendFlag = false.
            System.out.println("1. Append the content in the file with an additional new line.");
            System.out.println("2. Overwrite the file with the new content.");

            if (possiblyDuplicate)
                System.out.println("3. Keep Both Files, Renaming the new file as \"" + localFileName + "." + fileExt + "\"");
            else
                System.out.println("3. Keep Both Files, Renaming the new file as \"" + fileName + " (" +
                        rnCount + ")" + "." + fileExt + "\"");
//            System.out.println("4. Go Back!");
            resetFlags();
            ch = userInputInt();

            switch (ch) {
                case 1:
                    //  For Appending, set temp = true.
                    result = true;
                    break;
                case 2:
                    //  For Overwriting, set temp = false.
                    result = false;
                    break;
                case 3:
                    //  To Keep both the files, update the fileName with localFileName i.e. count value of 1 more than
                    //  the last version of the existing file.
                    if (possiblyDuplicate)
                        fileName = localFileName;
                    else
                        fileName += " (" + rnCount + ")";
                    result = false;
                    break;
                default:
                    displayError();
                    if (ch == -2 )      //  In case of only Empty input by user.
                        System.out.println("Choice can't be Empty!");
                    else if (ch == -3 )  //  In case of only white space characters input by user.
                        System.out.println("Choice can't be Blank!");
                    else if (ch == -4 )     //  In case of More than 1 character input by user.
                        System.out.println("Choice must be a Single Character!");
                    else if (ch == -5 )     //  Other Cases.
                        System.out.println("Choice must be a Single Numeric Digit Only!");
                    else
                        System.out.println("Select the Choice between: 0 and 1 only!");
                    System.out.println("Please enter a valid choice!\n");
            }
        }
        return result;
    }

    private int gatherRenameCounter() {
        //  Time Stamp: 31st October 2K18, 03:03 PM!

        //  Initially, the file already exists, so the rename count has to start with 1.
        int count = 1;

        //  initialize localFileName with the given fileName.
        localFileName = fileName;
//        boolean tmpExists = (new File(filePath + fileName + "." + fileExt).exists());
        //  If file doesn't exists then return -1.
        if (!(new File(filePath + fileName + "." + fileExt).exists())) {
            return -1;
        }
        if (fileName.contains("(") && fileName.contains(")"))
            possiblyDuplicate = true;

        // Requirement:
        // check if file name already consists the (1), then increment the count within the name itself.

        //  check if the file name is in the format: "...(count)"

        labelSpecialCheck:
        if(possiblyDuplicate) {
            //  initialize lastIndex with the last index, most probably the index of ')' for the special check.
            int lastIndex = fileName.length() - 1;

            //  Check for single Digit between parenthesis at the end of the String.
            //  Example:    fileName (without extension) : "a (1)"  |   lastIndex = ')'
            if (fileName.charAt(lastIndex) == ')') {

                int numOfDigits = 0;  //  numOfDigits => number of digits between parenthesis.

                //  Count num of Digits starting from the 2nd last char.
                for (int i1 = fileName.length() - 2; i1 >= 0; i1--) {

                    //  Increment the counter if the char at i1 is a digit.
                    if (hasDigit(fileName.charAt(i1) + "")) {
                        numOfDigits++;
                    } else if ((fileName.charAt(i1) == '(')) {

                        //  If there is no digit between parenthesis, treat fileName as a complete string.
                        if(numOfDigits == 0){
                            //  fileName is in format: "...()"
                            //  break out & follow the middleCounterCheck method for duplicate file check.
                            break labelSpecialCheck;
                        }

                        /*
                         *  Time Stamp: 2nd November 2K18, 07:31 PM!
                         *  if fileName contains more than 10 digits between parenthesis,
                         *  it already exceeds the max. limit of counter i.e. Integer.MAX_VALUE!
                         *  Treat the entire number between the parenthesis as a string,
                         *  add another pair of parenthesis with 1 as the middle count.
                         */
                        if(numOfDigits > 10) {
                            displayWarning();
                            System.out.println("Count Limit Exceeded! (2147483647)");
                            break labelSpecialCheck;
                        }
                        break;
                    } else {
                        //  fileName ain't duplicate anymore, its simply in the form: "...)"
                        break labelSpecialCheck;
                    }
                }
                if (fileName.charAt(lastIndex - (numOfDigits + 1)) == '(') {
                    count = 0;
                    int currentIndex = lastIndex - numOfDigits;
                    for (int i1 = 0; i1 < numOfDigits; i1++) {
                        if (fileName.charAt(lastIndex - numOfDigits) >= '0'
                                && fileName.charAt(lastIndex - numOfDigits) <= '9') {
                            count = (count * 10) + gatherCharValue(fileName.charAt(currentIndex));
                            currentIndex++;
                        }
                    }
                    //  fileName: "a (101)" | front: path + "a ("
                    String front = filePath + fileName.substring(0, lastIndex - numOfDigits);
                    count = middleCounterCheck( front, count);

                    //  if the count value is -1 i.e. file name violates the Max. Limit for count!
                    if(count == -1){
                        break labelSpecialCheck;
                    }
                    localFileName = fileName.substring(0, lastIndex - numOfDigits);
                    localFileName += count + ")";
                    System.out.println(localFileName);
                    possiblyDuplicate = true;
                    return count;
                }
            }
        }

        /*
         *  File Name ain't in the format: "...(count)", or it violated the Max. Count,
         *  but file with given fileName already exists, so we need to check
         *  if the "... (1)" file exists or not & keep incrementing the middle counter
         *  until the file name with the middle count doesn't exists.
         */
            count = existingCounterCheck();

        return count;
    }

    /*
     *  Time Stamp: 2nd November 2K18, 08:22 PM!
     *  Until now, the fileName doesn't consists any ambiguous content.
     *  fileName is treated entirely as fresh string & will be checked for any duplicate files with
     *  count value present between a parenthesis.
     *  Example:    fileName: "abc ()"
     *              " (middleCount)" will be appended to it & checked if it already exists,
     *              Keep incrementing the middleCounter until file doesn't exists.
     *              Returning the value of middleCount.
     */
    private int existingCounterCheck() {

        int middleCounter = 1;
        while(true) {
            //  check if the file with next count (within the parenthesis) also already exists.
            if (new File(filePath + fileName + " (" + middleCounter + ")" + "." + fileExt).exists()) {

                //  increase the counter if the file with next count value between parenthesis already exists.
                //  i.e. if "... (17)" already exists, increment 17 to 18.
                middleCounter++;

                //  if the value of counter goes above 20, either there are too many copies of the file,
                //  or there is a bug in code. Hence, requires manual check for confirmation!
                if (middleCounter > 20) {
                    displayWarning();
                    System.out.println("Too Many Duplicate Files!!!");
                    System.out.println("Check it quick!");
                }
            } else
                break;
        }

        //  possiblyDuplicate case is already dealt above, now it ain't duplicate anymore.
        possiblyDuplicate = false;
        return middleCounter;
    }

    private int middleCounterCheck(String front, int initialCount) {
        //  Time Stamp: 2nd November 2K18, 07:58 PM!

        int middleCounter = initialCount;
        while(true) {

            if(middleCounter >= Integer.MAX_VALUE ){
                displayWarning();
                System.out.println("Count Limit Exceeded! (2147483647)");
                return -1;
            }

            //  check if the file with next count (within the parenthesis) also already exists.
            if (new File(front + middleCounter + ")" + "." + fileExt).exists()) {

                //  increase the counter if the file with next count value already exists.
                middleCounter++;

                //  if the value of i goes above 8, either there are too many copies of the file,
                //  or there is a bug in code. Hence, requires manual check for confirmation!
                if (middleCounter > 20) {
                    displayWarning();
                    System.out.println("Too Many Duplicate Files!!!");
                    System.out.println("Check it quick!");
                }
            } else
                //  In case of file doesn't exists with the middleCounter in between parenthesis,
                //  Stop incrementing the counter & break out of while loop.
                break;
        }

        //  Set possiblyDuplicate to false as the value of middleCounter is optimized accordingly
        //  & no duplicacy issue anymore.
        possiblyDuplicate = false;
        return middleCounter;
    }

    private int gatherCharValue(char c) {
        switch (c) {
            case '\u0030':
                return 0;
            case '\u0031':
                return 1;
            case '\u0032':
                return 2;
            case '\u0033':
                return 3;
            case '\u0034':
                return 4;
            case '\u0035':
                return 5;
            case '\u0036':
                return 6;
            case '\u0037':
                return 7;
            case '\u0038':
                return 8;
            case '\u0039':
                return 9;
            default:
                displayError();
                System.out.println("Char Value Error!");
                return -1;
        }
    }

    private String obtainFileData() {
        //  local variable for storing the file content i.e. String.
        String fData = "";


        //  Add New Line at the end of the existing file content if wanna append with a New Line character.
        if (appendFlag)
            fData += "\n";

        do {
            System.out.println("Enter File Content:");

            //  Append the entire Line of content entered by user to fData.
            fData += scanner.nextLine();

            //  Prompt user for choice of entering more content in form of: (y/n)
                //  Add New Line for every iteration.
                fData += "\n";

        } while (repeatAgain());    //  exit the loop only when user opts to stop entering the content for file.

        displayFileContentToBeWritten(fData);

        return fData;   //  return the local variable which contains the entire content of the file.
    }

    private void displayFileContentToBeWritten(String fData) {
//        Time Stamp: 1st November 2K18, 05:42 PM!
//Updated Time Stamp: 4th November 2K18, 10:22 PM..!!


        System.out.println("Content to be written in File:");
        System.out.println("----------------------");

        //  If appending, then skip printing the first & last characters i.e. '\n' on the console.
        if(appendFlag)
            System.out.println(fData.substring(1, fData.length()-1));

        //  Otherwise, skip printing the last character i.e. '\n' on the console.
        else
            System.out.println(fData.substring(0, fData.length()-1));
        System.out.println("----------------------");
    }

    private boolean repeatAgain() {
//        Time Stamp: 1st November 2K18, 05:42 PM!

        char ch = 0;
        while (true) {    //  exit the loop only when user opts to stop entering the content for file.
            System.out.print("Want to enter more content? (y/n) : ");

            //  Consider only the first character of the user's input word for the choice.
            ch = userInputChar();

            switch (ch) {
                case 'y':
                case 'Y':

                    //  Continue Prompting the user for entering another line content.
                    return true;
                case 'n':
                case 'N':
//                System.out.println(fData);
                    return false;

                /*
                 *  Time Stamp: 5th November 2K18, 12:02 PM..!!
                 *  In case of User Input other than the desired ones i.e. [y, Y, n, N]
                 *  for suitable errors, if else statements are used inside the default case so
                 *  that, first of all, the error msg. could be printed for every invalid choice,
                 *  then listing the particular reason stating what's wrong in user input.
                 */
                default:
                    displayError();
                    if (ch == '2')      //  In case of only Empty input by user.
                        System.out.println("Choice can't be Empty!");
                    else if (ch == '3') //  In case of only white space characters input by user.
                        System.out.println("Choice can't be Blank!");
                    else if (ch == '4') //  In case of More than 1 character input by user.
                        System.out.println("Choice must be a Single Character!");
                    else if (ch == '5') //  In case of wrong yet single character input. (" 5 ")
                        System.out.println("Choice must be a Single Alphabet Character only!");
                    else //  In case of wrong (yet syntactically correct) but single alphabet character input (" a ").
                        System.out.println("Select the Choice among: [y, Y, n, N] only!");
                    System.out.println("Please enter valid choice!\n");
            }
        }
    }

    private void createNewFile() throws FileNotFoundException {
//        Time Stamp: 22nd October 2K18, 4:17 PM!
//Updated Time Stamp: 28th October 2K18, 08:16 PM!

        //  fPath must always consist of '/' directory delimiter
        //  & character at its last index must be '/'.
        OutputStream fos = new FileOutputStream(filePath + fileName + "." + fileExt, appendFlag);

        try {

            fos.write(fileData.getBytes());

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
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
        if(appendFlag)
            System.out.println("File |" + fileName + "." + fileExt + "| successfully updated in the following directory,");
        else
            System.out.println("File |" + fileName + "." + fileExt + "| successfully created in the following directory,");
        System.out.println(new File("" + fileName + "." + fileExt).getAbsolutePath());
    }
    private void displayWarning() {
        //  Time Stamp: 4th November 2K18, 11:55 AM!

        //  If there's already a warning given before, do not print it multiple times.
        //  Simply skip printing "Warning:" as its already been done before.
        if(!isWarningRaised()) {
            System.out.println("Warning:");
            setWarningRaised(true);
        }
    }
    private void displayError() {
        //  Time Stamp: 4th November 2K18, 11:55 AM!

        //  If there's already a Error given before, do not print it multiple times.
        //  Simply skip printing "Error:" as its already been done before.
        if(!isErrorOccured()) {
            System.out.println("Error:");
            setErrorOccured(true);
        }
    }

    private String replaceReverseSolidus(String temp) {
        return temp.replace('\\', '/');
    }

    public boolean isWarningRaised() {
        return warningRaised;
    }

    public boolean isErrorOccured() {
        return errorOccured;
    }

    public void setWarningRaised(boolean warningRaised) {
        this.warningRaised = warningRaised;
    }

    public void setErrorOccured(boolean errorOccured) {
        this.errorOccured = errorOccured;
    }
}

/*
 * Date Created: 22nd October 2K18, 03:52 PM!
 * Date Modified: 5th November 2K18, 02:39 PM!
 *
 * Code Developed By,
 * K.O.H..!! ^__^
 */


//  warning count   | Pending!
//  check file exists again before writing. Pending.





















