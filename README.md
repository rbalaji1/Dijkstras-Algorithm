# Dijkstras-Algorithm
Implementation of the shortest path algorithm of dijsktras

————————————————————————————————
	README
————————————————————————————————

1: FOLDER INFORMATION:

-	The zip file contains two java files. LZWencoder.java and LZWdecoder.java
-	LZWencoder.java is the encoder code and LZWdecoder.java is the decoder.
-	The programming language used here is JAVA and can be compiled using javac compiler.



2: Compiling and execution:

-	To compile the encoder/decoder program in linux or mac simply use terminal

command:
“javac LZWencoder.java” or “javac LZWdecoder.java”

and to run the program

command:
“java LZWencoder <path of the input file(contains string)> <bit length>”
or
“java LZWdecoder <path of the input file(contains encoded code)> <bit length>”

you can also use eclipse to run the program.



3: PROGRAM DESIGN:

(A) ENCODER:

-	The main class LZWencoder contains 3 private variables, an arraylist of strings for storing the table, an arraylist of integers to store the output code and a double variable MAX that has the value of max size of the table 

-	The constructor of the class initializes the arraylist “table” with the 256 characters in ASCII table in string type.
It also sets the MAX variable with the max size using the argument passed to it.

-	The main function accepts the input file and bit length as arguments. Instantiating the object of the class with bit length for the constructor. The input file is read and the each individual character is separated and put in an arraylist “code”. This array list is of type string.

-	Then using Lempel-Ziv-Welch algorithm the output arraylist is populated with the encoded integers. The encoded integer is then written to a binary file using DataOutputStream object and using writeShort method to write two bytes.

-	All this is done within try-catch block to catch exceptions that can be generated.

(B) DECODER:

-	Similar to encoder the decoder class LZWdecoder also contains the same private variables, except now the output arraylist will be an integer type.

- 	The constructor of decoder does the same initialization as that of the encoder of population  the table arraylist.

-	The main method now reads the binary file using DataInputStream object.

-	The file is read and each short integer is stored in an arraylist “code” of integer type.

-	Now the Lempel-Ziv-Welch decoding algorithm is used to decode the input integer code and the output string arraylist is populated with the respective codes from the table.

-	Once the output arraylist is populated, each element is added to the previous thus recreating the string which is the same as that of the input string provided to the encoder. 

-	The correctness can be checked by using the diff command in the terminal.

-	The output string is now written to a text file using PrintWriter object.



4: Program defect and solutions:

- The program did not read new line character, To fix this issue in the program, after every line read from the input the code included another string “\n” to it so the new line can be considered.

- The program can read any string that include characters in the ASCII table.
- The size of the input file is limited to the size of bit length that is provided during run time to the program. Thus may restrict large text files as input.

- The first character in the input text should be present in the ascii table. And if any character not present in the ascii table is present in the text, then -1 is returned.
- “-1” is considered as in invalid encoding and “#” is printed during decoding.

- now you can potentially encode and decode this readme file.



Version 2.0

The previous version included “\n” (new line character) code even for a single line text. To fix this issue another conditional statement to check for new line present has been added to the encoder.

