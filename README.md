# longest-common-subsequence

Christopher R. Reyes ([@creyes](https://github.com/creyes17)) and Chrisantha Perera ([@cperera](https://github.com/cperera)) present the Reyes-Perera LCS Method for finding the longest common subsequence between two inputs in O(N logN) time.

Specifically, given:
* An input sequence A of size N
* An input sequence B of size M where M < N
* A function that can compare any elements of A and/or B in O(1) time

Returns a subsequence S such that:
* S is a subsequence of A 
* S is a subsequence of B
* There does not exist a different subsequence S' of both A and B such that the S' contains more elements than S

## Algorithm Overview

We'll use an example input of:
* A = [ 2 7 8 1 5 ]
* B = [ 8 1 2 6 4 ]

Expected longest common subsequence is [ 8 1 ]

1. Transform the inputs into sequences of tuples containing the value and the index
  * We'll represent them here as [value index]
  * A => [ [2 0] [7 1] [8 2] [1 3] [5 4] ]
  * B => [ [8 0] [1 1] [2 2] [6 3] [4 4] ]
  * O(N) time

2. Sort inputs by value
  * A => [ [1 3] [2 0] [5 4] [7 1] [8 2] ]
  * B => [ [1 1] [2 2] [4 4] [6 3] [8 0] ]
  * O(N logN) time

3. Combine matching values into a new sequence C
  1. Compare the first element of A (a) and B (b)
    * If equal, push [value indexA indexB] onto C and drop the first element of both A and B
    * Otherwise, find the index of the first element in the sequence with the smaller value that has value greater than or equal the larger value
      * Because both sequences are sorted by value, you can do this step in O(logN) time with binary search
  2. a = [1 3], b = [1, 1]
    * The values are equal, so C => [ [1 3 1] ] (value, index of element in A, index of element in B)
  4. a = [2 0], b = [2 2], C => [ [1 3 1] [2 0 2] ]
  5. a = [5 4], b = [4 4]
    * The values are different, so move b up in B until the first element with a greater than or equal value
  7. a = [5 4], b = [6 3]
  8. a = [7 1], b = [6 3]
  9. a = [7 1], b = [8 0]
  10. a = [8 2], b = [8 0], C => [ [1 3 1] [2 0 2] [8 2 0] ]
  11. Cumulatively, this is worst case O(N logN)

4. Sort C by index into A
  * C => [ [2 0 2] [8 2 0] [1 3 1] ]
  * O(N logN)

5. Find LIS by index into B
  * See algorithm below
  * mapped to Index into B: [2 0 1]
  * LIS => [0 1]
  * therefore LIS of C by index into B => [ [8 2 0] [1 3 1] ]
  * O(N logN)

6. Output value
  * Just map by value of previous step
  * [ [8 2 0] [1 3 1] ] => [ 8 1 ]
  * O(N)

## LIS algorithm overview

See other sites

For each element in input:
1. If the value is greater than any value yet encountered, append it to the end of a sequence S
2. Otherwise, find the smallest value in S that is greater than or equal to it. Replace that value with this element

Return S

TODO: Add example

## Installation

Download from http://example.com/FIXME.

## Usage

FIXME: explanation

    $ java -jar longest-common-subsequence-0.1.0-standalone.jar [args]

## Options

FIXME: listing of options this app accepts.

## Examples

...

### Bugs

...

### Might be Useful

## License

Copyright © 2017 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
