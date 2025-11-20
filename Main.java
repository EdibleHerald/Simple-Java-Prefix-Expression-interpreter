import java.util.Scanner;
import java.util.EmptyStackException;
import java.util.Stack;


public class Main {
    public static void main(String[] args) {
        Scanner scanObj = new Scanner(System.in);
        String operation;
        int sum;
        helpMenu();

        while(true){
            System.out.println("Enter expression below (enter h to see more info) :");
            operation = getUserInput(scanObj);

            if(operation.equalsIgnoreCase("h")){
                helpMenu();
            } else if (operation.equalsIgnoreCase("q")) {
                System.out.println("Quitting program...");
                System.exit(1);
            }else{
                try{
                    String[] arr = stringToArray(operation);
                    if(!checkForOperation( String.valueOf(operation.charAt(0)) ) ){ // If no operations are listed first, suggesting Postfix notation
                        postToPrefix(arr);
                    }
                    sum = prefixOperation(arr);
                    System.out.println("Expression given was: " + operation);
                    System.out.println("Result of expression: " + sum + "\n");
                }catch(Exception e){
                    // Catches not splitting operators. Other errors will throw an exception and exit from prefixOperation
                    System.out.println("Invalid expression format");
                }
            }
        }
    }

    public static void helpMenu(){
        System.out.println("This program asks calculates a given numeric expression");
        System.out.println("Expression must be prefix or postfix notation. Positive integers only");
        System.out.println("Operations available are:");
        System.out.println("Addition + ");
        System.out.println("Subtraction -");
        System.out.println("Multiplication *");
        System.out.println("Division /");
        System.out.println("Remainder %");
        System.out.println("Each operator must have two integers to work on, else program WILL end.");
        System.out.println("Each expression must have whitespace between numbers and operators");
        System.out.println("Example: + 45 73");
        System.out.println("\nEnter 'q' at any time to quit program");
    }


    /*
        Uses two stacks to split up int and operators.
        Then runs operations on said numbers when
    */
    public static int prefixOperation(String[] arr){
        // Dual Stack test implementation
        // + + A * B C D
        // + + 6 * 3 2 7 == 6 + (3 * 2) + 7 = 19
        Stack<String> mainStack = new Stack<>();
        Stack<String> secondStack = new Stack<>();
        int sum=0;

        for(String s : arr){
            mainStack.push(s);
        }

        while(!mainStack.isEmpty()){
            if(checkForOperation(mainStack.peek())) {
                /*
                    1.) Take two numbers from secondStack
                    2.) Parse strings into int
                    3.) run operation and add result to sum
                    Example Stack (top to bottom):
                    mainStack --> 3 2 5 * -
                    (After adding until reaching operator)
                    mainStack --> * -
                    secondStack --> 5 2 3

                    Now pop "5" and "2" off second stack to run "*" operator on them.
                */

                String operator = null;
                int b = 0;
                int a = 0;

                try {
                    a = Integer.parseInt(secondStack.pop());
                    b = Integer.parseInt(secondStack.pop());
                    operator = mainStack.pop();
                } catch (EmptyStackException e) {
                    System.err.println("Operation incorrectly setup");
                    System.out.println("Are you sure each operator has two integers to work on?");
                    System.exit(1);
                }
                sum = runOperation(a, b, operator);
                secondStack.push(String.valueOf(sum));
            }else{
                // Pushes number to second stack for later use
                secondStack.push(mainStack.pop());
            }
        }
        return sum;
    }

    /*
        This is a method for checking is a stack item is an operator or not.
    */
    public static boolean checkForOperation(String item){
        final String[] Operators = {"+","-","/","*","%"};

        for(String s : Operators){
            if(item.equals(s)){
                return true;
            }
        }
        return false;
    }

    public static int runOperation(int a,int b, String operation){
        // "a" is always in front, example : a + b, a - b, a * b, a / b, a % b
        // Check for null operation
        int result;
        switch(operation){
            case "+":
                result = (a + b);
                break;
            case "-":
                result = (a - b);
                break;
            case "*":
                result = (a * b);
                break;
            case "/":
                result = (a / b);
                break;
            case "%":
                result = (a % b);
                break;
            default:
                System.out.println("No valid operator given. If null, it indicates missing operator/too many integers given");
                throw new IllegalArgumentException("Incorrect operation '" + operation + "' was thrown");
        }
        return result;
    }

    /*
        My program hinges on PrefixOperation receiving a valid string[] array
        So these three methods below focus on turning a string into a string[] array
        or converting PostFix to PreFix.

        I realized far too late that stringToArray didn't parse multi digit integers,
        it originally expected single digit integers.

        Because this is last second change, the current implementation of stringToArray is a little messy but works.
    */

    public static String[] stringToArray(String input){
        // First we need to trim input so we don't get the wrong array size
        input = input.trim();
        // Use whitespace amount to calculate appropriate array size.
        int whitespace = countStringWhitespace(input);
        String[] stringArr = new String[whitespace+1];

        // Temporary variables to hold characters and build number strings respectively.
        char currChar;
        StringBuilder stringBuilder = new StringBuilder();

        // "i" tracks the new stringArr as we fill it out
        int i = -1;

        // Go through each character, adding to stringArr if not whitespace
        for(int j = 0;j<input.length();j++){
            currChar = input.charAt(j);
            stringBuilder.append(currChar);
            if(currChar != ' '){
                i++;

                // While there's a next number in string (Conditional below excludes operators and whitespace)
                // add to stringbuilder (to build multi digit integers), then increment j (to avoid duplicates)
                // Note: Since the first conditional is (j+1) < input.length() and triggers first,
                // I can avoid the IndexOutOfBoundsException I would've eventually gotten with input.charAt(j+1) != ' '
                while((j + 1) < input.length() && !checkForOperation(String.valueOf(currChar)) && input.charAt(j+1) != ' '){
                    stringBuilder.append(input.charAt(j+1));
                    j++;
                }
                stringArr[i] = String.valueOf(stringBuilder.toString());
            }

            stringBuilder.delete(0,stringBuilder.length());
        }

        return stringArr;
    }

    public static int countStringWhitespace(String input){
        int whitespace = 0;
        for(int i = 0; i<input.length();i++){
            if(input.charAt(i) == ' '){
                whitespace +=1;
            }
        }

        return whitespace;
    }

    public static void postToPrefix(String[] input){
        // All this method does is reverse the array
        // Preferably modifies the array instead of creating a new one
        // Technically I could just use a stack, add elements, then modify array based on popped elements.
        // But I want to stick with pointers, so I can practice.
        int a = 0;
        int b = input.length - 1;
        String temp;
        while(a<=b){
            temp = input[a];
            input[a] = input[b];
            input[b] = temp;
            a++;
            b--;
        }
    }

    /*
        Methods for getting user input
    */

    public static String getUserInput(Scanner scanObj){
        String input;
        input = scanObj.nextLine();
        while(input.equals(" ")){
            System.out.println("Please enter operation (in prefix or postfix)");
            input = scanObj.nextLine();
        }
        return input;
    }

    /*
     Notes:
        I stuck to primitive arrays for this instead of ArrayLists just to get better at using primitive arrays.
        And technically, primitives are better for CPU caching even if it doesn't matter much here.

        PrefixOperation Idea 1 (Abandoned):

        Prefix - read right to left
        We can keep 3 "pointers" for tracking:
            1.) Leader pointer: looks for operators
            2.) Follower pointer
            3.) Follower pointer: These 2 keep variables BEFORE the operator so we know which variables to run operators on
        Example: +*ABC
        We read right to left, as we get to "*", we have A and B in follow pointers. We then perform the operation.
        We need to replace "*AB" and then re-read the expression to add C as well.

        This very much mirrors a stack in a way, so I'll try to make that structure work here.
        Example: -*ABC, we can add items to this stack in reverse
        Stack (LIFO):
        Assume that B and C have been popped off the stack and stored in the follower pointers.

                C <-- Follower pointer   Transition       C <-- Popped (Added to queue)
                B <-- Follower Pointer      -->           B <-- Follower Pointer
                A <-- Leader Pointer                      A <-- Follower Pointer
                *                                         * <-- Leader Pointer
                -                                         -

        We go down the stack, then pop variables to keep them stored.
        If any variables need to be rid of, then we can add them to a Queue (FIFO) for later.
        Let's run the first operation:
                BA* --> B * A = D (Would assign to A, but keeping it a different letter for simplicity)

        Now, pop B,A, and * off the stack permanently.
        We now add D in first (to maintain order in case it matters like for subtraction),
        then we empty our queue onto the stack (which has C)

                C <-- Follower Pointer   Transition
                D <-- Follower Pointer      -->         D - C = answer!
                - <-- Leader Pointer
              (-DC)
         We determine that we have an answer if the stack is empty after an operation.

         // Pretend this is a Cool Algorithm
         if(stack.isEmpty()){
            return answer;
         }else{
            continue;
         }

        Constraints:
            1.) All inputs are integers and non-negative
            2.) Operations allowed: Addition, Subtraction, Division, Multiplication, Modulus

        PrefixOperation Idea 2 (what I went with):

        After trying to implement idea 1, I realized that trying to implement pointers in a stack was very difficult.
        I liked the idea of using a stack and a queue to track numbers, but eventually I realized that using two stacks
        would be much easier.
        Example:
         main stack   second stack
            [3]           [ ]
            [5]           [ ]
            [2]           [ ]
            [*]           [ ]
            [-]           [ ]
        Move numbers over to second stack while we keep the operations in the first stack.
         main stack   second stack
            [ ]           [ ]
            [ ]           [ ]
            [ ]           [2]
            [*]           [5]
            [-]           [3]
         1.) Pop "*" off main stack
         2.) Pop two numbers off second stack (2 & 5)
         3.) Use switch-case to compute operation (5 * 2)
         4.) Add result to second stack (10)
         5.) continue
         main stack   second stack
            [ ]           [ ]
            [ ]           [ ]
            [ ]           [ ]
            [ ]           [10]
            [-]           [3]
          10 - 3 = 7 <-- Final answer!

          Was overall a lot more intuitive and easier to implement than my first idea.
          However, this implementation technically turns Prefix into Postfix by reversing the order when adding it
          to the main stack.
          So if I wanted to implement Postfix support, I'd just reverse the operation array given if the first entry
          in the array was a number instead of an operation.

          "Intended" idea:
          After I finished my implementation, I googled and found out that the common implementation is to
          just directly read the already parsed and ordered String[] array. In hindsight this makes far more sense
          rather than creating an unnecessary "mainStack". I'll keep what I currently have though, since it works
          and is what I actually came up with myself.
    */
}