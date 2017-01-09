package main

import "fmt"

func main() {
    hexstring := "01FF00101ACC"

    size := len(hexstring)
    // corner cases
    result := make([]byte, size / 2)

    for i := 0; i < size / 2; i++ {
        digit1 := toDigit(hexstring[i * 2])
        digit2 := toDigit(hexstring[i * 2 + 1])
        sum := digit1 * 16 + digit2
        fmt.Printf("%d ", sum)
        result[i] = sum
    }
}

func toDigit(hc byte) byte {
    if hc >= 48 && hc < 57 {
        return hc - 48
    } else {
        return hc - 65 + 10
    }
}