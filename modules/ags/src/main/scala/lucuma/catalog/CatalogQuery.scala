// Copyright (c) 2016-2022 Association of Universities for Research in Astronomy, Inc. (AURA)
// For license information see LICENSE or https://opensource.org/licenses/BSD-3-Clause

package lucuma.catalog

import lucuma.core.math.Coordinates
import lucuma.core.enum.CatalogName

case class RadiusFilter(base: Coordinates, rc: RadiusConstraint) {
  def filter(t: Coordinates): Boolean = rc.targetsFilter(base)(t)
}
//
// /**
//  * CatalogQuery using the ConeSearch method with additional constraints on radius and magnitude
//  */
// final case class ConeSearchCatalogQuery(
//   base:                 Coordinates,
//   radiusConstraint:     RadiusConstraint,
//   magnitudeConstraints: List[MagnitudeConstraints],
//   catalog:              CatalogName
// ) extends CatalogQuery {
//
//   val filters: NonEmptyList[QueryResultsFilter] =
//     NonEmptyList(
//       RadiusFilter(base, radiusConstraint),
//       magnitudeConstraints.map(MagnitudeQueryFilter.apply): _*
//     )
//
//   override def filter(t: SiderealTarget): Boolean =
//     filters.toList.forall(_.filter(t))
//
//   private def closeEnough(c: ConeSearchCatalogQuery): Boolean = {
//     // Angular separation, or distance between the two.
//     val distance = Coordinates.difference(base, c.base).distance
//
//     // Add the given query's outer radius limit to the distance to get the
//     // maximum distance from this base position of any potential guide star.
//     val max = distance + c.radiusConstraint.maxLimit
//
//     // See whether the other base position falls out of range of our
//     // radius limits.
//     radiusConstraint.maxLimit >= max
//   }
//
//   // Compare the List[MagnitudeConstraints] with tolerance.
//   private def closeMagnitudes(c: ConeSearchCatalogQuery): Boolean = {
//     def closeMagConstraint(m0: MagConstraint, m1: MagConstraint): Boolean =
//       (m0.brightness - m1.brightness).abs < 0.000001
//
//     def closeOption[A](o0: Option[A], o1: Option[A])(f: (A, A) => Boolean) =
//       (o0, o1) match {
//         case (Some(a0), Some(a1)) => f(a0, a1)
//         case (None, None)         => true
//         case _                    => false
//       }
//
//     val m0 = magnitudeConstraints.groupBy(_.searchBands)
//     val m1 = c.magnitudeConstraints.groupBy(_.searchBands)
//
//     (m0.keySet == m1.keySet) && m0.forall { case (key, v0) =>
//       (v0.size === m1(key).size) && v0.zip(m1(key)).forall { case (mc0, mc1) =>
//         closeMagConstraint(mc0.faintnessConstraint, mc1.faintnessConstraint) &&
//         closeOption(mc0.saturationConstraint, mc1.saturationConstraint)(closeMagConstraint)
//       }
//     }
//   }
//
//   override def isSuperSetOf(q: CatalogQuery): Boolean =
//     q match {
//       case c: ConeSearchCatalogQuery =>
//         (q.catalog === catalog) && closeEnough(c) && closeMagnitudes(c)
//
//       case _ =>
//         false
//     }
// }
