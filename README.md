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

