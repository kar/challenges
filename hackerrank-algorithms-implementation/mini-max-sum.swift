import Foundation


if let line = readLine() {
    let numbers = line.components(separatedBy: " ")
    
    var sum: Int64 = 0
    var min: Int64 = 1_000_000_000
    var max: Int64 = 0

    for i in numbers {
        let num = Int64(i)!
        sum += num
        if num <= min {
            min = num
        }
        if num >= max {
            max = num
        }
    }

    print("\(sum - max) \(sum - min)")
}
