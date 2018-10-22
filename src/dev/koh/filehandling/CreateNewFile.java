package dev.koh.filehandling;

import java.io.*;

public class CreateNewFile {

    private static final String ABSOLUTE_FILE_PATH = "E:\\HEADQUARTERS..!!\\CodeBase\\Java\\CoreJava" +
            "\\FileHandling" + "\\src\\dev\\koh\\filehandling\\fileishere\\";

    private static final String RELATIVE_FILE_PATH = "E:\\HEADQUARTERS..!!\\CodeBase\\Java\\CoreJava" +
            "\\FileHandling" + "\\src\\dev\\koh\\filehandling\\";

    String filePath = null;
    String fileName = null;

    public static void main(String[] args) {
//        Time Stamp: 20th October 2K18, 7:00 PM!
        System.out.println("Begin.");

        CreateNewFile obj = new CreateNewFile();
        obj.majorMethod();


        System.out.println("End.");
//        Time Stamp: 20th October 2K18, 7:00 PM!
    }
/*
    private void mainMenu(){
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        try{
            while(true){

                if( br.read() == 1 )

                majorMethod();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }*/

    private void majorMethod() {

        BufferedReader br = null;
        boolean stop = false;
        String temp = null;
//        while(!stop){

            try {
                br = new BufferedReader(new InputStreamReader(System.in));
                System.out.print("Wanna Create New File?  | y/n | ?: ");
                temp = br.readLine();
                if(temp.equals("y"))
                    createFile();
                else
                    System.out.println("Program Terminating...");
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


//        }

    }

    private void createFile() {

        obtainFileNameAndPath();

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(new File(filePath + fileName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
                System.out.println("File: " + fileName + " created successfully.");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void obtainFileNameAndPath() {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String tempFileName = null;
        String tempFilePath = null;

        try {

            System.out.println("Enter File Name: ");
            tempFileName = br.readLine();

            String ch = null;
            do {
                System.out.println("1. Default File Path - " + ABSOLUTE_FILE_PATH);
                System.out.println("2. Relative File Path Root Dir. - " + RELATIVE_FILE_PATH);
                System.out.println("3. Change File Path.");
                System.out.print("?: ");

                ch =  br.readLine();
                if (ch .equals("1"))
                    tempFilePath = ABSOLUTE_FILE_PATH;
                else if (ch.equals("3")) {
                    System.out.print("Enter New File Path: ");
                    tempFilePath = br.readLine();
                }
                else
                    System.out.println("Please Enter a Valid Choice!");

            } while ( ! ( (ch.equals("1") || (ch.equals("3")) ) ) );

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(tempFilePath.contains("\\"))
                    filePath = replaceBackSlash(tempFilePath);
                else
                    filePath = tempFilePath;
                fileName = tempFileName;
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
// 8:53 PM!
}


















