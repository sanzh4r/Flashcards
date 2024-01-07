package flashcards
import java.io.File
import java.util.Collections.max
import kotlin.random.Random
import kotlin.system.exitProcess


// A function that saves all input and output into a mutable list
fun addToLog(mutableLog: MutableList<Any>, line: Any) {
    mutableLog.add(line.toString())
}

// Adjusting the println() function so it returns the same value that it outputs
fun println(message: Any): Any {
    kotlin.io.println(message)
    return message
}

//*********** THE "MENU" FUNCTION ***********//
fun menu(numOfCards: MutableMap<String, String>, mutableLog: MutableList<Any>, mistakesLog: MutableMap<String, Int>, importFile: String, importFlag: Int, exportFile: String, exportFlag: Int) {

    // Checks whether the importFlag is 1
    if (importFlag == 1) {
        import(numOfCards, mutableLog, mistakesLog, importFile, importFlag, exportFile, exportFlag)
    }

        addToLog(mutableLog, println("Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):"))
        val input: String = readln().toString()
        addToLog(mutableLog, input)
        when (input) {
            "add" -> add(numOfCards, mutableLog, mistakesLog, importFile, importFlag, exportFile, exportFlag)
            "remove" -> remove(numOfCards, mutableLog, mistakesLog, importFile, importFlag, exportFile, exportFlag)
            "import" -> import(numOfCards, mutableLog, mistakesLog, importFile, importFlag, exportFile, exportFlag)
            "export" -> export(numOfCards, mutableLog, mistakesLog, importFile, importFlag, exportFile, exportFlag)
            "ask" -> ask(numOfCards, mutableLog, mistakesLog, importFile, importFlag, exportFile, exportFlag)
            "exit" -> exit(numOfCards, mutableLog, mistakesLog, importFile, importFlag, exportFile, exportFlag)
            "log" -> log(numOfCards, mutableLog, mistakesLog, importFile, importFlag, exportFile, exportFlag)
            "hardest card" -> mistakes(numOfCards, mutableLog, mistakesLog, importFile, importFlag, exportFile, exportFlag)
            "reset stats" -> resetStats(numOfCards, mutableLog, mistakesLog, importFile, importFlag, exportFile, exportFlag)
            else -> menu(numOfCards, mutableLog, mistakesLog, importFile, importFlag, exportFile, exportFlag)
        }
}

//*********** THE "ADD" FUNCTION ***********//
fun add(numOfCards: MutableMap<String, String>, mutableLog: MutableList<Any>, mistakesLog: MutableMap<String, Int>, importFile: String, importFlag: Int, exportFile: String, exportFlag: Int) {
    addToLog(mutableLog, println("The card:"))
    val term = readln().toString() // Asking the user for the term of the flashcard
    addToLog(mutableLog, term)

    // Checking whether the term of the flashcard already exists and if yes - the loop is repeated indefinitely
    // until the user provides a unique term.
    if (numOfCards.containsKey(term)) {
        addToLog(mutableLog, println("The card \"$term\" already exists."))
        menu(numOfCards, mutableLog, mistakesLog, importFile, importFlag, exportFile, exportFlag)
    } else {
        // Asking the user for the definition of the flashcard
        addToLog(mutableLog, println("The definition of the card:"))
        val definition = readln().toString()
        addToLog(mutableLog, definition)
        // Checking whether the definition of the flashcard already exists and if yes - the loop is repeated indefinitely
        // until the user provides a unique definition.
        if (numOfCards.containsValue(definition)) {
            addToLog(mutableLog, println("The definition \"$definition\" already exists."))
            menu(numOfCards, mutableLog, mistakesLog, importFile, importFlag, exportFile, exportFlag)
        } else {
            // Putting the key with the value into the map
            numOfCards[term] = definition
            addToLog(mutableLog, println("The pair (\"$term\":\"$definition\") has been added."))
            menu(numOfCards, mutableLog, mistakesLog, importFile, importFlag, exportFile, exportFlag) // Invoking the menu function again
        }
    }
}

//*********** THE "ASK" FUNCTION ***********//
fun ask(numOfCards: MutableMap<String, String>, mutableLog: MutableList<Any>, mistakesLog: MutableMap<String, Int>, importFile: String, importFlag: Int, exportFile: String, exportFlag: Int) {

    // Creates a new map with reversed elements where keys and values switch places.
    // This is necessary to retrieve a correct term in the future.
    val reversedNumOfCards = numOfCards.entries.associateBy({ it.value }, { it.key })

    // Converting the numOfCards.keys into a List array
    val theListOfTerms: List<String> = numOfCards.keys.toList()

    // Program asks the user how many times to ask and checks whether the amount of questions
    // is not greater than the total amount of flashcards. If the input from the user is greater,
    // the program will ask for a new number that is equal or less than the total amount of cards.
    addToLog(mutableLog, println("How many times to ask?"))
    var timesAsk = readln()
    addToLog(mutableLog, timesAsk)
    while (timesAsk.toIntOrNull() == null || timesAsk.toInt() > numOfCards.size) {
        addToLog(mutableLog, println("The total amount of flashcards is: " + numOfCards.size + "."))
        addToLog(mutableLog, println("Please input the number that is either lower or equal to " + numOfCards.size + "."))
        timesAsk = readln()
        addToLog(mutableLog, timesAsk)
    }

    // Asking the user for answers and checks whether they are correct
    repeat(timesAsk.toInt()) {

        // Depending on the amount of flashcards, the (random) number is generated
        var randomCard = 0
        if (numOfCards.size > 1) {
            // If there are more than 1 flashcard, then the random number is generated.
            randomCard = Random.nextInt(1, numOfCards.size) // Generating a pseudorandom number.
        } // If there is only 1 flashcard, then no number is generated and the only existing element is retrieved.

            // Retrieving the term given the (generated random) number.
            val theTerm = theListOfTerms[randomCard]
            // Retrieving the definition of a given term.
            val definition = numOfCards[theTerm]

        // Asking the user for a definition of a given card
        addToLog(mutableLog, println("Print the definition of \"$theTerm\":"))
        val userDefinition = readln().toString()
        addToLog(mutableLog, userDefinition)

        if (userDefinition == definition) {
            addToLog(mutableLog, println("Correct!"))
        } else {
            if (userDefinition in numOfCards.values) {
                // The term that matches the user's answer is being retrieved from the new map with reversed elements.
                addToLog(mutableLog,
                        println(
                    "Wrong. The right answer is \"$definition\", but your definition is correct for \"" + reversedNumOfCards.get(
                        userDefinition
                    ) + "\"."
                ))
                // Retrieves the current amount of mistakes for a given card
                val currentAmountOfMistakes = mistakesLog.getOrDefault(theTerm, 0)
                // Adding the term to the mutable list with all mistaken cards and adds up one mistake
                mistakesLog[theTerm] = currentAmountOfMistakes + 1
            } else {
                addToLog(mutableLog, println("Wrong. The right answer is \"$definition\"."))
                // Retrieves the current amount of mistakes for a given card
                val currentAmountOfMistakes = mistakesLog.getOrDefault(theTerm, 0)
                // Adding the term to the mutable list with all mistaken cards and adds up one mistake
                mistakesLog[theTerm] = currentAmountOfMistakes + 1
            }
        }
    }
    //Invoking the menu function again
    menu(numOfCards, mutableLog, mistakesLog, importFile, importFlag, exportFile, exportFlag)
}

//*********** THE "REMOVE" FUNCTION ***********//
fun remove(numOfCards: MutableMap<String, String>, mutableLog: MutableList<Any>, mistakesLog: MutableMap<String, Int>, importFile: String, importFlag: Int, exportFile: String, exportFlag: Int) {
    // The program asks user for a term of the card to remove from the list
    addToLog(mutableLog, println("Which card?"))
    val removeCard = readln().toString()
    addToLog(mutableLog, removeCard)

    // Checks whether the key (term) exists in the numOfCards map and then either removes the pair with it
    // or displays an error message
    if (numOfCards.contains(removeCard)) {
        numOfCards.remove(removeCard)
        addToLog(mutableLog, println("The card has been removed."))
        if (mistakesLog.contains(removeCard)) {
            mistakesLog.remove(removeCard)
        }
    } else {
        addToLog( mutableLog, println("Can't remove \"$removeCard\": there is no such card."))
    }
    // Invoking the menu function again
    menu(numOfCards, mutableLog, mistakesLog, importFile, importFlag, exportFile, exportFlag)
}

//*********** THE "EXPORT" FUNCTION ***********//
fun export(numOfCards: MutableMap<String, String>, mutableLog: MutableList<Any>, mistakesLog: MutableMap<String, Int>, importFile: String, importFlag: Int, exportFile: String, exportFlag: Int) {
    // Stores the current size of numOfCards map
    val sizeNumOfCards = numOfCards.size

    // Creates a variable with a filename for export
    var fileNameExport = ""

    if (exportFlag == 2) {
        fileNameExport = exportFile
    } else {
        // Reading a user input with the file name
        addToLog(mutableLog, println("File name:"))
        fileNameExport = readln().toString()
        addToLog(mutableLog, fileNameExport)
    }

    // Asking the system for a working directory of the program
    val savingDirectory = System.getProperty("user.dir")
    // Creating a string with the absolute file path
    val savingPath = savingDirectory + File.separator + fileNameExport

    // Creating a mutable set with all the pairs (key, value) of terms and definitions.
    // The number of mistakes by card is saved as a third element after "=".
    val savingNumOfCards = mutableSetOf<String>()
        for ((k, v) in numOfCards) {
                savingNumOfCards.add(k + "=" + v + "=" + mistakesLog.getOrDefault(k, 0) + "\n")
    }
    // Turning the set into a String and writing it into a file
    File(savingPath).writeText(savingNumOfCards.joinToString("", "", ""))

    if (exportFlag == 2) {
        println("Bye bye!")

        // Writing the string with all key-value pair to the file
        addToLog(mutableLog, println("$sizeNumOfCards cards have been saved."))

        exitProcess(0)
    } else {
        // Writing the string with all key-value pair to the file
        addToLog(mutableLog, println("$sizeNumOfCards cards have been saved."))

        // Invoking the menu function again
        menu(numOfCards, mutableLog, mistakesLog, importFile, importFlag, exportFile, exportFlag)
    }
}

//*********** THE "IMPORT" FUNCTION ***********//
fun import(numOfCards: MutableMap<String, String>, mutableLog: MutableList<Any>, mistakesLog: MutableMap<String, Int>, importFile: String, importFlag: Int, exportFile: String, exportFlag: Int) {
    // Asking the system for a working directory of the program
    val importingDirectory = System.getProperty("user.dir")

    // Initiates the variable to start counting the number of cards imported
    var cardsImported = 0
    // Creates a variable for a file name
    var fileNameImport = ""

    // In case an argument -import is sent from the main() function
    if (importFlag == 1) {
        fileNameImport = importFile
    } else {
        // Otherwise asking for a user input
        addToLog(mutableLog, println("File name:"))
        fileNameImport = readln().toString()
        addToLog(mutableLog, fileNameImport)
    }

        // Creating a string with the absolute file path and a variable with the file
        val importingNumOfCards = importingDirectory + File.separator + fileNameImport
        val importedFile = File(importingNumOfCards)

        // Checks whether the file exists
        if (importedFile.exists()) {
            // Splitting each line in file in two parts and adding them as keys and values to numOfCards map
            val importedNumOfCards = importedFile.forEachLine {
                // Uses split() with a delimiter "="
                val splitPair = it.split("=")
                // Creates a variable for the first element (term) in the line
                val term = splitPair[0]
                // Creates a variable for the second element (definition) in the line
                val definition = splitPair[1]
                // Creates a variable for the third element (number of mistakes) in the line
                val mistakes = splitPair[2]
                // Adds the term and definition to the numOfCards map
                numOfCards += mapOf(term to definition)
                // Adding up one card to the variable with the number of imported cards
                ++cardsImported
                // Adds the term and the number of mistakes to the mistakesLog map
                mistakesLog[term] = mistakes.toInt()
            }
            addToLog(mutableLog, println("$cardsImported cards have been loaded."))
            // Invoking the menu function again
            menu(numOfCards, mutableLog, mistakesLog, importFile, 0, exportFile, exportFlag)
        } else {
            addToLog(mutableLog, println("File not found."))
            // Invoking the menu function again
            menu(numOfCards, mutableLog, mistakesLog, importFile, 0, exportFile, exportFlag)
        }
    }

//*********** THE "LOG" FUNCTION ***********//
fun log(numOfCards: MutableMap<String, String>, mutableLog: MutableList<Any>, mistakesLog: MutableMap<String, Int>, importFile: String, importFlag: Int, exportFile: String, exportFlag: Int) {
    // Asking the user for a log file name
    addToLog(mutableLog, println("File name:"))
    val fileNameLog = readln().toString()
    addToLog(mutableLog, fileNameLog)
    // Asking the system for a working directory of the program
    val logDirectory = System.getProperty("user.dir")
    // Creating a string with the absolute file path for the log file
    val logPath = logDirectory + File.separator + fileNameLog
    File(logPath).writeText(mutableLog.joinToString("\n", "", ""))

    addToLog(mutableLog, println("The log has been saved."))
    // Invoking the menu function again
    menu(numOfCards, mutableLog, mistakesLog, importFile, importFlag, exportFile, exportFlag)
}

//*********** THE "HARDEST CARD" FUNCTION ***********//
fun mistakes(numOfCards: MutableMap<String, String>, mutableLog: MutableList<Any>, mistakesLog: MutableMap<String, Int>, importFile: String, importFlag: Int, exportFile: String, exportFlag: Int) {
    // Creates local variable with the cards that have the most mistakes
    val hardestCardsNames = mutableListOf<String>()
    // Creates local variable with the number of times for the cards with the most mistakes
    var hardestCardsTimes = 0
    // Checks if there are no cards with mistakes in the mistakesLog map and notifies the user
    if (mistakesLog.values.count{ it != 0 } == 0) {
        addToLog(mutableLog, println("There are no cards with errors."))
        // Invokes the menu function again
        menu(numOfCards, mutableLog, mistakesLog, importFile, importFlag, exportFile, exportFlag)
    // Checks if there is only one card in the mistakesLog with the maximum amount of mistakes
    } else if (mistakesLog.values.count{ it == max(mistakesLog.values) } == 1) {
        var temporaryCard = ""
        var temporaryCardTimes = 0
        // Iterates through all the names in the mistakesLog.keys
        for (nameOfMistake in mistakesLog.keys) {
            // Checks if the value of the current key is the maximum number in mistakesLog.values
            if(mistakesLog[nameOfMistake]!!.toInt() == max(mistakesLog.values).toInt()) {
                // Adds the name of the current card (whose value is the maximum in mistakesLog)
                // to the hardestCardsNames list
                temporaryCard = nameOfMistake
                // Adds corresponding amount of mistakes for the current card
                // to the hardestCardsTimes list
                temporaryCardTimes = mistakesLog.get(nameOfMistake)!!.toInt()
            }
            }
        addToLog(mutableLog, println("The hardest card is \"$temporaryCard\". You have $temporaryCardTimes errors answering it."))
        // Invokes the menu function again
        menu(numOfCards, mutableLog, mistakesLog, importFile, importFlag, exportFile, exportFlag)
    // Checks whether there is only one card that was mistaken
    // or whether there are more than (or equal to) two cards in the mistakesLog map
    } else if (mistakesLog.values.toSet().count() >= 2 || mistakesLog.values.count{ it == max(mistakesLog.values) } >= 2) {
        // Iterates through the mistakesLog.keys
        for (nameOfMistake in mistakesLog.keys) {
            // Checks if the current key in the mistakesLog has maximum value among other elements
            if (mistakesLog[nameOfMistake] == max(mistakesLog.values)) {
                // If yes - the current name is added to the hardestCardsNames list
                hardestCardsNames.add(nameOfMistake)
                // If yes - the current value with mistakes for the current key is added
                // to the hardestCardsTimes list
                hardestCardsTimes += mistakesLog.get(nameOfMistake)!!.toInt()
            }
        }
        addToLog(
                mutableLog,
                println(
                    "The hardest cards are " + hardestCardsNames.joinToString(
                        "\", \"",
                        "\"",
                        "\""
                    ) + ". You have " + hardestCardsTimes + " errors answering them."
                )
        )
        // Invokes the menu function again
        menu(numOfCards, mutableLog, mistakesLog, importFile, importFlag, exportFile, exportFlag)
    }
}

//*********** THE "RESET STATS" FUNCTION ***********//
fun resetStats(numOfCards: MutableMap<String, String>, mutableLog: MutableList<Any>, mistakesLog: MutableMap<String, Int>, importFile: String, importFlag: Int, exportFile: String, exportFlag: Int) {
    // Clearing the mutable map with mistakes
    mistakesLog.clear()
    addToLog(mutableLog, println("Card statistics have been reset."))
    // Invoking the menu function again
    menu(numOfCards, mutableLog, mistakesLog, importFile, importFlag, exportFile, exportFlag)
}

//*********** THE "EXIT" FUNCTION ***********//
fun exit(numOfCards: MutableMap<String, String>, mutableLog: MutableList<Any>, mistakesLog: MutableMap<String, Int>, importFile: String, importFlag: Int, exportFile: String, exportFlag: Int) {
    if (exportFlag == 1) {
        export(numOfCards, mutableLog, mistakesLog, importFile, importFlag, exportFile, 2)
    } else {
        println("Bye bye!")
        exitProcess(0)
    }
}

//*********** THE MAIN FUNCTION ***********//
fun main(args: Array<String>) {

    // Creates the map with name "numOfCards" where the flashcards are stored
    val numOfCards = mutableMapOf<String, String>()

    // Creates a mutable list with logs
    val mutableLog = mutableListOf<Any>()

    // Creates a mutable map with the mistakes
    val mistakesLog = mutableMapOf<String, Int>()

    // Creates a variable with importFile name and importFlag
    var importFile = ""
    var importFlag = 0

    // Creates a variable with exportFile name and exportFlag
    var exportFile = ""
    var exportFlag = 0

    // Checks whether args is empty
    if (args.isNotEmpty()) {
        for (argument in args) {
            // If "-import" is found in the args it will set the importFlag to 1 and the next element to "-import" will
            // become importFile filename
            if (argument == "-import") {
                    val commandIndex = args.indexOf(argument)
                    importFile = args.elementAt(commandIndex + 1)
                    importFlag = 1
                // If "-export" is found in the args it will set the exportFlag to 1 and the next element to "-export"
                // will become exportFile filename
            } else if (argument == "-export") {
                val commandIndex2 = args.indexOf(argument)
                exportFile = args.elementAt(commandIndex2 + 1)
                exportFlag = 1
            }
        }
        menu(numOfCards, mutableLog, mistakesLog, importFile, importFlag, exportFile, exportFlag)
    } else {
        // Invoking the menu function for the first time
        menu(numOfCards, mutableLog, mistakesLog, "", 0, "", 0)
    }
}