package example

import scala.concurrent.{ExecutionContext, Future}

object HelloRunner {
  val customExecutionContext = scala.concurrent.ExecutionContext.fromExecutorService(
    new ForkJoinWithDynamicVariables[RequestContext](RequestContext.context)
  )

  def start: Unit = {
    println("<Global Execution Context ***********************>")
    runTests(scala.concurrent.ExecutionContext.Implicits.global)
    Thread.sleep(100)
    println("</<Global Execution Context ***********************>>")
    println("<Custom Execution Context ***********************>")
    runTests(customExecutionContext)
    Thread.sleep(100)
    println("</Custom Execution Context ***********************>")
  }

  def runTests(implicit executionContext: ExecutionContext): Unit = {
    testDifferentNestedStates
    testALotOfThreadsWithUserIds
  }

  private def testALotOfThreadsWithUserIds(implicit executionContext: ExecutionContext): Unit = {
    (1 until 40).foreach { userId =>
      RequestContext.withUserId(Some(userId)) {
        printContext(s"ID: $userId |")
      }
    }
  }


  private def testDifferentNestedStates(implicit executionContext: ExecutionContext): Unit = {
    printContext("<TopLevel>")
    RequestContext.withUserId(Some(1)) {
      printContext("<UserId>")
      RequestContext.withUserEmail(Some("alex@email.com")) {
        printContext("<UserEmail>")
        RequestContext.withRandomConfig(Some(RandomConfig(10, 20, "Alex"))) {
          printContext("<RandomConfig>")
          printContext("</RandomConfig>")
        }
        printContext("</UserEmail>")
      }
      printContext("</UserId>")
    }
    printContext("</TopLevel>")
  }

  def printContext(level: String)(implicit executionContext: ExecutionContext): Future[Unit] = Future {
    val RequestContext(userId, userEmail, _, randomConfig) = RequestContext.value
    println(s"$level RequestContext(userId: $userId, userEmail: $userEmail, randomConfig: $randomConfig)")
  }
}
