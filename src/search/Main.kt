package search

import java.io.File

fun main(args: Array<String>) {
    val peoplesList = File(args[1]).readLines().toMutableList()
    val invertedIndexMap: MutableMap<String, MutableSet<Int>> = makeInvertedIndexMap(peoplesList)
    var exit: Boolean = true
    while (exit) {
        println("=== Menu ===")
        println("1. Find a person")
        println("2. Print all people")
        println("0. Exit")
        when (readLine()!!.toInt()) {
            0 -> {
                println("Bye")
                exit = false
            }
            1 -> {
                println("Select a matching strategy: ALL, ANY, NONE")
                val searchingStrategy = readLine()!!
                println("Enter a name or email to search all suitable people.")
                val searchData = readLine()!!.lowercase().split(" ")
                lateinit var indexSet: Set<Int>
                when (searchingStrategy) {
                    "ALL" -> {
                        indexSet = (0 until invertedIndexMap.size).toSet()
                        for (i in searchData) {
                            if (i !in invertedIndexMap) {
                                indexSet = emptySet()
                                break
                            }
                            indexSet = indexSet.intersect(invertedIndexMap.getValue(i))
                        }

                        printPeople(peoplesList, indexSet)
                    }
                    "ANY" -> {
                        indexSet = emptySet<Int>()
                        for (i in searchData) {
                            if (i !in invertedIndexMap) continue
                            indexSet = indexSet.union(invertedIndexMap.getValue(i))
                        }
                        printPeople(peoplesList, indexSet)
                    }
                    "NONE" -> {
                        indexSet = emptySet<Int>()

                        for (i in searchData) {
                            if (i !in invertedIndexMap) continue
                            indexSet = indexSet.union(invertedIndexMap.getValue(i))
                        }
                        val noneResult = (0 until peoplesList.size).toMutableSet()
                        noneResult.removeAll(indexSet)
                        indexSet = noneResult
                        printPeople(peoplesList, indexSet)

                    }
                    else -> {
                        println("Unknown strategy")
                        return
                    }
                }
            }
            2 -> {
                printAllPeople(peoplesList)
            }
            else -> {
                println("Incorrect option! Try again.")
            }
        }
    }
}


fun makeInvertedIndexMap(peoplesList: MutableList<String>): MutableMap<String, MutableSet<Int>> {
    var indexList = mutableMapOf<String, MutableSet<Int>>()
    for (i in peoplesList) {
        for (j in i.split(" ")) {
            if (indexList.containsKey(j.lowercase())) {
                indexList[j.lowercase()]?.add(peoplesList.indexOf(i))
            } else {
                indexList[j.lowercase()] = mutableSetOf<Int>(peoplesList.indexOf(i))
            }
        }
    }
    return indexList
}


fun printPeople(peoplesList: MutableList<String>, indexSet: Set<Int>) {
    for (i in indexSet) {
        println(peoplesList[i])
    }
}

fun printAllPeople(peoplesList: MutableList<String>) {
    println("=== List of people ===")
    for (i in peoplesList) {
        println(i)
    }
}




