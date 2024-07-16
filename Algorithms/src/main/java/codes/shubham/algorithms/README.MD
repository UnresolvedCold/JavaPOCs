# 2048: Next balanced numerically balanced number

## Approach 1 - Generate these numbers in sequence and check if they are balanced and numerically greater than n

### Generator

Smallest balanced number is 1.

Next is 22.

Then comes 122 and 333.

Next is 1333, 4444.

Next is 14444, 22333, 55555.

#### Which digits will come in the next such number if digit length is l?

| digit length | numbers                        |
|--------------|--------------------------------|
| 1            | 1                              |
| 2            | 22                             |
| 3            | 122, 333                       |
| 4            | 1333, 4444                     |
| 5            | 22333, 14444, 55555            |
| 6            | 122333, 224444, 155555, 666666 |

A balanced number with l digits will surely have lll... l times as a number.
Apart from that, it can potentially have a (l-k) digit number attached to (k) digit number.
And it can also have any combination of those digits.

This means, to create a balanced number of length l, 
we can explore all the ways in which l can be formed by adding m numbers.

Example, 

4 can be formed by adding `0 + 4`, `1 + 3`, `2 + 2`.

`2 + 2` can be ignored as it will not be balanced.

The balanced numbers can be 4444 and 1333, 3133, 3313, 3331.

The problem here it to find the digits that will be involved.

We can create a function that can compute the list of digits 
required to create a balanced number of l digits `findBalancedDigits(l): List<List<Integer>> `.

Now, we can iterate over each of the elements and generate all number combinations using the digits.
Let's create a function `generateBalancedNumbers(l): List<Integer>`.

Then we can iterate over all the combinations and find the least number greater than `n` that is a balanced number.
This will be our answer.
