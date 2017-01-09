// Sherlock and Cost
//
// The key observation is that each Ai has to be either 1 or Bi
// (values in between do not lead to the maximum sum), so that
// the result subsequence will have a zigzag pattern:
//
// 1 B2 1 B4 1 B6 ... (or B1 1 B3 1 B5 ...).
//
// In some cases, selecting same value twice next to each other
// may lead to a higher sum (lets call it the jump):
//
// B1 1 1 B4 1 B5 ... (or B1 1 B3 B3 1 B6 ...).
//
// The sums matrix keeps partial sums for each element in the 
// subsequence, starting either with B1 (row 0) or 1 (row 1).
// For each cell, we pick the maximum partial sum between
// the zigzag pattern or the jump. You can imagine each cell
// corresponding to the following subsequence pattern:
//
//     0   1       2
// 0 | - | B1  1 | B1  1  1  or 1 B2 1  | ...
// 1 | - | 1  B2 | B1  1  B3 or 1 B2 B2 | ...
//
// Note: This solution scores at 31.25/50 because some test
// cases time out (and I am not sure why).

import Foundation

func solve(n: Int, b: [Int]) -> Int {
    var sums = [[Int]](repeating: [Int](repeating: 0, count: 2), count: n)
    
    for i in 1..<n {
        var a = sums[i - 1][0]
        var y = sums[i - 1][1] + b[i - 1] - 1
        sums[i][0] = a
        if a < y {
            sums[i][0] = y
        }
        
        a = sums[i - 1][0] + b[i] - 1
        y = sums[i - 1][1]
        sums[i][1] = a
        if a < y {
            sums[i][1] = y
        }
    }
    
    return max(sums[n - 1][0], sums[n - 1][1])
}

if let cases = Int(readLine()!) {
    for c in 0..<cases {
        if let n = Int(readLine()!) {
            let b = readLine()!.components(separatedBy: " ").map { num in Int(num)! }
            let solution = solve(n: n, b: b)
            print(solution)
        }
    }
}

