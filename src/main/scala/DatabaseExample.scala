import java.sql._
import scala.io.StdIn._
import scala.collection.mutable.ArrayBuffer

object DatabaseExample extends App {

  var wordArray: ArrayBuffer[String] = ArrayBuffer.empty

  // connect to the database named "mysql" on port 8889 of localhost
  val url = "jdbc:mysql://localhost:3306/hangman"
  val driver = "com.mysql.jdbc.Driver"
  val username = "root"
  val password = "password"
  var connection: Connection = _
  try {
    Class.forName(driver)
    connection = DriverManager.getConnection(url, username, password)
    val statement = connection.createStatement
    val rs = statement.executeQuery("SELECT * FROM Words")
    while (rs.next) {
      val host = rs.getString("Word")
      wordArray += host

    }
  } catch {
    case e: Exception => e.printStackTrace
  }



  def hangman(): Unit = {
    var running: Boolean = true
    val random = new scala.util.Random
    val randomword = wordArray(random.nextInt(wordArray.length))
    var wrongGuesses = 0

    val answerArray = randomword.toCharArray
    var guesses: Set[Char] = Set.empty
    println("Computer Word Chosen! Take a guess at a letter!")

    while (running) {
      println("Pick A Letter!")
      val input = readLine().toLowerCase()

      if (input.length != 1) {
        println("Please enter a character, baka")
      } else {
        var guess = ""
        if (!answerArray.contains(input.toCharArray()(0))) {
          wrongGuesses += 1
        }
        guesses += input.charAt(0)
        print("Previous Guesses: ")
        guesses.foreach(guess => print(s"$guess, "))
        println("")

        answerArray.foreach(letter => {
          if (guesses.contains(letter)) {
            print(letter+" ")
            guess += letter
          } else {
            print("_ ")
          }
        })

        hangmanPicture(wrongGuesses)

        if (guess == randomword) {
          println(s"\nCongratulations!, the word was $randomword")
          running = false

          connection.close
        }
        if (wrongGuesses == 9) {
          println(s"\nRIP u, the word was, in fact, $randomword")
          running = false

          connection.close
        }
        println("")
      }
    }
  }

  def hangmanPicture(guesses: Int): Unit = {
    guesses match {
      case 0 => println("\n\nMan Status: Pretty much fully alive")
      case 1 => println("\n\n    |\n    |\n    |\n    |\n    |\n    |\n    |\n    |\n    |\n    |\n    |\n    |")
      case 2 => println("\n\n    _____________\n    |\n    |\n    |\n    |\n    |\n    |\n    |\n    |\n    |\n    |\n    |")
      case 3 => println("\n\n    _____________\n    |           |\n    |\n    |\n    |\n    |\n    |\n    |\n    |\n    |\n    |\n    |")
      case 4 => println("\n\n    _____________\n    |           |\n    |          _|_\n    |       ( ͡° ͜ʖ ͡°)\n    |           |\n    |\n    |\n    |\n    |\n    |\n    |\n    |")
      case 5 => println("\n\n    _____________\n    |           |\n    |          _|_\n    |       ( ͡° ͜ʖ ͡°)\n    |           |\n    |           |\n    |           |\n    |           |\n    |\n    |\n    |\n    |")
      case 6 => println("\n\n    _____________\n    |           |\n    |          _|_\n    |       ( ͡° ͜ʖ ͡°)\n    |          /|\n    |         / |\n    |        /  |\n    |           |\n    |\n    |\n    |\n    |")
      case 7 => println("\n\n    _____________\n    |           |\n    |          _|_\n    |       ( ͡° ͜ʖ ͡°)\n    |          /|\\\n    |         / | \\\n    |        /  |  \\\n    |           |\n    |\n    |\n    |\n    |")
      case 8 => println("\n\n    _____________\n    |           |\n    |          _|_\n    |       ( ͡° ͜ʖ ͡°)\n    |          /|\\\n    |         / | \\\n    |        /  |  \\\n    |           |\n    |          /\n    |         /\n    |        /\n    |")
      case 9 => println("\n\n    _____________\n    |           |\n    |          _|_\n    |       ( ͡° ͜ʖ ͡°)\n    |          /|\\\n    |         / | \\\n    |        /  |  \\\n    |           |\n    |          / \\\n    |         /   \\\n    |        /     \\\n    |")
      case _ => println("Aren't you dead yet?")
    }
  }

  hangman()
}
