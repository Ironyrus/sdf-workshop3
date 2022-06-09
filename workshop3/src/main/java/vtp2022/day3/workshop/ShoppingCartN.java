package vtp2022.day3.workshop;
/*
PS C:\Users\vans_\sdf-workshop1> git add . (add ALL content of cart to github)
git commit -m "While Loop"                  (add comment while committing)
git push origin main                        (push to main branch)
*/

/*
https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html
->
mvn archetype:generate -DgroupId=vtp2022.day2.workshop -DartifactId=workshop2 -DarchetypeArtifactId=maven-archetype-quickstart -DarchetypeVersion=1.4 -DinteractiveMode=false
->
C:\Users\vans_\sdf-workshop1>cd sdf-workshop2
C:\Users\vans_\sdf-workshop1\sdf-workshop2>mvn compile
(RUN) --> mvn compile exec:java -Dexec.mainClass="vtp2022.day3.workshop.ShoppingCartN"
*/

/*
 vans_@LAPTOP-AS886SBL MINGW64 ~/VISA NUS-ISS VTTP/sdf-workshop3/workshop3 (main)
$ mvn compile exec:java -Dexec.mainClass="vtp2022.day3.workshop.ShoppingCartN"
*/

import java.util.*;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Console;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.*;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;

public class ShoppingCartN {

    public static void main(String[] args) {
        System.out.println("Shopping Cart");
        List<String> cart = new LinkedList<String>();
        ArrayList<String> userCart = new ArrayList<String>();
        Console cons = System.console();
        String input;
        int delIndex;
        boolean stop = false;
        String userPath = "";

        String argss = "cartdb";
        if (!(argss == null)) {
        //CREATE NEW DIRECTORY
        String dirName = "cartdb";
        Path newDir = Paths.get(dirName);
        if(!Files.exists(newDir)) {
            try {
                Files.createDirectories(newDir);
                System.out.println("Directory created at " + newDir);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Directory " + newDir + " already exists!");
        }
        } else {
            argss = "db";
        }
        //END CREATING NEW DIRECTORY

        while(!stop) {
            input = cons.readLine("> ");
            System.out.printf("READ: %s\n", input);
            String[] terms = input.split(" ");
            String cmd = terms[0].toLowerCase();

            switch(cmd) {
                case "add":
                    String fruitsStr = terms[1];
                    String fruitsReplaced = fruitsStr.replace(",", " ");
                    String[] fruits = fruitsReplaced.split(" ");

                    for (int i = 0; i < fruits.length; i++) {
                        boolean found = false;
                        for (int j = 0; j < cart.size(); j++) {
                            if(fruits[i].equals(cart.get(j))) {
                                found = true;
                                System.out.println("You have " + fruits[i] + " in your cart");
                                break;
                            }
                        }
                        if(!found) {
                            userCart.add(fruits[i]);
                            cart.add(fruits[i]);
                            System.out.printf("Added %s to cart\n", fruits[i]);
                        }

                    }
                    break;
                case "list":
                    if(cart.size() > 0) {
                        for (int i = 0; i < cart.size(); i++) {
                            System.out.printf("%d. %s\n", i+1, cart.get(i));
                        }
                    } else {
                        System.out.println("Your cart is empty.");
                    }
                    break;
                case "del":
                    if(terms.length < 2) {
                        System.out.println("Usage: del NUMBER");
                    } else {
                        try {
                            delIndex = Integer.parseInt(terms[1]) - 1;
                            if (delIndex < cart.size()) {
                                System.out.println(cart.get(delIndex) + " removed from cart");
                                cart.remove(delIndex);
                            } else {
                                System.out.println("No such item.");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Usage: del NUMBER");
                        }
                    }
                    break;
                case "end":
                    stop = true;
                    break;
                case "login":
                    userCart.clear(); //Every time a new user logs in, clear usercart.
                    cart.clear();
                    //CREATE NEW FILE
                    String username = terms[1];
                    userPath = argss + "/" + terms[1] + ".txt";
                    Path path = Paths.get(userPath);
                    //ArrayList<String> userCart = new ArrayList<String>();
                    if (!Files.exists(path)) {
                        try {
                            Path createdFilePath = Files.createFile(path);
                            System.out.println("Created a file at " + createdFilePath);    
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        System.out.println("File " + path + " already exists!");
                    }

                    try {
                        File userFile = new File(userPath);
                        FileReader frobj = new FileReader(userFile);
                        BufferedReader brobj = new BufferedReader(frobj);
                        StringBuffer sbobj = new StringBuffer();
                        String line;
                        int count = 1;

                        if(brobj.readLine() == null) //Check for empty cart.
                            System.out.println(username + ", your cart is empty!");
                        else
                            System.out.println(username + ", your cart contains the following items");
                            
                        FileReader frobj2 = new FileReader(userFile);
                        BufferedReader brobj2 = new BufferedReader(frobj2); //Re-initialize after checking for empty cart
                        while ((line = brobj2.readLine()) != null) {
                            sbobj.append(line); //Not so important
                            userCart.add(line); //Populate user cart with current user's items
                            System.out.println(count + ". " + line);
                            count++;
                    }
                    //System.out.println(sbobj.toString()); APPLEEGGSBABANAS
                    frobj.close();
                    frobj2.close();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    
                    break;
                
                case "save":
                    if (userPath != "") {
                        try {
                            File userFile = new File(userPath);
                            BufferedWriter writer = new BufferedWriter(new FileWriter(userFile));
                            
                            if (!userCart.isEmpty()) {
                                for (String item : userCart) {
                                    writer.append(item + "\n");
                                }
                                System.out.println("Your cart has been saved.");
                            } else {
                                System.out.println("Cart is empty. Nothing to save.");
                            }
                            writer.close();                            
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    } else {
                        System.out.println("Error. Please Login First!");
                    }
                    
                    break;
                
                case "check":
                    for (String item : userCart) {
                        System.out.println(item);                        
                    }

                    // try {
                    //     File userFile = new File(userPath);
                    //     FileInputStream fin = new FileInputStream(userFile);
                    //     BufferedInputStream bin = new BufferedInputStream(fin);
                    //     int i;
                    //     while ((i = bin.read()) != -1) {
                    //         System.out.println((char)i); //Can even recognize new line "\n"
                    //     }
                    //     bin.close();
                    //     fin.close();
                    // } catch (Exception e) {
                    //     //TODO: handle exception
                    // }

                    break;
                
                case "users":
                    String name = "";
                    int count = 1;
                    File[] dir = new File("./cartdb/").listFiles();
                    if (dir.length == 0)
                        System.out.println("No users registered.");
                    else {
                        System.out.println("The following users are registered:");
                        for (File filename : dir) {
                            if (!filename.isDirectory()) {
                                name = filename.getName().split("\\.")[0];
                                System.out.println(count + ". " + name);
                                count++;
                        }
                    }
                    }
                    
                    break;
                
                default:
                    System.out.println("Usage: add/del/end SOMETHING");
            }
        }
        System.out.println("Thank you for shopping with us");
    }
}
