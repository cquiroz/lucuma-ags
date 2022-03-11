// Copyright (c) 2016-2022 Association of Universities for Research in Astronomy, Inc. (AURA)
// For license information see LICENSE or https://opensource.org/licenses/BSD-3-Clause

// Copyright (c) 2016-2022 Association of Universities for Research in Astronomy, Inc. (AURA)
// For license information see LICENSE or https://opensource.org/licenses/BSD-3-Clause

package lucuma.ags.api

import cats.Applicative
import lucuma.core.model.Target

/**
 * Implements the logic for estimation and selection for a single guide probe. The same logic is
 * applied to various single-star guiding scenarios (i.e., everything except for GeMS).
 */
final case class SingleProbeStrategy[F[_]: Applicative](
  params: SingleProbeStrategyParams /*, backend: Option[VoTableBackend] = None*/
) extends AgsStrategy[F] {
  import SingleProbeStrategy._

  // override def catalogQueries(ctx: ObsContext, mt: MagnitudeTable): List[CatalogQuery] =
  //   params.catalogQueries(ctx, mt).toList
  //
  // private def candidates(ctx: ObsContext, mt: MagnitudeTable): F[List[ProbeCandidates]] = {
  //   val empty =
  //     Applicative[F].pure(List(ProbeCandidates(params.guideProbe, List.empty[Target.Sidereal])))
  //
  //   // We cannot let VoTableClient to filter targets as usual, instead we provide an empty magnitude constraint and filter locally
  //   catalogQueries(withCorrectedSite(ctx), mt)
  //   //     .strengthR(backend)
  //   //     .headOption
  //   //     .map { case (a, b) => VoTableClient.catalog(a, b)(ec) }
  //   //     .map(_.flatMap {
  //   //       case r if r.result.containsError => Future.failed(CatalogException(r.result.problems))
  //   //       case r                           => Future.successful(List(ProbeCandidates(params.guideProbe, r.result.targets.rows)))
  //   //     })
  //   //     .getOrElse(empty)
  // }

  // private def catalogResult(ctx: ObsContext, mt: MagnitudeTable): F[List[Target.Sidereal]] =
  //   // call candidates and extract the one and only tuple for this strategy,
  //   // throw away the guide probe (which we know anyway), and obtain just the
  //   // list of guide stars
  //   candidates(ctx, mt).map(_.headOption.foldMap(_.targets))
  //
  def select(ctx: ObsContext, mt: MagnitudeTable): F[Selection] = ???
  //   // val ct = withCorrectedSite(ctx)
  //   catalogResult(ctx, mt) // .map(select(ct, mt, _))
}
