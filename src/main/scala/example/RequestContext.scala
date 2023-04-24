package example

import scala.util.DynamicVariable

case class RandomConfig(
  weight: Int,
  height: Int,
  name: String
)

case class RequestContext(
  userId: Option[Int],
  userEmail: Option[String],
  requestUrl: Option[String],
  randomConfig: Option[RandomConfig]
)

object RequestContext {
  final val context = new DynamicVariable[RequestContext](RequestContext(None, None, None, None))

  def withUserId[T](userId: Option[Int])(block: => T): T = {
    val current = context.value
    context.withValue(current.copy(userId = userId))(block)
  }

  def withUserEmail[T](userEmail: Option[String])(block: => T): T = {
    val current = context.value
    context.withValue(current.copy(userEmail = userEmail))(block)
  }

  def withRequestUrl[T](requestUrl: Option[String])(block: => T): T = {
    val current = context.value
    context.withValue(current.copy(requestUrl = requestUrl))(block)
  }

  def withRandomConfig[T](randomConfig: Option[RandomConfig])(block: => T): T = {
    val current = context.value
    context.withValue(current.copy(randomConfig = randomConfig))(block)
  }

  def value: RequestContext = context.value
}
