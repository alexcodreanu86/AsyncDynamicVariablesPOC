package example

object Hello extends Greeting with App {
  HelloRunner.start
}

trait Greeting {
  lazy val greeting: String = "hello Alex"
}
