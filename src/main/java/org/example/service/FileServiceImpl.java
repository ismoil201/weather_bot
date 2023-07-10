package org.example.service;

import java.io.*;

public class FileServiceImpl implements  FileServer
{
    @Override
    public void fileWrite(String text) {

        try {
            File file = new File("Ob-Havo.txt");
            file.createNewFile();

            FileOutputStream fileOutputStream = new FileOutputStream(file,true);

            byte[] byte1 = text.getBytes();
            fileOutputStream.write(byte1);
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void fileRead(String text) {
        try {
            File file = new File("Ob-Havo.txt");
            file.createNewFile();


            InputStream inputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String row;
            StringBuilder stringBuilder = new StringBuilder();
            while((row=bufferedReader.readLine())!=null){

                stringBuilder.append(row);
            }
            System.out.println(stringBuilder.toString());


        }catch (Exception e){
            e.printStackTrace();
        }


    }
}
