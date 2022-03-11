// Copyright (c) 2016-2022 Association of Universities for Research in Astronomy, Inc. (AURA)
// For license information see LICENSE or https://opensource.org/licenses/BSD-3-Clause

package lucuma.catalog

import cats.Order
import cats.syntax.all._
import lucuma.core.math.Angle
import lucuma.core.math.Offset
import lucuma.core.math.Coordinates

trait RadiusConstraint {
  val maxLimit: Angle
  val minLimit: Angle

  /**
   * If there is an offset but there isn't a posAngle, then we have to adjust the search radius to
   * take into account any position angle. That means the outer limit increases by the distance from
   * the base to the offset and the inner limit decreases by the same distance (never less than 0
   * though).
   *
   * @return
   *   a new (possibly) adjusted radius limits
   */
  def adjust(offset: Offset): RadiusConstraint

  /**
   * Returns a filter for coordinates with a distance inside the range
   */
  def targetsFilter(base: Coordinates): Coordinates => Boolean

}

/**
 * Describes limits for catalog cone search radius values. See OT-17.
 */
object RadiusConstraint {
  implicit val order: Order[Angle] = Angle.AngleOrder

  val empty = between(Angle.Angle0, Angle.Angle0)

  implicit class OffsetOps(val offset: Offset) extends AnyVal {

    /**
     * Calculates the distance between the base position and the offset position
     *
     * @return
     *   angular separation between base position and offset position
     */
    def distance: Angle = {
      val p = offset.p.toSignedDoubleRadians
      val q = offset.q.toSignedDoubleRadians

      val d = Math.sqrt(p * p + q * q);
      Angle.fromDoubleRadians(d)
    }
  }

  private case class RadiusConstraintImpl(minLimit: Angle, maxLimit: Angle)
      extends RadiusConstraint {

    def adjust(offset: Offset): RadiusConstraint = {
      val d    = offset.distance
      val maxL = maxLimit + d
      val minL = minLimit - d

      RadiusConstraint.between(maxL, minL.max(Angle.Angle0))
    }

    /**
     * Returns a filter for coordinates with a distance inside the range
     */
    def targetsFilter(base: Coordinates): Coordinates => Boolean = coordinates => {
      val distance = base.angularDistance(coordinates)
      distance >= minLimit && distance <= maxLimit
    }

  }

  /**
   * Constructs a range between 2 angles. It will produce the correct ordering to create a valid
   * range
   */
  def between(minLimit: Angle, maxLimit: Angle): RadiusConstraint =
    if (minLimit < maxLimit) {
      RadiusConstraintImpl(minLimit, maxLimit)
    } else {
      RadiusConstraintImpl(maxLimit, minLimit)
    }

}
