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

The overall idea is to reduce the LCS problem into a Longest Increasing Subsequence (LIS) problem which we already know how to solve in O(N logN) time

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

### Proof of correctness

Need to show that for a result sequence R of our algorithm:
1) R is a subsequence of A
2) R is a subsequence of B
3) There doesn't exist a sequence R' such that:
  * R' is a subsequence of A
  * R' is a subsequence of B
  * R' has more elements than R

In order for R to be a subsequence of A, all of its elements must be in A and they must appear in the same order in R as in A
* We know that all elements of R are in A because in step 3, we only took elements whose values were in both A and B
* We know that the elements of R are in the same order as in A because we sorted them by their index into A in step 4

In order for R to be a subsequence of B, the same properties must hold
* We know that all elements of R are in B because in step 3, we only took elements whose values were in both A and B
* By finding the longest increasing subsequence of indices into B, we guaranteed that our elements would be in the same order as they are in B
  * If there was an element R[i] such that for some positive k, there is no R[i+k] which comes after some R[i] in B, then the index into B of R[i+k] couldn't be greater than the index into B of R[i]. Therefore the LIS from step 5 is not actually an increasing subsequence.

Assume there exists a sequence R' described above. Let e' be an element in R' that is not in R.
* e' must be a member of both A and B. Therefore, it will be included in C in step 3 above
  * If e' was not a member of A, then R' could not be a subsequence of A. Same with B
* TODO: Finish proof. 
  * Basically, by sorting by index into A in step 4, we have all elements of A that are also in B ordered by A
  * Then, the LIS by index B maintains the order in A (otherwise it's not a subsequence and not an LIS)
     * It's also a longest possible subsequence (again, otherwise it's not an LIS)
  * If there were an R', then it would have to be longer than the longest increasing subsequence by index into B which is a contradiction
  * I'm still trying to think of the best way to put that into formal logic. Right now it sounds kind of circular to me

Eventually I'll try to write a proof here in more formal language and notation with LaTex

## LIS algorithm overview

Given a single input sequence, I, find a subsequence S of I such that:
* there does not exist a subsequence S' of I with more elements than S and
* S[i] < S[i+1] for all 0 <= i < |S|

S is a subsequence of I if you can remove some number of elements from I without reordering it to yield S

You can find O(N logN) algorithms for LIS many places. [Here's one of many examples](http://www.techiedelight.com/longest-increasing-subsequence/)

Create a sequence S containing the first element in the input. Then, for each remaining element in input:
1. If the value is greater than the last value in S, append it to the end of S
2. Otherwise, find the smallest value in S that is greater than or equal to it. Replace that value with this element

So if you had the input [2 0 1] for example:
* Let e be the element we're examining from the input
* Initialize S = [2]
* S = [2], e = 0
  * e is less than the last value in S
  * So find the smallest value in S that is greater than or equal to it: S[0] = 2
  * And replace it with this element's value
  * S => [0]
* S = [0], e = 1
  * e is greater than the last value in S. Append it
  * S => [0 1]
* We've run out of elements in the input, so return S

I leave the proof of correctness for LIS as an exercise to the reader. 
I'm convinced this algorithm is not correct, but even if it's not, then just replace with your favorite O(N logN) solution to LIS that is actually correct instead.

## Installation

You'll need the [leiningen tool](https://github.com/technomancy/leiningen) installed to build yourself.

Clone this repository. I'll reference the root directory of the repository below as $REPO.

Then from $REPO, run `lein compile; lein uberjar`

If you have the [lein bin-plus](https://github.com/BrunoBonacci/lein-binplus) plugin, you can run `lein bin` from the root directory to create a bash executable.
* Then run the executable with `target/default/longest-common-subsequence "sequenceA" "sequenceB"`

As of version 1.0.0, it only accepts strings as inputs, but I'll update in the near future to accept any input as long as there's a comparison function supplied.

## Usage

TODO: provide better usage documentation

let $REPO be the root directory of the repository

You can run the compiled jar file from $REPO with 

    $ java -jar target/uberjar/longest-common-subsequence-1.0.0-standalone.jar sequenceA sequenceB

You can run the executable from $REPO with

    $ target/default/longest-common-subsequence sequenceA sequenceB

sequenceA and sequenceB should both be strings of alphanumeric characters. Each character will be treated as one element

## Options

Currently does not accept any options

## Examples

    $ java -jar target/uberjar/longest-common-subsequence-1.0.0-standalone.jar "sequenceA" "sequenceB"
    sequence
    $ java -jar longest-common-subsequence-1.0.0-standalone.jar "27815" "81264"
    81
    $ target/default/longest-common-subsequence "sequenceA" "sequenceB"
    sequence
    $ target/default/longest-common-subsequence "27815" "81264"
    81

## See Also

TODO: Link to various papers discussing runtime of LCS algorithms
TODO: Link to various other implementations of LIS

## License

Copyright &copy; 2017 Christopher R Reyes

This project is licensed under the [GNU General Public License v3.0][license].

[license]: http://www.gnu.org/licenses/gpl-3.0.txt
