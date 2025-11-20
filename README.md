# Simple-Java-Prefix-Expression-interpreter
This short program is able to evaluate basic arithmetic expressions in Prefix or Postfix form. It can maintain operation order and is implemented using the Stack data structure.

Example:
+ + 45 2 10 = 45 + 2 + 10
+ * 5 2 3 = (5 * 2) + 3
+ + 6 * 3 2 7 = 6 + (3 * 2) + 7

## Implementation 

For this project, I used the Stack data structure, A FIFO (First-in First-out) structure that in this case is perfect for the nuances that come with operator precedence. Each subsection here describes descriptions of the main methods I used and how I designed them.
<details>
    <summary> Click me </summary>
    <br></br>
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
    popped --> [*]           [5] <-- Will get popped
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

    ### stringToArray(String input)

    This method takes an assumed prefix form expression and break it up into seperate elements to be processed by prefixOperation
</details>
