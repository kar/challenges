package main

import "fmt"
import "sort"

func main() {
    a1 := []int {1,2,4,5,7,4}
    a2 := []int {3,4,6,7,8}
    // 1, 2, 3, 5, 6, 8

    fmt.Println(findUnique(a1, a2))
}

func findUnique(a1 []int, a2 []int) []int {
    sort.Ints(a1)
    sort.Ints(a2)
    a3 := make([]int, 0)
    p := -1

    for i, j := 0, 0;; {
        if i >= len(a1) {
            if j >= len(a2) {
                return a3;
            } else {
                a3 = append(a3, a2[j])
                j++
            }
        } else if a1[i] == a2[j] {4
            i++
            j++
            p = a1[i]
        } else if a1[i] < a2[j] {
            if a1[i] == p {
                i++
            } else {
                a3 = append(a3, a1[i])
                p = a1[i]
                i++
            }
        } else if a1[i] > a2[j] {
            if a2[j] == p {
                j++
            } else {
                a3 = append(a3, a2[j])
                p = a2[j]
                j++
            }
        }
    }
}
