package parking
import java.util.*

// var to hold size of lot to be used to init the lot/# of plates/# of colours to be stored
var sizeOfLot: Int = 0
// var on the spot being checked, and if empty, return that spot number to main
var spotToCheck = 0
// array of status of specific parking spot if free or not; false = free, true = occupied
var spots = BooleanArray(0)
// array to hold Car object, containing reg # and colour
var cars = emptyArray<Car>()

// class to hold a Car object's reg # (aka plate) and colour
data class Car(var plate: String, var colour: String) {
    // override toString() in order to use later on in certain functions
    override fun toString(): String {
        return "$plate $colour"
    }
}

// empty val to be used to init the cars Array in fun buildLot
val emptyCar = Car(" ", " ")

// Set up the parking lot, including spot status, plates, and colours based on the number specified after "create" command
fun buildLot(numberOfSpots: Int) {
    // init cars Array
    cars = Array<Car>(numberOfSpots) {emptyCar}

    // init parking lot spots status (false = free spot), i.e. create an empty lot
    spots = BooleanArray(numberOfSpots)
    for (i in spots.indices) {
        spots[i] = false
    }
}

// Fun to check the parking lot for any empty spots, and return
fun checkLot() {
    // var to see if # of empty spots == size of lot, therefore indicating empty lot
    var emptySpots = 0

    // for loop to identify if lot is empty (if emptyspots == spots.size, then there are as many empty spots as there are total spots, indicating an empty parking lot),
    // or list out occupied spots + plate/colour of car
    for (i in spots.indices) {
        // if spot is false, it is free, so count it as an emptyspot
        if (!spots[i]) {
            emptySpots++
            // else if there is a spot that is occupied (spots[i] == true), return the spot number (index + 1) and the corresponding car's info (reg plate and colour)
        } else if (spots[i]) {
            println("${i+1} ${cars[i]}")
        }
    }

    // see comment on line 40
    if (emptySpots == spots.size) {
        println("Parking lot is empty.")
    }
}

// function to determine whether or not a spot is free (false = free, true = occupied); used primarily for the "leave" command in the program
fun hasCar(spot: Int) {
    // spot - 1 to account for indices starting at 0
    if (!spots[spot-1]) {
        println("There is no car in the spot $spot.")
    } else if (spots[spot-1]) {
        println("Spot $spot is free.")
        // reset spot to false, thereby indicating it is now free
        spots[spot-1] = false
        // since car has left the spot, that plate and colour information isn't applicable anymore (this was causing an issue in test #3)
        cars[spot-1].plate = " "
        cars[spot-1].colour = " "
    }

}

// function to look for a spot that is free, or if all the spots are occupied
fun freeSpot(): Int {

    // if function to check if there are any empty spots
    if (spots.contains(false)) {
        for (i in 0..spots.size) {
            if (!spots[i]) {
                // return the spot number, add 1 to offset indices starting at 0
                spotToCheck = i + 1
                // change corresponding spots[i] to true, indicating that spot is full
                spots[i] = true
                // break from this for loop, as an empty spot has been found
                break
            }
        }
    } else {
        // arbitrary number > 'size' just to divert my if statement later in the code ->
        // if spotToCheck > sizeOfLot [i.e. sizeOfLot + 2), will print "lot is full"
        spotToCheck = sizeOfLot + 2
    }

    // number to be used in print statement for the spot the car is parked in
    return spotToCheck
}

// fun to return reg plate by searching for colour
fun regColor(colToCheck: String) {
    // mutableList to hold the plate found; jointToString() later used to print results
    val platesFound = mutableListOf<String>()
    // to identify if the lot is empty/devoid of cars of the colour being checked (colToCheck)
    var isEmpty = true

        for (i in cars.indices) {
        if (cars[i].colour == colToCheck.toLowerCase()) {
            // false indicates that there is at least one car with the color being checked
            isEmpty = false
            // plates are recorded in lower case for ease of checking against colToCheck, so have to print in upper case
            // add any plates found to above mutableList
            platesFound.add(cars[i].plate.toUpperCase())
        }
    }

    // check if there are any cars with the colour being checked or not, and then print corresponding response
    if (isEmpty) {
        println("No cars with color $colToCheck were found.")
    } else {
        println(platesFound.joinToString())
    }
}

// fun to return spot # by searching for colour
fun spotColor(colToCheck: String) {
    // mutableList to hold the spots found; jointToString() later used to print results
    val spotsFound = mutableListOf<String>()
    // to identify if the lot is empty/devoid of cars of the colour being checked (colToCheck)
    var isEmpty = true

    for (i in cars.indices) {
        if (cars[i].colour == colToCheck.toLowerCase()) {
            // false indicates that there is at least one car with the color being checked
            isEmpty = false
            // add any spots found to above mutableList; change Int toString for ease of using later in println() and joinToString()
            spotsFound.add((i + 1).toString())
        }
    }

    // check if there are any cars with the colour being checked or not, and then print corresponding response
    if (isEmpty) {
        println("No cars with color $colToCheck were found.")
    } else {
        println(spotsFound.joinToString())
    }

}

// fun to return spot # by searching for reg plate
fun spotReg(regToCheck: String) {
    // mutableList to hold the spots found; jointToString() later used to print results
    val spotsFound = mutableListOf<String>()
    // to identify if the lot is empty/devoid of cars of the reg plate being checked (regToCheck)
    var isEmpty = true

    for (i in cars.indices) {
        if (cars[i].plate == regToCheck.toLowerCase()) {
            // false indicates that there is at least one car with the reg plate being checked
            isEmpty = false
            // add any spots found to above mutableList; change Int toString for ease of using later in println() and joinToString()
            spotsFound.add((i + 1).toString())
        }
    }

    // check if there are any cars with the colour being checked or not, and then print corresponding response
    if (isEmpty) {
        println("No cars with registration number $regToCheck were found.")
    } else {
        println(spotsFound.joinToString())
    }

}

fun main() {
    val scanner = Scanner(System.`in`)
    // split the input to later use/assign different parts to the appropriate parameter/function depending on the command specified
    var input = scanner.nextLine().split(" ")

    // var to determine course of action in program (create, park, leave, or exit)
    var command = input[0].toLowerCase()

    // while loop until input explicitly requests to exit the program
    while (command != "exit") {

        // if tree to correspond to the specified command from the list of commands being tested
        if (command == "create") {
            // assign the number of spots user is interested in creating to sizeOfLot which is then to init various arrays
            sizeOfLot = input[1].toInt()
            // fun that builds spots (parking lot) and cars arrays, which will be used to hold info on cars parked and exited
            buildLot(sizeOfLot)
            println("Created a parking lot with $sizeOfLot spots.")
        // ensure lot is created, then call fun to search for reg plate by colour
        } else if (command == "reg_by_color" && sizeOfLot > 0) {
            regColor(input[1])
            // ensure lot is created, then call fun to search for spot # by colour
        } else if (command == "spot_by_color" && sizeOfLot > 0) {
            spotColor(input[1])
            // ensure lot is created, then call fun to search for spot # by reg plate
        } else if (command == "spot_by_reg" && sizeOfLot > 0){
            spotReg(input[1])
            // ensure lot is created, then call fun to check status of lot
        } else if (command == "status" && sizeOfLot > 0) {
            checkLot()
            // ensure lot is created, then call fun to check if there is a car in a specified spot (input[1]) and then return appropriate response
        } else if (command == "leave" && sizeOfLot > 0) {
            hasCar(input[1].toInt())
            // ensure lot is created, then check size of lot to ensure an available parking space and return appropriate response
        } else if (command == "park" && sizeOfLot > 0) {
            // assign info from input to corresponding field to be stored in a Car object
            val plate = input[1]
            val colour = input[2]
            // var to hold the results of freeSpot(), & to be then used in the following when
            val newSpot = freeSpot()
            when {
                // check that the spot available is within the size specified in sizeOfLot, thereby indicating an empty spot is available
                newSpot <= sizeOfLot -> {
                    // assign corresponding plate + colour info to the spot number for later recall using "status" command
                    cars[newSpot - 1] = Car(plate.toLowerCase(), colour.toLowerCase())
                    println("$colour car parked in spot $newSpot.")
                }
                // if newspot > sizeOfLot, it indicates that the lot is full
                newSpot > sizeOfLot -> {
                    println("Sorry, the parking lot is full.")
                }

                else -> {
                    println("Sorry, a parking lot has not been created.")
                }
            }
            // if a parking lot has not been created, OR a command other than the ones tested is used; should modify this to return a
            // "sorry, that input is not valid" statement, but I'm tired at this point so....lol
        } else {
            println("Sorry, a parking lot has not been created.")
        }

        // receive next input to determine next course of action in program
        input = scanner.nextLine().split(" ")
        command = input[0].toLowerCase()

    }
}
