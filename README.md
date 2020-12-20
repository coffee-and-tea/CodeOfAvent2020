# CodeOfAvent2020
Practice for https://adventofcode.com/2020/

Fun fact:
Repository name CodeOfAvent2020 is not a typo: I started my journey of first advent of code on year of 2020, also raising my first new born boy using lots of `Avent` brand things. More of a daddy joke here.

Day01:
2 sum and 3 sum questions, with 2 solutions:
- solution one: first sort the input then binary search for result, with 2 sum complexity is O(n*logn), 3 sum is O(n^2*logn)
- solution two use exact space without sorting: with 2 sum complexity is O(n), 3 sum is (n^2)

Day02:

String data validation, using stream function first question complexity of O(n), second question complexity of O(1)

Day03: 

Base graph traverse problem, boundary check required. Complexity O(n) n is number of lines in graph. 

Day04: 

String parsing and validation. Got a chance to practice builder pattern and reflection in builder. Complexity O(n).
Input processing is the fun part, where code implements 2 way of how to parse data in different lines together. 

Day05:

Binary conversion, part one is to convert values to binary and search max, complexity O(n). 
Part two: using an extra space of O(n), Drop value from set if it's present, find the max value in set is the answer. 
Complexity O(n); 


Day06:

Similar to day 04, parse input from multi-lines, then process with set. Complexity per group answer is O(n) n is number of answers.

Day07:

Input consists different rules, parsing rules then store into map to store.
Part one: determine whether a bag included in other ones, using an extra space of O(n), complexity of O(n) to determine.
Part two: how many bags inside one bag, perform a deep or width first search in input, complexity of O(n)

Day08:

Input to programming instructions, parse through input and simulate execution. Remind me of compiler construction class. 
Part one: use additional space to store execution records extra space O(n) n is line of code. Complexity: O(n)
Part two: Need to remember execution state of the code, using a deep first search. Complexity of O(n*n), space complexity O(n*n) to store executed instructions 
Is there a better solution with less complexity? 

Day09

Parsing input values, then valid input and finding input range.
Part one: find input not result of previous 25 input of 2 sum. Complexity O(n), extra space of 25 element and create a set while determine 2 sum. space O(1)
Part two: find sum range to the none valid element find in part one, complexity O(n), create linked list to store values and extra variable for sum, offer and poll on list.

Day10:

Graphical path problems.
Part one: longest path in solution, complexity O(n).
Part two: how many paths in total, extra space O(n) to store possible path from beginning to reach current node. Then at each node going back to sum all possible path can reach it. Complexity O(n)

Day11:

Graphical problems similar to image processing, data from one node requires calculation from other.
Part one: one time calculation requires node to consider all neighbor nodes, one pass complexity O(n*m), this can be highly parallelized to have better performance. extra space O(n*m)
Part one: same as part one, only neighbors are looking at each direction, so each node can takes O(max(n,m)) or O(n+m), overall it's O((m+n)*n*m), extra space(n*m)

Day12:

Coordinates and matrix transformation problem
Part one: perform moves from original point by different direction, trick part is calculate direction and how to model it. Complexity O(n) n is length of input file
Part two: perform moves base on a reference point, trick part is to calculate waypoint has to translate new coordinates Complexity O(n).

Day13:

Math prime number and common factors
Part one: calculate gap between arrival time and parse through to find min gap, complexity O(n)
Part two: search find a number `x` match bus `a1` and gap `g1`, then when match bus `a2` and gap `g2`, since bus are prime number, to reserve the gap needs to increament `a1*a2` everytime.
Complexity: O(n*m) m is the max prime bus number.

Day14:

Binary operations
Part one: update value base on mask, Complexity O(n)
Part two: update address base on mask, complexity O(n)

Day15:

Need to understand type of the question, this is linar progression?
Part one: calculate value base on stored index of last turn number showed and before last, for round 2020 complexity of O(n), n number of turns, space complexity O(n) to store index
Part two: same process but calculate more, wondering if there's a way to free up unused space.

Day16:

Modeling ticket and range, with validation
Part one: validate tickets base on input range, complexity O(m*n) m is number of tickets, n is number of fields. Space O(n) to store fields
Part two: determine the field base in ticket input complexity: O(m*n + n*nlogn), is there better way to do this?

Day17

Graphic in 3D and 4D.
Part one: propagate on 3D, Complexity O(m*n*c*c) m is width of input, n is length of input, c is the cycle.
Part two: propagate on 4D, Complexity O(m*n*c*c*c), how to improve this?

Day18

Similar to day08, parsing input and execute instructions, this time with priority.
Part one: equal priority, evaluate result left to right, for single line length of n complexity O(nlogn) parse logn times on n character
Part two: priority given to different operation, identify low level priority operation first, same complexity.

