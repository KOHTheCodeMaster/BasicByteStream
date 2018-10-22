package dev.koh.filehandling;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Scanner;

public class BasicByteStream {

    Scanner scanner = new Scanner(System.in);
    private static final String ABSOLUTE_FILE_PATH = "E:\\HEADQUARTERS..!!\\CodeBase\\Java\\CoreJava\\" +
            "FileHandling\\src\\dev\\koh\\filehandling\\res\\txtfiles\\";

    private String filePath = null;
    private String fileName = null;
    private String fileExt = null;

    public static void main(String[] args) throws FileNotFoundException {

//        Time Stamp: 22nd October 2K18, 3:52 PM!
        System.out.println("Begin.");

        BasicByteStream obj = new BasicByteStream();
        obj.menu();

        System.out.println("End.");
//        Time Stamp: 22nd October 2K18, 3:52 PM!

    }

    private void menu() throws FileNotFoundException {

        int ch;

        do {
            System.out.println("1. Create New File.");
            System.out.println("0. Exit!");
            System.out.print("Enter Choice: ?: ");

            ch = scanner.nextInt();
            scanner.nextLine(); //  Empty Buffer.
            switch (ch) {
                case 0:
                    System.out.println("Shutting Down the program.");
                    break;
                case 1:
                    createFile();
                    break;

                default:
                    System.out.println("Please Enter Valid Choice!");
            }
        } while (ch != 0);

    }

    private void createFile() throws FileNotFoundException {

        String fPath = null;
        String fName = null;
        String fExt = null;

        System.out.print("Enter File Name: ");
        fName = scanner.nextLine();
        System.out.print("Enter File Extention: ");
        fExt = scanner.nextLine();

        int ch = 0;
        do {
            System.out.println("Choose File Path:\n1. Default: " + ABSOLUTE_FILE_PATH);
            System.out.println("2. Enter Path Manually. ");

            ch = scanner.nextInt();
            scanner.nextLine();
            switch (ch){
                case 1:
                    fPath = ABSOLUTE_FILE_PATH;
                    break;
                case 2:
                    System.out.println("Enter New File Path: ");
                    fPath = scanner.nextLine();
                    //  checking required.
                    break;
                default:
                    System.out.println("Invalid Choice! Please Try Again...");
            }

        } while (ch != 1 && ch != 2);

        filePath = fPath;
        fileName = fName;
        fileExt = fExt;

        createNewFile();

    }

    private void createNewFile() throws FileNotFoundException {
//        Time Stamp: 22nd October 2K18, 4:17 PM!

        //  fPath must always consist of '/' directory delimiter
        //  & character at its last index must be '/'.
        OutputStream fos = new FileOutputStream(filePath + fileName + fileExt, false);

        String content = null;
        content = "Java IO streams are flows of data you can either read from, or write to. As mentioned earlier in this tutorial, streams are typically connected to a data source, or data destination, like a file, network connection etc.\n" +
                "\n" +
                "A stream has no concept of an index of the read or written data, like an array does. Nor can you typically move forth and back in a stream, like you can in an array, or in a file using RandomAccessFile. A stream is just a continuous flow of data.\n" +
                "\n" +
                "Some stream implementations like the PushbackInputStream allows you to push data back into the stream, to be re-read again later. But you can only push back a limited amount of data, and you cannot traverse the data at will, like you can with an array. Data can only be accessed sequentially.\n" +
                "\n" +
                "Java IO streams are typically either byte based or character based. The streams that are byte based are typically called something with \"stream\", like InputStream or OutputStream. These streams read and write a raw byte at a time, with the exception of the DataInputStream and DataOutputStream which can also read and write int, long, float and double values.\n" +
                "\n" +
                "The streams that are character based are typically called something with \"Reader\" or \"Writer\". The character based streams can read / write characters (like Latin1 or UNICODE characters). See the text Java Readers and Writers for more information about character based input and output.";
        String str = "Hello World!";
        try {

            fos.write(str.getBytes());

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
                try {
                    fos.close();
                    System.out.println("File |" + fileName + "| successfully created in the following directory");
                    System.out.println(filePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }

//        Time Stamp: 22nd October 2K18, 4:53 PM!
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
 * Date Modified: 22nd October 2K18, 03:52 PM!
 *
 * Code Developed By,
 * K.O.H..!! ^__^
 */
























