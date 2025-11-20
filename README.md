# Simple-Java-Prefix-Expression-interpreter
This short program is able to evaluate basic arithmetic expressions in Prefix or Postfix form. It can maintain operation order and is implemented using the Stack data structure.

Example:<br>
\+ \+ 45 2 10 = 45 + 2 + 10 <br>
\+ \* 5 2 3 = (5 * 2) + 3 <br>
\+ \+ 6 * 3 2 7 = 6 + (3 * 2) + 7 <br>

## Implementation 

For this project, I used the Stack data structure, A FIFO (First-in First-out) structure that in this case is perfect for the nuances that come with operator precedence. Each subsection here describes descriptions of the most important methods I used and how I designed them.

<details>
 <summary> See Method Descriptions Here! </summary>
 
 * <details>
    <summary> prefixOperation Method </summary>

    ### prefixOperation(String[] input)

     This method implements the stacks to accurately evaluate the given expression (in String[] form).

     I use two stacks called "mainStack" and "secondStack" respectively (which I'll call mS and sS from now on). A loop reads the given String[] array (which is assumed to be correctly formatted by this point) and adds each element to mS.
     Example:

         main stack   second stack
             [3]           [ ]
             [5]           [ ]
             [2]           [ ]
             [*]           [ ]
             [-]           [ ]
     The method then pops integers off the mS and adds them to sS. Once an operator is popped off main stack, it pops two integers off second stack and runs the operation. The result gets added back to second stack.

         main stack   second stack
             [ ]           [ ]
             [ ]           [ ]
             [ ]           [2] <-- Will get popped
             [*]           [5] <-- Will get popped
             [-]           [3]
             2 * 5 = 10

         main stack   second stack
             [ ]           [ ]
             [ ]           [ ]
             [ ]           [ ]
             [ ]           [10]
             [-]           [3]
             10 - 3 = 7
     Once nothing is left in mS, the method returns the resulting integer. 

     After creating this implementation, I found out that the more efficient solution is to skip the creation of a "mainStack" in favor of just reading off the already parsed and ordered array. I only keep my current implementation because its what I was able to come up with.

     It is important to note that prefixOperation tries to avoid outright ending the program unless a completely invalid expression is entered. More information on that in the section about the main method.

     returns: int
   </details>

 * <details>
    <summary>stringToArray Method</summary>

    ### stringToArray(String input)

     This method takes an assumed prefix form expression and break it up into seperate elements to be processed by prefixOperation

     1. Trim given string, as we use whitespace to parse integers and operators in string

     2. Count the amount of whitespace to calculate the length of the new array. Since we have a space between each operator/integer, I can confidently say that there should be (whiteSpaceCount + 1) elements in the new array. I also create "stringArr" to hold our parsed string.

     3. I create currChar to temporarily hold the current character as we go through the string input. I create a StringBuilder which helps me put together integers with more than one digit (as I'm working with characters here).

     4. I create int i to track elements in stringArr as we fill out all of its indexes.

     5. I start a for-loop to get each character in the input string.

     6. In the loop, I grab a character and append it to my StringBuilder. If that character is whitespace, then I empty StringBuilder and continue the loop (to get the next character)

     7. In the loop, if the character chosen is NOT whitespace, then we check if theres another integer character after, if there is, then we append it to our StringBuilder. We also increment i and put our new integer/operator in the correct index of stringArr.

     This loops continues until nothing is left and gives us a parsed String[] array we can use in prefixOperation. 

     returns: String[]
   </details>

 * <details>
    <summary>postToPrefix method </summary>

    ### postToPrefix(String[] input)

     In order to convert a postfix form to prefix form (after being parsed by stringToArray), all we need to do is reverse the given array!

     Using pointers "low" and "high", we swap the beginning and end elements of an array, then increment "low" and decrement "high". We continue this until either low == high or low > high. This conditional covers both even and odd sized arrays.

     returns: String[]
   </details>

 * <details>
    <summary> runOperation</summary>

    ### runOperation(int a, int b, String operation)
     
     This method is run by prefixOperation and performs a given operation using switch-cases. It simply matches the operation string to its corresponding mathematical operation and performs it, returning its result.

     One important thing to note about this method is that this method and prefixOperation assumes and operates assuming "int a" has precedence over "int b". So we run "a / b" NOT "b / a". 
     Fortunately, the dual stack setup we have helps to keep operations and integers in order.

     This method also throws out an IllegalArgumentException if no valid operation is given. This is picked up in the main method as to not immediately halt the program.

     returns: int
   </details>

 * <details>
    <summary>main method</summary>

    ### main(String[] args)
     
     This method does a few things:

     1. Shows helpful information for users through the helpMenu() method. 

     2. Creates an infinite loop where users can either:
        1. input their pre/postfix expression
        2. enter "h" to get the helpMenu() menu again
        3. enter "q" to quit
    
     3. Reverses user input expression if its in postfix form to get prefix form. (Postfix form has an integer in the first index instead of an operator)

     4. Handles exceptions in case pre/postfix expression is invalid/not formatted correctly. It does this by wrapping calls to methods in a try-catch statement, assuming that any exception caused is easily fixed by just trying again. The ONLY exception to this is if the expression is not logically sound. While it can catch simple mistakes like "++", it can't catch mistakes such as "+ 45 + 60" (Extra operator). So, the prefixOperation method instead ends the program on the stop in that case. 

     returns: nothing (void)
   </details>

### Final Comments

This was a short but insightful project and I hope to expand on it or remake it in C++ once I start to learn it!
The Main.java file has plently of comments I left while working on it, with extensive planning notes towards the bottom of the file detailing my original idea and how it failed. 
I hope to write up more docs on the projects I do to get a better understanding of not just my own code, but also my thought process and how I can improve how I think instead of what I think!