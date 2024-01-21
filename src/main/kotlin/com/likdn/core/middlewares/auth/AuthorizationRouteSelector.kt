package com.likdn.core.middlewares.auth

import io.ktor.server.routing.*

object AuthorizationRouteSelector : RouteSelector() {
    override fun evaluate(context: RoutingResolveContext, segmentIndex: Int): RouteSelectorEvaluation {
        return RouteSelectorEvaluation.Transparent
    }

    override fun toString(): String = ""
}