package example

import java.util.concurrent.ForkJoinPool
import scala.util.DynamicVariable

class ForkJoinWithDynamicVariables[T](dynamicVariable: DynamicVariable[T]) extends ForkJoinPool {
  override def execute(task: Runnable): Unit = {
    val copyValue = dynamicVariable.value
    super.execute(() => {
      dynamicVariable.value = copyValue
      task.run
    })
  }
}
